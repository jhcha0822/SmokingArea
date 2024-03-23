package com.sds.smokingapp.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiParse {
	public static void main(String[] args) throws IOException {
    	// 1. URL을 만들기 위한 StringBuilder
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/3040000/smokingService/getSmkAreaList"); /*URL*/
        // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
        urlBuilder.append("?" + URLEncoder
        		.encode("serviceKey","UTF-8") + "=5XuhuoQexLdGRNEAvAnhIOWFjP6tCKgUahcCE1nzgw1OlpZmm0p%2B5Le%2FAaef8tPbhXKeRHAvqZzw6SwZZ0aCFQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*응답메시지 결과 요청 타입(xml, json, geojson)*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("40", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//        urlBuilder.append("&" + URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode("군자동-02-01-020", "UTF-8")); /*흡연구역 아이디 전체 출력은 빈칸*/
        urlBuilder.append("&" + URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*전체 출력은 빈칸*/
        // 3. URL 객체 생성
        URL url = new URL(urlBuilder.toString());
        // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 5. 통신을 위한 메소드 SET.
        conn.setRequestMethod("GET");
        // 6. 통신을 위한 Content-type SET. 
        conn.setRequestProperty("Content-type", "application/json");
        // 7. 통신 응답 코드 확인.
        System.out.println("Response code: " + conn.getResponseCode());
        // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        // 10. 객체 해제
        rd.close();
        conn.disconnect();
        // 11. 전달받은 데이터 확인.
        System.out.println(sb.toString());
        
     // 데이터베이스 연결
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 데이터베이스 드라이버 로드 및 연결 설정
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "web", "1234");

            // JSON 파싱
            //JSONObject jsonObj = new JSONObject(sb.toString());
            JSONArray jsonArray = new JSONArray(sb.toString()); // 받은 데이터를 배열에 저장
            //JsonArray안엔 json 데이터가 배열로 들어감
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                // 데이터베이스에 저장하기 위한 SQL 쿼리 준비
                String sql = "INSERT INTO smoking_area(id, area_nm, area_desc, ctprvnnm, signgunm, emdnm, area_se, rdnmadr, lnmadr, inst_nm) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(sql);

                // JSON 객체에서 데이터 추출 및 쿼리 파라미터 설정
                // ex) json 데이터 중 id 키에 해당하는 값을 문자열로 추출하여 1번째 파라미터로 설정
                pstmt.setString(1, item.optString("id"));
                pstmt.setString(2, item.optString("area_nm"));
                pstmt.setString(3, item.optString("area_desc"));
                pstmt.setString(4, item.optString("ctprvnnm"));
                pstmt.setString(5, item.optString("signgunm"));
                pstmt.setString(6, item.optString("emdnm"));
                pstmt.setString(7, item.optString("area_se"));
                pstmt.setString(8, item.optString("rdnmadr"));
                pstmt.setString(9, item.optString("lnmadr"));
                pstmt.setString(10, item.optString("inst_nm"));

                // 쿼리 실행
                pstmt.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
            // 자원 해제
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (con != null) try { con.close(); } catch(SQLException ex) {}
        }
        
        System.out.println("insert 성공");

    }
}
