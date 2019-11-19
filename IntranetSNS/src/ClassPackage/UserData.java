package ClassPackage;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.1.2
클래스 이름 : UserData
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 사용자 테이블에서 가져온 데이터를 담아두는 클래스

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
