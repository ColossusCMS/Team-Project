package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassPackage.Notice;
import ClassPackage.NoticeTableView;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;

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
		String sql = "select schno, schtitle, schcontent from testschedule where schuserno = ? and schentrydate = date(now()) order by schentrydate desc;";
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
		String sql = "select schno, schtitle, schcontent from testschedule where (schgroup = ("
				+ "select d.deptno from depttbl d inner join usertbl u on u.userdept = d.deptname where u.userno = ?)"
				+ "or schgroup = 0) and schentrydate = date(now()) order by schentrydate desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new NoticeTableView(rs.getInt("schno"), "일정-단체", rs.getString("schtitle")));
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
