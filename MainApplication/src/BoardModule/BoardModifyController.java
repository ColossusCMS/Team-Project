package BoardModule;
 
import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.Board;
import Dao.BoardDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//게시물 수정 부분 수정 필요
public class BoardModifyController implements Initializable {
	@FXML private TextField fieldTitle;
	@FXML private TextArea txtAreaContent;
	@FXML private Button btnModify, btnCancel;
	@FXML private ComboBox<String> comboHeader;
	@FXML private PasswordField fieldPw;
	
	Board board;
	BoardDao bd = new BoardDao();
	//이 부분도 데이터베이스에서 긁어올 것
	//부서 테이블 사용
	ObservableList<String> deptList = FXCollections.observableArrayList();
	String userNo, boardPassword, boardDate, boardFile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { 
		board = bd.loadAllBoardContent("1");
		comboHeader.setItems(deptList);
//		fieldTitle.setText(board.getBbsTitle());
//		txtAreaContent.setText(board.getBbsContent());
//		comboHeader.getSelectionModel().select(board.getBbsHeader());
//		fieldPw.setText(board.getBbsPassword());
		
		btnModify.setOnAction(event -> handleBtnModifyAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		
		fieldTitle.setText(board.getBoardTitle());
		fieldPw.setText(board.getBoardPassword());
		txtAreaContent.setText(board.getBoardContent());
		comboHeader.getSelectionModel().select(board.getBoardHeader());
		userNo = board.getBoardUserNo();
		boardPassword = board.getBoardPassword();
		boardDate = board.getBoardDate();
		boardFile = board.getBoardFile();
	}
	
	public void handleBtnModifyAction() {
		String str = new String();
		board = new Board(comboHeader.getSelectionModel().getSelectedItem().toString(), fieldTitle.getText(), txtAreaContent.getText(), boardDate, boardFile);
		bd.updateBbsContent(board);
	}
	
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}