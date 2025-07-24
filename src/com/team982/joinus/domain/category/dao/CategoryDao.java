package com.team982.joinus.domain.category.dao;

import java.sql.Connection;
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

import com.team982.joinus.domain.category.model.Category;

public class CategoryDao {
	static DataSource dataSource;

	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 카테고리 추가
	public int insertCategory(Category cat) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "insert into category (category_id, category_name, parent_id) "
					+ "values (seq_category_id.NEXTVal,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cat.getCategoryName());
			if(cat.getParentId() == null) {
				stmt.setNull(2, Types.INTEGER);
			}else {
				stmt.setInt(2, cat.getParentId());
			}
			return stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("카테고리 등록 중 오류 발생", e);
		}
	} // end insertCategory
	
	// 카테고리 수정
	public int updateCategory(Category cat) {
		try (Connection con = dataSource.getConnection()) {
			String sql = "UPDATE category "
					   + "SET category_name=?, parent_id=? "
					   + "WHERE category_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cat.getCategoryName());
			if(cat.getParentId() == null) {
				stmt.setNull(2, Types.INTEGER);
			}else {
				stmt.setInt(2, cat.getParentId());
			}
			stmt.setInt(3, cat.getCategoryId());
			return stmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("카테고리 수정 중 오류",e);
		}
	} // end updateCategory
	
	// 카테고리 삭제
	public int deleteCategory(int categoryId) {
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			String sql = "delete from category where category_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, categoryId);
			int result = stmt.executeUpdate();
			con.commit();
			return result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {con.rollback();}catch(Exception e2) {}
			throw new RuntimeException(e);
		}finally {
			try {con.setAutoCommit(true);}catch(Exception e) {}
			try {con.close();}catch(Exception e) {}
		}
	} // end deleteCategory
	
	// 카테고리 전체 조회
	public List<Category> getCategoryAll(){
		List<Category> catList = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			String sql = "select category_id, category_name,parent_id "
					+ "from category order by category_id";
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Category cat = new Category();
				cat.setCategoryId(rs.getInt("category_id"));
				cat.setCategoryName(rs.getString("category_name"));
				cat.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null );
				catList.add(cat);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		return catList;
	} // end getCategoryAll
}
