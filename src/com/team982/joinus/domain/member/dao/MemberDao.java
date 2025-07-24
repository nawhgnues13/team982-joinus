package com.team982.joinus.domain.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.team982.joinus.domain.member.model.MemberDto;

public class MemberDao {
	static DataSource dataSource;

	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 로그인
	public int login(MemberDto member) {
		int memberId = -1;
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT member_id "
					   + "FROM member "
					   + "WHERE email=? AND password=? AND is_deleted='N'";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getPassword());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				memberId = rs.getInt("member_id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		return memberId;
	} // end login()
	
	// 회원가입
	public int insertMember(MemberDto member) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "INSERT INTO member (member_id, email, password, member_name) "
					   + "VALUES (seq_member_id.NEXTVAL, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getPassword());
			stmt.setString(3, member.getMemberName());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end insertMember()
	
	// 회원 탈퇴
	public int deleteMember(int memberId, MemberDto member) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE member "
					+ "SET is_deleted = 'Y' "
					+ "WHERE member_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end deleteMember()
	
	// 아이디, 비밀번호로 회원 확인
	public boolean checkMember(int memberId, MemberDto member) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT * "
					   + "FROM member "
					   + "WHERE member_id=? AND password=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setString(2, member.getPassword());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
