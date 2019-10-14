package DBConnectionPractice;
//JAVA Bean이라고 함.
public class User {
	//모델 1:1 대응(MVC패턴)
    private int userno;
    private String username;
    private String userpw;
    private String usermail;
    private String usertel;
    private String userimg;
    private String dept;
    private String usergreet;
    private int admin;
    
    public User() {	};
    
	public User(int userno, String username, String userpw, String usermail, String usertel, String userimg,
			String dept, String usergreet, int admin) {
		this.userno = userno;
		this.username = username;
		this.userpw = userpw;
		this.usermail = usermail;
		this.usertel = usertel;
		this.userimg = userimg;
		this.dept = dept;
		this.usergreet = usergreet;
		this.admin = admin;
	}

	public int getUserno() {
		return userno;
	}

	public void setUserno(int userno) {
		this.userno = userno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}

	public String getUsermail() {
		return usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}

	public String getUsertel() {
		return usertel;
	}

	public void setUsertel(String usertel) {
		this.usertel = usertel;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getUsergreet() {
		return usergreet;
	}

	public void setUsergreet(String usergreet) {
		this.usergreet = usergreet;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}
	
	@Override
	public String toString() {
		return userno + " " + username + " " + userpw + " " + usermail + " " + usertel + " " + userimg + " " + dept + " " + usergreet + " " + admin;
	}
}