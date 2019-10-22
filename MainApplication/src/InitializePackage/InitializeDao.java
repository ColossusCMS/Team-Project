package InitializePackage;

import java.sql.Connection;
import java.sql.DriverManager;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 데이터베이스 접속 및 연결유지
모듈 버전 : 1.0.0
클래스 이름 : InitializeDao
해당 클래스 작성 : 최문석

필요 전체 Java파일
- InitializeDao.java (데이터베이스에 접속하는 클래스)

해당 클래스 주요 기능
- 프로그램이 실행되면 데이터베이스에 접속해서
- 접속한 상태를 유지해 데이터베이스에 재접속할 때의 지연시간을 없앰.
 */
public class InitializeDao {
	public static Connection conn;
	private static final String USERNAME = "sample";
	private static final String PASSWORD = "9999";
	private static final String URL = "jdbc:mysql://125.185.21.163:3306/sampledb";
//	private static final String URL = "jdbc:mysql://192.168.219.14:3306/sampledb";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("드라이버 로딩 성공");
		} catch (Exception e) {
			System.out.println("드라이버 로드 실패");
			e.printStackTrace();
		}
	}
}
