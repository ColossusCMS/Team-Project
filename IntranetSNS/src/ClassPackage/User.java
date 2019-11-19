package ClassPackage;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.1.2
클래스 이름 : UserData
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 사용자 등록 창에서 입력받은 모든 정보를 하나의 클래스로 만들어 데이터베이스로 전송할 때 사용

패키지 버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화

1.1.1
- Dao 인스턴스 통합 (데이터 베이스 초기화 클래스 생성)
- 콤보박스의 내용을 데이터베이스와 연동

1.1.2
- 사용자 직급, 상태 메시지용, 관리자확인 변수 추가
 */
public class User {
	private String userNo;
	private String userName;
	private String userPassword;
	private String userMail;
	private String userTel;
	private String userImgPath;
	private String userDept;
	private String userPosition;
	private String userStatusMsg;
	private Integer adminAvailable;

	public User() {
	}

	public User(String userNo, String userName, String userPassword, String userMail, String userTel,
			String userImgPath, String userDept, String userPosition, String userStatusMsg, Integer adminAvailable) {
		this.userNo = userNo;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userMail = userMail;
		this.userTel = userTel;
		this.userImgPath = userImgPath;
		this.userDept = userDept;
		this.userPosition = userPosition;
		this.userStatusMsg = userStatusMsg;
		this.adminAvailable = adminAvailable;
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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(String userPosition) {
		this.userPosition = userPosition;
	}

	public String getUserStatusMsg() {
		return userStatusMsg;
	}

	public void setUserStatusMsg(String userStatusMsg) {
		this.userStatusMsg = userStatusMsg;
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

	public String getUserImgPath() {
		return userImgPath;
	}

	public void setUserImgPath(String userImgPath) {
		this.userImgPath = userImgPath;
	}

	public String getUserDept() {
		return userDept;
	}

	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}

	public Integer getAdminAvailable() {
		return adminAvailable;
	}

	public void setAdminAvailable(Integer adminAvailable) {
		this.adminAvailable = adminAvailable;
	}
}
