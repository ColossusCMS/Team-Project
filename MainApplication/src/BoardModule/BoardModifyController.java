package BoardModule;
 
import java.net.URL;
import java.util.ResourceBundle;

import BoardModule.Board;
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
	ObservableList<String> comboList = FXCollections.observableArrayList("전체 게시판","자유게시판","경리부","개발부");
	int userId;
	String bbsPw, bbsDate;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { 
		board = bd.loadAllBbsContent(1);
		comboHeader.setItems(comboList);
//		fieldTitle.setText(board.getBbsTitle());
//		txtAreaContent.setText(board.getBbsContent());
//		comboHeader.getSelectionModel().select(board.getBbsHeader());
//		fieldPw.setText(board.getBbsPassword());
		
		btnModify.setOnAction(event -> this.handleBtnModifyAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		
		fieldTitle.setText(board.getBbsTitle());
		fieldPw.setText(board.getBbsPassword());
		txtAreaContent.setText(board.getBbsContent());
		comboHeader.getSelectionModel().select(board.getBbsHeader());
		userId = board.getUserId();
		bbsPw = board.getBbsPassword();
		bbsDate = board.getBbsDate();
	}
	
	public void handleBtnModifyAction() {
		board = new Board(comboHeader.getSelectionModel().getSelectedItem().toString(), fieldTitle.getText(), userId, bbsDate, txtAreaContent.getText(), bbsPw);
		bd.updateBbsContent(board);
	}
	
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}