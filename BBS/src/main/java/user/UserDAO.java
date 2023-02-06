package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "(my root password)";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int login(String userID, String userPassword) {
		String SQL = "Select userPassword FROM USER WHERE userID = ?";	//SQL Injection을 방어하기 위한 수단이다.
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1;	//로그인 성공
				}
				else
					return 0;	//비밀번호 틀림
			}
			return -1;	// 회원 아이디를 넣은 값에 매칭되는 값이 없으면 아이디가 없음을 알려준다.
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -2; // 데이터베이스 오류
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public User getmyInfo(String userID) {
		User mf = null;
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			 if(rs.next()) {
					mf = new User();
					mf.setUserID(rs.getString("userID"));
					mf.setUserName(rs.getString("userName"));
					mf.setUserGender(rs.getString("userGender"));
					mf.setUserEmail(rs.getString("userEmail"));
			 }
		} catch(Exception e) {
			e.printStackTrace();
		}
		return mf; 
	}
	public int updateMember(User user) {
		String SQL = "Select userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(user.getUserPassword().equals(rs.getString("userPassword"))) {
					SQL = "UPDATE user SET userName = ?, userGender = ?, userEmail = ? " + "where userID = ?";
					pstmt = conn.prepareStatement(SQL);
					pstmt.setString(1, user.getUserName());
					pstmt.setString(2, user.getUserGender());
					pstmt.setString(3, user.getUserEmail());
					pstmt.setString(4, user.getUserID());
					pstmt.executeUpdate();
					//System.out.println("회원정보수정성공");
					return 1;
				} else {
					return 0;
					// System.out.println("비밀번호가 틀렸습니다.");
				}
			} else {
				return -1;
				// System.out.println("존재하지 않는 아이디 - 회원 정보 수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
}
