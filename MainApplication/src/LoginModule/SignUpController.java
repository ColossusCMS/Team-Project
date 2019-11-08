package LoginModule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import ClassPackage.User;
import CreateDialogModule.ChkDialogMain;
import Dao.DeptDao;
import Dao.LoginDao;
import FTPUploadDownloadModule.FTPUploader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 로그인
모듈 버전 : 1.1.2
클래스 이름 : SignUpController
해당 클래스 작성 : 최문석

필요 모듈 Java파일
- LoginMain.java (로그인 화면이 실행되는 메인 클래스)
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
- Dao.LoginDao (로그인 정보를 데이터 베이스로 처리할 수 있는 메서드)
- EncryptionDecryption.PasswordEncryption (비밀번호를 암호화하고 복호화하는 메서드를 포함하고 있음)
- ChkDialogModule.ChkDialogMain (안내문 출력을 위한 임시 다이얼로그를 생성하는 패키지)
- SendMail.SendMail (메일 보내는 메서드를 포함하고 있음)

해당 클래스 주요 기능 요약
- singUp.fxml의 주 컨트롤러로 사용자 등록 창을 생성하고 입력받은 값들의 상태를 체크,
- 데이터베이스로 접속해 입력한 값들을 데이터베이스에 저장함.

모듈 버전 변경 사항
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
 */
public class SignUpController implements Initializable {
	@FXML private TextField fieldUserNo, fieldUserName, fieldUserTel, fieldUserMail, fieldImgPath;
	@FXML private Button btnUserNoChk, btnImg, btnSubmit, btnCancel, btnUserMailChk, btnUserTelChk;
	@FXML private PasswordField fieldPassword, fieldPasswordChk;
	@FXML private Label lblPwChk;
	@FXML private ImageView viewImg;
	@FXML private ComboBox<String> comboBoxDept;
	
	private ObservableList<String> comboList = FXCollections.observableArrayList();
	
	LoginDao loginDao = new LoginDao();	//로그인시 사용하는 Dao
	DeptDao deptDao = new DeptDao();	//부서정보 가져올 때 사용하는 Dao
	
	int overlapChkNum = 0;	//중복체크 여부 확인을 위한 변수
	Pattern telPattern = Pattern.compile("(010)-\\d{4}-\\d{4}");	//휴대폰 번호 패턴
	Pattern mailPattern = Pattern.compile("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}");	//이메일 패턴
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		deptDao.loadAllDept(comboList);
		comboBoxDept.setItems(comboList);
		btnUserNoChk.setOnAction(event -> handleBtnUserNoChkAction());
		btnUserMailChk.setOnAction(event -> handleBtnUserMailChkAction());
		btnUserTelChk.setOnAction(event -> handleBtnUserTelChkAction());
		btnImg.setOnAction(event -> handleBtnImgAction());
		btnSubmit.setOnAction(event -> handleBtnSubmitAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		
		fieldImgPath.setEditable(false);
		
		//만약 중복체크를 하고나서 사원번호를 새로 입력하면 다시 중복체크 버튼을 원상복구시켜야 해야하니
		//속성 감시를 해서 값이 바뀐다면 버튼을 원복시킴
		fieldUserNo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!oldValue.equals(newValue)) {
					btnUserNoChk.setText("중복체크");
					btnUserNoChk.setDisable(false);
					overlapChkNum = 0;
				}
			}
		});
		
		//비밀번호확인 필드의 속성을 항상 체크하면서 앞서 입력한 비밀번호와 동일한지 계속해서 체크
		//만약 같아진다면 일치한다고 출력하고 그렇지 않으면 불일치로 계속 출력
		fieldPasswordChk.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals(fieldPassword.getText())) {
					lblPwChk.setText("일치");
					lblPwChk.setTextFill(Color.GREEN);
				}
				else {
					lblPwChk.setText("불일치");
					lblPwChk.setTextFill(Color.RED);
				}
			}
		});
	}
	
	//사원번호 중복체크 버튼을 눌렀을 때의 이벤트
	public void handleBtnUserNoChkAction() {
		if(fieldUserNo.getText().equals("")) {		//만약 필드에 아무 값도 입력하지 않고 버튼을 눌렀다면 값을 입력하라고 다이얼로그를 띄움
			ChkDialogMain.chkDialog("사원번호를 입력하세요.");
		}
		else {	//그게 아니라면 정상적으로 진행
			boolean overlap = loginDao.chkUserNo(fieldUserNo.getText());	//DB에서 중복되는 번호가 있는지 체크함
			if(overlap) {		//만약 true라면 중복된 값이 있다는 뜻이니 사용할 수 없다는 다이얼로그를 띄움
				ChkDialogMain.chkDialog("이미 등록된 사원번호입니다.");
				fieldUserNo.clear();
				overlapChkNum = 0;
			}
			else {					//false라면 중복되는 값이 없다는 뜻이니 중복체크 버튼의 텍스트를 사용가능으로 바꿔줌
				btnUserNoChk.setText("사용가능");
//				overlapChk.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY	)));
				btnUserNoChk.setDisable(true);
				overlapChkNum = 1;
			}
		}
	}
	
	//이메일 중복체크 버튼을 눌렀을 때의 이벤트
	public void handleBtnUserMailChkAction() {
		if(fieldUserMail.getText().equals("")) {		//만약 필드에 아무 값도 입력하지 않고 버튼을 눌렀다면 값을 입력하라고 다이얼로그를 띄움
			ChkDialogMain.chkDialog("이메일을 입력하세요.");
		}
		else if(!mailPattern.matcher(fieldUserMail.getText()).matches()) {	//만약 이메일 형식에 맞지 않다면 다이얼로그를 띄움
			ChkDialogMain.chkDialog("이메일 형식에 맞지 않습니다.");
		}
		else {	//그게 아니라면 정상적으로 진행
			boolean overlap = loginDao.chkUserMail(fieldUserMail.getText());	//DB에서 중복되는 이메일이 있는지 체크함
			if(overlap) {		//만약 true라면 중복된 값이 있다는 뜻이니 사용할 수 없다는 다이얼로그를 띄움
				ChkDialogMain.chkDialog("이미 등록된 이메일입니다.");
				fieldUserMail.clear();
				overlapChkNum = 0;
			}
			else {					//false라면 중복되는 값이 없다는 뜻이니 중복체크 버튼의 텍스트를 사용가능으로 바꿔줌
				btnUserMailChk.setText("사용가능");
//				overlapChk.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY	)));
				btnUserMailChk.setDisable(true);
				overlapChkNum = 1;
			}
		}
	}
	
	//전화번호 중복체크 버튼을 눌렀을 때의 이벤트
	public void handleBtnUserTelChkAction() {
		if(fieldUserTel.getText().equals("")) {		//만약 필드에 아무 값도 입력하지 않고 버튼을 눌렀다면 값을 입력하라고 다이얼로그를 띄움
			ChkDialogMain.chkDialog("전화번호를 입력하세요.");
		}
		else if(!telPattern.matcher(fieldUserTel.getText()).matches()) {	//만약 전화번호 형식에 맞지 않다면 다이얼로그를 띄움
			ChkDialogMain.chkDialog("전화번호 형식에 맞지 않습니다.");
		}
		else {	//그게 아니라면 정상적으로 진행
			boolean overlap = loginDao.chkUserTel(fieldUserTel.getText());	//DB에서 중복되는 전화번호가 있는지 체크함
			if(overlap) {		//만약 true라면 중복된 값이 있다는 뜻이니 사용할 수 없다는 다이얼로그를 띄움
				ChkDialogMain.chkDialog("이미 등록된 전화번호입니다.");
				fieldUserTel.clear();
				overlapChkNum = 0;
			}
			else {					//false라면 중복되는 값이 없다는 뜻이니 중복체크 버튼의 텍스트를 사용가능으로 바꿔줌
				btnUserTelChk.setText("사용가능");
//					overlapChk.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY	)));
				btnUserTelChk.setDisable(true);
				overlapChkNum = 1;
			}
		}
	}	
	
	//찾기 버튼을 누르면 이미지를 찾기위한 열기 다이얼로그가 띄워짐
	//파일을 선택하면 해당 디렉토리부터 파일명까지 imgPath에 자동으로 입력이 됨
	public void handleBtnImgAction() {
		//파일 열기 다이얼로그를 불러옴
		FileChooser imgChooser = new FileChooser();
		//선택 가능한 확장자 지정(대표 이미지 확장자)
		imgChooser.getExtensionFilters().addAll(
				new ExtensionFilter("모든 이미지 파일(*.jpg, *.png, *.gif)", "*.jpg", "*.png", "*.gif")
			);
		//파일 열기 다이얼로그를 띄울 위치
		File selectedFile = imgChooser.showOpenDialog((Stage)btnImg.getScene().getWindow());
		try {
			//파일 읽어오기
			FileInputStream fis = new FileInputStream(selectedFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			//이미지 생성
			Image img = new Image(bis);
			//경로 필드에 선택한 이미지의 경로를 출력하고
			fieldImgPath.setText(selectedFile.getAbsolutePath());
			//이미지뷰에 선택한 이미지를 띄움
			viewImg.setImage(img);
			viewImg.setFitHeight(133);
			viewImg.setFitWidth(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//나중에 버튼을 하나 더 추가해서 인터넷 url을 입력해 이미지를 추가할 수 있는 방법도 구상.
	
	//등록버튼 동작 순서
	//1. DB의 not null요소를 모두 체크해서 입력하지 않은 값이 있는지 체크(필수항목)
	//1_1. not null요소 중에 입력하지 않은 값이 있다면 다이얼로그를 띄움
	//2_1. 모두 입력되었다면 중복체크를 했는지 안했는지 체크 overlapChkNum == 1인가
	//2_2. pwChkLbl이 '일치'라고 되어 있는지 체크
	//2_3. 소속을 선택했는지 체크
	//2_4. 전화번호 형식에 맞게 입력했는지
	//2_5. 이메일 형식에 맞게 입력했는지 (패턴 정규식을 활용해 체크한다.)
	//3. 모두 만족한다면 DB로 데이터를 전송
	public void handleBtnSubmitAction() {
		//필수항목을 체크한다.
		//만약 입력하지 않은 값이 있다면 다시 입력하라고 다이얼로그를 띄움
		if(fieldUserNo.getText().isEmpty() || fieldUserName.getText().isEmpty() || fieldPassword.getText().isEmpty() || fieldUserTel.getText().isEmpty()
				|| fieldUserMail.getText().isEmpty()) {
			ChkDialogMain.chkDialog("필수항목을 입력하세요.");
		}
		//중복체크했는지 안했는지 체크
		else if(overlapChkNum == 0) {
			ChkDialogMain.chkDialog("중복체크를 하세요.");
		}
		//비밀번호가 일치하는지
		else if(lblPwChk.getText().equals("불일치")) {
			ChkDialogMain.chkDialog("비밀번호를 확인하세요.");
		}
		//소속 부서를 선택했는지
		else if(comboBoxDept.getSelectionModel().getSelectedItem() == null) {
			ChkDialogMain.chkDialog("소속을 선택하세요.");
		}
		//모두 만족했다면 데이터베이스에 저장한다.
		else {
			String imagePath = new String();
			if(!fieldImgPath.getText().isEmpty()) {
				File imageFile = new File(fieldImgPath.getText());	//선택한 이미지를 File객체로 하나 만듦
				imagePath = FTPUploader.uploadFile("image", imageFile);
			}
			else {
				imagePath = "uploadedfiles/images/default.jpg";
			}
			User user = new User(fieldUserNo.getText(), fieldUserName.getText(), fieldPassword.getText(), fieldUserMail.getText(), fieldUserTel.getText(), imagePath, comboBoxDept.getSelectionModel().getSelectedItem().toString(), "", "", 0);
			loginDao.insertUserData(user);
			ChkDialogMain.chkDialog("등록이 완료되었습니다.");
			Stage stage = (Stage)btnSubmit.getScene().getWindow();
			try {
				Parent loginDialog = FXMLLoader.load(getClass().getResource("login.fxml"));
				Scene scene = new Scene(loginDialog);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//취소 버튼을 누르면 사용자 등록창을 닫음
	public void handleBtnCancelAction() {
		Stage stage = (Stage)btnCancel.getScene().getWindow();
		try {
			Parent loginDialog = FXMLLoader.load(getClass().getResource("login.fxml"));
			Scene scene = new Scene(loginDialog);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
