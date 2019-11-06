package ClassPackage;

public class Notice {
	private String noticeClass;
	private String noticeTitle;
	private String noticeContent;
	
	public Notice(String noticeClass, String noticeTitle, String noticeContent) {		
		this.noticeClass = noticeClass;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
	}

	public String getNoticeClass() {
		return noticeClass;
	}

	public void setNoticeClass(String noticeClass) {
		this.noticeClass = noticeClass;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
}
