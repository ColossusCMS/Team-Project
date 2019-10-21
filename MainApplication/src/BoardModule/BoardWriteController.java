package BoardModule;
 
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import BoardModule.Board;
import ChkDialogModule.ChkDialogMain;
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

public class BoardWriteController implements Initializable {
	@FXML private TextField fieldTitle;
	@FXML private PasswordField fieldPw;
	@FXML private ComboBox<String> comboHeader;
	@FXML private TextArea txtAreaContent;
	@FXML private Button btnReg, btnCancel;
	
	BoardDao bd;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnReg.setOnAction(event -> handleBtnRegAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		
		ObservableList<String> comboList = FXCollections.observableArrayList("전체", "경리팀", "개발팀", "경영팀", "인사팀");
		comboHeader.setItems(comboList);	
	}
	
	public void handleBtnRegAction() {
		if(fieldTitle.getText().isEmpty()) {	//만약 제목을 입력하지 않았다면
			ChkDialogMain.chkDialog("제목을 입력하세요.");
		}
		else if(comboHeader.getSelectionModel().isEmpty()) {	//게시판을 선택하지 않았다면
			ChkDialogMain.chkDialog("게시판을 선택하세요.");
		}
		else if(fieldPw.getText().isEmpty()) {	//비밀번호를 입력하지 않았다면
			ChkDialogMain.chkDialog("비밀번호를 입력하세요.");
		}
		else {	//모두 만족한다면 db로 전송
			bd = new BoardDao();
			boolean write = bd.insertBbsContent(new Board(comboHeader.getSelectionModel().getSelectedItem().toString(), fieldTitle.getText(), 1234, "20191112", txtAreaContent.getText(), fieldPw.getText()));
			if(write == true) {
				ChkDialogMain.chkDialog("게시물을 등록했습니다.");
				btnReg.getScene().getWindow().hide();
			}
			else {
				ChkDialogMain.chkDialog("오류로 인하여 게시물 등록에 실패했습니다.");
			}
		}
	}
	
	public void handleBtnCancelAction() {
		bd = new BoardDao();
		bd.callProcedure();
	}
}