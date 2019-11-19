package SystemTray;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.imageio.ImageIO;

import Dao.LoginDao;
import IdSaveLoadModule.IdSaveLoad;
import InitializePackage.DataProperties;
import javafx.application.Platform;
import javafx.stage.Stage;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : SystemTray
패키지 버전 : 1.0.0
클래스 이름 : SystemTrayMain
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 프로그램의 시스템 트레이 기능을 구현한 클래스

패키지 버전 변경 사항
 */
public class SystemTrayMain {
	private Stage stage; // 시스템 트레이를 적용할 스테이지를 가져옴(여기서는 메인 컨트롤러)
	private boolean firstTime; // 툴팁을 최초 한 번만 띄우기 위한 변수
	private TrayIcon trayIcon;
	private String userNo; // 데이터베이스에서 사용자 정보를 변경하기 위해서 값을 가져옴

	public SystemTrayMain(Stage stage, boolean firstTime, String userNo) {
		this.stage = stage;
		this.firstTime = firstTime;
		this.userNo = userNo;
	}

	public void createTrayIcon() {
		// 시스템 트레이를 지원한다면
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray(); // 현재 데스크탑의 시스템 트레이 인스턴스를 가져옴
			Image image = null;
			try {
				// SFTP서버에 있는 트레이아이콘을 가져옴
				URL url = new URL("http://" + DataProperties.getIpAddress() + ":"
						+ DataProperties.getPortNumber("HTTPServer") + "/images/TrayIconSample.png");
				image = ImageIO.read(url); // 서버에서 가져온 경로로 이미지 생성
			} catch (Exception e) {
				e.printStackTrace();
			}
			stage.setOnCloseRequest(event -> hide(stage));

			// 이 부분의 액션리스너는 시스템 트레이의 메뉴에서 선택할 때의 액션리스너
			// 프로그램을 종료할 때의 액션리스너
			final ActionListener closeListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LoginDao loginDao = new LoginDao();
					loginDao.updateLoginStatus(userNo, "logout"); // 프로그램을 종료했으니 데이터베이스에도 로그아웃 상태로 바꿈
					IdSaveLoad.resetUserId(); // 파일로 저장되어 있는 사용자의 아이디를 없앰
					System.exit(0); // 프로그램 완전 종료
				}
			};

			// 트레이 상태에서 다시 창을 활성화하는 액션리스너
			ActionListener showListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Platform.runLater(() -> {
						stage.show(); // 창을 다시 활성화
					});
				}
			};

			// 시스템 트레이 메뉴 만드는 부분
			PopupMenu popup = new PopupMenu();
			MenuItem showItem = new MenuItem("열기");
			MenuItem closeItem = new MenuItem("프로그램 종료");
			showItem.addActionListener(showListener);
			closeItem.addActionListener(closeListener);
			popup.add(showItem);
			popup.addSeparator();
			popup.add(closeItem);

			// 이 부분은 트레이 아이콘을 적용시키는 부분
			trayIcon = new TrayIcon(image, "사내SNS", popup);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(showListener);
			try {
				tray.add(trayIcon);
			} catch (Exception e) {
			}
		}
	}

	// 시스템 트레이 상태로 바뀔 때 출력되는 팝업
	public void showProgramIsMinimizedMsg() {
		// 최초 한 번만 출력되도록
		if (firstTime) {
			trayIcon.displayMessage("프로그램을 최소화했습니다.", "", MessageType.INFO);
			firstTime = false;
		}
	}

	// 프로그램을 닫았을 때 메서드
	public void hide(final Stage stage) {
		Platform.runLater(() -> {
			// 시스템 트레이가 동작한다면 프로그램 종료가 아니라 창을 닫고 시스템 트레이 상태로 변경
			if (SystemTray.isSupported()) {
				stage.hide();
				showProgramIsMinimizedMsg();
			}
			// 시스템 트레이를 지원하지 않는다면 프로그램 종료
			else {
				System.exit(0);
			}
		});
	}
}
