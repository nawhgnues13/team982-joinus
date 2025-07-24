package com.team982.joinus.domain.participation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.team982.joinus.domain.participation.model.Participation;

public class ParticipationDao {
	static DataSource dataSource;

	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 회원별 참여기록 조회
	public List<Participation> getParticipationsByMember(int memberId) {
		String sql = "SELECT "
				   + "    participation_id, member_id, activity_id, participation_date "
				   + "FROM participation WHERE member_id = ?";
		List<Participation> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, memberId);
			ResultSet rs = ps.executeQuery(); 
			while (rs.next()) {
				Participation p = new Participation();
				p.setParticipationId (rs.getInt("participation_id"));
				p.setMemberId        (rs.getInt("member_id"));
				p.setActivityId      (rs.getInt("activity_id"));
				p.setParticipationDate(rs.getDate("participation_date"));
				list.add(p);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("회원별 참여 기록 조회 중 오류", e);
		}
		return list;
	} // end getParticipationsByMember
	
	// 참여 기록 삽입
	public int insertParticipation(Participation p) {
		String sql = "INSERT INTO participation "
				   + "    (participation_id, member_id, "
				   + "     activity_id, participation_date) "
				   + "VALUES "
				   + "    (seq_participation_id.NEXTVAL, ?, ?, SYSDATE)";
		try (Connection con = dataSource.getConnection()) {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, p.getMemberId());
			ps.setInt(2, p.getActivityId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Participation 삽입 중 오류", e);
		}
	} // end insertParticipation
}
