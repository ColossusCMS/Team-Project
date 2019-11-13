package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : Dao
패키지 버전 : 1.0.0
클래스 이름 : DeptDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 부서 테이블에서 등록된 테이블을 가져오는 메서드를 포함한 클래스

사용한 외부 라이브러리
- mysql-connector-java-5.1.47.jar

패키지 버전 변경 사항
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
	
	//해당 부서의 게시판 목록만 들고옴
	public int loadAllDept(ObservableList<String> deptList, String userNo) {
		int rowCnt = 0;
		String sql = "select d.deptname from depttbl d inner join usertbl u on u.userdept = d.deptname where u.userno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
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
