package com.sds.smokingapp.smoking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SmokingAreaDAO {
	
	PoolManager poolManager = new PoolManager();
	
	// Create(Insert)
	public int insert(SmokingArea smokingArea) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		con = poolManager.getConnection();
		
		String sql = "INSERT INTO smoking_area(id, area_nm, area_desc, ctprvnnm, signgunm, emdnm, area_se, rdnmadr, lnmadr, inst_nm)";
		sql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, smokingArea.getId());
			pstmt.setString(2, smokingArea.getArea_nm());
			pstmt.setString(3, smokingArea.getArea_desc());
			pstmt.setString(4, smokingArea.getCtprvnnm());
			pstmt.setString(5, smokingArea.getSigngunm());
			pstmt.setString(6, smokingArea.getEmdnm());
			pstmt.setString(7, smokingArea.getArea_se());
			pstmt.setString(8, smokingArea.getRdnmadr());
			pstmt.setString(9, smokingArea.getLnmadr());
			pstmt.setString(10, smokingArea.getInst_nm());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			poolManager.release(con, pstmt);
		}
		
		return result;
	}
	
}
