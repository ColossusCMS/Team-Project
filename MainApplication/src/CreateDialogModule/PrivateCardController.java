package CreateDialogModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.User;
import Dao.UserInfoDao;
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
프로그램 버전 : 0.7.0
모듈 이름 : 사용자 상세 정보 다이얼로그
모듈 버전 : 1.0.0
클래스 이름 : PrivateCardController
해당 클래스 작성 : 최문석

필요 전체 Java파일
- PrivateCardController.java (실행 결과나 오류를 보여주는 사용자 중심 안내용 임시 다이얼로그)

필요 fxml파일
- privateCard.fxml (개인 정보 상세 창 fxml)

해당 클래스 주요 기능
- 사용자의 상세 정보를 출력
- 상태 메시지를 수정할 수 있음
 */
public class PrivateCardController implements Initializable {
	@FXML private Label lblDept, lblPosition, lblUserName, lblUserMail, lblUserTel;
	@FXML private ImageView viewImg;
	@FXML private AnchorPane paneProfile;
	@FXML private Button btnCancel, btnStatusSave;
	@FXML private TextArea txtAreaStatus;
	
	public static String userNo;
	User user;
	UserInfoDao userInfoDao = new UserInfoDao();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		user = userInfoDao.selectMyInfo(userNo);
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnStatusSave.setOnAction(event -> handleBtnStatusSaveAction());
		
		lblDept.setText(user.getUserDept());
		lblUserName.setText(user.getUserName());
		lblPosition.setText(user.getUserPosition());
		lblUserMail.setText(user.getUserMail());
		lblUserTel.setText(user.getUserTel());
		String url = "http://yaahq.dothome.co.kr/" + user.getUserImgPath();
		viewImg.setImage(new Image(url));
		txtAreaStatus.setText(user.getUserStatusMsg());
	}
	
	public void handleBtnStatusSaveAction() {
		userInfoDao.updateStatusMsg(txtAreaStatus.getText(), userNo);
		ChkDialogMain.chkDialog("등록되었습니다.");
	}
	
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
}
