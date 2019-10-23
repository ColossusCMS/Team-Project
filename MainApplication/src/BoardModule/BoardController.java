package BoardModule;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.Board;
import Dao.BoardDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BoardController implements Initializable {
	@FXML
	private Button btnCancel, btnModify, btnDelete;
	@FXML
	private Label lblHeader, lblWriter, lblDate, lblTitle, lblContent;
	
	public static String BBS_ID;	//게시물 번호를 Dao클래스로 전달하기 위한 변수
	
	BoardDao dao = new BoardDao();
	Board board;	//게시물의 모든 내용을 담기 위한 클래스
	
	@Override
	public void initialize(URL loc, ResourceBundle resources) {
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnModify.setOnAction(event -> handleBtnModifyAction());
		btnDelete.setOnAction(event -> handleBtnDeleteAction());
		
		board = dao.loadAllBoardContent(BBS_ID);	//게시물 열람을 했을 때 해당하는 모든 내용을 가져오기 위해 매개변수로 게시물 번호를 보냄
		lblHeader.setText(board.getBoardHeader());
		lblWriter.setText(board.getBoardUserNo());
		lblDate.setText(board.getBoardDate());
		lblTitle.setText(board.getBoardTitle());
		lblContent.setText(board.getBoardContent());
	}
	
	public void handleBtnModifyAction() {
		Stage stage = (Stage)btnModify.getScene().getWindow();
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("boardModify.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//삭제는 데이터베이스에서 삭제하는 것이 아니라
	//데이터베이스의 삭제 여부를 알려주는 bbsAvailable을 1에서 0으로 변경만 함
	public void handleBtnDeleteAction() {
		//여기 만들어야 됨
		String str = new String();
	}

	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
