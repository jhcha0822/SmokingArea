package com.sds.smokingapp.smoking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//DAO에서 공통된 작업 수행
//xml의 커넥션 풀을 검색하여 DataSource로부터 필요한 Connection이 있을때 DAO에게 반환 및 반납

public class PoolManager {
	InitialContext context; // xml 검색 객체
	DataSource ds; // connection pool 구현체

	public PoolManager() {
		try {
			context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jndi/oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	// DAO등이 커넥션풀로부터 Connection 한개를 얻어갈 수 있도록 메서드 제공
	public Connection getConnection() {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	// DAO등이 반납을 요청할때 처리
	public void release(Connection con) {
		if (con != null) {
			try {
				con.close(); // 반납의 의미
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 오버로딩
	public void release(Connection con, PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close(); // 반납의 의미
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 오버로딩
	public void release(Connection con, PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close(); // 반납의 의미
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
