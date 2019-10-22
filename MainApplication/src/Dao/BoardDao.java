package Dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BoardModule.Board;
import BoardModule.BoardTableView;
import InitializePackage.InitializeDao;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class BoardDao {
	//게시판 내용을 다 저장하는 메서드
	public boolean insertBbsContent(Board board) {
		String sql = "insert into bbs values(null, ?, ?, ?, ?, ?, ?, default);";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, board.getBbsHeader());
			pstmt.setString(2, board.getBbsTitle());
			pstmt.setInt(3, board.getUserId());
			pstmt.setString(4, board.getBbsDate());
			pstmt.setString(5, board.getBbsContent());
			pstmt.setString(6, board.getBbsPassword());
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
	
	public void updateBbsContent(Board board) {
		String sql = "update bbs set bbsheader = ?, bbstitle = ?, bbscontent = ? where bbsid = ? and userid = ? and bbspw = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, board.getBbsHeader());
			pstmt.setString(2, board.getBbsTitle());
			pstmt.setString(3, board.getBbsContent());
			pstmt.setString(4, board.getBbsId() + "");
			pstmt.setString(5, board.getUserId() + "");
			pstmt.setString(6, board.getBbsPassword());
			pstmt.executeUpdate();
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
	
	public Board loadAllBbsContent(int bbsId) {
		Board board = null;
		String sql = "select * from bbs where bbsid = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setInt(1, bbsId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				board = new Board(rs.getString("bbsheader"), rs.getString("bbstitle"), rs.getInt("userid"), rs.getString("bbsdate"), rs.getString("bbscontent"), rs.getString("bbspw"));
				board.setBbsId(rs.getInt("bbsid"));
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
	
	public void loadAllBbsList(TableView<BoardTableView> viewTable, ObservableList<BoardTableView> list) {
		String sql = "select b.bbsid, b.bbsheader, b.bbstitle, l.username, b.bbsdate from bbs b inner join login l on b.userid = l.userno order by b.bbsid desc;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			list.clear();
			while(rs.next()) {
				list.add(new BoardTableView(rs.getString("b.bbsid"), rs.getString("b.bbsheader"), rs.getString("b.bbstitle"), rs.getString("l.username"), rs.getString("b.bbsdate")));
			}
			viewTable.setItems(list);
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
	
	public void loadFilteredBbsList(TableView<BoardTableView> viewTable, ObservableList<BoardTableView> list, String bbsHeader) {
		String sql = "select bbsid, bbsheader, bbstitle, userid, bbsdate from bbs where bbsheader = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = InitializeDao.conn.prepareStatement(sql);
			pstmt.setString(1, bbsHeader);
			ResultSet rs = pstmt.executeQuery();
			list.clear();
			while(rs.next()) {
				list.add(new BoardTableView(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
			viewTable.setItems(list);
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
	
	
	
	//자바 안에서 프로시저 call하는 방법
	public void callProcedure() {
		try {
			CallableStatement cs = InitializeDao.conn.prepareCall("call prod()");
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
