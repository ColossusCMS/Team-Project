package MainModule;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import BoardModule.BoardController;
import ChatClientModule.ChatController;
import ClassPackage.BoardTableView;
import ClassPackage.Notice;
import ClassPackage.NoticeTableView;
import ClassPackage.User;
import CreateDialogModule.ChkDialogMain;
import CreateDialogModule.NoticeDialogController;
import Dao.BoardDao;
import Dao.DeptDao;
import Dao.LoginDao;
import Dao.NoticeDao;
import Dao.UserInfoDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 메인 화면
클래스 이름 : MainController
모듈버전 : 0.6.0
해당 클래스 작성 : 최문석

필요 전체 Java파일
- MainController.java (로그인 이후 등장하는 프로그램의 메인화면)

필요 fxml파일
- main.fxml (메인화면 창 fxml)

필요 import 사용자 정의 package
- 

해당 클래스 주요 기능
- 프로그램의 메인화면, 필요한 요소를 모두 만들고 초기화함

버전 변경 사항
1.0.0
- 
 */
public class MainController implements Initializable {
	//왼쪽 영역
	@FXML private ToggleButton toggleBtnNotice, toggleBtnUser, toggleBtnChat, toggleBtnBoard; // 왼쪽 영역 버튼
	@FXML private ToggleGroup groupCategory;	//왼쪽 영역 버튼 토글 그룹
	@FXML private Button btnSchedule;	//왼쪽 영역 일정 버튼
	
	//오른쪽 영역
	@FXML private ComboBox<String> comboBoxSideFilter;		//오른쪽 영역 부서선택 콤보박스
	@FXML private TableView<User> tblViewSideUserList;	//오른쪽 영역 사용자 목록 테이블뷰
	@FXML private TextField txtFieldSideFilter;	//오른쪽 영역 이름 검색 필드
	
	//가운데 영역
	@FXML private AnchorPane anchorPaneNotice, anchorPaneUser, anchorPaneChat, anchorPaneBoard;
	
	//알림
	@FXML private Label lblMainNoticeTitle, lblMainNoticeContent;
	@FXML private Button btnNoticeRefresh;
	@FXML private TableView<NoticeTableView> tblViewNotice;
	@FXML private Button btnFold;
	@FXML private HBox boxMainNotice;
	
	//사용자정보
	@FXML private AnchorPane anchorPaneStackedPane;	//내 정보 카드 더블 클릭 때 사용
	@FXML private Button btnUserRefresh;	//사용자 상세 목록 새로고침 버튼
	@FXML private ImageView imgViewUserImg;
	@FXML private TabPane tabPaneUser;	//사용자 탭 목록 영역
	@FXML private TextField txtFieldUserFilter;	//사용자 탭 이름 검색 필드
	@FXML private Label lblMyDept, lblMyName, lblMyPosition, lblMyStatusMsg;
	
	//채팅
	@FXML private Button btnOpenChat, btnDeptChat;
	
	//게시판
	@FXML private Button btnWrite, btnBoardRefresh;	//게시물 탭 글쓰기, 새로고침 버튼
	@FXML private TableView<BoardTableView> tblViewBoardList;	//게시판 탭 테이블뷰
	@FXML private ComboBox<String> comboBoxBoardFilter;

	BackgroundFill selectedFill = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
	Background selectedBack = new Background(selectedFill);

	BackgroundFill notSelectedFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
	Background notSelectedBack = new Background(notSelectedFill);

	BoardDao boardDao = new BoardDao();
	LoginDao loginDao = new LoginDao();
	UserInfoDao userInfoDao = new UserInfoDao();
	DeptDao deptDao = new DeptDao();
	NoticeDao noticeDao = new NoticeDao();
	
	User myProfile;	//현재 접속중인 유저의 정보를 가져오는 변수
	public static String USER_NO;	//현재 접속중인 사용자 번호를 가져오는 변수
	
	//오른쪽 영역에서 사용하는 리스트
	ObservableList<User> sideTblViewUserList = FXCollections.observableArrayList();	//오른쪽 영역 테이블뷰 사용자 목록 리스트
	ObservableList<String> sideComboBoxList = FXCollections.observableArrayList();	//오른쪽 영역 콤보박스 필터용 리스트
	
	//알림 탭에서 사용하는 리스트
	ObservableList<NoticeTableView> noticeTblViewNoticeList = FXCollections.observableArrayList();
	
	//사용자 탭에서 사용하는 리스트
	ObservableList<User> userTblViewUserList = FXCollections.observableArrayList();	//사용자 탭 테이블에 띄우는 사용자 리스트
	ObservableList<String> userTabDeptList = FXCollections.observableArrayList();		//사용자 탭 내에서 탭목록 띄우는 부서 리스트
	
	
	//게시판에서 사용하는 리스트
	ObservableList<BoardTableView> boardTblViewBoardList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		myProfile = userInfoDao.selectMyInfo(USER_NO);
		
		// 왼쪽 영역 버튼 초기화
		selected(toggleBtnNotice);
		notSelected(toggleBtnUser);
		notSelected(toggleBtnChat);
		notSelected(toggleBtnBoard);
		anchorPaneNotice.setVisible(true);
		anchorPaneUser.setVisible(false);
		anchorPaneChat.setVisible(false);
		anchorPaneBoard.setVisible(false);
		
		//왼쪽 영역 토글 버튼 그룹화
		groupCategory.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				click(newValue.getUserData().toString());
			}
		});
		
		//오른쪽 영역
		//오른쪽 영역 테이블 부분
		createSideTable(tblViewSideUserList);	//오른쪽 영역 테이블 생성
		tblViewSideUserList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);	//가로 스크롤바 없애려고 씀
		tblViewSideUserList.setOnMouseClicked(event -> {
			if(event.getClickCount() >= 2) {
				String userNo = tblViewSideUserList.getSelectionModel().getSelectedItem().getUserNo();
				ChkDialogMain.businessCardDialog(userNo);
			}
		});
		
		//오른쪽 영역 콤보박스 만드는 부분
		sideComboBoxList.add("전체");
		deptDao.loadAllDept(sideComboBoxList);
		comboBoxSideFilter.setItems(sideComboBoxList);
		comboBoxSideFilter.getSelectionModel().selectFirst();
		//콤보박스 필터를 기준으로 동작했을 경우
		comboBoxSideFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> sideFilterAction());
		//텍스트필드 필터를 기준으로 동작했을 경우
		txtFieldSideFilter.setOnKeyPressed(event -> {
			KeyCode keyCode = event.getCode();
			//키보드에서 엔터를 눌렀을 때 동작한다는 말
			if(keyCode.equals(KeyCode.ENTER)) {
				sideFilterAction();
			}
		});
		
		// 알림 탭
		btnNoticeRefresh.setOnAction(event -> handleBtnNoticeRefreshAction());
		btnFold.setOnAction(event -> noticeFold(btnFold));
		setNotice();
		createNoticeTable(tblViewNotice);
		
		//목록 자동으로 새로고침하는 쓰레드
		//데몬 쓰레드로 실행
		Thread thread = new Thread() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(300000);
						setNotice();
						noticeTblViewNoticeList.clear();
						noticeDao.getAllNotice(noticeTblViewNoticeList);
						noticeDao.getPrivateSchedule(noticeTblViewNoticeList, USER_NO);
						noticeDao.getGroupSchedule(noticeTblViewNoticeList, USER_NO);
						noticeDao.getRecentlyDeptBoard(noticeTblViewNoticeList, USER_NO);
						noticeDao.getRecentlyBoard(noticeTblViewNoticeList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
		
		//사용자 정보 탭
		//내정보 띄우는 부분
		lblMyName.setText(myProfile.getUserName());
		lblMyDept.setText(myProfile.getUserDept());
		lblMyPosition.setText(myProfile.getUserPosition());
		lblMyStatusMsg.setText(myProfile.getUserStatusMsg());
		//내정보 이미지 파일 가져오는 곳
		if(!myProfile.getUserImgPath().isEmpty() && !(myProfile.getUserImgPath() == null)) {
			String url = "http://yaahq.dothome.co.kr/" + myProfile.getUserImgPath();
			imgViewUserImg.setImage(new Image(url));
		}
		//내정보 더블클릭 했을 때 동작
		anchorPaneStackedPane.setOnMouseClicked(event -> {
			if(event.getClickCount() >= 2) {
				ChkDialogMain.privateCardDialog(USER_NO);
			}
		});
		
		//사용자탭 탭영역 생성
		userTabDeptList.add("전체");	//부서에는 전체라는 탭이 없기 때문에 전체 사용자를 출력하기 위해
											//리스트의 맨 처음에 전체라는 항목을 삽입
		createTabPane(tabPaneUser);
		//탭 선택에 따른 동작
		tabPaneUser.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> centerFilterAction());
		
		//사용자탭 새로고침 버튼
		btnUserRefresh.setOnAction(event -> {
			comboBoxSideFilter.getSelectionModel().selectFirst();
			txtFieldSideFilter.clear();
			userInfoDao.loadAllUser(sideTblViewUserList, USER_NO);
			createSideTable(tblViewSideUserList);
		});		
		
		// 채팅방 탭 부분
		btnOpenChat.setOnAction(event -> handleBtnChatAction(btnOpenChat.getText()));
		btnDeptChat.setOnAction(event -> handleBtnChatAction(btnDeptChat.getText()));
		
		
		// 게시판 탭 부분
		// 테이블뷰에서 스크롤바 없애는 코드
		createBoardTable(tblViewBoardList);
		btnWrite.setOnAction(event -> writeBoard());
		btnBoardRefresh.setOnAction(event -> {
			boardDao.loadAllBoardList(boardTblViewBoardList);
			tblViewBoardList.getSelectionModel().selectFirst();
		});
		
		//게시판 탭 콤보박스 만드는 부분
		comboBoxBoardFilter.setItems(userTabDeptList);
		comboBoxBoardFilter.getSelectionModel().selectFirst();		//기본적으로 콤보박스는 첫 번째 요소를 선택
		comboBoxBoardFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue)->{
			if(newValue.equals("전체")) {	//콤보박스에서 전체를 선택하면 모든 게시판을 선택했다는 뜻
				boardDao.loadAllBoardList(boardTblViewBoardList);	//필터링이 없으니 모든 게시물 출력
			}
			else {	//콤보박스에서 무언가를 선택했다면
				boardDao.loadFilteredBoardList(boardTblViewBoardList, newValue);	//필터링한 결과만 출력
			}
		});
		
		// 일정 탭 부분
		btnSchedule.setOnAction(event -> handleBtnScheduleAction());
	}
	
	//오른쪽 영역 테이블 생성 메서드
	public void createSideTable(TableView<User> sideTable) {
		//테이블뷰의 헤더 부분없애는 부분
		sideTable.widthProperty().addListener((obs, oldValue, newValue) -> {
			Pane header = (Pane)sideTable.lookup("TableHeaderRow");
			if(header.isVisible()) {
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
		
		//테이블뷰의 열을 만드는 작업
		TableColumn<User, User> userInfoCol = new TableColumn<User, User>();
		userInfoCol.setStyle("-fx-pref-width:90; -fx-border-width:1; -fx-pref-height:20; -fx-alignment:center-left");
		userInfoCol.setCellValueFactory(new Callback<CellDataFeatures<User,User>, ObservableValue<User>>() {
			@Override
			public ObservableValue<User> call(CellDataFeatures<User, User> param) {
				return new ReadOnlyObjectWrapper<User>(param.getValue());
			}
		});
		userInfoCol.setCellFactory(new Callback<TableColumn<User,User>, TableCell<User,User>>() {
			@Override
			public TableCell<User, User> call(TableColumn<User, User> param) {
				return new TableCell<User, User>() {
					private VBox vbox;
					private HBox hbox;
					private Label dept, name, position;
					{
						vbox = new VBox();
						hbox = new HBox();
						dept = new Label();
						name = new Label();
						position = new Label();
						hbox.getChildren().addAll(position, name);
						vbox.getChildren().addAll(hbox, dept);
						hbox.setStyle("-fx-pref-width:95; -fx-pref-height:25;");
						vbox.setStyle("-fx-pref-width:95; -fx-pref-height:50;");
						dept.setStyle("-fx-pref-width:95; -fx-font-size:13; -fx-alignment:center_right");
						name.setStyle("-fx-pref-width:60; -fx-font-size:14; -fx-font-weight:bold; -fx-alignment:center_left;");
						position.setStyle("-fx-pref-width:30; -fx-font-size:12; -fx-alignment:center_left");
					}
					@Override
					protected void updateItem(User item, boolean empty) {
						super.updateItem(item, empty);
						if(item == null) {
							setGraphic(null);
						}
						else {
							dept.setText(item.getUserDept());
							name.setText(item.getUserName());
							position.setText(item.getUserPosition());
							setGraphic(vbox);
						}
					}
				};
			}
		});
		
		userInfoDao.loadAllUser(sideTblViewUserList, USER_NO);
		sideTable.setItems(sideTblViewUserList);
		sideTable.getColumns().add(userInfoCol);
	}
	
	//오른쪽 영역 필터링
	//콤보박스를 선택, 필드를 입력.
	public void sideFilterAction() {
		if(txtFieldSideFilter.getText().isEmpty() || txtFieldSideFilter.getText().equals("")) {	//검색필드가 비어있다면
			if(comboBoxSideFilter.getSelectionModel().getSelectedItem().equals("전체")) {		//콤보박스를 전체로 선택하면 모든 정보 불러옴
				userInfoDao.loadAllUser(sideTblViewUserList, USER_NO);
			}
			else {	//필드가 비어있고 콤보박스를 선택하면 콤보박스를 기준으로 결과 불러옴
				userInfoDao.loadFilteredAllUser(sideTblViewUserList, USER_NO, comboBoxSideFilter.getSelectionModel().getSelectedItem());
			}
		}
		else {	//필드에 무언가가 적혀있다면
			if(comboBoxSideFilter.getSelectionModel().getSelectedItem().equals("전체")) {	//필드에 적혀있고 전체를 선택하면 필드를 기준으로 결과 불러옴
				userInfoDao.loadFilteredAllUser(USER_NO, sideTblViewUserList, txtFieldSideFilter.getText());
			}
			else {	//필드에 적혀있고 콤보박스를 선택하면 둘 다 포함한 결과 불러옴
				userInfoDao.loadFilteredAllUser(sideTblViewUserList, USER_NO, comboBoxSideFilter.getSelectionModel().getSelectedItem(), txtFieldSideFilter.getText());
			}
		}
	}
	
	public void createTabPane(TabPane tabPane) {
		int rowCnt = deptDao.loadAllDept(userTabDeptList);
		for(int i = 0; i < rowCnt; i++) {
			Tab tab = new Tab();
			tab.setText(userTabDeptList.get(i));
			tabPaneUser.getTabs().add(tab);
		}
		//이 부분은 전체 탭에 사용자 정보를 넣기 위한 작업
		tabPaneUser.getSelectionModel().getSelectedItem().setContent(createUserInfoTable(userTabDeptList.get(0), ""));
	}
	
	//사용자 탭 안의 탭에 들어갈 각각의 테이블 생성
	@SuppressWarnings("unchecked")
	public TableView<User> createUserInfoTable(String tabName, String filterTxt) {
		TableView<User> userTable = new TableView<User>();
		//탭 이름에 따라 다른 테이블을 생성하기 위해 작성
		if(filterTxt.equals("")) {	//텍스트 필드에 아무것도 입력하지 않고
			if(tabName.equals("전체")) {	//콤보박스에 전체라고 선택되어 있다면 자신을 제외한 모든 사용자 출력
				userInfoDao.loadAllUser(userTblViewUserList, USER_NO);
			}
			else {	//탭이 선택된 상태라면 해당 탭에 맞는 테이블 생성
				userInfoDao.loadFilteredAllUser(userTblViewUserList, USER_NO, tabName);
			}
		}
		else {	//텍스트 필드에 무언가가 입력된 상태라면
			if(tabName.equals("전체")) {	//필드에 입력된 값을 기준으로 모든 사용자를 출력
				userInfoDao.loadAllUser(userTblViewUserList, USER_NO, filterTxt);
			}
			else {	//필드와 탭 모두 만족하는 테이블 생성
				userInfoDao.loadFilteredAllUser(userTblViewUserList, USER_NO, tabName, filterTxt);
			}
		}
		//테이블 헤더 없애는 부분
		userTable.widthProperty().addListener((obs, oldValue, newValue) -> {
			Pane header = (Pane)userTable.lookup("TableHeaderRow");
			if(header.isVisible()) {
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
		
		//사용자 이미지가 들어갈 첫번째 열
		TableColumn<User, User> userImgCol = new TableColumn<User, User>();
		userImgCol.setStyle("-fx-pref-width:80; -fx-border-width:1; -fx-pref-height:80; -fx-alignment:center");
		userImgCol.setCellValueFactory(new Callback<CellDataFeatures<User,User>, ObservableValue<User>>() {
			@Override
			public ObservableValue<User> call(CellDataFeatures<User, User> param) {
				return new ReadOnlyObjectWrapper<User>(param.getValue());
			}
		});
		userImgCol.setCellFactory(new Callback<TableColumn<User,User>, TableCell<User,User>>() {
			@Override
			public TableCell<User, User> call(TableColumn<User, User> param) {
				return new TableCell<User, User>() {
					private Pane box;
					private ImageView imgView;
					{
						box = new Pane();
						imgView = new ImageView();
						imgView.setFitWidth(75.0);
						imgView.setPreserveRatio(true);
						box.getChildren().add(imgView);
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					}
					@Override
					protected void updateItem(User item, boolean empty) {
						super.updateItem(item, empty);
						if(item == null) {
							setGraphic(null);
						}
						else {
							//나중에 DB에서 가져와 해당 사용자의 이미지로 대체할 수 있도록
							String url = "http://yaahq.dothome.co.kr/" + item.getUserImgPath();
							imgView.setImage(new Image(url));
							setGraphic(box);
						}
					}
				};
			}
		});
		
		
		//사용자의 이름, 소속 등이 출력되는 열
		TableColumn<User, User> userInfoCol = new TableColumn<User, User>();
		userInfoCol.setStyle("-fx-pref-width:220; -fx-border-width:1; -fx-pref-height:80; -fx-alignment:center_left");
		userInfoCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User,User>, ObservableValue<User>>() {
			@Override
			public ObservableValue<User> call(CellDataFeatures<User, User> param) {
				return new ReadOnlyObjectWrapper<User>(param.getValue());
			}
		});
		userInfoCol.setCellFactory(new Callback<TableColumn<User,User>, TableCell<User,User>>() {
			@Override
			public TableCell<User, User> call(TableColumn<User, User> param) {
				return new TableCell<User, User>() {
					private VBox vbox;
					private HBox hbox;
					private Label dept, name, position, msg;
					{
						vbox = new VBox();
						hbox = new HBox();
						dept = new Label();
						name = new Label();
						position = new Label();
						msg = new Label();
						hbox.getChildren().addAll(dept, name, position);
						vbox.getChildren().addAll(hbox, msg);
						hbox.setStyle("-fx-pref-height:30");
						dept.setStyle("-fx-pref-width:70; -fx-pref-height:30; -fx-font-size:15px; -fx-alignment:center_left;");
						name.setStyle("-fx-pref-width:80; -fx-pref-height:30; -fx-font-size:20px; -fx-alignment:center_left; -fx-font-weight:bold;");
						position.setStyle("-fx-pref-width:50; -fx-pref-height:30; -fx-font-size:14px; -fx-alignment:center_left;");
						msg.setStyle("-fx-pref-width:200; -fx-pref-height:50; -fx-font-size:12px; -fx-alignment:center_left; -fx-padding:5");
					}
					@Override
					protected void updateItem(User item, boolean empty) {
						super.updateItem(item, empty);
						if(item == null) {
							setGraphic(null);
						}
						else {
							dept.setText(item.getUserDept());
							name.setText(item.getUserName());
							position.setText(item.getUserPosition());
							msg.setText(item.getUserStatusMsg());
							setGraphic(vbox);
						}
					}
				};
			}
		});
		//테이블뷰의 가로 스크롤 없애는 부분
		userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//더블클릭 동작 구현
		userTable.setOnMouseClicked(event -> {
			if(event.getClickCount() >= 2) {
				ChkDialogMain.businessCardDialog(userTable.getSelectionModel().getSelectedItem().getUserNo());
			}
		});
		userTable.getColumns().addAll(userImgCol, userInfoCol);
		userTable.setItems(userTblViewUserList);
		return userTable;
	}

	//사용자 탭에서 사용할 필터 동작
	//탭을 선택할 때마다 달라져야 하기 때문에
	public void centerFilterAction() {
		//필터링 동작 순서
		//필드에 값이 있는지 없는 판단
		//선택된 탭을 확인
		//필드에 공백일 때 탭을 선택 또는 필드에 값이 있는 상태에서 탭을 선택
		Tab selectedTab = tabPaneUser.getSelectionModel().getSelectedItem();
		String inputField = txtFieldUserFilter.getText();
		TableView<User> table = new TableView<User>();
		if(inputField.equals("") || inputField.isEmpty()) {	//필드에 값이 없는 상태라면
			//그러면 탭에 맞춰서 결과를 출력
			table = createUserInfoTable(selectedTab.getText(), "");
		}
		else {	//필드에 어떠한 값이 있다면
			//필드값과 탭의 이름을 같이 비교해서 출력
			table = createUserInfoTable(selectedTab.getText(), inputField);
		}
		selectedTab.setContent(table);
	}

	// 게시판 탭의 테이블 생성하는 부분
	@SuppressWarnings("unchecked")
	public void createBoardTable(TableView<BoardTableView> boardTable) {
		// 테이블뷰의 헤더 영역을 없애는 코드
		boardTable.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Pane header = (Pane) boardTable.lookup("TableHeaderRow");
				if (header.isVisible()) {
					header.setPrefHeight(0);
					header.setVisible(false);
				}
			}
		});
		
		TableColumn<BoardTableView, String> headerCol = new TableColumn<BoardTableView, String>();
		headerCol.setStyle("-fx-pref-width:40; -fx-border-width:1; -fx-pref-height:40; -fx-font-size:10px; -fx-alignment:center");
		headerCol.setCellValueFactory(new PropertyValueFactory<BoardTableView, String>("boardHeader"));
		TableColumn<BoardTableView, String> titleCol = new TableColumn<BoardTableView, String>();
		titleCol.setStyle("-fx-pref-width:180; -fx-border-width:1; -fx-pref-height:40; -fx-font-size:15px; -fx-alignment:center-left");
		titleCol.setCellValueFactory(new PropertyValueFactory<BoardTableView, String>("boardTitle"));
		TableColumn<BoardTableView, BoardTableView> writerDateCol = new TableColumn<BoardTableView, BoardTableView>();
		writerDateCol.setStyle("-fx-pref-width:85; -fx-border-width:1; -fx-pref-height:40; -fx-alignment:center-left");
		//3번째 열은 사용자 지정형태로 만들기 위해서 새로 작업
		writerDateCol.setCellValueFactory(new Callback<CellDataFeatures<BoardTableView, BoardTableView>, ObservableValue<BoardTableView>>() {
			@Override
			public ObservableValue<BoardTableView> call(
				CellDataFeatures<BoardTableView, BoardTableView> features) {
				return new ReadOnlyObjectWrapper<BoardTableView>(features.getValue());
			}
		});
		writerDateCol.setCellFactory(	new Callback<TableColumn<BoardTableView, BoardTableView>, TableCell<BoardTableView, BoardTableView>>() {
			@Override
			public TableCell<BoardTableView, BoardTableView> call(
					TableColumn<BoardTableView, BoardTableView> param) {
				return new TableCell<BoardTableView, BoardTableView>() {
					private VBox box;
					private Label writer;
					private Label date;
					{
						box = new VBox();
						writer = new Label();
						date = new Label();
						writer.setStyle("-fx-alignment:center_left; -fx-pref-height:20");
						date.setStyle("-fx-font-size:10px; -fx-alignment:center_left; -fx-pref-height:20");
						box.getChildren().addAll(writer, date);
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					}
					@Override
					protected void updateItem(BoardTableView item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null) {
							setGraphic(null);
						}
						else {
							writer.setText(item.getBoardWriter());
							date.setText(item.getBoardDate().substring(0, 10));
							setGraphic(box);
						}
					}
				};
			}
		});
		
		//가로 스크롤 없앰
		boardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);		
		// 테이블뷰에서 마우스 더블클릭 동작을 구현
		// 테이블뷰에서 해당 게시물을 더블클릭하면 해당 게시물의 내용을 열람할 수 있음.
		boardTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() >= 2) {
					// 더블클릭을 했다면 게시물 열람 창을 생성
					readContent(boardTable.getSelectionModel().getSelectedItem());
				}
			}
		});
		
		boardDao.loadAllBoardList(boardTblViewBoardList);
		boardTable.getColumns().addAll(headerCol, titleCol, writerDateCol);
		boardTable.setItems(boardTblViewBoardList);	
	}
	
	//게시판 탭에서 게시물을 더블클릭했을 때 게시물 열람 창을 만드는 메서드
	public void readContent(BoardTableView selectedCell) {
		BoardController.BBS_ID = selectedCell.getBoardNo();
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("/BoardModule/board.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setResizable(false);
			stage.setTitle(selectedCell.getBoardTitle());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//게시판 탭에서 글쓰기 버튼을 눌렀을 때
	public void writeBoard() {
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("/BoardModule/boardWrite.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//채팅방을 선택하고 채팅을 실행하는 메서드
	public void handleBtnChatAction(String btnName) {
		User user = userInfoDao.selectMyInfo(USER_NO);
		ChatController.name = user.getUserName();
		ChatController.dept = "";
		String engName = "";
		if(btnName.equals("전체 채팅")) {
			engName = "all";
		}
		else {
			String deptName = user.getUserDept();
			switch(deptName) {
			case "개발":
				engName = "dev";
				break;
			case "경영":
				engName = "opt";
				break;
			case "인사":
				engName = "hr";
				break;
			case "영업":
				engName = "sales";
				break;
			case "디자인":
				engName = "design";
				break;
			}
		}
		ChatController.dept = engName;
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("/ChatClientModule/chat.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//왼쪽 영역 토글버튼 메서드
	//선택할 때마다 버튼 상태가 달라지도록 함
	public void click(String btnName) {
		//알림 탭 버튼을 눌렀을 때
		if (btnName.equals("notice")) {
			selected(toggleBtnNotice);
			notSelected(toggleBtnUser);
			notSelected(toggleBtnChat);
			notSelected(toggleBtnBoard);

			anchorPaneNotice.setVisible(true);
			anchorPaneUser.setVisible(false);
			anchorPaneChat.setVisible(false);
			anchorPaneBoard.setVisible(false);
		}
		//사용자 탭 버튼을 눌렀을 때
		else if (btnName.equals("user")) {
			notSelected(toggleBtnChat);
			selected(toggleBtnUser);
			notSelected(toggleBtnNotice);
			notSelected(toggleBtnBoard);

			anchorPaneNotice.setVisible(false);
			anchorPaneUser.setVisible(true);
			anchorPaneChat.setVisible(false);
			anchorPaneBoard.setVisible(false);
		}
		//채팅 탭을 눌렀을 때
		else if (btnName.equals("chat")) {
			selected(toggleBtnChat);
			notSelected(toggleBtnUser);
			notSelected(toggleBtnNotice);
			notSelected(toggleBtnBoard);

			anchorPaneNotice.setVisible(false);
			anchorPaneUser.setVisible(false);
			anchorPaneChat.setVisible(true);
			anchorPaneBoard.setVisible(false);
		}
		//게시판 탭을 눌렀을 때
		else if (btnName.equals("board")) {
			selected(toggleBtnBoard);
			notSelected(toggleBtnUser);
			notSelected(toggleBtnChat);
			notSelected(toggleBtnNotice);

			anchorPaneNotice.setVisible(false);
			anchorPaneUser.setVisible(false);
			anchorPaneChat.setVisible(false);
			anchorPaneBoard.setVisible(true);
		}
	}
	
	//토글 버튼을 눌렀을 때
	public void selected(ToggleButton btn) {
		btn.setDisable(true);
		btn.setBackground(selectedBack);
	}
	//토글이 풀렸을 때
	public void notSelected(ToggleButton btn) {
		btn.setDisable(false);
		btn.setBackground(notSelectedBack);
	}
	
	//알림 탭에서 접기, 펼치기 버튼 눌렀을 때
	public void noticeFold(Button btn) {
		if(btn.getText().equals("접기")) {	//접기 버튼을 눌렀을 때
			btn.setText("펼치기");
			lblMainNoticeContent.setVisible(false);
			boxMainNotice.setPrefHeight(35.0);
		}
		else {	//펼치기 버튼을 눌렀을 때
			btn.setText("접기");
			lblMainNoticeContent.setVisible(true);
			boxMainNotice.setPrefHeight(120.0);
		}
	}
	
	//공지사항 부분 생성하는 메서드
	public void setNotice() {
		Notice notice = noticeDao.getMainNotice();
		lblMainNoticeTitle.setText(notice.getNoticeTitle());
		lblMainNoticeContent.setText(notice.getNoticeContent());
	}
	
	//알림 탭 새로고침 버튼 액션
	public void handleBtnNoticeRefreshAction() {
//		setNotice();
		noticeTblViewNoticeList.clear();
		noticeDao.getAllNotice(noticeTblViewNoticeList);
		noticeDao.getPrivateSchedule(noticeTblViewNoticeList, USER_NO);
		noticeDao.getGroupSchedule(noticeTblViewNoticeList, USER_NO);
		noticeDao.getRecentlyDeptBoard(noticeTblViewNoticeList, USER_NO);
		noticeDao.getRecentlyBoard(noticeTblViewNoticeList);
	}
	
	//알림 탭 테이블 생성
	@SuppressWarnings("unchecked")
	public void createNoticeTable(TableView<NoticeTableView> noticeTable) {
		// 테이블뷰의 헤더 영역을 없애는 코드
		noticeTable.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Pane header = (Pane) noticeTable.lookup("TableHeaderRow");
				if (header.isVisible()) {
					header.setPrefHeight(0);
					header.setVisible(false);
				}
			}
		});
		
		TableColumn<NoticeTableView, String> classCol = new TableColumn<NoticeTableView, String>("구분");
		classCol.setStyle("-fx-pref-width:80; -fx-border-width:1; -fx-pref-height:40; -fx-font-size:10px; -fx-alignment:center");
		classCol.setCellValueFactory(new PropertyValueFactory<NoticeTableView, String>("noticeClass"));
		TableColumn<NoticeTableView, NoticeTableView> noticeContentCol = new TableColumn<NoticeTableView, NoticeTableView>("알림 내용");
		noticeContentCol.setStyle("-fx-pref-width:220; -fx-border-width:1; -fx-pref-height:40; -fx-alignment:center-left");
		//사용자 지정형태
		noticeContentCol.setCellValueFactory(new Callback<CellDataFeatures<NoticeTableView, NoticeTableView>, ObservableValue<NoticeTableView>>() {
			@Override
			public ObservableValue<NoticeTableView> call(
				CellDataFeatures<NoticeTableView, NoticeTableView> features) {
				return new ReadOnlyObjectWrapper<NoticeTableView>(features.getValue());
			}
		});
		noticeContentCol.setCellFactory(new Callback<TableColumn<NoticeTableView, NoticeTableView>, TableCell<NoticeTableView, NoticeTableView>>() {
			@Override
			public TableCell<NoticeTableView, NoticeTableView> call(
					TableColumn<NoticeTableView, NoticeTableView> param) {
				return new TableCell<NoticeTableView, NoticeTableView>() {
					private VBox box;
					private Label title;
					private Label content;
					{
						box = new VBox();
						title = new Label();
						content = new Label();
						title.setStyle("-fx-alignment:center_left; -fx-pref-height:20");
						content.setStyle("-fx-font-size:10px; -fx-alignment:center_left; -fx-pref-height:20");
						box.getChildren().addAll(title, content);
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					}
					@Override
					protected void updateItem(NoticeTableView item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null) {
							setGraphic(null);
						}
						else {
							title.setText(item.getNoticeTitle());
							content.setText("자세한 내용을 확인하려면 더블 클릭하세요.");
							setGraphic(box);
						}
					}
				};
			}
		});
		
		//가로 스크롤 없앰
		noticeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);		
		// 테이블뷰에서 마우스 더블클릭 동작을 구현
		// 테이블뷰에서 해당 게시물을 더블클릭하면 해당 게시물의 내용을 열람할 수 있음.
		noticeTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() >= 2) {
					// 더블클릭을 했다면 게시물 열람 창을 생성
					NoticeTableView selectedCell = tblViewNotice.getSelectionModel().getSelectedItem();
					String className = selectedCell.getNoticeClass();
					String no = selectedCell.getNoticeNo();
					if(className.contains("공지")) {
						NoticeDialogController.noticeNo = no;
						ChkDialogMain.noticeDialog();
					}
					else if(className.contains("게시판")) {
						readContent(selectedCell);
					}
					else if(className.contains("일정")) {
						handleBtnScheduleAction();
					}
				}
			}
		});
		
		handleBtnNoticeRefreshAction();
		noticeTable.getColumns().addAll(classCol, noticeContentCol);
		noticeTable.setItems(noticeTblViewNoticeList);	
	}
	
	//알림탭 목록에서 게시판 내용을 선택했을 때 사용하는 메서드(오버로딩 됨)
	public void readContent(NoticeTableView selectedCell) {
		BoardController.BBS_ID = selectedCell.getNoticeNo();
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("/BoardModule/board.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setResizable(false);
			stage.setTitle(selectedCell.getNoticeTitle());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//일정 버튼을 눌렀을 때 동작
	public void handleBtnScheduleAction() {
		Stage stage = new Stage(StageStyle.UTILITY);
		try {
			Parent readBoardWindow = FXMLLoader.load(getClass().getResource("/ScheduleModule/schedule.fxml"));
			Scene scene = new Scene(readBoardWindow);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
