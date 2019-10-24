package CreateDialogModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.User;
import Dao.UserInfoDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 사용자 상세 정보 다이얼로그
모듈 버전 : 1.0.0
클래스 이름 : BusinessCardController
해당 클래스 작성 : 최문석

필요 전체 Java파일
- BusinessCardController.java (실행 결과나 오류를 보여주는 사용자 중심 안내용 임시 다이얼로그)

필요 fxml파일
- businessCard.fxml (안내용 임시 다이얼로그 fxml)

해당 클래스 주요 기능
- 테이블 뷰에서 사용자를 선택하면 나타나는 다이얼로그
- 사용자의 세부 정보를 출력함
 */
public class BusinessCardController implements Initializable {
	@FXML private Label lblDept, lblPosition, lblUserName, lblUserMail, lblUserTel, lblUserGreet;
	@FXML private ImageView viewImg;
	@FXML private AnchorPane paneProfile, stackedPane;
	@FXML private Button btnCancel;
	public static String userNo;
	User user;
	UserInfoDao userInfoDao = new UserInfoDao();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		user = userInfoDao.selectMyInfo(userNo);
		btnCancel.setOnAction(event -> handleBtnChatAction());
		lblDept.setText(user.getUserDept());
		lblUserName.setText(user.getUserName());
		lblPosition.setText(user.getUserPosition());
		lblUserMail.setText(user.getUserMail());
		lblUserTel.setText(user.getUserTel());
		lblUserGreet.setText(user.getUserStatusMsg());
		lblUserGreet.setWrapText(true);
		
		lblUserGreet.setOnMouseEntered(event -> {
			lblUserGreet.setPrefHeight(70);
			lblUserGreet.setPrefWidth(170);
		});
		lblUserGreet.setOnMouseExited(event -> {
			lblUserGreet.setPrefHeight(52);
			lblUserGreet.setPrefWidth(165);
		});
		
		stackedPane.setOnMouseClicked(event -> btnCancel.getScene().getWindow().hide());
	}
	
	public void handleBtnChatAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
