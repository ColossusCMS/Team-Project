package LoginModule;

import EncryptionDecryption.PasswordEncryption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
프로젝트 주제 : 사내 SNS
모듈 이름 : 로그인
클래스 이름 : LoginDao
버전 : 1.1.0
해당 클래스 작성 : 최문석

필요 전체 Java파일
- LoginMain.java (로그인 화면이 실행되는 메인 클래스)
- LoginDao.java (데이터베이스 접속, 데이터 불러오기, 데이터 삽입 등)
- LoginController.java (로그인 창 컨트롤러)
- SignUpController.java (사용자 등록 창 컨트롤러)
- FindAccountController.java (계정 찾기 창 컨트롤러)
- User.java (사용자 등록에 사용하는 사용자 정보 클래스[사용자의 모든 정보를 담고 있음])
- UserData.java (계정 찾기에서 사용하는 사용자 정보 클래스[사용자번호, 이름, 이메일, 비밀번호])

필요 fxml파일
- login.fxml (로그인 창 fxml)
- signUp.fxml (사용자등록 창 fxml)
- findAccount.fxml (계정 찾기 창 fxml)

필요 import 사용자 정의 package
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 데이터베이스에 접속
- 로그인을 시도하거나 중복된 사용자 번호 체크 또는 이메일 확인을 위해 데이터베이스에서 검색해 결과를 가져옴
- 사용자 등록 창에서 입력한 정보들을 데이터베이스로 전달해 저장

버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화
 */
public class LoginDao {
	private Connection conn;
	private static final String USERNAME = "sample";
	private static final String PASSWORD = "9999";
	private static final String URL = "jdbc:mysql://125.185.21.163:3306/sampledb";
//	private static final String URL = "jdbc:mysql://192.168.219.14:3306/sampledb";
	
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
	
	//확인 메일을 보내기 위해 데이터베이스에서 사용자 정보를 검색하고 결과를 가져옴
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
	
	//사용자번호가 중복인지 데이터베이스에서 확인하는 메서드
	public boolean chkUserNo(String userNo) {
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
	
	//사용자번호가 중복인지 데이터베이스에서 확인하는 메서드
	public boolean chkUserMail(String userMail) {
		String sql = "select userno from login where usermail = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
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
	
	//데이터베이스에 사용자 정보를 등록하는 메서드
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
