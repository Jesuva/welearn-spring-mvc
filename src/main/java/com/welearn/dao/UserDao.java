package com.welearn.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welearn.mapper.LoginMapper;
import com.welearn.mapper.UserMapper;
import com.welearn.model.Login;
import com.welearn.model.User;

@Service
public class UserDao extends JdbcDaoSupport implements UserInterface {
	 
	 @Autowired
	    public UserDao(DataSource datasource) {
	        this.setDataSource(datasource);
	    }
	 
	 public Login findUser(String email,String password) {
		 	try {
				MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
				sha2.update(password.getBytes());
				BigInteger hash = new BigInteger(1,sha2.digest());
				String HashPassword = hash.toString(16);

		        String sql = "Select u.userName,u.email,u.password "//
		                + " from users u where u.email = ? and u.password = ? ";
		 
		        Object[] params = new Object[] { email,HashPassword };
		        LoginMapper mapper = new LoginMapper();
		        Login userInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
	            return userInfo;
			} 
		 	catch (EmptyResultDataAccessException e) {
	            return null;
	        }		 	
		 	catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		 	 
	    }
	 
	 public void addUser(String name,String email,String password) {
		 
		 try {
				MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
				sha2.update(password.getBytes());
				BigInteger hash = new BigInteger(1,sha2.digest());
				String HashPassword = hash.toString(16);
			 	sha2.update(email.getBytes());
			 	BigInteger mailHash = new BigInteger(1,sha2.digest());
			 	String userId = mailHash.toString(16);
			 String sql = "insert into users (`userId`, `userName`, `email`, `password`) VALUES (?,?,?,?)";
			 Object[] params = new Object[] {userId,name,email,HashPassword};
			 int userInfo = this.getJdbcTemplate().update(sql, params);
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		   
	 }

	public boolean checkUserMail(String email) {
		
		try {
			String sql = "Select count(*) "//
	                + " from users u where u.email = ?";
	 
	        Object[] params = new Object[] { email};
	        int userCount = this.getJdbcTemplate().queryForObject(sql, params, Integer.class);
            if(userCount==0) {
            	return true;
            }
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	
}
