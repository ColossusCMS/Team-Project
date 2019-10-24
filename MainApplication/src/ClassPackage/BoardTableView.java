package ClassPackage;

import javafx.beans.property.SimpleStringProperty;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 클래스 패키지
모듈 버전 : 1.1.2
클래스 이름 : BoardTableView
해당 클래스 작성 : 최문석

필요모듈 Java파일
- BoardTableView.java

필요 import 사용자 정의 package
- 

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

//테이블뷰 전용
//테이블뷰에서 리스트를 띄우기 출력하기 위해서
//SimpleStringProperty를 사용함.
public class BoardTableView {
	private SimpleStringProperty boardNo;
	private SimpleStringProperty boardHeader;
	private SimpleStringProperty boardTitle;
	private SimpleStringProperty boardWriter;
	private SimpleStringProperty boardDate;
	
	public BoardTableView(String boardNo, String boardHeader, String boardTitle, String boardWriter, String boardDate) {
		this.boardNo = new SimpleStringProperty(boardNo);
		this.boardHeader = new SimpleStringProperty(boardHeader);
		this.boardTitle = new SimpleStringProperty(boardTitle);
		this.boardWriter = new SimpleStringProperty(boardWriter);
		this.boardDate = new SimpleStringProperty(boardDate);
	}
	public String getBoardNo() {
		return boardNo.get();
	}
	public void setBoardNo(String boardNo) {
		this.boardNo.set(boardNo);
	}
	public String getBoardHeader() {
		return boardHeader.get();
	}
	public void setBoardHeader(String boardHeader) {
		this.boardHeader.set(boardHeader);
	}
	public String getBoardTitle() {
		return boardTitle.get();
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle.set(boardTitle);
	}
	public String getBoardWriter() {
		return boardWriter.get();
	}
	public void setBoardWriter(String boardWriter) {
		this.boardWriter.set(boardWriter);
	}
	public String getBoardDate() {
		return boardDate.get();
	}
	public void setBoardDate(String boardDate) {
		this.boardDate.set(boardDate);
	}
}
