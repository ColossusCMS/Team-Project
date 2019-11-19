package InitializePackage;

import java.sql.Connection;
import java.sql.DriverManager;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : InitializePackage
패키지 버전 : 1.0.0
클래스 이름 : InitializeDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 프로그램이 실행되면 데이터베이스에 접속해서
- 접속한 상태를 유지해 데이터베이스에 재접속할 때의 지연시간을 없앰.

사용한 외부 라이브러리
- mysql-connector-java-5.1.47.jar

패키지 버전 변경 사항
 */
public class InitializeDao {
	public static Connection conn;
	private static final String USERNAME = DataProperties.getIdProfile("MainDatabase");
	private static final String PASSWORD = DataProperties.getPassword("MainDatabase");
	private static final String IP = DataProperties.getIpAddress();
	private static final int PORT = DataProperties.getPortNumber("MainDatabase");
	private static final String URL = "jdbc:mysql://" + IP + ":" + PORT + "/snsproject";

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
