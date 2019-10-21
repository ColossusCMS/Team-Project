package BoardModule;
//게시물을 등록하거나 게시물 열람, 수정, 삭제 시에 사용하는 클래스
public class Board {
	private int bbsId;
	private String bbsHeader;
	private String bbsTitle;
	private int userId;
	private String bbsDate;
	private String bbsContent;
	private String bbsPassword;
	
	public Board(String bbsHeader, String bbsTitle, int userId, String bbsDate, String bbsContent, String bbsPassword) {
		this.bbsHeader = bbsHeader;
		this.bbsTitle = bbsTitle;
		this.userId = userId;
		this.bbsDate = bbsDate;
		this.bbsContent = bbsContent;
		this.bbsPassword = bbsPassword;
	}
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	public String getBbsHeader() {
		return bbsHeader;
	}
	public void setBbsHeader(String bbsHeader) {
		this.bbsHeader = bbsHeader;
	}
	public String getBbsTitle() {
		return bbsTitle;
	}
	public void setBbsTitle(String bbsTitle) {
		this.bbsTitle = bbsTitle;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBbsDate() {
		return bbsDate;
	}
	public void setBbsDate(String bbsDate) {
		this.bbsDate = bbsDate;
	}
	public String getBbsContent() {
		return bbsContent;
	}
	public void setBbsContent(String bbsContent) {
		this.bbsContent = bbsContent;
	}
	public String getBbsPassword() {
		return bbsPassword;
	}
	public void setBbsPassword(String bbsPassword) {
		this.bbsPassword = bbsPassword;
	}
}
