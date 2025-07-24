package com.team982.joinus.domain.club.dao;

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

import com.team982.joinus.domain.club.model.ClubDetailResponse;
import com.team982.joinus.domain.club.model.ClubInsertRequest;
import com.team982.joinus.domain.club.model.ClubListByMemberIdResponse;
import com.team982.joinus.domain.club.model.ClubUpdateRequest;

public class ClubDao {
	static DataSource dataSource;

	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 동아리 생성
	public int insertClub(ClubInsertRequest club) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "INSERT INTO club (club_id, club_name, member_id, max_member_count) "
					   + "VALUES (seq_club_id.NEXTVAL, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, club.getClubName());
			stmt.setInt(2, club.getMemberId());
			stmt.setInt(3, club.getMaxMemberCount());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end insertClub
	
	// 동아리 수정
	public int updateClub(ClubUpdateRequest club) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE club"
					   + "SET club_name=?, club_description=?, "
					   + "member_id=?, max_member_count=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, club.getClubName());
			stmt.setString(2, club.getClubDescription());
			stmt.setInt(3, club.getMemberId());
			stmt.setInt(4, club.getMaxMemberCount());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end updateClub
	
	// 동아리 삭제
	public int deleteClub(int clubId) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE club "
					   + "SET is_deleted='Y' "
					   + "WHERE club_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end deleteClub
	
	// 동아리에 회원 추가
	public int insertClubMember(int clubId, int memberId) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "INSERT INTO club_member (club_id, member_id) "
					   + "VALUES (?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			stmt.setInt(2, memberId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end insertClubMember
	
	// 동아리 탈퇴
	public int deleteClubMember(int clubId, int memberId) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "DELETE FROM club_member "
					   + "WHERE club_id=? AND member_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			stmt.setInt(2, memberId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end deleteClubMember
	
	// 동아리 가입 인원 수 조회
	public int getClubMemberCount(int clubId) {
		int count = 0;
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT COUNT(*) AS count "
					   + "FROM club_member "
					   + "WHERE club_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
				return count;
			} else {
				System.out.println("동아리 가입 인원 수 조회 실패");
				throw new RuntimeException("동아리 가입 인원 수 조회에 실패하였습니다.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end getClubMemberCount
	
	// 회원 아이디로 가입된 동아리 조회
	public List<ClubListByMemberIdResponse> getClubListByMemberId(int memberId) {
		List<ClubListByMemberIdResponse> clubList = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT cm.club_id, c.club_name "
					   + "FROM club_member cm "
					   + "JOIN club c ON cm.club_id=c.club_id "
					   + "WHERE cm.member_id=? AND is_deleted='N'"
					   + "ORDER BY cm.join_date";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ClubListByMemberIdResponse club = new ClubListByMemberIdResponse();
				club.setClubId(rs.getInt("club_id"));
				club.setClubName(rs.getString("club_name"));
				clubList.add(club);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		return clubList;
	} // end getClubListByMemberId
	
	// 동아리 이름으로 동아리 아이디 조회
	public int getClubIdByClubName(String clubName) {
		int clubId = 0;
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT club_id "
					   + "FROM club "
					   + "WHERE club_name=? AND is_deleted='N'";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, clubName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				clubId = rs.getInt("club_id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		return clubId;
	} // end getClubIdByClubName
	
	// 동아리 아이디로 동아리 상세정보 조회
	public ClubDetailResponse getClubDetailById(int clubId) {
		ClubDetailResponse cd = new ClubDetailResponse();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT "
					   + "    c.club_id, club_name, club_description, "
					   + "    member_name, max_member_count "
					   + "FROM club c "
					   + "JOIN member m ON c.member_id=m.member_id "
					   + "WHERE c.club_id=? AND c.is_deleted='N'";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				cd.setClubId(clubId);
				cd.setClubName(rs.getString("club_name"));
				cd.setClubDescription(rs.getString("club_description"));
				cd.setMemberName(rs.getString("member_name"));
				cd.setMaxMemberCount(rs.getInt("max_member_count"));
				cd.setCurrentMemberCount(this.getClubMemberCount(clubId));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		return cd;
	} // end getClubDetailById
	
	// 동아리 전체 목록 출력(로그인되어 있는 아이디가 가입된 동아리 제외)
	public List<ClubDetailResponse> getClubList(int memberId) {
		List<ClubDetailResponse> clubList = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT club_id, club_name, max_member_count "
					   + "FROM club "
					   + "WHERE is_deleted='N' "
					   + "MINUS "
					   + "SELECT c.club_id, c.club_name, max_member_count "
					   + "FROM club c "
					   + "JOIN club_member cm ON c.member_id=cm.member_id "
					   + "WHERE cm.member_id=? AND c.is_deleted='N'";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, memberId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ClubDetailResponse cd = new ClubDetailResponse();
				cd.setClubId(rs.getInt("club_id"));
				cd.setClubName(rs.getString("club_name"));
				cd.setMaxMemberCount(rs.getInt("max_member_count"));
				cd.setCurrentMemberCount(this.getClubMemberCount(cd.getClubId()));
				clubList.add(cd);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		return clubList;
	} // end getClubList
	
	// 동아리 이름 중복 체크
	public boolean isClubNameDuplicate(String ClubName) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT club_id "
					   + "FROM club "
					   + "WHERE club_name=? AND is_deleted='N'";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, ClubName);
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
	} // end isClubNameDuplicate
	
	// 동아리장 여부
	public boolean isClubLeader(int clubId, int memberId) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "SELECT club_id "
					   + "FROM club "
					   + "WHERE club_id=? AND member_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, clubId);
			stmt.setInt(2, memberId);
			int result = stmt.executeUpdate();
			if (result > 0) {
				return true;
			} else {
				return false;				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end isClubLeader
	
	// 동아리 소개 수정
	public int updateClubDescriptionByClubId(int clubId, String description) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE club "
					+ "SET club_description=? "
					+ "WHERE club_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, description);
			stmt.setInt(2, clubId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end updateClubDescriptionByClubId
	
	// 동아리 최대 인원 수정
	public int updateClubMaxMemberCount(int clubId, int maxMemberCount) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE club "
					+ "SET max_member_count=? "
					+ "WHERE club_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, maxMemberCount);
			stmt.setInt(2, clubId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	} // end updateClubMaxMemberCount
}
