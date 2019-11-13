package ClassPackage;

import javafx.beans.property.SimpleStringProperty;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.0.0
클래스 이름 : BoardTableView
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 알림 탭의 테이블에 게시판 목록을 출력하는데 사용하는 클래스
- SimpleStringProperty를 이용해 속성을 테이블 뷰의 값으로 적용

패키지 버전 변경 사항
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
