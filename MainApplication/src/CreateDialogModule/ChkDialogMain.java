package CreateDialogModule;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 안내용 다이얼로그
모듈 버전 : 1.0.0
클래스 이름 : ChkDialogMain
해당 클래스 작성 : 최문석

필요 전체 Java파일
- ChkDialogMain.java (실행 결과나 오류를 보여주는 사용자 중심 안내용 임시 다이얼로그)

필요 fxml파일
- chkDialog.fxml (안내용 임시 다이얼로그 fxml)

해당 클래스 주요 기능
- 사용자가 실행한 결과 또는 오류를 안내하기 위한 다이얼로그
- 각기 다른 상황에서 해당하는 안내문을 출력함
 */
public class ChkDialogMain {
	//안내용 임시 다이얼로그를 띄우는 메서드
	//매개변수로 해당 안내문을 입력받아 각각 다른 상황에서 다른 다이얼로그 안내문을 출력
	public static void chkDialog(String labelText) {
		Stage chkDialog = new Stage(StageStyle.UTILITY);
		Parent parent;
		try {
			parent = (Parent)FXMLLoader.load(ChkDialogMain.class.getResource("chkDialog.fxml"));
			Scene scene = new Scene(parent);
			Label dialogLbl = (Label)parent.lookup("#dialogLbl");
			dialogLbl.setText(labelText);
			dialogLbl.setFont(new Font(12));
			Button dialogBtn = (Button)parent.lookup("#dialogBtn");
			dialogBtn.setOnAction(e -> chkDialog.hide());
			chkDialog.initModality(Modality.APPLICATION_MODAL);
			chkDialog.setAlwaysOnTop(true);
			chkDialog.setResizable(false);
			chkDialog.setScene(scene);
			chkDialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//내 정보나 등록된 사용자를 선택했을 때
	//상세정보를 띄워주는 다이얼로그
	public static void businessCardDialog(String userNo) {
		BusinessCardController.userNo = userNo;
		Stage businessCard = new Stage(StageStyle.UNDECORATED);
		Parent another;
		try {
			another = FXMLLoader.load(ChkDialogMain.class.getResource("businessCard.fxml"));
			Scene scene = new Scene(another);
			businessCard.setScene(scene);
			businessCard.setResizable(false);
			businessCard.initModality(Modality.APPLICATION_MODAL);
			businessCard.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//공지사항을 띄우는 창
	public static void noticeDialog() {
		Stage noticeDialog = new Stage(StageStyle.UTILITY);
		Parent another;
		try {
			another = FXMLLoader.load(ChkDialogMain.class.getResource("noticeDialog.fxml"));
			Scene scene = new Scene(another);
			noticeDialog.setScene(scene);
			noticeDialog.setResizable(false);
			noticeDialog.initModality(Modality.APPLICATION_MODAL);
			noticeDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
