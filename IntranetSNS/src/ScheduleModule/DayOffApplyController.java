package ScheduleModule;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ClassPackage.DayOff;
import CreateDialogModule.ChkDialogMain;
import Dao.ScheduleDao;
import MainModule.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ScheduleModule
패키지 버전 : 1.0.0
클래스 이름 : DayOffApplyController
해당 클래스 작성 : 최문석, 심대훈

해당 클래스 주요 기능
- 휴가신청 창 컨트롤러
- 휴가를 신청할 날짜를 선택하고 사유를 입력함
- 신청한 날짜를 확인하는 기능이 포함되어 있고 신청한 날짜는 데이터베이스에 저장

패키지 버전 변경 사항
 */
public class DayOffApplyController implements Initializable {
	@FXML private DatePicker pickerDayOffStart, pickerDayOffFinish;
	@FXML private TextArea txtAreaReason;
	@FXML private Button btnApply;
	LocalDate dateStart, dateFinish;
	ScheduleDao scheduleDao;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnApply.setOnAction(event -> handleBtnApplyAction());
	}
	
	//체크해야할 상황
	//휴가 사유를 입력하지 않은 경우
	//끝나는 날짜가 시작 날짜보다 앞일 경우
	//시작날짜 또는 끝나는 날짜가 오늘 날짜보다 앞일 경우
	//날짜를 선택하지 않은 경우
	public void handleBtnApplyAction() {
		dateStart = pickerDayOffStart.getValue();
		dateFinish = pickerDayOffFinish.getValue();
		//선택한 날짜에 대한 조건을 판별
		if(dateStart == null || dateFinish == null) {	//날짜 선택을 하지 않았을 때
			ChkDialogMain.chkDialog("날짜를 선택하세요.");
		}
		else if(dateStart.until(dateFinish).isNegative() || dateStart.until(dateFinish).isZero()) {	//끝나는 날짜가 시작 날짜보다 앞 일 경우
			ChkDialogMain.chkDialog("끝나는 날짜는 시작하는 날짜보다 같거나 빠를 수 없습니다.");
		}
		else if(!dateStart.until(LocalDate.now()).isNegative() || dateStart.until(LocalDate.now()).isZero()) {	//시작 날짜가 현재 날짜보다 앞 일 경우 
			ChkDialogMain.chkDialog("시작하는 날짜는 오늘보다 같거나 빠를 수 없습니다.");
		}
		else if(txtAreaReason.getText().isEmpty()) {	//휴가 사유를 입력하지 않았다면
			ChkDialogMain.chkDialog("휴가 사유를 입력하세요.");
		}
		else {
			String userid = MainController.USER_NO;
			scheduleDao = new ScheduleDao();
			scheduleDao.entryDayOff(new DayOff(userid, pickerDayOffStart.getPromptText(), pickerDayOffFinish.getPromptText(), txtAreaReason.getText()));
			ChkDialogMain.chkDialog(dateStart.until(dateFinish).getDays() + "일간\n휴가신청이 완료되었습니다.");
			btnApply.getScene().getWindow().hide();
		}
	}
}