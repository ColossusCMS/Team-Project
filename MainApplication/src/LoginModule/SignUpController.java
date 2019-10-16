package LoginModule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import ChkDialogModule.ChkDialogMain;
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
 * 프로젝트 제목 : 사용자 등록
 * 버전 : 0.9.0
 * 작성 : 최문석
 * 
 * 필요 Java파일
 * - Main.java (사용자 등록이 실행되는 메인 클래스)
 * - SignUpDao.java (데이터베이스 접속, 데이터 불러오기, 데이터 삽입)
 * - SignUpController.java (사용자 등록창 컨트롤러)
 * - User.java (사용자 정보 저장을 위한 클래스)
 * 
 * 필요 fxml파일 :
 * - signUp.fxml (사용자 등록창 fxml)
 * - chkDialog.fxml (안내 다이얼로그 fxml)
 * 
 * 주요 기능
 * - 사용자 등록을 위한 사용자 정보 입력 (사원번호, 이름, 비밀번호, 이메일, 전화번호, 대표사진),
 * - 데이터베이스에 정보를 저장하기 위해 데이터베이스 연동 및 데이터베이스 삽입,
 * - 중복체크를 위해 데이터베이스로부터 값을 읽어옴
 * 
 * 차후계획 : 이미지를 선택하는 기능에서 로컬이 아닌 웹에서 URL을 이용해 등록 가능하게 기능을 추가
 */
public class SignUpController implements Initializable {
	@FXML private TextField userNo, userName, userTel, userMail, imgPath;
	@FXML private Button overlapChk, imgBtn, submitBtn, cancelBtn;
	@FXML private PasswordField password, passwordChk;
	@FXML private Label pwChkLbl;
	@FXML private ImageView imgView;
	@FXML private ComboBox<String> dept;
	
	private ObservableList<String> comboList = FXCollections.observableArrayList("개발1팀", "개발2팀", "영업1팀", "영업2팀", "기획팀", "디자인팀", "경영팀", "인사팀");
	LoginDao ld = new LoginDao();
	int overlapChkNum = 0;	//중복체크 여부 확인을 위한 변수
	Pattern telPattern = Pattern.compile("(010)-\\d{4}-\\d{4}");	//휴대폰 번호 패턴
	Pattern mailPattern = Pattern.compile("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}");	//이메일 패턴
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dept.setItems(comboList);
		overlapChk.setOnAction(event -> overlapChk());
		imgBtn.setOnAction(event -> loadImg());
		submitBtn.setOnAction(event -> submit());
		cancelBtn.setOnAction(event -> cancel());
		
		imgPath.setEditable(false);
		
		//만약 중복체크를 하고나서 사원번호를 새로 입력하면 다시 중복체크 버튼을 원상복구시켜야 해야하니
		//속성 감시를 해서 값이 바뀐다면 버튼을 원복시킴
		userNo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!oldValue.equals(newValue)) {
					overlapChk.setText("중복체크");
					overlapChk.setDisable(false);
					overlapChkNum = 0;
				}
			}
		});
		
		//비밀번호확인 필드의 속성을 항상 체크하면서 앞서 입력한 비밀번호와 동일한지 계속해서 체크
		//만약 같아진다면 일치한다고 출력하고 그렇지 않으면 불일치로 계속 출력
		passwordChk.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals(password.getText())) {
					pwChkLbl.setText("일치");
					pwChkLbl.setTextFill(Color.GREEN);
				}
				else {
					pwChkLbl.setText("불일치");
					pwChkLbl.setTextFill(Color.RED);
				}
			}
		});
	}
	
	//중복체크 버튼을 눌렀을 때의 이벤트
	public void overlapChk() {
		if(userNo.getText().equals("")) {		//만약 필드에 아무 값도 입력하지 않고 버튼을 눌렀다면 값을 입력하라고 다이얼로그를 띄움
			ChkDialogMain.chkDialog("사원번호를 입력하세요.");
		}
		else {	//그게 아니라면 정상적으로 진행
			boolean overlap = ld.loadUserNo(userNo.getText());	//DB에서 중복되는 번호가 있는지 체크함
			if(overlap) {		//만약 true라면 중복된 값이 있다는 뜻이니 사용할 수 없다는 다이얼로그를 띄움
				ChkDialogMain.chkDialog("이미 등록된 사원번호입니다.");
				userNo.clear();
				overlapChkNum = 0;
			}
			else {					//false라면 중복되는 값이 없다는 뜻이니 중복체크 버튼의 텍스트를 사용가능으로 바꿔줌
				overlapChk.setText("사용가능");
//				overlapChk.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY	)));
				overlapChk.setDisable(true);
				overlapChkNum = 1;
			}
		}
	}
	
	//찾기 버튼을 누르면 이미지를 찾기위한 열기 다이얼로그가 띄워짐
	//파일을 선택하면 해당 디렉토리부터 파일명까지 imgPath에 자동으로 입력이 됨
	public void loadImg() {
		//파일 열기 다이얼로그를 불러옴
		FileChooser imgChooser = new FileChooser();
		//선택 가능한 확장자 지정(대표 이미지 확장자)
		imgChooser.getExtensionFilters().addAll(
				new ExtensionFilter("모든 이미지 파일(*.jpg, *.png, *.gif)", "*.jpg", "*.png", "*.gif")
			);
		//파일 열기 다이얼로그를 띄울 위치
		File selectedFile = imgChooser.showOpenDialog((Stage)imgBtn.getScene().getWindow());
		try {
			//파일 읽어오기
			FileInputStream fis = new FileInputStream(selectedFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			//이미지 생성
			Image img = new Image(bis);
			//경로 필드에 선택한 이미지의 경로를 출력하고
			imgPath.setText(selectedFile.getAbsolutePath());
			//이미지뷰에 선택한 이미지를 띄움
			imgView.setImage(img);
			imgView.setFitHeight(133);
			imgView.setFitWidth(100);
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
	public void submit() {
		//필수항목을 체크한다.
		//만약 입력하지 않은 값이 있다면 다시 입력하라고 다이얼로그를 띄움
		if(userNo.getText().isEmpty() || userName.getText().isEmpty() || password.getText().isEmpty() || userTel.getText().isEmpty()
				|| userMail.getText().isEmpty()) {
			ChkDialogMain.chkDialog("필수항목을 입력하세요.");
		}
		//중복체크했는지 안했는지 체크
		else if(overlapChkNum == 0) {
			ChkDialogMain.chkDialog("중복체크를 하세요.");
		}
		//비밀번호가 일치하는지
		else if(pwChkLbl.getText().equals("불일치")) {
			ChkDialogMain.chkDialog("비밀번호를 확인하세요.");
		}
		//소속 부서를 선택했는지
		else if(dept.getSelectionModel().getSelectedItem() == null) {
			ChkDialogMain.chkDialog("소속을 선택하세요.");
		}
		//전화번호 형식에 맞는지
		else if(!telPattern.matcher(userTel.getText()).matches()) {
			ChkDialogMain.chkDialog("전화번호 형식에 맞지 않습니다.");
		}
		//메일형식에 맞는지
		else if(!mailPattern.matcher(userMail.getText()).matches()) {
			ChkDialogMain.chkDialog("이메일 형식에 맞지 않습니다.");
		}
		//모두 만족했다면 데이터베이스에 저장한다.
		else {
			User user = new User(userNo.getText(), userName.getText(), password.getText(), userMail.getText(), userTel.getText(), imgPath.getText(), dept.getSelectionModel().getSelectedItem().toString());
			ld.insertUserData(user);
			ChkDialogMain.chkDialog("등록이 완료되었습니다.");
			Stage stage = (Stage)submitBtn.getScene().getWindow();
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
	public void cancel() {
		Stage stage = (Stage)cancelBtn.getScene().getWindow();
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
