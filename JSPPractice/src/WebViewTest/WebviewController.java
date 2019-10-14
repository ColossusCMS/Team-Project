package WebViewTest;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebviewController implements Initializable {
	@FXML private WebView view;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		WebEngine we = view.getEngine();
		we.load("File://D:/Java ÀÛ¾÷/Private_Project/Private_Testing/src/WebViewTest/test.html");
	}
}
