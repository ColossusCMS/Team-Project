package BoardModule;
 
import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.Board;
import CreateDialogModule.ChkDialogMain;
import Dao.BoardDao;
import Dao.DeptDao;
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
클래스 이름 : BoardModifyController
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

해당 클래스 주요 기능
- 게시판 내용 열람창에서 수정버튼을 눌렀을 때 나타나는 창을 생성
- 해당 게시물의 내용 중에서 수정이 가능한 부분을 보여주며
- 수정을 완료하고 수정 버튼을 누르면 갱신된 내용이 데이터베이스에 저장됨

모듈 버전 변경 사항
1.0.0
 */

//게시물 수정 부분 수정 필요
public class BoardModifyController implements Initializable {
	@FXML private TextField txtFieldTitle;
	@FXML private TextArea txtAreaContent;
	@FXML private Button btnModify, btnCancel;
	@FXML private ComboBox<String> comboBoxHeader;
	@FXML private PasswordField txtFieldPassword;
	
	Board board;
	DeptDao deptDao = new DeptDao();
	BoardDao boardDao = new BoardDao();
	//이 부분도 데이터베이스에서 긁어올 것
	//부서 테이블 사용
	ObservableList<String> deptList = FXCollections.observableArrayList();
	String boardUserNo, boardDate, boardFile;	//수정이 필요없는 값, DB에서 비교하는 조건으로 쓰임
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		deptDao.loadAllDept(deptList);	//부서 리스트 가져옴
		board = boardDao.loadAllBoardContent(BoardController.BBS_ID);	//게시물 번호
		comboBoxHeader.setItems(deptList);	//콤보박스 세팅
		
		//버튼 세팅
		btnModify.setOnAction(event -> handleBtnModifyAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		
		//수정이 가능한 부분은 컨트롤로 처리
		txtFieldTitle.setText(board.getBoardTitle());
		txtFieldPassword.setText(board.getBoardPassword());
		txtAreaContent.setText(board.getBoardContent());
		comboBoxHeader.getSelectionModel().select(board.getBoardHeader());
		
		//아래의 변수들은 수정할 수 없는 내용이고 같은 내용을 DB로 넘기기 위해서 변수로 저장
		boardUserNo = board.getBoardUserNo();
		boardDate = board.getBoardDate();
		boardFile = board.getBoardFile();
		//비밀번호는 바꿀 수 없음
		txtFieldPassword.setEditable(false);
	}
	
	//수정 버튼 눌렀을 때
	public void handleBtnModifyAction() {
		board = new Board(Integer.parseInt(BoardController.BBS_ID), comboBoxHeader.getSelectionModel().getSelectedItem().toString(), txtFieldTitle.getText(), txtAreaContent.getText(),
				txtFieldPassword.getText(), boardUserNo, boardDate, boardFile, 0);
		boolean modify = boardDao.updateBoardContent(board);
		if(modify) {	//수정이 되었다면 되었다고 알림창 띄우고 수정 창 닫기
			ChkDialogMain.chkDialog("수정이 완료되었습니다.");
			btnModify.getScene().getWindow().hide();
		}
		else {	//어딘가 오류가 발생했다면 오류 발생했다고 알림창 띄우고 현재 상태 유지
			ChkDialogMain.chkDialog("오류가 발생했습니다.");
		}
	}
	
	//취소 버튼
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}