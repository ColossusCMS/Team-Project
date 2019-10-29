package ClassPackage;

import javafx.beans.property.SimpleStringProperty;

public class Reg {
	private SimpleStringProperty schUserNo;
	private SimpleStringProperty schTitle;
	private SimpleStringProperty schContent;
	private SimpleStringProperty schEntryDate;
	private SimpleStringProperty schGroup;
	
	public Reg(String schUserNo, String schTitle, String schContent, String schEntryDate, String schGroup) {
		this.schUserNo = new  SimpleStringProperty(schUserNo);
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
