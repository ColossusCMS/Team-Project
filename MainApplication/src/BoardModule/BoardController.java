package BoardModule;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
	
	public static String BBS_ID;
	BoardDao dao = new BoardDao();
	Board board;

	@Override
	public void initialize(URL loc, ResourceBundle resources) {
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnModify.setOnAction(event -> handleBtnModifyAction());
		btnDelete.setOnAction(event -> handleBtnDeleteAction());
		
//		board = dao.loadAllBbsContent(this.getBbsId());
		board = dao.loadAllBbsContent(Integer.parseInt(BBS_ID));
		lblHeader.setText(board.getBbsHeader());
		lblWriter.setText(board.getUserId() + "");
		lblDate.setText(board.getBbsDate());
		lblTitle.setText(board.getBbsTitle());
		lblContent.setText(board.getBbsContent());
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
		
	}

	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
