package LoginModule;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 로그인
모듈 버전 : 1.1.0
클래스 이름 : User
해당 클래스 작성 : 최문석

필요 모듈 Java파일
- LoginMain.java (로그인 화면이 실행되는 메인 클래스)
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
- Dao.LoginDao (로그인 정보를 데이터 베이스로 처리할 수 있는 메서드)
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 사용자 등록 창에서 입력받은 모든 정보를 하나의 클래스로 만들어 데이터베이스로 전송할 때 사용

모듈 버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화

1.1.1
- Dao 인스턴스 통합 (데이터 베이스 초기화 클래스 생성)
- 콤보박스의 내용을 데이터베이스와 연동
 */
public class User {
	private String userNo;
	private String userName;
	private String password;
	private String userMail;
	private String userTel;
	private String imgPath;
	private String dept;
	
	public User() {}
	
	public User(String userNo, String userName, String password, String userMail, String userTel, String imgPath, String dept) {
		super();
		this.userNo = userNo;
		this.userName = userName;
		this.password = password;
		this.userMail = userMail;
		this.userTel = userTel;
		this.imgPath = imgPath;
		this.dept = dept;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
}
