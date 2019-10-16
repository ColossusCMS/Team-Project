package LoginModule;

import java.net.URL;
import java.util.ResourceBundle;

import ChkDialogModule.ChkDialogMain;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/*
프로젝트 제목 : 로그인
버전 : 0.9.0
작성 : 최문석

필요 Java파일
- Main.java (로그인 화면이 실행되는 메인 클래스)
- LoginDao.java (데이터베이스 접속, 데이터 불러오기)
- LoginController.java (사용자 등록창 컨트롤러)

필요 fxml파일 :
- login.fxml (로그인창 fxml)
- chkDialog.fxml (안내 다이얼로그 fxml)

주요 기능
- 로그인을 위한 사용자 정보 입력(사원번호, 비밀번호),
- 데이터베이스에서 사용자 정보를 가져오기 위한 데이터베이스 연동,
- 해당하는 데이터가 존재하는지 체크를 위해 데이터베이스로부터 값을 읽어옴
- 모든 조건을 만족한다면 로그인에 성공하고 다음 화면으로 넘어감
- 사용자 등록 버튼이나 계정 찾기 버튼을 누르면 해당하는 창을 새로 띄움
 */
public class LoginController implements Initializable {	
	@FXML private Button loginBtn, findAccountBtn, signUpBtn;
	@FXML private TextField userNoField;
	@FXML private PasswordField passwordField;
	
	LoginDao ld = new LoginDao();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginBtn.setOnAction(event -> loginSubmit());
		findAccountBtn.setOnAction(event -> findAccount());
		signUpBtn.setOnAction(event -> signUp());
		
		//비밀번호를 입력하고 로그인 버튼을 누르지않고 엔터키를 눌러서 로그인을 시도할 수 있게 만듦.
		passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				if(keyCode.equals(KeyCode.ENTER)) {
					loginSubmit();
				}
			}
		});
	}
	
	//로그인 버튼을 눌렀을때
	//1. 사원번호를 입력하지 않았다면 사원번호를 입력하라는 다이얼로그 띄움
	//2. 비밀번호를 입력하지 않았다면 비밀번호를 입력하라는 다이얼로그 띄움
	//3_1. 만약 사원번호나 비밀번호 둘 중 하나라도 틀렸다면(리턴값으로 null을 받음) 계정을 확인하라는 다이얼로그 띄움
	//3_2. 로그인에 성공했다면(리턴값으로 이름을 받음) 사용자 이름을 띄우면서 로그인에 성공했다는 다이얼로그 띄움
	public void loginSubmit() {
		String labelText = new String();
		if(userNoField.getText().isEmpty() || passwordField.getText().isEmpty()) {	//사원번호, 비밀번호 둘 중 하나라도 입력하지 않았다면
			if(userNoField.getText().isEmpty()) {
				labelText = "사원번호를 입력하세요.";
				userNoField.requestFocus();
			}
			else {
				labelText = "비밀번호를 입력하세요.";
				passwordField.requestFocus();
			}
			ChkDialogMain.chkDialog(labelText);
		}
		else {	//값을 입력했다면 일단 넘어감
			String name = ld.chkUserData(userNoField.getText(), passwordField.getText());
			if(name == null) {	//입력한 정보가 맞지 않다는 뜻
				labelText = "일치하는 회원정보가 없습니다.";
				ChkDialogMain.chkDialog(labelText);
			}
			else {	//그게 아니라면 입력한 정보가 올바름
				labelText = name + "님\n어서오세요!";
				ChkDialogMain.chkDialog(labelText);
			}
		}
	}
	
	//계정찾기 버튼을 눌렀을 때
	//계정찾기 창으로 전환됨
	public void findAccount() {
		Stage stage = (Stage)findAccountBtn.getScene().getWindow();
		try {
			Parent findAccountDialog = FXMLLoader.load(getClass().getResource("findAccount.fxml"));
			Scene scene = new Scene(findAccountDialog);
			stage.setTitle("계정 찾기");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//사용자등록 버튼을 눌렀을 때
	//사용자등록 창으로 전환됨
	public void signUp() {
		Stage stage = (Stage)signUpBtn.getScene().getWindow();
		try {
			Parent signUpDialog = FXMLLoader.load(getClass().getResource("signUp.fxml"));
			Scene scene = new Scene(signUpDialog);
			stage.setTitle("사용자 등록");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
