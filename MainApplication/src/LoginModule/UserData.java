package LoginModule;
/*
프로젝트 주제 : 사내 SNS
모듈 이름 : 로그인
클래스 이름 : UserData
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
- 

버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화
 */
public class UserData {
	private String userNo;
	private String userName;
	private String userPw;
	private String userMail;
	
	public UserData(String userNo, String userName, String userPw, String userMail) {
		this.userNo = userNo;
		this.userName = userName;
		this.userPw = userPw;
		this.userMail = userMail;
	}
	
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
}
