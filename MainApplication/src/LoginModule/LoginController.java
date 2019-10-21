package LoginModule;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ChkDialogModule.ChkDialogMain;
import Dao.LoginDao;
import IdSaveLoadModule.IdSaveLoad;
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
프로젝트 주제 : 사내 SNS
모듈 이름 : 로그인
클래스 이름 : LoginController
버전 : 1.1.0
해당 클래스 작성 : 최문석, 김도엽

필요 전체 Java파일
- LoginMain.java (로그인 화면이 실행되는 메인 클래스)
- LoginDao.java (데이터베이스 접속, 데이터 불러오기, 데이터 삽입 등)
- LoginController.java (로그인 창 컨트롤러)
- SignUpController.java (사용자 등록 창 컨트롤러)
- FindAccountController.java (계정 찾기 창 컨트롤러)
- User.java (사용자 등록에 사용하는 사용자 정보 클래스[사용자의 모든 정보를 담고 있음])
- UserData.java (계정 찾기에서 사용하는 사용자 정보 클래스[사용자번호, 이름, 이메일, 비밀번호])

필요 fxml파일
- login.fxml (로그인 창 fxml)
- signUp.fxml (사용자등록 창 fxml)
- findAccount.fxml (계정 찾기 창 fxml)

필요 import 사용자 정의 package
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능
- 

버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화
 */
public class LoginController implements Initializable {	
	@FXML private Button btnLogin, btnFindAccount, btnSignUp;
	@FXML private TextField fieldUserNo;
	@FXML private PasswordField fieldPassword;
	
	LoginDao loginDao = new LoginDao();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setOnAction(event -> handleBtnLoginAction());
		btnFindAccount.setOnAction(event -> handleBtnFindAccountAction());
		btnSignUp.setOnAction(event -> handleBtnSignUpAction());
		
		//비밀번호를 입력하고 로그인 버튼을 누르지않고 엔터키를 눌러서 로그인을 시도할 수 있게 만듦.
		fieldPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode keyCode = event.getCode();
				if(keyCode.equals(KeyCode.ENTER)) {
					handleBtnLoginAction();
				}
			}
		});
	}
	
	//로그인 버튼을 눌렀을때
	//1. 사원번호를 입력하지 않았다면 사원번호를 입력하라는 다이얼로그 띄움
	//2. 비밀번호를 입력하지 않았다면 비밀번호를 입력하라는 다이얼로그 띄움
	//3_1. 만약 사원번호나 비밀번호 둘 중 하나라도 틀렸다면(리턴값으로 null을 받음) 계정을 확인하라는 다이얼로그 띄움
	//3_2. 로그인에 성공했다면(리턴값으로 이름을 받음) 사용자 이름을 띄우면서 로그인에 성공했다는 다이얼로그 띄움
	public void handleBtnLoginAction() {
		String labelText = new String();
		if(fieldUserNo.getText().isEmpty() || fieldPassword.getText().isEmpty()) {	//사원번호, 비밀번호 둘 중 하나라도 입력하지 않았다면
			if(fieldUserNo.getText().isEmpty()) {
				labelText = "사원번호를 입력하세요.";
				fieldUserNo.requestFocus();
			}
			else {
				labelText = "비밀번호를 입력하세요.";
				fieldPassword.requestFocus();
			}
			ChkDialogMain.chkDialog(labelText);
		}
		else {	//값을 입력했다면 일단 넘어감
			String name = loginDao.chkUserData(fieldUserNo.getText(), fieldPassword.getText());
			if(name == null) {	//입력한 정보가 맞지 않다는 뜻
				labelText = "일치하는 회원정보가 없습니다.";
				ChkDialogMain.chkDialog(labelText);
			}
			else {	//그게 아니라면 입력한 정보가 올바름
//				labelText = name + "님\n어서오세요!";
//				ChkDialogMain.chkDialog(labelText);
				Stage stage = (Stage)btnLogin.getScene().getWindow();
				try {
					Parent mainPane = FXMLLoader.load(getClass().getResource("/MainModule/main.fxml"));
					Scene scene = new Scene(mainPane);
					stage.setScene(scene);
					stage.setResizable(false);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//사용자번호를 텍스트파일로 저장
				IdSaveLoad.saveUserId(fieldUserNo.getText());
			}
		}
	}
	
	//계정찾기 버튼을 눌렀을 때
	//계정찾기 창으로 전환됨
	public void handleBtnFindAccountAction() {
		Stage stage = (Stage)btnFindAccount.getScene().getWindow();
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
	public void handleBtnSignUpAction() {
		Stage stage = (Stage)btnSignUp.getScene().getWindow();
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
