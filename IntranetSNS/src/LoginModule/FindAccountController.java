package LoginModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.UserData;
import CreateDialogModule.ChkDialogMain;
import Dao.LoginDao;
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
프로그램 버전 : 1.0.0
패키지 이름 : LoginModule
패키지 버전 : 1.2.0
클래스 이름 : FindAccountController
해당 클래스 작성 : 최문석, 심대훈

해당 클래스 주요 기능
- 사용자가 이름과 이메일을 입력하고 보내기 버튼을 누르면
- 데이터베이스에서 동일한 데이터가 존재하는지 확인하고 존재한다면
- 해당 사용자에게 사용자번호와 비밀번호를 메일로 전송한다.

패키지 버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화

1.1.1
- Dao 인스턴스 통합 (데이터 베이스 초기화 클래스 생성)
- 콤보박스의 내용을 데이터베이스와 연동

1.1.2
- 전화번호 중복체크 버튼, 기능 추가
- 중복로그인 방지 기능 추가

1.2.0
- 사용자 프로필용 이미지 업로드 기능 추가, SFTP서버를 이용해 구현
 */

public class FindAccountController implements Initializable {
	// 이름, 이메일
	@FXML
	private TextField txtFieldUserName, txtFieldUserMail;
	@FXML
	private Button btnReg, btnCancel;

	LoginDao loginDao = new LoginDao(); // DB 접속 시 사용

	@Override
	public void initialize(URL loc, ResourceBundle resources) {
		btnReg.setOnAction(event -> handleBtnRegAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
	}

	// 보내기 버튼 동작 순서
	// 1_1. 이름 필드확인, 공백이면 입력하라는 다이얼로그
	// 1_2. 이메일 필드확인, 공백이면 입력하라는 다이얼로그
	// 2_1. 이름과 이메일을 가지고 DB에서 검색해서 매칭되면 메일보내기
	// 2_2. 만약 매칭이 안된다면(결과가 null이면) 사용자 없다고 다이얼로그
	// 3. 메일보내기까지 완료했으면 다시 로그인창으로 전환
	public void handleBtnRegAction() {
		if (txtFieldUserName.getText().equals("") || txtFieldUserMail.getText().equals("")) { // 필드에 공백이 있다면
			if (txtFieldUserName.getText().isEmpty()) {
				ChkDialogMain.chkDialog("이름을 입력하세요.");
				txtFieldUserName.requestFocus();
			} else {
				ChkDialogMain.chkDialog("이메일을 입력하세요.");
				txtFieldUserMail.requestFocus();
			}
		} else {
			UserData ud = loginDao.chkUserNameMail(txtFieldUserName.getText(), txtFieldUserMail.getText());
			// chk가 true라면 값이 맞다는 뜻이니 메일을 보낸다.
			// 그 다음 메일을 보냈다는 다이얼로그를 띄우고 로그인창으로 전환
			if (ud != null) {
				new SendMail(ud.getUserMail(), ud.getUserName(), ud.getUserNo(), ud.getUserPw()); // 메일 보내는 거
				ChkDialogMain.chkDialog("메일을 전송했습니다.");
				// 로그인 창 이동
				handleBtnCancelAction();
			}
			// 만약 값이 존재하지 않는다면 해당 사용자가 없다는 다이얼로그
			else {
				ChkDialogMain.chkDialog("해당 사용자 정보가\n존재하지 않습니다.");
				txtFieldUserName.clear();
				txtFieldUserMail.clear();
			}
		}
	}

	// 취소버튼 동작 -> 로그인창으로 전환
	public void handleBtnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
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
