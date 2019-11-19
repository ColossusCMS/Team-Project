package LoginModule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : LoginModule
패키지 버전 : 1.2.0
클래스 이름 : LoginMain
해당 클래스 작성 : 최문석, 김도엽

해당 클래스 주요 기능
- 프로그램이 실행되었을 때 가장 먼저 등장하는 로그인 창을 띄움
- 프로그램의 전체 시작점

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

//프로그램의 시작점
public class LoginMain extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setTitle("로그인");
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}