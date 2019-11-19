package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassPackage.User;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : Dao
패키지 버전 : 1.0.0
클래스 이름 : UserInfoDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 데이터베이스의 사용자 테이블에 데이터를 저장하거나 가져오는 메서드를 포함한 클래스

사용한 외부 라이브러리
- mysql-connector-java-5.1.47.jar

패키지 버전 변경 사항
 */
public class UserInfoDao {
	// 현재 사용하고 있는 사용자의 정보를 가져오는 메서드
	public User selectMyInfo(String myNo) {
		User user = null;
		String sql = "select * from usertbl where userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, myNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"),
						rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"),
						rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"),
						rs.getInt("adminavailable"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	// 자신을 제외한 등록된 사용자 모두를 가져오는 쿼리
	// 탭에서 전체 선택시 사용
	public void loadAllUser(String side, ObservableList<User> userList, String userId) {
		String sql = new String();
		if (side.equals("right")) {
			sql = "select * from usertbl where userno not in (?) and userLoginStatus = 1;";
		} else if (side.equals("center")) {
			sql = "select * from usertbl where userno not in (?);";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while (rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"),
						rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"),
						rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"),
						rs.getInt("adminavailable")));
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
	}

	// 전체 출력인데 입력값으로 필터링하는 쿼리
	public void loadAllUser(String side, ObservableList<User> userList, String userId, String filterText) {
		String sql = new String();
		if (side.equals("right")) {
			sql = "select * from usertbl where userno not in (?) and username = ? and userLoginStatus = 1;";
		} else if (side.equals("center")) {
			sql = "select * from usertbl where userno not in (?) and username = ?;";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, filterText);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while (rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"),
						rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"),
						rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"),
						rs.getInt("adminavailable")));
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
	}

	// 자신을 제외한 해당 부서의 사용자만 가져오는 쿼리
	// 탭에서 부서 선택했을 때 사용
	public void loadFilteredAllUser(String side, ObservableList<User> userList, String userId, String dept) {
		String sql = new String();
		if (side.equals("right")) {
			sql = "select * from usertbl where userno not in (?) and userdept = ? and userLoginStatus = 1;";
		} else if (side.equals("center")) {
			sql = "select * from usertbl where userno not in (?) and userdept = ?;";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, dept);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while (rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"),
						rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"),
						rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"),
						rs.getInt("adminavailable")));
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
	}

	// 자신을 제외한 해당 부서의 사용자들을 필터에 맞게 가져오는 쿼리
	public void loadFilteredAllUser(String side, ObservableList<User> userList, String userId, String dept,
			String filterText) {
		String sql = new String();
		if (side.equals("right")) {
			sql = "select * from usertbl where userno not in (?) and userdept = ? and username = ? and userLoginStatus = 1;";
		} else if (side.equals("center")) {
			sql = "select * from usertbl where userno not in (?) and userdept = ? and username = ?;";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, dept);
			pstmt.setString(3, filterText);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while (rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"),
						rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"),
						rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"),
						rs.getInt("adminavailable")));
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
	}

	// 필드로만 필터링
	// 매개변수 수정요망
	public void loadFilteredAllUser(String side, String userId, ObservableList<User> userList, String filterText) {
		String sql = new String();
		if (side.equals("right")) {
			sql = "select * from usertbl where userno not in (?) and username = ? and userLoginStatus = 1;";
		} else if (side.equals("center")) {
			sql = "select * from usertbl where userno not in (?) and username = ?;";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, filterText);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while (rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"),
						rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"),
						rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"),
						rs.getInt("adminavailable")));
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
	}

	// 사용자 상태 메시지 갱신하는 메서드
	public void updateStatusMsg(String statusMsg, String userId) {
		String sql = "update usertbl set userstatusmsg = ? where userno = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, statusMsg);
			pstmt.setString(2, userId);
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
