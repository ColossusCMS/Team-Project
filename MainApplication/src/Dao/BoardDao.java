package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassPackage.Board;
import ClassPackage.BoardTableView;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;

public class BoardDao {
	//게시판 내용을 전부 데이터베이스에 저장하는 메서드
	public boolean insertBbsContent(Board board) {
		String sql = "insert into boardtbl values(null, ?, ?, ?, ?, ?, ?, ?, default);";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardHeader());
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setString(4, board.getBoardPassword());
			pstmt.setString(5, board.getBoardUserNo());
			pstmt.setString(6, board.getBoardDate());
			pstmt.setString(7, board.getBoardFile());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
            try {
                if (pstmt != null && !pstmt.isClosed())
                    pstmt.close();
            } catch (SQLException e) {                
                e.printStackTrace();
            }
        }
	}
	
	//게시판 내용을 수정할 때 사용하는 메서드
	//모든 변수가 아닌 수정할 수 있는 부분만 갱신함.
	//머리말, 제목, 내용, 파일
	public boolean updateBbsContent(Board board) {
		String sql = "update boardtbl set boardheader = ?, boardtitle = ?, boardcontent = ?, boardfile = ? where boardno = ? and boarduserid = ? and boardpassword = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardHeader());
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setString(4, board.getBoardFile());
			pstmt.setString(5, board.getBoardNo() + "");
			pstmt.setString(6, board.getBoardUserNo() + "");
			pstmt.setString(7, board.getBoardPassword());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
            try {
                if (pstmt != null && !pstmt.isClosed())
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
	//게시판에서 해당 게시물을 선택했을 때 사용하는 메서드
	//게시판 테이블에서 해당 게시물번호의 게시물을 가져오는 메서드
	//매개변수로 받는 변수는 데이터베이스에서 pk인 게시물번호
	public Board loadAllBoardContent(String boardNo) {
		Board board = null;
		String sql = "select * from boardtbl where boardno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, boardNo);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				board = new Board(rs.getInt("boardno"), rs.getString("boardheader"), rs.getString("boardtitle"), rs.getString("boardcontent"),
						rs.getString("boardpassword"), rs.getString("boarduserno"), rs.getString("boarddate"), rs.getString("boardfile"), rs.getInt("boardavailable"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
                if (pstmt != null && !pstmt.isClosed())
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return board;
	}
	
	//게시판 테이블의 모든 목록을 가져옴. 단, boardAvailable이 1인 게시물만 가져옴
	//0은 삭제되었다는 의미, 1은 삭제하지 않았다는 의미
	//그리고 userTbl과의 join을 이용해 작성자번호를 작성자의 이름으로 대체
	public void loadAllBbsList(ObservableList<BoardTableView> list) {
		String sql = "select b.boardno, b.boardheader, b.boardtitle, u.username, b.boarddate from boardtbl b inner join usertbl u on b.boarduserno = u.userno order by b.boardno desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			list.clear();
			while(rs.next()) {
				list.add(new BoardTableView(rs.getString("b.boardno"), rs.getString("b.boardheader"), rs.getString("b.boardtitle"), rs.getString("u.username"), rs.getString("b.boarddate")));
			}
		} catch (Exception e) {
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
	
	//필터링했을 때의 게시판 목록을 가져오는 메서드
	//매개변수로 받는 boardHeader는 머리말로 부서별 필터링을 실행
	public void loadFilteredBbsList(ObservableList<BoardTableView> list, String boardHeader) {
		String sql = "select b.boardno, b.boardheader, b.boardtitle, u.username, b.boarddate from boardtbl b inner join usertbl u on b.boarduserno = u.userno "
				+ "where boardheader = ? order by b.boardno desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, boardHeader);
			ResultSet rs = pstmt.executeQuery();
			list.clear();
			while(rs.next()) {
				list.add(new BoardTableView(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (Exception e) {
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
