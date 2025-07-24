package com.team982.joinus.domain.activity.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.team982.joinus.domain.activity.model.Activity;

public class ActivityDao {
	static DataSource dataSource;

	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 활동 생성
	public int insertActivity(Activity act) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "insert into activity "
					   + "    (activity_id, title, activity_description,"
					   + "    activity_date, club_id, parent_id, category_id) "
					   + "values(?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, act.getActivityId());
			stmt.setString(2, act.getTitle());
			stmt.setString(3, act.getActivityDescription());
			stmt.setDate(4, act.getActivityDate());
			stmt.setInt(5, act.getClubId());
			if(act.getParentId() == null) {
				stmt.setNull(6, Types.INTEGER);
			}else {
				stmt.setInt(6, act.getParentId());
			}
			stmt.setInt(7, act.getCategoryId());

			return stmt.executeUpdate();
		}catch(SQLException e){
			System.out.println(e.getMessage());
			throw new RuntimeException("Activity 삽입 중 오류",e);
		}
	} // end insertActivity
	
	// 활동 삭제
	public int deleteActivity(int activityId) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "update activity "
					   + "set is_deleted='Y' where activity_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, activityId);
			return stmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Activity 삭제 중 오류", e);
		}
	} // end deleteActivity
	
	// 활동 수정
	public int updateActivity(int activityId, String title, String description, String dateStr) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "update activity "
					   + "set title=?, activity_description=?, "
					   + "    activity_date=TO_DATE(?, 'YYYY-MM-DD') "
					   + "where activity_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setString(3, dateStr);
			stmt.setInt(4, activityId);
			return stmt.executeUpdate();
		}catch(SQLException e){
			System.out.println(e.getMessage());
			throw new RuntimeException("Activity 수정 중 오류",e);
		}
	} // end updateActivity
	
	//동아리명으로 동아리 전체 활동 조회
	public List<Activity> getAllActivitiesByClubId(int clubId) {
		List<Activity> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT a.activity_id, a.title, a.activity_description," +
					"       a.activity_date, c.category_name " +
					" FROM activity a " +
					" JOIN category c " +
					"    ON a.category_id = c.category_id " +
					" WHERE a.club_id  =? " +
					" 	AND a.is_deleted = 'N' " +                   
					" ORDER BY a.activity_date, a.activity_id";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Activity act = new Activity();
				act.setActivityId(rs.getInt("activity_id"));
				act.setTitle(rs.getString("title"));
				act.setActivityDescription(rs.getString("activity_description"));
				act.setActivityDate(rs.getDate("activity_date"));
				act.setCategoryName(rs.getString("category_name"));
				list.add(act);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Activity 전체 조회 중 오류", e);
		}
		return list;
	} // end getAllActivitiesByClubId
	
	// 마이페이지에서 활동 내역 보기
	public List<Activity> getActivityInMypage(int memberId) {
		List<Activity> list= new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql ="SELECT  a.title, a.activity_description, a.activity_date, c.club_name "
					+ "FROM participation p "
					+ "JOIN activity a "
					+ "	ON p.activity_id = a.activity_id "
					+ "JOIN club c "
					+ "	ON a.club_id = c.club_id "
					+ "WHERE p.member_id=? "
					+ "	AND a.is_deleted = 'N' "
					+ "ORDER BY a.activity_date";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Activity act = new Activity();
				act.setTitle(rs.getString("title"));
				act.setActivityDescription(rs.getString("activity_description"));
				act.setActivityDate(rs.getDate("activity_date"));
				act.setClubName(rs.getString("club_name"));
				list.add(act);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Mypage_Activity 조회 중 오류",e);
		}
		return list;
	} // end getActivityInMypage
	
	// 회원이 참여한 활동 중, 날짜가 fromDate ~ toDate 사이인 것만 조회
	public List<Activity> getActivityInPeriod(int memberId, Date fromDate, Date toDate) {
		List<Activity> list = new ArrayList<>();
		String sql = "SELECT a.title, a.activity_description, a.activity_date, c.club_name " +
				"  FROM participation p " +
				"  JOIN activity a ON p.activity_id = a.activity_id " +
				"  JOIN club c ON a.club_id = c.club_id " +
				" WHERE p.member_id = ? " +
				"   AND a.activity_date BETWEEN ? AND ? " +
				"   AND a.is_deleted = 'N' " +
				" ORDER BY a.activity_date DESC";
		try (Connection con = dataSource.getConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setDate(2, fromDate);
			stmt.setDate(3, toDate);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Activity act = new Activity();
				act.setTitle(rs.getString("title"));
				act.setActivityDescription(rs.getString("activity_description"));
				act.setActivityDate(rs.getDate("activity_date"));
				act.setClubName(rs.getString("club_name"));
				list.add(act);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("기간별 내 활동 조회 중 오류", e);
		}
		return list;
	} // end getActivityInPeriod
	
	// ActivityDAO.java 에 추가
	public List<Activity> getActivityInMypageBetweenDates(
			int memberId, String fromDateStr, String toDateStr) {
		String sql = "SELECT a.title, a.activity_description, a.activity_date, c.club_name " +
				"  FROM participation p " +
				"  JOIN activity a ON p.activity_id = a.activity_id " +
				"  JOIN club     c ON a.club_id       = c.club_id " +
				" WHERE p.member_id = ? " +
				"   AND a.is_deleted = 'N' " +
				"   AND p.participation_date BETWEEN "
				+ "TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') " +
				" ORDER BY p.participation_date DESC";
		List<Activity> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, memberId);
			ps.setString(2, fromDateStr);
			ps.setString(3, toDateStr);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Activity act = new Activity();
				act.setTitle(rs.getString("title"));
				act.setActivityDescription(rs.getString("activity_description"));
				act.setActivityDate(rs.getDate("activity_date"));
				act.setClubName(rs.getString("club_name"));
				list.add(act);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("기간별 마이페이지 활동 조회 중 오류", e);
		}
		return list;
	} // end getActivityInMypageBetweenDates
	
	// ActivityDAO.java
	public List<Activity> getMyActivitiesByPeriod(int memberId, java.sql.Date from, java.sql.Date to) {
		List<Activity> list = new ArrayList<>();
		String sql = "SELECT a.title, a.activity_description, a.activity_date, c.club_name " +
				"  FROM participation p " +
				"  JOIN activity a ON p.activity_id = a.activity_id " +
				"  JOIN club c    ON a.club_id      = c.club_id " +
				" WHERE p.member_id = ? " +
				"   AND a.is_deleted = 'N' " +
				"   AND a.activity_date BETWEEN ? AND ? " +
				" ORDER BY a.activity_date";
		try (Connection con = dataSource.getConnection()) {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, memberId);
			ps.setDate(2, from);
			ps.setDate(3, to);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Activity a = new Activity();
				a.setTitle(rs.getString("title"));
				a.setActivityDescription(rs.getString("activity_description"));
				a.setActivityDate(rs.getDate("activity_date"));
				a.setClubName(rs.getString("club_name"));
				list.add(a);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("기간별 내 활동 조회 중 오류", e);
		}
		return list;
	} // end getMyActivitiesByPeriod
	
	// 카테고리별 내 활동 보기
	public List<Activity> getMyActivitiesByCategory(int memberId, int categoryId) {
		List<Activity> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT DISTINCT a.activity_id, a.title, a.activity_description, a.activity_date, c.club_name " +
					"  FROM participation p " +
					"  JOIN activity a ON p.activity_id = a.activity_id " +
					"  JOIN club c     ON a.club_id       = c.club_id " +
					" WHERE p.member_id  = ? " +
					"   AND a.category_id = ? " +
					"   AND a.is_deleted  = 'N' " +
					" ORDER BY a.activity_date ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, categoryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Activity a = new Activity();
				a.setActivityId(rs.getInt("activity_id"));
				a.setTitle(rs.getString("title"));
				a.setActivityDescription(rs.getString("activity_description"));
				a.setActivityDate(rs.getDate("activity_date"));
				a.setClubName(rs.getString("club_name"));
				list.add(a);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("카테고리별 내 활동 조회 중 오류", e);
		}
		return list;
	} // end getMyActivitiesByPeriod
	
	// 동아리 활동 보기(미래)
	public List<Activity> getUpcomingActivitiesByClubId(int clubId, Date today, int memberId) {
		List<Activity> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT activity_id, title, activity_description, activity_date "
					+ "FROM ( "
					+ "    SELECT activity_id, title, activity_description, activity_date "
					+ "	   FROM activity "
					+ "    WHERE club_id = ? "
					+ "    AND is_deleted = 'N' "
					+ "    AND activity_date >= ? "
					+ "    MINUS "
					+ "    SELECT a.activity_id, a.title, a.activity_description, a.activity_date"
					+ "    FROM participation p "
					+ "    JOIN activity a ON a.activity_id=p.activity_id "
					+ "    WHERE p.member_id=? "
					+ ") "
					+ "ORDER BY activity_date ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			stmt.setDate(2, today);
			stmt.setInt(3, memberId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Activity a = new Activity();
				a.setActivityId(rs.getInt("activity_id"));
				a.setTitle(rs.getString("title"));
				a.setActivityDescription(rs.getString("activity_description"));
				a.setActivityDate(rs.getDate("activity_date"));
				list.add(a);
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("향후 활동 조회 중 오류", e);
		}
		return list;
	} // end getUpcomingActivitiesByClubId
	
	//동아리 활동 내역 보기(과거)
	public List<Activity> getPastActivitiesByClubId(int clubId, Date today) {
		List<Activity> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT activity_id, title, activity_description, activity_date " +
					"  FROM activity " +
					" WHERE club_id = ? " +
					"   AND is_deleted = 'N' " +
					"   AND activity_date < ? " +
					" ORDER BY activity_date DESC";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			stmt.setDate(2, today);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Activity a = new Activity();
				a.setActivityId(rs.getInt("activity_id"));
				a.setTitle(rs.getString("title"));
				a.setActivityDescription(rs.getString("activity_description"));
				a.setActivityDate(rs.getDate("activity_date"));
				list.add(a);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("과거 활동 조회 중 오류", e);
		}
		return list;
	} // end getPastActivitiesByClubId
	
	public int getSeqActivityId() {
		int seqId = 0;
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT seq_activity_id.NEXTVAL AS id FROM dual";
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				seqId = rs.getInt("id");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return seqId;
	} // end getSeqActivityId
}
