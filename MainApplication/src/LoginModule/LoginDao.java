package LoginModule;

import EncryptionDecryption.PasswordEncryption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
프로젝트 제목 : 로그인
버전 : 0.9.0
작성 : 최문석

필요 Java파일
- Main.java (로그인 화면이 실행되는 메인 클래스)
- LoginDao.java (데이터베이스 접속, 데이터 불러오기)
- LoginController.java (사용자 등록창 컨트롤러)

필요 fxml파일 :
- login.fxml (로그인창 fxml)
- chkDialog.fxml (안내 다이얼로그 fxml)

주요 기능
- 로그인을 위한 사용자 정보 입력(사원번호, 비밀번호),
- 데이터베이스에서 사용자 정보를 가져오기 위한 데이터베이스 연동,
- 해당하는 데이터가 존재하는지 체크를 위해 데이터베이스로부터 값을 읽어옴
- 모든 조건을 만족한다면 로그인에 성공하고 다음 화면으로 넘어감
- 사용자 등록 버튼이나 계정 찾기 버튼을 누르면 해당하는 창을 새로 띄움
 */
public class LoginDao {
	private Connection conn;
	private static final String USERNAME = "sample";
	private static final String PASSWORD = "9999";
//	private static final String URL = "jdbc:mysql://125.185.21.163:3306/sampledb";
	private static final String URL = "jdbc:mysql://192.168.219.14:3306/sampledb";
	
	UserData ud;
	
	public LoginDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("드라이버 로딩 성공");
		} catch (Exception e) {
			System.out.println("드라이버 로드 실패");
			e.printStackTrace();
		}
	}
	
	//로그인을 시도했을 때 DB에서 검색해서 동일한 값이 있는지 체크해줌
	//있으면 true, 없으면 false리턴
	public String chkUserData(String userNo, String password) {
		String sql = "select username from login where userno = ? and userpw = ?;";
		String encPassword = PasswordEncryption.pwEncryption(password);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
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
	
	public UserData chkUserNameMail(String userName, String userMail) {
		String sql = "select userno, username, userpw, usermail from login where username = ? and usermail = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, userMail);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {	//검색했는데 만약 값이 출력이 되었다면 정보가 맞다는 뜻이니 true를 리턴함
				String decPassword = PasswordEncryption.pwDecryption(rs.getString("userpw"));
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
	
	public boolean loadUserNo(String userNo) {
		String sql = "select userno from login where userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
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
	
	public void insertUserData(User user) {
		String sql = "insert into login values (?, ?, ?, ?, ?, ?, ?, null, 0);";
		String encPassword = PasswordEncryption.pwEncryption(user.getPassword());
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserNo());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, encPassword);
			pstmt.setString(4, user.getUserMail());
			pstmt.setString(5, user.getUserTel());
			pstmt.setString(6, user.getImgPath());
			pstmt.setString(7, user.getDept());
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
