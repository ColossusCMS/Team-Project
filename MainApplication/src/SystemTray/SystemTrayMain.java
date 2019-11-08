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
import javafx.application.Platform;
import javafx.stage.Stage;

public class SystemTrayMain {
	private Stage stage;
	private boolean firstTime;
	private TrayIcon trayIcon;
	private String userNo;
	
	public SystemTrayMain(Stage stage, boolean firstTime, String userNo) {
		this.stage = stage;
		this.firstTime = firstTime;
		this.userNo = userNo;
	}
	
	public void createTrayIcon() {
		if(SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;
			try {
				URL url = new URL("http://yaahq.dothome.co.kr/uploadedfiles/images/TrayIconSample.png");
				image = ImageIO.read(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stage.setOnCloseRequest(event -> hide(stage));
			
			final ActionListener closeListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("체크");
					LoginDao loginDao = new LoginDao();
					loginDao.updateLoginStatus(userNo, "logout");
					IdSaveLoad.resetUserId();
					System.exit(0);
				}
			};
			
			ActionListener showListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Platform.runLater(() -> {
						stage.show();
					});					
				}
			};
			
			PopupMenu popup = new PopupMenu();
			MenuItem showItem = new MenuItem("열기");
			MenuItem closeItem = new MenuItem("프로그램 종료");
			showItem.addActionListener(showListener);
			closeItem.addActionListener(closeListener);
			popup.add(showItem);
			popup.addSeparator();
			popup.add(closeItem);
			
			trayIcon = new TrayIcon(image, "사내SNS", popup);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(showListener);
			try {
				tray.add(trayIcon);
			} catch (Exception e) {	}
		}
	}
	
	public void showProgramIsMinimizedMsg() {
		if(firstTime) {
			trayIcon.displayMessage("프로그램을 최소화했습니다.", "", MessageType.INFO);
			firstTime = false;
		}
	}
	
	public void hide(final Stage stage) {
		Platform.runLater(() -> {
			if(SystemTray.isSupported()) {
				stage.hide();
				showProgramIsMinimizedMsg();
			}
			else {
				System.exit(0);
			}
		});
	}
}
