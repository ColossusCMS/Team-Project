package ClassPackage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 클래스 패키지
모듈 버전 : 1.1.2
클래스 이름 : UserData
해당 클래스 작성 : 최문석

필요모듈 Java파일
- UserData.java (계정 찾기에서 사용하는 사용자 정보 클래스[사용자번호, 이름, 이메일, 비밀번호])

필요 import 사용자 정의 package
- Dao.LoginDao (로그인 정보를 데이터 베이스로 처리할 수 있는 메서드)
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 

모듈 버전 변경 사항
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

//게시물을 등록하거나 게시물 열람, 수정, 삭제 시에 사용하는 클래스
public class Board {
	private Integer boardNo;
	private String boardHeader;
	private String boardTitle;
	private String boardContent;
	private String boardPassword;
	private String boardUserNo;
	private String boardDate;
	private String boardFile;
	private Integer boardAvailable;
	
	public Board(Integer boardNo, String boardHeader, String boardTitle, String boardContent, String boardPassword,
			String boardUserNo, String boardDate, String boardFile, Integer boardAvailable) {
		this.boardNo = boardNo;
		this.boardHeader = boardHeader;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardPassword = boardPassword;
		this.boardUserNo = boardUserNo;
		this.boardDate = boardDate;
		this.boardFile = boardFile;
		this.boardAvailable = boardAvailable;
	}
	public Integer getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}
	public String getBoardHeader() {
		return boardHeader;
	}
	public void setBoardHeader(String boardHeader) {
		this.boardHeader = boardHeader;
	}
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getBoardContent() {
		return boardContent;
	}
	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}
	public String getBoardPassword() {
		return boardPassword;
	}
	public void setBoardPassword(String boardPassword) {
		this.boardPassword = boardPassword;
	}
	public String getBoardUserNo() {
		return boardUserNo;
	}
	public void setBoardUserNo(String boardUserNo) {
		this.boardUserNo = boardUserNo;
	}
	public String getBoardDate() {
		return boardDate;
	}
	public void setBoardDate(String boardDate) {
		this.boardDate = boardDate;
	}
	public String getBoardFile() {
		return boardFile;
	}
	public void setBoardFile(String boardFile) {
		this.boardFile = boardFile;
	}
	public Integer getBoardAvailable() {
		return boardAvailable;
	}
	public void setBoardAvailable(Integer boardAvailable) {
		this.boardAvailable = boardAvailable;
	}
}
