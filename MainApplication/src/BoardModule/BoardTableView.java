package BoardModule;

import javafx.beans.property.SimpleStringProperty;
//테이블뷰에서 리스트를 띄우기 출력하기 위해서
//SimpleStringProperty를 사용함.
public class BoardTableView {
	private SimpleStringProperty boardId;
	private SimpleStringProperty boardHeader;
	private SimpleStringProperty boardTitle;
	private SimpleStringProperty boardWriter;
	private SimpleStringProperty boardDate;
	
	public BoardTableView(String boardId, String boardHeader, String boardTitle, String boardWriter, String boardDate) {
		this.boardId = new SimpleStringProperty(boardId);
		this.boardHeader = new SimpleStringProperty(boardHeader);
		this.boardTitle = new SimpleStringProperty(boardTitle);
		this.boardWriter = new SimpleStringProperty(boardWriter);
		this.boardDate = new SimpleStringProperty(boardDate);
	}
	public String getBoardId() {
		return boardId.get();
	}
	public void setBoardId(String boardId) {
		this.boardId.set(boardId);
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
