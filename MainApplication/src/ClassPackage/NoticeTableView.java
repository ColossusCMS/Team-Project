package ClassPackage;

import javafx.beans.property.SimpleStringProperty;

public class NoticeTableView {
	private SimpleStringProperty noticeNo;
	private SimpleStringProperty noticeClass;
	private SimpleStringProperty noticeTitle;
	
	
	public NoticeTableView(int noticeNo, String noticeClass, String noticeTitle) {		
		this.noticeClass = new SimpleStringProperty(noticeClass);
		this.noticeTitle = new SimpleStringProperty(noticeTitle);
		this.noticeNo = new SimpleStringProperty(String.valueOf(noticeNo));
	}

	public String getNoticeClass() {
		return noticeClass.get();
	}

	public void setNoticeClass(String noticeClass) {
		this.noticeClass.set(noticeClass);
	}

	public String getNoticeTitle() {
		return noticeTitle.get();
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle.set(noticeTitle);
	}

	public String getNoticeNo() {
		return noticeNo.get();
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo.set(noticeNo);
	}
}
