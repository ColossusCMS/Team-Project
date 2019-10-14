package WebViewTest;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MainController implements Initializable {
	@FXML private WebView userPage, mainPage;
	@FXML private ToggleButton notice, user, chat, board;
	@FXML private ToggleGroup category;
	@FXML private Button schedule;
	
	 BackgroundFill selectedFill = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY); 
	 Background selectedBack = new Background(selectedFill); 
	
	 BackgroundFill notSelectedFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY); 
	 Background notSelectedBack = new Background(notSelectedFill); 
	 
	 WebEngine we, we2;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		we = mainPage.getEngine();
		we2 = userPage.getEngine();
		we.load("http://localhost:8090/Notice.jsp");
		we2.load("http://localhost:8090/SideUser.jsp");
		
		selected(notice);
		notSelected(user);
		notSelected(chat);
		notSelected(board);
		
		category.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				System.out.println(newValue.getUserData().toString());
				click(newValue.getUserData().toString());
			}
		});
		schedule.setOnMouseClicked(event -> schedule());
	}
	
	public void click(String btnName) {
		if(btnName.equals("notice")) {
			selected(notice);
			notSelected(user);
			notSelected(chat);
			notSelected(board);
			we.load("http://localhost:8081/BBS/Notice.jsp");
		}
		else if(btnName.equals("user")) {
			selected(user);
			notSelected(notice);
			notSelected(chat);
			notSelected(board);
			we.load("http://localhost:8081/BBS/User.jsp");
		}
		else if(btnName.equals("chat")) {
			selected(chat);
			notSelected(user);
			notSelected(notice);
			notSelected(board);
			we.load("http://localhost:8081/BBS/Chat.jsp");
		}
		else {
			selected(board);
			notSelected(user);
			notSelected(chat);
			notSelected(notice);
			we.load("http://localhost:8081/BBS/Board.jsp");
		}
	}
	
	public void selected(ToggleButton btn) {
		btn.setDisable(true);
		btn.setBackground(selectedBack);
	}
	
	public void notSelected(ToggleButton btn) {
		btn.setDisable(false);
		btn.setBackground(notSelectedBack);
	}
	
	public void schedule() {
		
	}
}
