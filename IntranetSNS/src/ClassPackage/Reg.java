package ClassPackage;

import javafx.beans.property.SimpleStringProperty;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.0.0
클래스 이름 : Reg
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 일정 등록창의 테이블뷰에 내용을 출력하기 위해서 사용하는 클래스
- SimpleStringProperty를 이용해 테이블뷰의 값으로 적용

패키지 버전 변경 사항
 */
public class Reg {
	private SimpleStringProperty schUserNo;
	private SimpleStringProperty schTitle;
	private SimpleStringProperty schContent;
	private SimpleStringProperty schEntryDate;
	private SimpleStringProperty schGroup;

	public Reg(String schUserNo, String schTitle, String schContent, String schEntryDate, String schGroup) {
		this.schUserNo = new SimpleStringProperty(schUserNo);
		this.schTitle = new SimpleStringProperty(schTitle);
		this.schContent = new SimpleStringProperty(schContent);
		this.schEntryDate = new SimpleStringProperty(schEntryDate);
		this.schGroup = new SimpleStringProperty(schGroup);
	}

	public String getSchUserNo() {
		return schUserNo.get();
	}

	public void setSchUserNo(String schUserNo) {
		this.schUserNo.set(schUserNo);
	}

	public String getSchTitle() {
		return schTitle.get();
	}

	public void setSchTitle(String schTitle) {
		this.schTitle.set(schTitle);
	}

	public String getSchContent() {
		return schContent.get();
	}

	public void setSchContent(String schContent) {
		this.schContent.set(schContent);
	}

	public String getSchEntryDate() {
		return schEntryDate.get();
	}

	public void setSchEntryDate(String schEntryDate) {
		this.schEntryDate.set(schEntryDate);
	}

	public String getSchGroup() {
		return schGroup.get();
	}

	public void setSchGroup(String schGroup) {
		this.schGroup.set(schGroup);
	}
}
