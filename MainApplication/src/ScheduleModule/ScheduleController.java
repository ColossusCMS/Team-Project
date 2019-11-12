package ScheduleModule;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ClassPackage.Schedule;
import Dao.ScheduleDao;
import MainModule.MainController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ScheduleModule
패키지 버전 : 1.0.0
클래스 이름 : ScheduleController
해당 클래스 작성 : 최문석, 심대훈

해당 클래스 주요 기능
- 일정 화면을 구현하는 컨트롤러
- 달력 형태로 화면이 구성되어 있고 개인 일정 또는 단체 일정 등을 표시한다.
- 라디오버튼을 이용해 일정의 종류에 따라 다르게 표시도 가능

패키지 버전 변경 사항
 */
public class ScheduleController implements Initializable {
	@FXML RadioButton radioBtnAll, radioBtnPrivate, radioBtnGroup;
	@FXML ToggleGroup radioGroupSchedule;
	@FXML Button btnPrevMonth, btnNextMonth, btnDayOffApply;
	@FXML GridPane gridPaneCalendar;
	@FXML Label lblYear, lblMonth, lblMonthTxt;
	
	private ArrayList<Integer> daysList = new ArrayList<Integer>(42);	//달력입력될 날짜들을 넣는 리스트
	private ArrayList<String> privateSchList = new ArrayList<String>();	//개인 일정을 담아두는 리스트
	private ArrayList<String> groupSchList = new ArrayList<String>();	//단체 일정을 담아두는 리스트
	/*
	날짜를 사용하는데 Calendar와 Date를 사용하지 않은 이유
	1. 불변 객체가 아니기 때문에 set으로 변경을 했을 때 올바른 값이 출력되지 않을 수 있다는 문제점이 있다.
	2. 상수 필드 남용
	3. 월표기가 0부터 시작하는 것
	4. 요일 상수에 일관성이 없다. 어디서는 일요일이 0, 어디서는 일요일이 1
	5. Date와 Calendar객체의 역할 분담.
	 */
	YearMonth yearMonth;	//연월을 저장하는 변수
	LocalDate localDate;		//컴퓨터의 현재 날짜를 가져오는 변수
	
//	ObservableList<Integer> list = FXCollections.observableArrayList(daysList);
	
	int thisYear;		//현재 날짜의 연도
	int thisMonth;	//현재 날짜의 월
	public static String USERNO = MainController.USER_NO;	//해당 사용자를 확인하기 위한 정적 변수
	ScheduleDao scheduleDao = new ScheduleDao();			//데이터베이스 접속
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		yearMonth = YearMonth.now();	//현재 날짜의 연월을 가져옴 20XX-XX의 형태
		thisYear = yearMonth.getYear();	//연도
		thisMonth = yearMonth.getMonthValue();	//월
		lblYear.setText(thisYear + "");
		lblMonth.setText(thisMonth + "");
		lblMonthTxt.setText(yearMonth.getMonth().toString());
		
		btnPrevMonth.setPrefSize(100, 30);
		btnNextMonth.setPrefSize(100, 30);
		btnPrevMonth.setOnAction(event -> handleBtnPrevMonth());
		btnNextMonth.setOnAction(event -> handleBtnNextMonth());
		btnPrevMonth.setText(yearMonth.minusMonths(1).getMonthValue() + "월");	//현재 월의 이전 달
		btnNextMonth.setText(yearMonth.plusMonths(1).getMonthValue() + "월");		//현재 월의 다음 달
		
		inputListDays(yearMonth);		//달력의 날짜를 구성
		createCalendar();	//달력을 만듦
		
		//일정 창 상단의 라디오 버튼의 동작 구현
		radioGroupSchedule.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(javafx.beans.value.ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				createCalendar();
			};
		});
		btnDayOffApply.setOnAction(event -> handleBtnDayOffApplyAction());	//휴가신청 버튼
	}
	
	//달력을 구현하는데 해당 날짜들을 해당 요일에 맞게 넣기 위한 초기 작업
	//아이디어
	//한 달이 최소 4주, 최대 6주일 경우가 발생, 따라서 최대 일 수인 42일을 리스트의 크기로 지정하고
	//해당 월의 요일에 맞춰 1부터 해당 월의 마지막일까지 입력
	//일요일 0 ~ 토요일 6으로 지정해 리스트의 시작점을 지정한다.
	//일을 전부 입력하고 리스트의 남은 부분은 모두 0으로 초기화
	public void inputListDays(YearMonth yearMonth) {
		daysList.clear();	//리스트 초기화
		int dayLength = yearMonth.lengthOfMonth();	//해당 달의 전체 일 수를 구함.
		int thisYear = yearMonth.getYear();				//해당 연도를 정수로 구함
		int thisMonth = yearMonth.getMonthValue();	//해당 월을 정수로 구함
		String dayOfWeek = LocalDate.of(thisYear, thisMonth, 1).getDayOfWeek().toString();	//해당 연 월의 1일(첫째 날)의 요일
		int start = 0;	//리스트의 시작점 인덱스가 됨
		int end = 0;	//리스트의 끝점 인덱스가 됨
		//첫째 날의 요일을 가지고 인덱스의 시작점을 정함
		switch(dayOfWeek) {
		case "SUNDAY":
			start = 0;
			break;
		case "MONDAY":
			start = 1;
			break;
		case "TUESDAY":
			start = 2;
			break;
		case "WEDNESDAY":
			start = 3;
			break;
		case "THURSDAY":
			start = 4;
			break;
		case "FRIDAY":
			start = 5;
			break;
		case "SATURDAY":
			start = 6;
			break;
		}
		//끝점을 계산해서 정함
		end = dayLength + start;
		//리스트로 쓰려면 처음부터 집어넣어야함.
		//리스트의 맨 앞부터 차례대로 집어넣음
		for(int i = 0; i < start; i++) {
			daysList.add(0);
		}
		//시작점부터 날짜를 차례대로 입력
		int day = 1;
		for(int i = 0; i < dayLength; i++) {
			daysList.add(day);
			day++;
		}
		for(int i = end; i < 42; i++) {
			daysList.add(0);
		}
	}
	
	//달력을 생성하는 부분
	public void createCalendar() {
		int index = 0;
		gridPaneCalendar.getChildren().clear();		//이걸 하지 않으면 이 전의 데이터가 남기 때문에 겹쳐 보임
		scheduleDao.entryDate(thisYear, thisMonth, privateSchList, groupSchList, USERNO);
		for(int i = 0; i < 6; i++) {	//행 수
			for(int j = 0; j < 7; j++) {	//열 수
				Button btn = new Button();
				btn.setText(daysList.get(index) + "");	//리스트 순으로 버튼 생성
				btn.setFont(new Font(20));
				btn.setPrefSize(gridPaneCalendar.getPrefWidth()/7, gridPaneCalendar.getPrefHeight()/6);
				if(daysList.get(index) == 0) {		//일 수가 0이면 해당 월의 일이 아니기 때문에 버튼에 아무것도 입력하지 않고 버튼 비활성화
					btn.setText("");
					btn.setDisable(true);
				}
				if(j == 0) {		//1열 : 일요일
					btn.setTextFill(Paint.valueOf("RED"));		//일요일이니 버튼 글자 빨간색
				}
				else if(j == 6) {	//7열 : 토요일
					btn.setTextFill(Paint.valueOf("BLUE"));	//토요일이니 버튼 글자 파란색
				}
				gridPaneCalendar.add(btn, j, i);	//그리드팬에 순서대로 버튼 추가
				
				//라디오 버튼에 따라서 버튼 색을 다르게 표현하는 과정
				if(radioBtnAll.isSelected()) {	//전체 일정으로 선택했을 때
					//개인 일정과 단체 일정이 모두 등록되어 있다면 버튼을 녹색으로 표시
					if(privateSchList.contains(daysList.get(index) + "") && groupSchList.contains(daysList.get(index) + "")) {
						btn.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
					}
					//개인 일정만 등록되어 있다면 버튼을 빨간색으로 표시
					else if(privateSchList.contains(daysList.get(index) + "")) {
						btn.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
					}
					//단체 일정만 등록되어 있다면 버튼을 파란색으로 표시
					else if(groupSchList.contains(daysList.get(index) + "")) {
						btn.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
					}
				}
				else if(radioBtnPrivate.isSelected()) {	//개인 일정으로 선택했을 때
					//버튼 색을 빨간색으로
					if(privateSchList.contains(daysList.get(index) + "")) {
						btn.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
					}
				}
				else if(radioBtnGroup.isSelected()) {		//단체 일정으로 선택했을 때
					//버튼 색을 파란색으로
					if(groupSchList.contains(daysList.get(index) + "")) {
						btn.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
					}
				}
				index++;
				btn.setOnAction(event -> writeSchedule(btn.getText()));	//버튼 액션
			}
		}
	}
	
//	public void colorChange() {
//		scheduleDao.entryDate(thisYear, thisMonth, privateSchList, groupSchList, USERNO);
//	}
	
	public void handleBtnPrevMonth() {
		yearMonth = yearMonth.minusMonths(1);
		moveMonth();
	}
	
	public void handleBtnNextMonth() {
		yearMonth = yearMonth.plusMonths(1);
		moveMonth();
	}
	
	//월을 이동했을 때의 메서드
	//이 메서드가 동작할 때는 월이 바뀐 상태
	//월이 바뀐 상태로 달력을 새로 생성하는 것
	public void moveMonth() {
		inputListDays(yearMonth);		//해당 월의 일 수로 리스트 새로 생성
		thisYear = yearMonth.getYear();
		thisMonth = yearMonth.getMonthValue();
		lblYear.setText(yearMonth.getYear() + "");
		lblMonth.setText(yearMonth.getMonthValue() + "");
		lblMonthTxt.setText(yearMonth.getMonth().toString());
		btnPrevMonth.setText(yearMonth.minusMonths(1).getMonthValue() + "월");
		btnNextMonth.setText(yearMonth.plusMonths(1).getMonthValue() + "월");
		createCalendar();
	}
	
	//날짜를 클릭하면 스케줄 입력창이 뜸
	public void writeSchedule(String date) {
		//이 부분은 전부 일정 등록 창으로 데이터를 보내기 위해 정적 변수를 사용
		Schedule.year = thisYear;
		Schedule.month = thisMonth;
		Schedule.date = date;
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent anotherPane = FXMLLoader.load(getClass().getResource("createSchedule.fxml"));
			Scene scene = new Scene(anotherPane);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setOnHiding(event -> createCalendar());	//일정 등록이 끝나면 달력을 새로 갱신함
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//휴가신청 창 만드는 메서드
	public void handleBtnDayOffApplyAction() {
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent anotherPane = FXMLLoader.load(getClass().getResource("dayOffApply.fxml"));
			Scene scene = new Scene(anotherPane);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
