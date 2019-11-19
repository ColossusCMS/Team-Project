package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassPackage.User;
import ClassPackage.UserData;
import EncryptionDecryption.PasswordEncryption;
import InitializePackage.InitializeDao;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : Dao
패키지 버전 : 1.0.0
클래스 이름 : LoginDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 로그인을 시도하거나 중복된 사용자 번호 체크 또는 이메일 확인을 위해 데이터베이스에서 검색해 결과를 가져옴
- 사용자 등록 창에서 입력한 정보들을 데이터베이스의 사용자 테이블에 저장

사용한 외부 라이브러리
- mysql-connector-java-5.1.47.jar

패키지 버전 변경 사항
 */
public class LoginDao {
	UserData userData;

	// 로그인을 시도했을 때 DB에서 검색해서 동일한 값이 있는지 체크해줌
	// 있으면 true, 없으면 false리턴
	public String chkUserData(String userNo, String userPassword) {
		String sql = "select username from usertbl where userno = ? and userpassword = ?;";
		String encPassword = PasswordEncryption.pwEncryption(userPassword);
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			pstmt.setString(2, encPassword);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) { // 검색했는데 만약 값이 출력이 되었다면 정보가 맞다는 뜻이니 이름을 리턴함
				return rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 확인 메일을 보내기 위해 데이터베이스에서 사용자 정보를 검색하고 결과를 가져옴
	public UserData chkUserNameMail(String userName, String userMail) {
		String sql = "select userno, username, userpassword, usermail from usertbl where username = ? and usermail = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, userMail);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) { // 검색했는데 만약 값이 출력이 되었다면 정보가 맞다는 뜻이니 true를 리턴함
				String decPassword = PasswordEncryption.pwDecryption(rs.getString("userpassword"));
				userData = new UserData(rs.getString("userno"), rs.getString("username"), decPassword,
						rs.getString("usermail"));
				return userData;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 사용자번호가 중복인지 데이터베이스에서 확인하는 메서드
	public boolean chkUserNo(String userNo) {
		String sql = "select userno from usertbl where userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) { // 만약 db로 검색했는데 결과가 나왔다면(중복된 사원번호가 존재한다는 의미)
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 사용자번호가 중복인지 데이터베이스에서 확인하는 메서드
	public boolean chkUserMail(String userMail) {
		String sql = "select userno from usertbl where usermail = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userMail);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) { // 만약 db로 검색했는데 결과가 나왔다면(중복된 이메일이 존재한다는 의미)
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 전화번호 중복인지 체크하는 메서드
	public boolean chkUserTel(String userTel) {
		String sql = "select userno from usertbl where usertel = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userTel);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) { // 만약 db로 검색했는데 결과가 나왔다면(중복된 전화번호가 존재한다는 의미)
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 데이터베이스에 사용자 정보를 등록하는 메서드
	public void insertUserData(User user) {
		String sql = "insert into usertbl values (?, ?, ?, ?, ?, ?, ?, default, ?, default, default);";
		String encPassword = PasswordEncryption.pwEncryption(user.getUserPassword());
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserNo());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, encPassword);
			pstmt.setString(4, user.getUserMail());
			pstmt.setString(5, user.getUserTel());
			pstmt.setString(6, user.getUserImgPath());
			pstmt.setString(7, user.getUserDept());
			pstmt.setString(8, user.getUserStatusMsg());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 중복 로그인 방지용. userLoginStatus를 가져옴
	public int getLoginStatus(String userNo) {
		String sql = "select userloginstatus from usertbl where userno = ?;";
		int status = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				status = rs.getInt("userloginstatus");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	// 로그인할 때 로그인 상태를 1로, 로그아웃할 때 로그인 상태를 0으로 업데이트하는 메서드
	public void updateLoginStatus(String userNo, String status) {
		String sql = "update usertbl set userloginstatus = ? where userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			if (status.equals("login")) { // 로그인하는 거라면 0을 1로 바꿔야 함
				pstmt.setInt(1, 1);
			} else if (status.equals("logout")) { // 로그아웃하는 거라면 1을 0으로 바꿔야 함
				pstmt.setInt(1, 0);
			}
			pstmt.setString(2, userNo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
