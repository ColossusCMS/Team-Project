package WebViewTest;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class BbsDao {
	
	private Connection conn;			//DB에 접속하는데 사용하는 커넥션 변수
	private static final String USERNAME = "root";		//DB 아이디 나중에 새 DB로 변경할 예정
	private static final String PASSWORD = "1234";		//DB 비밀번호 나중에 새 DB로 변경할 예정
	private static final String URL = "jdbc:mysql://localhost/sqldb";		//DB 주소 나중에 새 DB로 변경할 예정

	//생성자
	public BbsDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");		//드라이버 로드
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("드라이버 로딩 성공!");
			callProcedure();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("드라이버 로드 실패!");
		}
	}
	
	public ArrayList<Bbs> getList() {
		String sql = "select * from bbs where bbsavailable = 1 order by bbsid desc;";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsId(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserId(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//자바 안에서 프로시저 call하는 방법
	public void callProcedure() {
		try {
			CallableStatement cs = conn.prepareCall("call casePrac(?, ?)");
			cs.setString(1, "99");
			cs.registerOutParameter(2, Types.CHAR);
			cs.execute();
			System.out.println(cs.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
