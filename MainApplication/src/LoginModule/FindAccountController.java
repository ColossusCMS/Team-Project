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
