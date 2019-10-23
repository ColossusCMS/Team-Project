package MainModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import BoardModule.BoardController;
import ClassPackage.BoardTableView;
import ClassPackage.User;
import CreateDialogModule.ChkDialogMain;
import Dao.BoardDao;
import Dao.DeptDao;
import Dao.LoginDao;
import Dao.UserInfoDao;
import EncryptionDecryption.PasswordEncryption;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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
모듈버전 : 0.5.0
해당 클래스 작성 : 최문석

필요 전체 Java파일
- MainController.java (로그인 이후 등장하는 프로그램의 메인화면)

필요 fxml파일
- main.fxml (메인화면 창 fxml)

필요 import 사용자 정의 package
- 

해당 클래스 주요 기능
- 

버전 변경 사항
1.0.0
- 
 */
public class MainController implements Initializable {
	@FXML private ToggleButton toggleBtnNotice, toggleBtnUser, toggleBtnChat, toggleBtnBoard; // 왼쪽 영역 버튼
	@FXML private ToggleGroup groupCategory;	//왼쪽 영역 버튼 토글 그룹
	@FXML private Button btnSchedule;	//왼쪽 영역 일정 버튼
	@FXML private Button btnWrite, btnBoardRefresh;	//게시물 탭 글쓰기, 새로고침 버튼
	@FXML private Button btnUserRefresh;	//사용자 상세 목록 새로고침 버튼
	@FXML private ComboBox<String> comboSideFilter;		//오른쪽 영역 이름 검색 필드
	@FXML private TableView<BoardTableView> viewBoardList;	//게시판 탭 테이블뷰
	@FXML private TableView<User> viewSIdeUserList;	//오른쪽 영역 사용자 목록 테이블뷰
	@FXML private AnchorPane paneNotice, paneUser, paneChat, paneBoard, stackedPane;
	@FXML private TextField fieldCenterFilter, fieldSideFilter;	//이름 검색 필드
	@FXML private TabPane paneUserList;	//사용자 탭 목록 영역
	@FXML private ImageView viewUserImg;
	@FXML private Label lblMyDept, lblMyName, lblMyPosition, lblMyGreet;

	BackgroundFill selectedFill = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
	Background selectedBack = new Background(selectedFill);

	BackgroundFill notSelectedFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
	Background notSelectedBack = new Background(notSelectedFill);
	
	//게시판에서 사용하는 리스트
	ObservableList<BoardTableView> list = FXCollections.observableArrayList();
	ObservableList<String> comboList = FXCollections.observableArrayList();
	
	BoardDao bd = new BoardDao();
	LoginDao ld = new LoginDao();
	UserInfoDao userInfoDao = new UserInfoDao();
	DeptDao deptDao = new DeptDao();
	
	User myProfile;	//현재 접속중인 유저의 정보를 가져오는 변수
	public static String USER_NO;	//현재 접속중인 사용자 번호를 가져오는 변수
	
	
	//오른쪽 영역에서 사용하는 리스트
	ObservableList<User> sideUserList = FXCollections.observableArrayList();	//오른쪽 영역 테이블뷰 사용자 목록 리스트
	ObservableList<String> sideFilterList = FXCollections.observableArrayList();	//오른쪽 영역 콤보박스 필터용 리스트
	
	
	
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		USER_NO = loadUserNo();
		myProfile = userInfoDao.selectMyInfo(USER_NO);
		
		// 왼쪽 영역 버튼 초기화
		selected(toggleBtnNotice);
		notSelected(toggleBtnUser);
		notSelected(toggleBtnChat);
		notSelected(toggleBtnBoard);
		paneNotice.setVisible(true);
		paneUser.setVisible(false);
		paneChat.setVisible(false);
		paneBoard.setVisible(false);
		btnSchedule.setOnMouseClicked(event -> schedule());
		
		//왼쪽 영역 토글 버튼 그룹화
		groupCategory.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				click(newValue.getUserData().toString());
			}
		});

		// 오른쪽 영역 부분
		viewSIdeUserList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);	//가로 스크롤바 없애려고 씀
		createSideTable(viewSIdeUserList);
		viewSIdeUserList.setOnMouseClicked(event -> {
			if(event.getClickCount() >= 2) {
				String userNo = viewSIdeUserList.getSelectionModel().getSelectedItem().getUserNo();
				ChkDialogMain.businessCardDialog(userNo);
			}
		});
		
		//오른쪽 영역 콤보박스 만드는 부분
		deptDao.loadDept(sideFilterList);
		comboSideFilter.setItems(sideFilterList);
		//콤보박스 필터를 기준으로 동작했을 경우
		comboSideFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterAction());
		//텍스트필드 필터를 기준으로 동작했을 경우
		fieldSideFilter.setOnKeyPressed(event -> {
			KeyCode keyCode = event.getCode();
			//키보드에서 엔터를 눌렀을 때 동작한다는 말
			if(keyCode.equals(KeyCode.ENTER)) {
				filterAction();
			}
		});
		
		//오른쪽 영역 새로고침 버튼
		btnUserRefresh.setOnAction(event -> {
			comboSideFilter.getSelectionModel().selectFirst();
			fieldSideFilter.clear();
			userInfoDao.loadAllUser(sideUserList, USER_NO);
			createSideTable(viewSIdeUserList);
		});
		
		// 알림 탭 부분

		// 사용자 정보 탭 부분

		// 채팅방 탭 부분

		// 게시판 탭 부분
		// 테이블뷰에서 스크롤바 없애는 코드
//		viewBoardList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//		createTable();
//		btnWrite.setOnAction(event -> writeBoard());
//		btnBoardRefresh.setOnAction(event -> {
//			bd.loadAllBbsList(viewBoardList, list);
//			comboFilter.getSelectionModel().selectFirst();
//		});
		// 일정 탭 부분
	}

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
					private HBox box;
					private Label dept, name, position;
					{
						box = new HBox();
						dept = new Label();
						name = new Label();
						position = new Label();
						box.getChildren().addAll(dept, name, position);
						dept.setStyle("-fx-pref-width:30; -fx-font-size:13; -fx-alignment:ceter-right");
						name.setStyle("-fx-pref-width:45; -fx-font-size:14; -fx-font-weight:bold; -fx-alignment:center-left;");
						position.setStyle("-fx-pref-width:20; -fx-font-size:12; -fx-alignment:center-left");
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
							setGraphic(box);
						}
					}
				};
			}
		});
		
		userInfoDao.loadAllUser(sideUserList, USER_NO);
		sideTable.setItems(sideUserList);
		sideTable.getColumns().add(userInfoCol);
	}
	
	
//	public void filterAction() {
//		if(fieldFilter.getText().isEmpty() || fieldFilter.getText().equals("")) {	//검색필드가 비어있다면
//			if(comboFilter.getSelectionModel().getSelectedItem().equals("전체")) {		//콤보박스를 전체로 선택하면 모든 정보 불러옴
//				uid.loadAllUser(userList, viewUserList, userNo);
//			}
//			else {	//필드가 비어있고 콤보박스를 선택하면 콤보박스를 기준으로 결과 불러옴
//				uid.loadFilteredAllUser(userList, viewUserList, userNo, comboFilter.getSelectionModel().getSelectedItem());
//			}
//		}
//		else {	//필드에 무언가가 적혀있다면
//			if(comboFilter.getSelectionModel().getSelectedItem().equals("전체")) {	//필드에 적혀있고 전체를 선택하면 필드를 기준으로 결과 불러옴
//				uid.loadFilteredAllUser(userNo, userList, viewUserList, fieldFilter.getText());
//			}
//			else {	//필드에 적혀있고 콤보박스를 선택하면 둘 다 포함한 결과 불러옴
//				uid.loadFilteredAllUser(userList, viewUserList, userNo, comboFilter.getSelectionModel().getSelectedItem(), fieldFilter.getText());
//			}
//		}
//	}
	
	
	
	public void filterAction() {
		
	}
	
	// 게시판 탭의 테이블 생성하는 부분
	public void createTable() {
		TableColumn<BoardTableView, String> col1 = new TableColumn<BoardTableView, String>();
		col1.setStyle(
				"-fx-pref-width:40; -fx-border-width:1; -fx-pref-height:40; -fx-font-size:10px; -fx-alignment:center");
		col1.setCellValueFactory(new PropertyValueFactory<BoardTableView, String>("boardHeader"));
		TableColumn<BoardTableView, String> col2 = new TableColumn<BoardTableView, String>();
		col2.setStyle(
				"-fx-pref-width:180; -fx-border-width:1; -fx-pref-height:40; -fx-font-size:15px; -fx-alignment:center-left");
		col2.setCellValueFactory(new PropertyValueFactory<BoardTableView, String>("boardTitle"));
		TableColumn<BoardTableView, BoardTableView> col3 = new TableColumn<BoardTableView, BoardTableView>();
		col3.setStyle("-fx-pref-width:85; -fx-border-width:1; -fx-pref-height:40; -fx-alignment:center-left");

		col3.setCellValueFactory(
				new Callback<CellDataFeatures<BoardTableView, BoardTableView>, ObservableValue<BoardTableView>>() {
					@SuppressWarnings("rawtypes")
					@Override
					public ObservableValue<BoardTableView> call(
							CellDataFeatures<BoardTableView, BoardTableView> features) {
						return new ReadOnlyObjectWrapper<BoardTableView>(features.getValue());
					}
				});
		col3.setCellFactory(
				new Callback<TableColumn<BoardTableView, BoardTableView>, TableCell<BoardTableView, BoardTableView>>() {
					@Override
					public TableCell<BoardTableView, BoardTableView> call(
							TableColumn<BoardTableView, BoardTableView> param) {
						return new TableCell<BoardTableView, BoardTableView>() {
							private VBox box;
							private Label writer;
							private Label date;
							{
								box = new VBox();
								writer = creatLabel();
								date = creatLabel();
								box.getChildren().addAll(writer, date);
								setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
							}

							private final Label creatLabel() {
								Label label = new Label();
								VBox.setVgrow(label, Priority.ALWAYS);
								return label;
							}

							@Override
							protected void updateItem(BoardTableView item, boolean empty) {
								super.updateItem(item, empty);
								if (item == null) {
									setGraphic(null);
								} else {
									writer.setText(item.getBoardWriter());
									date.setText(item.getBoardDate());
									setGraphic(box);
								}
							}
						};
					}
				});
		viewBoardList.getColumns().addAll(col1, col2, col3);
		viewBoardList.setItems(list);

		bd.loadAllBbsList(viewBoardList, list);
		comboFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals("전체게시판")) {
					bd.loadAllBbsList(viewBoardList, list);
				} else {
					bd.loadFilteredBbsList(viewBoardList, list, newValue);
				}
			}
		});

		// 테이블뷰의 헤더 영역을 없애는 코드
		viewBoardList.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Pane header = (Pane) viewBoardList.lookup("TableHeaderRow");
				if (header.isVisible()) {
					header.setPrefHeight(0);
					header.setVisible(false);
				}
			}
		});

		// 테이블뷰에서 마우스 더블클릭 동작을 구현
		// 테이블뷰에서 해당 게시물을 더블클릭하면 해당 게시물의 내용을 열람할 수 있음.
		viewBoardList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() >= 2) {
					// 더블클릭을 했다면 게시물 열람 창을 생성
					readContent(viewBoardList.getSelectionModel().getSelectedItem());
				}
			}
		});
	}
	
	public String loadUserNo() {
//		String path = System.getProperty("user.home") + "/Documents/MySNS/id.txt;		//윈10에서 내 문서에 있는 파일 찾으러 갈 수 있는 경로
		String path = "c:/MySNS/id.txt";
//		String path = "e:/MySNS/id.txt";
		String id = new String();
		FileReader fr = null;
		BufferedReader br = null;
		StringWriter sw = null;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			sw = new StringWriter();
			int ch = 0;
			while((ch = br.read()) != -1) {
				sw.write(ch);
			}
			br.close();
			//복호화해서 사용자번호를 가져옴
			id = PasswordEncryption.pwDecryption(sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
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

			paneNotice.setVisible(true);
			paneUser.setVisible(false);
			paneChat.setVisible(false);
			paneBoard.setVisible(false);
		}
		//사용자 탭 버튼을 눌렀을 때
		else if (btnName.equals("user")) {
			notSelected(toggleBtnChat);
			selected(toggleBtnUser);
			notSelected(toggleBtnNotice);
			notSelected(toggleBtnBoard);

			paneNotice.setVisible(false);
			paneUser.setVisible(true);
			paneChat.setVisible(false);
			paneBoard.setVisible(false);
		}
		//채팅 탭을 눌렀을 때
		else if (btnName.equals("chat")) {
			selected(toggleBtnChat);
			notSelected(toggleBtnUser);
			notSelected(toggleBtnNotice);
			notSelected(toggleBtnBoard);

			paneNotice.setVisible(false);
			paneUser.setVisible(false);
			paneChat.setVisible(true);
			paneBoard.setVisible(false);
		}
		//게시판 탭을 눌렀을 때
		else if (btnName.equals("board")) {
			selected(toggleBtnBoard);
			notSelected(toggleBtnUser);
			notSelected(toggleBtnChat);
			notSelected(toggleBtnNotice);

			paneNotice.setVisible(false);
			paneUser.setVisible(false);
			paneChat.setVisible(false);
			paneBoard.setVisible(true);
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
	
	//일정 버튼을 눌렀을 때 동작
	public void schedule() {

	}
}
