package ScheduleModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.Reg;
import ClassPackage.Schedule;
import Dao.ScheduleDao;
import MainModule.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ScheduleModule
패키지 버전 : 1.0.0
클래스 이름 : CreateScheduleController
해당 클래스 작성 : 최문석, 심대훈

해당 클래스 주요 기능
- 일정 등록 창 컨트롤러
- 화면 왼쪽에서는 개인 일정을 등록 또는 삭제
- 오른쪽 화면에서는 등록된 개인 일정 또는 단체 일정 리스트를 확인

패키지 버전 변경 사항
 */
public class CreateScheduleController implements Initializable {
	@FXML TextField txtFieldTitle;
	@FXML TextArea txtAreaContent;
	@FXML Button btnReg, btnCancel,btnDel;
	@FXML Label lblDate;
	@FXML TableView<Reg> tblViewPrivateSch, tblViewGroupSch;

	ObservableList<Reg> privateList = FXCollections.observableArrayList();	//개인 일정을 담아두는 리스트
	ObservableList<Reg> groupList = FXCollections.observableArrayList();	//단체 일정을 담아두는 리스트
	ScheduleDao sd = new ScheduleDao();
	
	//버튼으로 선택한 날짜를 가져옴
	int year = Schedule.year;
	int month = Schedule.month;
	String date = Schedule.date;
	//String userno = ScheduleController.USERNO;
	String today;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblDate.setText(year + "년 " + month + "월 " + date + "일");
		btnReg.setOnAction(event -> handleBtnRegAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnDel.setOnAction(event -> handleBtnDelAction());
		
		today = year + "-" + month + "-" + date;
		sd.loadPrivateSchedule(privateList, MainController.USER_NO, today);
		sd.loadGroupSchedule(groupList, MainController.USER_NO, today);
		
		//테이블 생성
		createTbl(tblViewPrivateSch, privateList);
		createTbl(tblViewGroupSch, groupList);
		
		//테이블 선택 액션
		tblViewPrivateSch.setOnMouseClicked(event -> {
			if(!tblViewPrivateSch.getSelectionModel().isEmpty()) {
				txtFieldTitle.setText(tblViewPrivateSch.getSelectionModel().getSelectedItem().getSchTitle());
				txtAreaContent.setText(tblViewPrivateSch.getSelectionModel().getSelectedItem().getSchContent());
				btnReg.setDisable(false);
				btnDel.setDisable(false);
			}
		});
		tblViewGroupSch.setOnMouseClicked(event -> {
			if(!tblViewPrivateSch.getSelectionModel().isEmpty()) {
				txtFieldTitle.setText(tblViewGroupSch.getSelectionModel().getSelectedItem().getSchTitle());
				txtAreaContent.setText(tblViewGroupSch.getSelectionModel().getSelectedItem().getSchContent());
				btnReg.setDisable(true);
				btnDel.setDisable(true);
			}
		});
	}
	
	//테이블을 생성하는 메서드
	//개인 일정 테이블과 단체 일정 테이블을 생성하는데 매개변수에 따라 다른 테이블 생성
	public void createTbl(TableView<Reg> table, ObservableList<Reg> list) {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);	//테이블 열 크기 지정 불가
		//열을 구성하는 부분
		TableColumn<Reg, String> schTitleCol = new TableColumn<Reg, String>();
		schTitleCol.setCellValueFactory(new PropertyValueFactory<Reg, String>("schTitle"));
		table.getColumns().add(schTitleCol);
		table.setItems(list);
		//테이블의 헤더 삭제
		table.widthProperty().addListener((obs, oldValue, newValue) -> {
			Pane header = (Pane)table.lookup("TableHeaderRow");
			if(header.isVisible()) {
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
	}
	
	//일정 등록 버튼
	//등록하고 리스트 자동 갱신
	public void handleBtnRegAction() {
		if(tblViewPrivateSch.getSelectionModel().isEmpty()) {
			sd.entrySchedule(new Reg(MainController.USER_NO, txtFieldTitle.getText(), txtAreaContent.getText(), today, null));
		}
		else {
			sd.updateSchedule(new Reg(MainController.USER_NO, txtFieldTitle.getText(), txtAreaContent.getText(), today, null));
		}
		privateList.clear();
		sd.loadPrivateSchedule(privateList, MainController.USER_NO, today);
		tblViewPrivateSch.setItems(privateList);
		txtFieldTitle.clear();
		txtAreaContent.clear();
	}
	
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
	
	//개인 일정 삭제 메서드
	//데이터베이스에서 해당 내용을 삭제하고
	//개인 일정 리스트 재구성하고
	//다시 테이블 안의 내용을 구성
	public void handleBtnDelAction() {
		sd.deleteSchedule(new Reg(MainController.USER_NO, txtFieldTitle.getText(), txtAreaContent.getText(), today, "1"));
		privateList.clear();
		sd.loadPrivateSchedule(privateList, MainController.USER_NO, today);
		tblViewPrivateSch.setItems(privateList);
		txtFieldTitle.clear();
		txtAreaContent.clear();
	}
}
