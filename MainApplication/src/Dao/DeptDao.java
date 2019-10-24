package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 로그인 데이터베이스 클래스
모듈 버전 : 0.7.0
클래스 이름 : DeptDao
해당 클래스 작성 : 최문석

필요 전체 Java파일
- DeptDao.java (사용자 테이블 데이터베이스 접속, 데이터 불러오기, 데이터 삽입 등)

필요 import 사용자 정의 package
- InitializePackage.InitializeDao (데이터 베이스 접속 초기화)
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 데이터베이스에 접속
- 부서 정보를 가져오기 위함

버전 변경 사항
1.0.0

 */
public class DeptDao {	
	//부서를 전부 가져옴
	public int loadAllDept(ObservableList<String> deptList) {
		int rowCnt = 0;
		String sql = "select deptname from depttbl;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				rowCnt++;
				deptList.add(rs.getString("deptname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rowCnt;
	}
}
