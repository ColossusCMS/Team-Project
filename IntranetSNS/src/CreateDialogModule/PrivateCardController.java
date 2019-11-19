package CreateDialogModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.User;
import Dao.UserInfoDao;
import InitializePackage.DataProperties;
import MainModule.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : CreateDialogModule
패키지 버전 : 1.0.0
클래스 이름 : PrivateCardController
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 현재 사용자 상세 정보 다이얼로그의 컨트롤러
- 데이터베이스의 사용자 테이블에서 현재 사용자의 정보를 가져와 다이얼로그를 생성

패키지 버전 변경 사항
 */
public class PrivateCardController implements Initializable {
	@FXML
	private Label lblDept, lblPosition, lblUserName, lblUserMail, lblUserTel;
	@FXML
	private ImageView imageViewImg;
	@FXML
	private AnchorPane paneProfile;
	@FXML
	private Button btnCancel, btnStatusSave;
	@FXML
	private TextArea txtAreaStatus;

	User user;
	UserInfoDao userInfoDao = new UserInfoDao();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user = userInfoDao.selectMyInfo(MainController.USER_NO); // 자기 자신의 사용자 번호로 데이터를 가져옴
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnStatusSave.setOnAction(event -> handleBtnStatusSaveAction());

		lblDept.setText(user.getUserDept());
		lblUserName.setText(user.getUserName());
		lblPosition.setText(user.getUserPosition());
		lblUserMail.setText(user.getUserMail());
		lblUserTel.setText(user.getUserTel());
		String url = "http://" + DataProperties.getIpAddress() + ":" + DataProperties.getPortNumber("HTTPServer")
				+ "/images/" + user.getUserImgPath();
		imageViewImg.setImage(new Image(url));
		txtAreaStatus.setText(user.getUserStatusMsg());
	}

	// 상태 메시지 갱신하는 메서드
	public void handleBtnStatusSaveAction() {
		userInfoDao.updateStatusMsg(txtAreaStatus.getText(), MainController.USER_NO);
		ChkDialogMain.chkDialog("등록되었습니다.");
	}

	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
