package ClassPackage;

import javafx.beans.property.SimpleStringProperty;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.0.0
클래스 이름 : NoticeTableView
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 알림 탭의 테이블뷰에 값을 적용하기 위한 클래스
- SimpleStringProperty를 이용해 테이블뷰의 값으로 적용

패키지 버전 변경 사항
 */
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
