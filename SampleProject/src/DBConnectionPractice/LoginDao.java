package DBConnectionPractice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginDao {
	private Connection conn;    //DB 커넥션(연결) 객체
    private static final String USERNAME = "sample";   //DB 접속시 ID
    private static final String PASSWORD = "9999";	 //DB 접속시 패스워드
    //DB접속 경로(스키마=데이터베이스명)설정
    private static final String URL = "jdbc:mysql://125.185.21.163:3306/sampledb"; 
 
    //생성자
    public LoginDao() {
        // connection객체를 생성해서 DB에 연결함.
        try {
            System.out.println("생성자");
        	//동적 객체를 만들어줌 
            Class.forName("com.mysql.jdbc.Driver"); 
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("드라이버 로딩 성공!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("드라이버 로드 실패!!");
        }
    }
    
    //테이블에 존재하는 모든 행을 가져오는 메서드임
    public List<User> slectAll() {
    	String sql = "select * from login;";
        PreparedStatement pstmt = null; 
        List<User> list = new ArrayList<User>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
 
            while (rs.next()) {	//가져올게 있느냐?
                User u = new User();
                u.setUserno(rs.getInt("userno"));
                u.setUsername(rs.getString("username"));
                u.setUserpw(rs.getString("userpw"));
                u.setUsermail(rs.getString("usermail"));
                u.setUsertel(rs.getString("usertel"));
                u.setUserimg(rs.getString("userimg"));
                u.setDept(rs.getString("dept"));
                u.setUsergreet(rs.getString("usergreet"));
                u.setAdmin(rs.getInt("admin"));
                list.add(u);   //List<User>에다가 추가함.
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null && !pstmt.isClosed())
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}