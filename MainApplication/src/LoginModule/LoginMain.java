package LoginModule;
    
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
프로젝트 제목 : 로그인
버전 : 0.9.0
작성 : 김도엽, 최문석

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
public class LoginMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
    	Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
//		scene.getStylesheets().add(getClass().getResource("project.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("로그인");
    }
    
    public static void main(String[] args) {
    	Application.launch(args);
    }
}