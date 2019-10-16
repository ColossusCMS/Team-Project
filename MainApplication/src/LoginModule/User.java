package LoginModule;
/*
 * 프로젝트 제목 : 사용자 등록
 * 버전 : 0.9.0
 * 작성 : 최문석
 * 
 * 필요 Java파일
 * - Main.java (사용자 등록이 실행되는 메인 클래스)
 * - SignUpDao.java (데이터베이스 접속, 데이터 불러오기, 데이터 삽입)
 * - SignUpController.java (사용자 등록창 컨트롤러)
 * - User.java (사용자 정보 저장을 위한 클래스)
 * 
 * 필요 fxml파일 :
 * - signUp.fxml (사용자 등록창 fxml)
 * - chkDialog.fxml (안내 다이얼로그 fxml)
 * 
 * 주요 기능
 * - 사용자 등록을 위한 사용자 정보 입력 (사원번호, 이름, 비밀번호, 이메일, 전화번호, 대표사진),
 * - 데이터베이스에 정보를 저장하기 위해 데이터베이스 연동 및 데이터베이스 삽입,
 * - 중복체크를 위해 데이터베이스로부터 값을 읽어옴
 * 
 * 차후계획 : 이미지를 선택하는 기능에서 로컬이 아닌 웹에서 URL을 이용해 등록 가능하게 기능을 추가
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
