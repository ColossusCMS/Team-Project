package CreateDialogModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.User;
import Dao.UserInfoDao;
import InitializePackage.DataProperties;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : CreateDialogModule
패키지 버전 : 1.0.0
클래스 이름 : BusinessCardController
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 다른 사용자의 상세 정보를 출력하는 다이얼로그의 컨트롤러

패키지 버전 변경 사항
 */
public class BusinessCardController implements Initializable {
	@FXML private Label lblDept, lblPosition, lblUserName, lblUserMail, lblUserTel, lblUserGreet;
	@FXML private ImageView imageViewImg;
	@FXML private AnchorPane paneProfile;
	@FXML private Button btnCancel;
	
	public static String userNo;	//여기에서는 선택한 다른 사용자의 사용자 번호를 받아옴
	User user;
	UserInfoDao userInfoDao = new UserInfoDao();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		user = userInfoDao.selectMyInfo(userNo);
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		lblDept.setText(user.getUserDept());
		lblUserName.setText(user.getUserName());
		lblPosition.setText(user.getUserPosition());
		lblUserMail.setText(user.getUserMail());
		lblUserTel.setText(user.getUserTel());
		lblUserGreet.setText(user.getUserStatusMsg());
		lblUserGreet.setWrapText(true);
		String url = "http://" + DataProperties.getIpAddress() + ":" + DataProperties.getPortNumber("HTTPServer") + "/images/" + user.getUserImgPath();
		imageViewImg.setImage(new Image(url));
		lblUserGreet.setOnMouseEntered(event -> {
			lblUserGreet.setPrefHeight(70);
			lblUserGreet.setPrefWidth(170);
		});
		lblUserGreet.setOnMouseExited(event -> {
			lblUserGreet.setPrefHeight(52);
			lblUserGreet.setPrefWidth(165);
		});
	}
	
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
