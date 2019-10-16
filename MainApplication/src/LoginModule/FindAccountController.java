package LoginModule;
 
import java.net.URL;
import java.util.ResourceBundle;

import ChkDialogModule.ChkDialogMain;
import SendMail.SendMail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/*
프로젝트 주제 : 사내 SNS
모듈 이름 : 로그인
클래스 이름 : FindAccountController
버전 : 1.0.0
해당 클래스 작성 : 최문석, 심대훈

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
 */
public class FindAccountController implements Initializable {
	//이름, 이메일
    @FXML private TextField userName, userMail;
    @FXML private Button btnReg, btnCancel;
    
    LoginDao ld = new LoginDao();
    UserData ud;
    
    @Override
    public void initialize(URL loc, ResourceBundle resources) {
    	btnReg.setOnAction(event -> handleBtnRegAction());
    	btnCancel.setOnAction(event -> handleBtnCancelAction());
    }
    
    //보내기 버튼 동작 순서
    //1_1. 이름 필드확인, 공백이면 입력하라는 다이얼로그
    //1_2. 이메일 필드확인, 공백이면 입력하라는 다이얼로그
    //2_1. 이름과 이메일을 가지고 DB에서 검색해서 매칭되면 메일보내기
    //2_2. 만약 매칭이 안된다면(결과가 null이면) 사용자 없다고 다이얼로그
    //3. 메일보내기까지 완료했으면 다시 로그인창으로 전환
    public void handleBtnRegAction() {
        if(userName.getText().isEmpty() || userMail.getText().isEmpty()) {	//필드에 공백이 있다면
        	if(userName.getText().isEmpty()) {
        		ChkDialogMain.chkDialog("이름을 입력하세요.");
        		userName.requestFocus();
        	}
        	else {
        		ChkDialogMain.chkDialog("이메일을 입력하세요.");
        		userMail.requestFocus();
        	}
        }
        else {
        	ud = ld.chkUserNameMail(userName.getText(), userMail.getText());
        	//chk가 true라면 값이 맞다는 뜻이니 메일을 보낸다.
        	//그 다음 메일을 보냈다는 다이얼로그를 띄우고 로그인창으로 전환
        	if(ud != null) {
        		new SendMail(ud.getUserMail(), ud.getUserName(), ud.getUserNo(), ud.getUserPw());	//메일 보내는 거
        		ChkDialogMain.chkDialog("메일을 전송했습니다.");
        		//로그인 창 이동
        		handleBtnCancelAction();
        	}
        	//만약 값이 존재하지 않는다면 해당 사용자가 없다는 다이얼로그
        	else {
        		ChkDialogMain.chkDialog("해당 사용자 정보가\n존재하지 않습니다.");
        		userName.clear();
        		userMail.clear();
        	}
        }
    }
    
    //취소버튼 동작 -> 로그인창으로 전환
    public void handleBtnCancelAction() {
    	Stage stage = (Stage)btnCancel.getScene().getWindow();
		try {
			Parent loginDialog = FXMLLoader.load(getClass().getResource("login.fxml"));
			Scene scene = new Scene(loginDialog);
			stage.setTitle("로그인");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
