package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BoardModule.BoardController;
import ClassPackage.Board;
import ClassPackage.BoardTableView;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : Dao
패키지 버전 : 1.0.0
클래스 이름 : BoardDao
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 게시판 테이블에서 게시물 리스트와 게시물 내용을 모두 가져오는 메서드가 포함된 클래스

사용한 외부 라이브러리
- mysql-connector-java-5.1.47.jar

패키지 버전 변경 사항
 */
public class BoardDao {
	//게시판 내용을 전부 데이터베이스에 저장하는 메서드
	public boolean insertBoardContent(Board board) {
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
	public boolean updateBoardContent(Board board) {
		String sql = "update boardtbl set boardheader = ?, boardtitle = ?, boardcontent = ?, boardfile = ? where boardno = ? and boardpassword = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardHeader());
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setString(4, board.getBoardFile());
			pstmt.setString(5, board.getBoardNo() + "");
			pstmt.setString(6, board.getBoardPassword());
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
		String sql = "select b.*, u.username, u.userimgpath from boardtbl b inner join usertbl u on b.boarduserno = u.userno where boardno = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, boardNo);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				board = new Board(rs.getInt("boardno"), rs.getString("boardheader"), rs.getString("boardtitle"), rs.getString("boardcontent"),
						rs.getString("boardpassword"), rs.getString("username"), rs.getString("boarddate"), rs.getString("boardfile"), rs.getInt("boardavailable"));
				BoardController.imgPath = rs.getString("userimgpath");
				BoardController.USER_NO = rs.getString("boarduserno");
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
	public void loadAllBoardList(ObservableList<BoardTableView> list) {
		String sql = "select b.boardno, b.boardheader, b.boardtitle, u.username, b.boarddate from boardtbl b inner join usertbl u on b.boarduserno = u.userno where boardavailable = 1 order by b.boardno desc;";
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
	public void loadFilteredBoardList(ObservableList<BoardTableView> list, String boardHeader) {
		String sql = "select b.boardno, b.boardheader, b.boardtitle, u.username, b.boarddate from boardtbl b inner join usertbl u on b.boarduserno = u.userno "
				+ "where boardheader = ? and boardavailable = 1 order by b.boardno desc;";
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
	
	//게시물을 삭제하는 메서드
	//실제로 데이터베이스에서 삭제하는 것이 아니라
	//available을 1에서 0으로 바꾸는 것 뿐
	//리스트에서는 나타나지 않아도 삭제한 게시물은 데이터베이스에 계속 남아있어야 한다는 규칙을 정함.
	public void deleteBoardContent(String boardNo) {
		String sql = "update boardtbl set boardavailable = 0 where boardno = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, boardNo);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
