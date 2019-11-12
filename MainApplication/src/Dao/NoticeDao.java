package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassPackage.Notice;
import ClassPackage.NoticeTableView;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : Dao
패키지 버전 : 1.0.0
클래스 이름 : UserInfoDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 데이터베이스에 접속
- 등록된 사용자들을 가져오기 위한 클래스
- 사용자 목록, 상세 정보 등에서 정보를 출력하기 위해 사용

패키지 버전 변경 사항
 */
public class NoticeDao {
	Notice notice;
	
	//공지사항 전체를 가져오는 메서드
	public void getAllNotice(ObservableList<NoticeTableView> list) {
		String sql = "select * from noticetbl where noticeavailable = 1 order by noticeno desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new NoticeTableView(rs.getInt("noticeno"), rs.getString("noticeclass"), rs.getString("noticetitle")));
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
	}
	
	//알림 목록에서 공지사항을 선택했을 때 해당 공지사항의 정보를 가져오는 메서드
	public Notice getSelectedNotice(String noticeNo) {
		String sql = "select * from noticetbl where noticeno = ? and noticeavailable = 1;";
		Notice notice = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, noticeNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				notice = new Notice(rs.getString("noticeclass"), rs.getString("noticetitle"), rs.getString("noticecontent"));
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
		return notice;
	}
	
	//공지사항 중 가장 최근의 공지사항을 가져오는 메서드
	public Notice getMainNotice() {
		Notice notice = null;
		String sql = "select * from noticetbl where noticeclass = '공지' and noticeavailable = 1 order by noticeno desc limit 1;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				notice = new Notice(rs.getString("noticeclass"), rs.getString("noticetitle"), rs.getString("noticecontent"));
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
		return notice;
	}
	
	//게시판의 구분이 사용자의 부서인 게시물 중 가장 최신글을 가져오는 메서드
	public void getRecentlyDeptBoard(ObservableList<NoticeTableView> list, String userno) {
		String sql = "select b.boardno, u.userdept, b.boardtitle from boardtbl b inner join usertbl u on b.boarduserno = u.userno\r\n" + 
				"where b.boardheader = (select d.deptname from depttbl d inner join usertbl u on u.userdept = d.deptname where u.userno = ?)" + 
				"and b.boardavailable = 1 order by b.boardno desc limit 1;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userno);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				list.add(new NoticeTableView(rs.getInt("boardno"), "게시판-" + rs.getString("userdept"), rs.getString("b.boardtitle")));
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
	}
	
	//게시판의 구분이 전체인 게시물 중 가장 최신글을 가져오는 메서드
	public void getRecentlyBoard(ObservableList<NoticeTableView> list) {
		String sql = "select b.boardno, b.boardtitle, u.username from boardtbl b inner join usertbl u on b.boarduserno = u.userno where b.boardheader = '전체'"
				+ "and b.boardavailable = 1 order by b.boardno desc limit 1;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				list.add(new NoticeTableView(rs.getInt("boardno"), "게시판-전체", rs.getString("b.boardtitle")));
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
	}
	
	//일정에서 당일의 개인 일정을 가져오는 메서드
	public void getPrivateSchedule(ObservableList<NoticeTableView> list, String userNo) {
		String sql = "select schno, schtitle, schcontent from scheduletbl where schuserno = ? and schentrydate = date(now()) order by schentrydate desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new NoticeTableView(rs.getInt("schno"), "일정-개인", rs.getString("schtitle")));
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
	}
	
	//일정에서 당일의 단체 일정을 가져오는 메서드
	public void getGroupSchedule(ObservableList<NoticeTableView> list, String userNo) {
		String sql = "select schno, schtitle, schcontent, schgroup from scheduletbl where (schgroup = ("
				+ "select d.deptno from depttbl d inner join usertbl u on u.userdept = d.deptname where u.userno = ?)"
				+ "or schgroup = 0) and schentrydate = date(now()) order by schentrydate desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String dept = new String();
				switch(rs.getInt("schgroup")) {
				case 0:
					dept = "전체";
					break;
				case 10:
					dept = "개발";
					break;
				case 20:
					dept = "기획";
					break;
				case 30:
					dept = "경영";
					break;
				case 40:
					dept = "인사";
					break;
				case 50:
					dept = "영업";
					break;
				case 60:
					dept = "디자인";
					break;
				}
				list.add(new NoticeTableView(rs.getInt("schno"), "일정-" + dept, rs.getString("schtitle")));
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
	}
}
