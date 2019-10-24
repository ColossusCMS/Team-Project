package BoardModule;
 
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import ClassPackage.Board;
import CreateDialogModule.ChkDialogMain;
import Dao.BoardDao;
import Dao.DeptDao;
import MainModule.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 게시판
모듈 버전 : 1.0.0
클래스 이름 : BoardWriterController
해당 클래스 작성 : 최문석

필요 모듈 Java파일
- BoardController.java (사용자 등록에 사용하는 사용자 정보 클래스[사용자의 모든 정보를 담고 있음])
- BoardWriteController
- BoardModifyController

필요 fxml파일
- board.fxml
- boardWrite.fxml
- boardModify.fxml

필요 import 사용자 정의 package
- ClassPackage.Board;
- CreateDialogModule.ChkDialogMain;
- Dao.BoardDao;
- Dao.DeptDao;
- MainModule.MainController;

해당 클래스 주요 기능
- 게시판탭에서 글쓰기 버튼을 눌렀을 때 등장하는 창을 생성
- 게시물의 내용을 작성할 수 있고 작성을 완료하고 등록버튼을 누르면 데이터베이스에 내용을 저장함.

모듈 버전 변경 사항
1.0.0
 */
public class BoardWriteController implements Initializable {
	@FXML private TextField txtFieldTitle;
	@FXML private PasswordField txtFieldPassword;
	@FXML private ComboBox<String> comboBoxHeader;
	@FXML private TextArea txtAreaContent;
	@FXML private Button btnReg, btnCancel;
	
	BoardDao boardDao = new BoardDao();
	DeptDao deptDao = new DeptDao();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	//글쓰기의 부서 리스트
	//이거 리스트 하나로 병합해서 같은거 가져올 수 있도록
	ObservableList<String> deptList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnReg.setOnAction(event -> handleBtnRegAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		
		//부서 테이블 긁어올것
		deptDao.loadAllDept(deptList);
		comboBoxHeader.setItems(deptList);	
	}
	
	public void handleBtnRegAction() {
		if(txtFieldTitle.getText().isEmpty()) {	//만약 제목을 입력하지 않았다면
			ChkDialogMain.chkDialog("제목을 입력하세요.");
		}
		else if(comboBoxHeader.getSelectionModel().isEmpty()) {	//게시판을 선택하지 않았다면
			ChkDialogMain.chkDialog("게시판을 선택하세요.");
		}
		else if(txtFieldPassword.getText().isEmpty()) {	//비밀번호를 입력하지 않았다면
			ChkDialogMain.chkDialog("비밀번호를 입력하세요.");
		}
		else {	//모두 만족한다면 db로 전송
			Date now = new Date();
			boolean write = boardDao.insertBoardContent(new Board(null, comboBoxHeader.getSelectionModel().getSelectedItem().toString(), txtFieldTitle.getText(), txtAreaContent.getText(), txtFieldPassword.getText(), MainController.USER_NO, sdf.format(now), "file", null));
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
		btnCancel.getScene().getWindow().hide();
	}
}