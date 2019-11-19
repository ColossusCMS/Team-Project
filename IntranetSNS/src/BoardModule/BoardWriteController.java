package BoardModule;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import ClassPackage.Board;
import CreateDialogModule.ChkDialogMain;
import Dao.BoardDao;
import Dao.DeptDao;
import InitializePackage.DataProperties;
import MainModule.MainController;
import SFTPUploadDownloadModule.SFTPModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : BoardModule
패키지 버전 : 1.0.0
클래스 이름 : BoardWriteController
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 게시판탭에서 글쓰기 버튼을 눌렀을 때 등장하는 창을 생성
- 게시물의 내용을 작성할 수 있고 작성을 완료하고 등록버튼을 누르면 데이터베이스에 내용을 저장함.

패키지 버전 변경 사항
 */
public class BoardWriteController implements Initializable {
	@FXML
	private TextField txtFieldTitle, txtFieldFilePath;
	@FXML
	private PasswordField txtFieldPassword;
	@FXML
	private ComboBox<String> comboBoxHeader;
	@FXML
	private TextArea txtAreaContent;
	@FXML
	private Button btnReg, btnCancel, btnFileAttach;

	BoardDao boardDao = new BoardDao();
	DeptDao deptDao = new DeptDao();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	// 글쓰기의 부서 리스트
	// 이거 리스트 하나로 병합해서 같은거 가져올 수 있도록
	ObservableList<String> deptList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnReg.setOnAction(event -> handleBtnRegAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnFileAttach.setOnAction(event -> handleBtnFileAttachAction());

		// 부서 테이블 긁어올것
		deptList.add("전체");
		deptDao.loadAllDept(deptList);
		comboBoxHeader.setItems(deptList);
	}

	// 글쓰기 버튼 동작
	public void handleBtnRegAction() {
		if (txtFieldTitle.getText().isEmpty()) { // 만약 제목을 입력하지 않았다면
			ChkDialogMain.chkDialog("제목을 입력하세요.");
		} else if (comboBoxHeader.getSelectionModel().isEmpty()) { // 게시판을 선택하지 않았다면
			ChkDialogMain.chkDialog("게시판을 선택하세요.");
		} else if (txtFieldPassword.getText().isEmpty()) { // 비밀번호를 입력하지 않았다면
			ChkDialogMain.chkDialog("비밀번호를 입력하세요.");
		} else { // 모두 만족한다면 db로 전송
			String filePath = "";
			if (!txtFieldFilePath.getText().isEmpty()) { // 파일 첨부를 했다면
				// SFTP서버로 접속
				SFTPModule sftpModule = new SFTPModule(DataProperties.getIpAddress(),
						DataProperties.getPortNumber("SFTPServer"), DataProperties.getIdProfile("SFTPServer"),
						DataProperties.getPassword("SFTPServer"));
				try {
					filePath = sftpModule.upload(txtFieldFilePath.getText(), "files"); // SFTP서버의 files경로에 첨부한 파일을 업로드하고 파일명을 리턴
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Date now = new Date(); // 오늘 날짜(게시물을 작성한 날짜/시간)
			boolean write = boardDao
					.insertBoardContent(new Board(null, comboBoxHeader.getSelectionModel().getSelectedItem().toString(),
							txtFieldTitle.getText(), txtAreaContent.getText(), txtFieldPassword.getText(),
							MainController.USER_NO, sdf.format(now), filePath, null));
			if (write == true) {
				ChkDialogMain.chkDialog("게시물을 등록했습니다.");
				btnReg.getScene().getWindow().hide();
			} else {
				ChkDialogMain.chkDialog("오류로 인하여 게시물 등록에 실패했습니다.");
			}
		}
	}

	// 파일첨부 버튼
	public void handleBtnFileAttachAction() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("모든 파일(*.*)", "*.*"));
		File selectedFile = fileChooser.showOpenDialog((Stage) btnFileAttach.getScene().getWindow());
		try {
			txtFieldFilePath.setText(selectedFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 취소버튼
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}