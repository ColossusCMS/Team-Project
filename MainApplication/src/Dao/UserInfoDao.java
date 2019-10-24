package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassPackage.User;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 로그인 데이터베이스 클래스
모듈 버전 : 0.7.0
클래스 이름 : UserInfoDao
해당 클래스 작성 : 최문석

필요 전체 Java파일
- UserInfoDao.java (사용자 테이블 데이터베이스 접속, 데이터 불러오기, 데이터 삽입 등)

필요 import 사용자 정의 package
- InitializePackage.InitializeDao (데이터 베이스 접속 초기화)
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 데이터베이스에 접속
- 등록된 사용자들을 가져오기 위한 클래스
- 사용자 목록, 상세 정보 등에서 정보를 출력하기 위해 사용

버전 변경 사항
1.0.0

 */
public class UserInfoDao {
	public User selectMyInfo(String myNo) {
		User user = null;
		String sql = "select * from usertbl where userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, myNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				user = new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"), rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"), rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"), rs.getInt("adminavailable"));
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
	
	//자신을 제외한 등록된 사용자 모두를 가져오는 쿼리
	//탭에서 전체 선택시 사용
	public void loadAllUser(ObservableList<User> userList, String userId) {
		String sql = "select * from usertbl where userno not in (?);";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while(rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"), rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"), rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"), rs.getInt("adminavailable")));
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
	
	//전체 출력인데 입력값으로 필터링하는 쿼리
	public void loadAllUser(ObservableList<User> userList, String userId, String filterText) {
		String sql = "select * from usertbl where userno not in (?) and username = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, filterText);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while(rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"), rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"), rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"), rs.getInt("adminavailable")));
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
	
	
	//자신을 제외한 해당 부서의 사용자만 가져오는 쿼리
	//탭에서 부서 선택했을 때 사용
	public void loadFilteredAllUser(ObservableList<User> userList, String userId, String dept) {
		String sql = "select * from usertbl where userno not in (?) and userdept = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, dept);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while(rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"), rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"), rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"), rs.getInt("adminavailable")));
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
	
	//자신을 제외한 해당 부서의 사용자들을 필터에 맞게 가져오는 쿼리
	public void loadFilteredAllUser(ObservableList<User> userList, String userId, String dept, String filterText) {
		String sql = "select * from usertbl where userno not in (?) and userdept = ? and username = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, dept);
			pstmt.setString(3, filterText);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while(rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"), rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"), rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"), rs.getInt("adminavailable")));
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
	
	//필드로만 필터링
	//매개변수 수정요망
	public void loadFilteredAllUser(String userId, ObservableList<User> userList, String filterText) {
		String sql = "select * from usertbl where userno not in (?) and username = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, filterText);
			ResultSet rs = pstmt.executeQuery();
			userList.clear();
			while(rs.next()) {
				userList.add(new User(rs.getString("userno"), rs.getString("username"), rs.getString("userpassword"), rs.getString("usermail"), rs.getString("usertel"), rs.getString("userimgpath"), rs.getString("userdept"), rs.getString("userposition"), rs.getString("userstatusmsg"), rs.getInt("adminavailable")));
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
}
