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
프로그램 버전 : 0.7.0
모듈 이름 : 로그인 데이터베이스 클래스
모듈 버전 : 1.1.0
클래스 이름 : LoginDao
해당 클래스 작성 : 최문석

필요 전체 Java파일
- LoginDao.java (데이터베이스 접속, 데이터 불러오기, 데이터 삽입 등)

필요 import 사용자 정의 package
- InitializePackage.InitializeDao (데이터 베이스 접속 초기화)
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 데이터베이스에 접속
- 로그인을 시도하거나 중복된 사용자 번호 체크 또는 이메일 확인을 위해 데이터베이스에서 검색해 결과를 가져옴
- 사용자 등록 창에서 입력한 정보들을 데이터베이스로 전달해 저장

버전 변경 사항
1.1.0
- 데이터베이스 접속 부분을 삭제하고 static 변수를 호출해 데이터베이스 재접속 및 지연 방지
- 이메일 체크하는 메서드 추가
 */
public class LoginDao {
	UserData ud;

	//로그인을 시도했을 때 DB에서 검색해서 동일한 값이 있는지 체크해줌
	//있으면 true, 없으면 false리턴
	public String chkUserData(String userNo, String userPassword) {
		String sql = "select username from usertbl where userno = ? and userpassword = ?;";
		String encPassword = PasswordEncryption.pwEncryption(userPassword);
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			pstmt.setString(2, encPassword);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {	//검색했는데 만약 값이 출력이 되었다면 정보가 맞다는 뜻이니 이름을 리턴함
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
	
	//확인 메일을 보내기 위해 데이터베이스에서 사용자 정보를 검색하고 결과를 가져옴
	public UserData chkUserNameMail(String userName, String userMail) {
		String sql = "select userno, username, userpassword, usermail from usertbl where username = ? and usermail = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, userMail);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {	//검색했는데 만약 값이 출력이 되었다면 정보가 맞다는 뜻이니 true를 리턴함
				String decPassword = PasswordEncryption.pwDecryption(rs.getString("userpassword"));
				ud = new UserData(rs.getString("userno"), rs.getString("username"), decPassword, rs.getString("usermail"));
				return ud;
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
	
	//사용자번호가 중복인지 데이터베이스에서 확인하는 메서드
	public boolean chkUserNo(String userNo) {
		String sql = "select userno from usertbl where userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {	//만약 db로 검색했는데 결과가 나왔다면(중복된 사원번호가 존재한다는 의미)
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
	
	//사용자번호가 중복인지 데이터베이스에서 확인하는 메서드
	public boolean chkUserMail(String userMail) {
		String sql = "select userno from usertbl where usermail = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userMail);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {	//만약 db로 검색했는데 결과가 나왔다면(중복된 이메일이 존재한다는 의미)
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
	
	//전화번호 중복인지 체크하는 메서드
	public boolean chkUserTel(String userTel) {
		String sql = "select userno from usertbl where usertel = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userTel);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {	//만약 db로 검색했는데 결과가 나왔다면(중복된 전화번호가 존재한다는 의미)
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
	
	//데이터베이스에 사용자 정보를 등록하는 메서드
	public void insertUserData(User user) {
		String sql = "insert into usertbl values (?, ?, ?, ?, ?, ?, ?, default, ?, default);";
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
}
