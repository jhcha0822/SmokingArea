<%@page import="com.sds.smokingapp.smoking.SmokingArea"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.sds.smokingapp.smoking.SmokingAreaDAO"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%!
	static private String apiUrl = "http://apis.data.go.kr/3040000/smokingService/getSmkAreaList";                                          // 광진구 흡연구역 지도
	static private String serviceKey = "=5XuhuoQexLdGRNEAvAnhIOWFjP6tCKgUahcCE1nzgw1OlpZmm0p%2B5Le%2FAaef8tPbhXKeRHAvqZzw6SwZZ0aCFQ%3D%3D"; // 인증키
	static private String type = "json";                                                                                                    // 응답메시지 결과 요청 타입(xml, json, geojson)
	static private String numOfRows = "40";                                                                                                 // 한 페이지 결과 수 
	static private String pageNo = "1";                                                                                                     // 페이지 번호
	static private String id = "";   																										// 흡연구역 id, 빈칸: 전체
	SmokingAreaDAO smokingAreaDAO = new SmokingAreaDAO();
%>
<%
	//1. URL을 만들기 위한 StringBuilder
	StringBuilder urlBuilder = new StringBuilder(apiUrl);
	
	// 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
	urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey);
	urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
	urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
	urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
	urlBuilder.append("&" + URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id, "UTF-8"));
	
	// 3. URL 객체 생성
	URL url = new URL(urlBuilder.toString());
	
	// 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
	// 5. 통신을 위한 메소드 SET.
	conn.setRequestMethod("GET");
	
	// 6. 통신을 위한 Content-type SET. 
	conn.setRequestProperty("Content-type", "application/json");
	
	// 7. 통신 응답 코드 확인.
	// System.out.println("Response code: " + conn.getResponseCode());
	
	// 8. 전달받은 데이터를 BufferedReader 객체로 저장.
	BufferedReader rd;
	if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
	    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	else
	    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	
	// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
	StringBuilder sb = new StringBuilder();
	String line;
	while ((line = rd.readLine()) != null)
	    sb.append(line);
	
	// 10. 객체 해제
	rd.close();
	conn.disconnect();
	
	// 11. 전달받은 데이터 확인.
	// System.out.println(sb.toString());
	
	// JSON 파싱
	//JSONObject jsonObj = new JSONObject(sb.toString());
	JSONArray jsonArray = new JSONArray(sb.toString()); // 받은 데이터를 배열에 저장
	//JsonArray안엔 json 데이터가 배열로 들어감
	for(int i=0; i<jsonArray.length(); i++) {
	    JSONObject item = jsonArray.getJSONObject(i);
	    
	    SmokingArea smokingArea = new SmokingArea();
	    smokingArea.setId(item.optString("id"));
	    smokingArea.setArea_nm(item.optString("area_nm"));
	    smokingArea.setArea_desc(item.optString("area_desc"));
	    smokingArea.setCtprvnnm(item.optString("ctprvnnm"));
	    smokingArea.setSigngunm(item.optString("signgunm"));
	    smokingArea.setEmdnm(item.optString("emdnm"));
	    smokingArea.setArea_se(item.optString("area_se"));
	    smokingArea.setRdnmadr(item.optString("rdnmadr"));
	    smokingArea.setLnmadr(item.optString("lnmadr"));
	    smokingArea.setInst_nm(item.optString("inst_nm"));
	    
	    int result = smokingAreaDAO.insert(smokingArea);	    
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

</body>
</html>