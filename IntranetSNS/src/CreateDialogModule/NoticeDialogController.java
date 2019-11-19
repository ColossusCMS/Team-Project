package CreateDialogModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.Notice;
import Dao.NoticeDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : CreateDialogModule
패키지 버전 : 1.0.0
클래스 이름 : NoticeDialogController
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 공지사항의 상세 내용을 출력하는 다이얼로그의 컨트롤러

패키지 버전 변경 사항
 */
public class NoticeDialogController implements Initializable {
	@FXML
	private Label lblTitle, lblContent;
	@FXML
	private Button btnClose;

	NoticeDao noticeDao = new NoticeDao();
	public static String noticeNo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Notice notice = noticeDao.getSelectedNotice(noticeNo);
		btnClose.setOnAction(event -> btnClose.getScene().getWindow().hide());
		lblTitle.setText(notice.getNoticeTitle());
		lblContent.setText(notice.getNoticeContent());
	}
}
