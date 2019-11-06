package ClassPackage;

import javafx.beans.property.SimpleStringProperty;

public class NoticeTableView {
	private SimpleStringProperty noticeClass;
	private SimpleStringProperty noticeTitle;
	private SimpleStringProperty noticeContent;
	
	public NoticeTableView(String noticeClass, String noticeTitle, String noticeContent) {		
		this.noticeClass = new SimpleStringProperty(noticeClass);
		this.noticeTitle = new SimpleStringProperty(noticeTitle);
		this.noticeContent = new SimpleStringProperty(noticeContent);
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

	public String getNoticeContent() {
		return noticeContent.get();
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent.set(noticeContent);
	}
}
