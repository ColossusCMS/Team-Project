package BoardModule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;

import ClassPackage.Board;
import CreateDialogModule.ChkDialogMain;
import Dao.BoardDao;
import FTPUploadDownloadModule.FTPDownloader;
import MainModule.MainController;
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
모듈 이름 : 게시판
모듈 버전 : 1.0.0
클래스 이름 : BoardController
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

해당 클래스 주요 기능
- 게시판 목록에서 게시물을 선택했을 때 내용을 열람하는 창을 생성

모듈 버전 변경 사항
1.0.0
 */
public class BoardController implements Initializable {
	@FXML private Button btnCancel, btnModify, btnDelete;
	@FXML private Label lblHeader, lblWriter, lblDate, lblTitle, lblContent;
	@FXML private ImageView imgViewUserImg;
	@FXML private Hyperlink hyperLinkAttachFile;
	
	public static String USER_NO;
	public static String BBS_ID;	//게시물 번호를 Dao클래스로 전달하기 위한 변수
	public static String imgPath;
	
	BoardDao boardDao = new BoardDao();
	Board board;	//게시물의 모든 내용을 담기 위한 클래스
	
	//비밀번호 부분은 작성자와 현재 열람중인 사용자 비교가 가능하면
	//따로 필요없는 부분이니 나중에 구현할 것
	//사용자 번호와 게시물의 작성자 번호를 매칭해서 같을 때만 삭제버튼과 수정버튼을 활성화하도록 수정할 것
	@Override
	public void initialize(URL loc, ResourceBundle resources) {
		
		btnDelete.setDisable(true);
		btnModify.setDisable(true);
		
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnModify.setOnAction(event -> handleBtnModifyAction());
		btnDelete.setOnAction(event -> handleBtnDeleteAction());
		
		board = boardDao.loadAllBoardContent(BBS_ID);	//게시물 열람을 했을 때 해당하는 모든 내용을 가져오기 위해 매개변수로 게시물 번호를 보냄
		lblHeader.setText(board.getBoardHeader());
		lblWriter.setText(board.getBoardUserNo());
		lblDate.setText(board.getBoardDate());
		lblTitle.setText(board.getBoardTitle());
		lblContent.setText(board.getBoardContent());
		String url = "http://yaahq.dothome.co.kr/" + imgPath;
		imgViewUserImg.setImage(new Image(url));
		
		if(board.getBoardFile().isEmpty()) {
			hyperLinkAttachFile.setVisible(false);
		}
		else {
			hyperLinkAttachFile.setVisible(true);
			setHyperLink();
		}
		
		if(USER_NO.equals(MainController.USER_NO)) {
			btnDelete.setDisable(false);
			btnModify.setDisable(false);
		}
	}
	
	public void handleBtnModifyAction() {
		Stage stage = (Stage)btnModify.getScene().getWindow();
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
	
	public void setHyperLink() {
		File downloadFile = new File(board.getBoardFile());
		hyperLinkAttachFile.setText(downloadFile.getName());
		hyperLinkAttachFile.setOnAction(e -> {
			handleSaveFileChooser(board.getBoardFile());
		});
	}
	
	public void handleSaveFileChooser(String file) {
		FileChooser fileChooser = new FileChooser();
		String ext = FilenameUtils.getExtension(file);
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("모든 파일(*." + ext + ")", "*." + ext)
			);
		File saveFile = fileChooser.showSaveDialog((Stage)hyperLinkAttachFile.getScene().getWindow());
		if(saveFile != null) {
			//파일 다운로드하는 부분
			FTPDownloader.receiveFTPServer("html/" + file, saveFile.getAbsolutePath());
		}
	}
	
	//삭제는 데이터베이스에서 삭제하는 것이 아니라
	//데이터베이스의 삭제 여부를 알려주는 bbsAvailable을 1에서 0으로 변경만 함
	//삭제 동작 순서
	//1안
	//삭제 버튼을 누르면 비밀번호를 입력하라는 창을 띄움
	//비밀번호가 일치하다면 삭제했다는 다이얼로그를 띄우고 창이 닫힘
	//2안
	//사용자 확인되니 그냥 삭제버튼 누르면 바로 삭제하고 다이얼로그 띄움
	//리스트에서 새로고침하면 없어지는걸 확인하도록
	
	
	public void handleBtnDeleteAction() {
		//여기 만들어야 됨
		boardDao.deleteBoardContent(BBS_ID);
		ChkDialogMain.chkDialog("삭제되었습니다.");
		btnDelete.getScene().getWindow().hide();
	}

	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
