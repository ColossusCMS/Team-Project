package BoardModule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;

import ClassPackage.Board;
import CreateDialogModule.ChkDialogMain;
import Dao.BoardDao;
import InitializePackage.DataProperties;
import MainModule.MainController;
import SFTPUploadDownloadModule.SFTPModule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : BoardModule
패키지 버전 : 1.0.0
클래스 이름 : BoardController
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 게시판 탭 테이블에서 게시물을 선택했을 때 열람 창을 띄움
- 해당 창에서는 해당 게시물을 작성한 사용자라면 수정, 삭제가 가능하고
- 첨부된 파일이 있다면 파일을 서버로부터 다운로드받을 수도 있다.

사용 외부 라이브러리
- commons-io-2.6.jar

패키지 버전 변경 사항
 */

public class BoardController implements Initializable {
	@FXML
	private Button btnCancel, btnModify, btnDelete;
	@FXML
	private Label lblHeader, lblWriter, lblDate, lblTitle, lblContent;
	@FXML
	private ImageView imgViewUserImg;
	@FXML
	private Hyperlink hyperLinkAttachFile;

	public static String USER_NO;
	public static String BBS_ID; // 게시물 번호를 Dao클래스로 전달하기 위한 변수
	public static String imgPath;

	BoardDao boardDao = new BoardDao();
	Board board; // 게시물의 모든 내용을 담기 위한 클래스

	// 비밀번호 부분은 작성자와 현재 열람중인 사용자 비교가 가능하면
	// 따로 필요없는 부분이니 나중에 구현할 것
	// 사용자 번호와 게시물의 작성자 번호를 매칭해서 같을 때만 삭제버튼과 수정버튼을 활성화하도록 수정할 것
	@Override
	public void initialize(URL loc, ResourceBundle resources) {

		btnDelete.setDisable(true);
		btnModify.setDisable(true);

		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnModify.setOnAction(event -> handleBtnModifyAction());
		btnDelete.setOnAction(event -> handleBtnDeleteAction());

		board = boardDao.loadAllBoardContent(BBS_ID); // 게시물 열람을 했을 때 해당하는 모든 내용을 가져오기 위해 매개변수로 게시물 번호를 보냄
		lblHeader.setText(board.getBoardHeader());
		lblWriter.setText(board.getBoardUserNo());
		lblDate.setText(board.getBoardDate());
		lblTitle.setText(board.getBoardTitle());
		lblContent.setText(board.getBoardContent());
		String url = "http://" + DataProperties.getIpAddress() + ":" + DataProperties.getPortNumber("SFTPServer")
				+ "/images/" + imgPath;
		imgViewUserImg.setImage(new Image(url));

		// 첨부파일이 있으면 하이퍼링크를 이용해 파일을 다운로드할 수 있게 함
		if (board.getBoardFile().isEmpty()) {
			hyperLinkAttachFile.setVisible(false);
		} else {
			hyperLinkAttachFile.setVisible(true);
			setHyperLink();
		}

		// 해당 게시물을 작성한 작성자인지 판단하는 부분
		// 작성자가 아닌데 게시물을 수정하거나 삭제하는 것을 방지함
		// 해당 게시물의 작성자가 맞다면 수정, 삭제 버튼을 활성화함
		if (USER_NO.equals(MainController.USER_NO)) {
			btnDelete.setDisable(false);
			btnModify.setDisable(false);
		}
	}

	// 수정버튼
	public void handleBtnModifyAction() {
		Stage stage = (Stage) btnModify.getScene().getWindow();
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("boardModify.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setScene(scene);
			stage.show();
			btnModify.setDisable(true);
			stage.setOnCloseRequest(event -> btnModify.setDisable(false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 하이퍼링크를 작성하는 부분
	public void setHyperLink() {
		File downloadFile = new File(board.getBoardFile());
		hyperLinkAttachFile.setText(downloadFile.getName());
		hyperLinkAttachFile.setOnAction(e -> {
			handleSaveFileChooser(board.getBoardFile());
		});
	}

	// 파일 저장하는 메서드
	public void handleSaveFileChooser(String file) {
		FileChooser fileChooser = new FileChooser();
		String ext = FilenameUtils.getExtension(file); // 이 메서드를 사용하면 파일명에서 확장자만 가져올 수 있다.
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("모든 파일(*." + ext + ")", "*." + ext) // 해당 파일의 확장자를
																											// 가지고 저장
																											// 다이얼로그에
																											// 확장자를 자동으로
																											// 지정할 수 있음
		);
		File saveFile = fileChooser.showSaveDialog((Stage) hyperLinkAttachFile.getScene().getWindow());
		if (saveFile != null) {
			// 파일 다운로드하는 부분
			SFTPModule sftpModule = new SFTPModule(DataProperties.getIpAddress(),
					DataProperties.getPortNumber("SFTPServer"), DataProperties.getIdProfile("SFTPServer"),
					DataProperties.getPassword("SFTPServer"));
			try {
				sftpModule.download(file, saveFile.getAbsolutePath()); // 서버의 해당 파일을 사용자가 지정한 경로로 다운로드
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 삭제는 데이터베이스에서 삭제하는 것이 아니라
	// 데이터베이스의 삭제 여부를 알려주는 bbsAvailable을 1에서 0으로 변경만 함
	public void handleBtnDeleteAction() {
		boardDao.deleteBoardContent(BBS_ID);
		ChkDialogMain.chkDialog("삭제되었습니다.");
		btnDelete.getScene().getWindow().hide();
	}

	// 닫기 버튼
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
