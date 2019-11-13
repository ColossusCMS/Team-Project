package ClassPackage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.0.0
클래스 이름 : Notice
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 공지사항 다이얼로그를 출력하는데 필요한 정보를 담아두는 클래스

패키지 버전 변경 사항
 */
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
