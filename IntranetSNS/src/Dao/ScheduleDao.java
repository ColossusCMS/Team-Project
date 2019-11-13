package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import ClassPackage.DayOff;
import ClassPackage.Reg;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : Dao
패키지 버전 : 1.0.0
클래스 이름 : ScheduleDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 데이터 베이스의 일정 테이블에 일정을 저장하거나 저장된 일정을 가져오는 메서드가 포함된 클래스

사용한 외부 라이브러리
- mysql-connector-java-5.1.47.jar

패키지 버전 변경 사항
 */
public class ScheduleDao {
	//개인 일정을 가져오는 메서드
	public void loadPrivateSchedule(ObservableList<Reg> list, String userNo, String date) {
		String sql = "select * from scheduletbl where schuserno = ? and schgroup = 1 and schentrydate = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			pstmt.setString(2, date);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Reg(rs.getString("schuserno"), rs.getString("schtitle"), rs.getString("schcontent"), rs.getString("schentrydate"), rs.getString("schgroup")));
			}
		} catch (Exception e) {
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
	}
	
	//단체 일정을 가져올때는 관리자 번호와 사용자의 부서 번호를 매칭해서 가져옴
	public void loadGroupSchedule(ObservableList<Reg> list, String userNo, String date) {
		String sql = "select * from scheduletbl where schuserno = 0000 and schgroup in"
				+ "((select d.deptno from depttbl d inner join usertbl u on u.userdept = d.deptname where userno = ?), 0)"
				+ "and schentrydate = ?;";
		PreparedStatement pstmt = null;
		try {
			list.clear();
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			pstmt.setString(2, date);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Reg(rs.getString("schuserno"), rs.getString("schtitle"), rs.getString("schcontent"), rs.getString("schentrydate"), rs.getString("schgroup")));
			}
		} catch (Exception e) {
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
	}
	
	//개인 일정 등록하는 메서드
	public boolean entrySchedule(Reg reg) {
		String sql = "insert into scheduletbl values(null, ?, ?, ?, ?, default);";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, reg.getSchUserNo());
			pstmt.setString(2, reg.getSchTitle());
			pstmt.setString(3, reg.getSchContent());
			pstmt.setString(4, reg.getSchEntryDate());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
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
		return false;
	}
	
	//개인 일정 갱신하는 메서드
	public boolean updateSchedule(Reg reg) {
		String sql = "update scheduletbl set schtitle = ?, schcontent = ? where schuserno = ? and schentrydate = ? and schgroup = 1;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, reg.getSchTitle());
			pstmt.setString(2, reg.getSchContent());
			pstmt.setString(3, reg.getSchUserNo());
			pstmt.setString(4, reg.getSchEntryDate());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
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
		return false;
	}
	
	public boolean deleteSchedule(Reg reg) {
		String sql = "delete from scheduletbl where schuserno = ? and schtitle = ? and schcontent = ? and schentrydate = ? and schgroup = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, reg.getSchUserNo());
			pstmt.setString(2, reg.getSchTitle());
			pstmt.setString(3, reg.getSchContent());
			pstmt.setString(4, reg.getSchEntryDate());
			pstmt.setString(5, reg.getSchGroup());
			pstmt.executeUpdate();
		} catch (Exception e) {
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
		return false;
	}
	
	//해당 달에 일정이 등록된 날짜를 가져오는 메서드
	public void entryDate(int year, int month, ArrayList<String> privateList, ArrayList<String> groupList, String userNo) {
		privateList.clear();
		groupList.clear();
		String sql = "select distinct day(schentrydate), schgroup from scheduletbl where substring(schentrydate, 1, 5) = ? and substring(schentrydate, 6, 8) = ? " + 
				"and (schuserno = 0000 or schuserno = ?) and (schgroup in (0, 1, (select d.deptno from depttbl d inner join usertbl u on u.userdept = d.deptname where u.userno = ?)));";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setInt(1, year);
			pstmt.setInt(2, month);
			pstmt.setString(3, userNo);
			pstmt.setString(4, userNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("schgroup").equals("1")) {
					privateList.add(rs.getString(1));
				}
				else {
					groupList.add(rs.getString(1));
				}
			}
		} catch (Exception e) {
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
	}
	
	public void entryDayOff(DayOff dayoff) {
		String sql = "insert into dayofftbl values(null, ?, ?, ?, ?);";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, dayoff.getDoUserNo());
			pstmt.setString(2, dayoff.getDoStart());
			pstmt.setString(3, dayoff.getDoEnd());
			pstmt.setString(4, dayoff.getDoContent());
			pstmt.executeUpdate();
		} catch (Exception e) {
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
	}
}