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
