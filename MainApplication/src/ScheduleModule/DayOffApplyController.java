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
		if(dateStart == null || dateFinish == null) {
			ChkDialogMain.chkDialog("날짜를 선택하세요.");
		}
		else if(dateStart.until(dateFinish).isNegative() || dateStart.until(dateFinish).isZero()) {
			ChkDialogMain.chkDialog("끝나는 날짜는 시작하는 날짜보다 같거나 빠를 수 없습니다.");
		}
		else if(!dateStart.until(LocalDate.now()).isNegative() || dateStart.until(LocalDate.now()).isZero()) {
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