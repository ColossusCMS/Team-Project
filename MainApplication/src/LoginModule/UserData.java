package LoginModule;

public class UserData {
	private String userNo;
	private String userName;
	private String userPw;
	private String userMail;
	
	public UserData(String userNo, String userName, String userPw, String userMail) {
		this.userNo = userNo;
		this.userName = userName;
		this.userPw = userPw;
		this.userMail = userMail;
	}
	
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
}
