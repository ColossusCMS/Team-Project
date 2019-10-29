package ScheduleModule;

import java.net.URL;
import java.util.ResourceBundle;

import ClassPackage.Reg;
import ClassPackage.Schedule;
import Dao.ScheduleDao;
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

public class CreateScheduleController implements Initializable {
	@FXML TextField txtFieldTitle;
	@FXML TextArea txtAreaContent;
	@FXML Button btnReg, btnCancel,btnDel;
	@FXML Label lblDate;
	@FXML TableView<Reg> tblViewPrivateSch, tblViewGroupSch;

	ObservableList<Reg> privateList = FXCollections.observableArrayList();
	ObservableList<Reg> groupList = FXCollections.observableArrayList();
	ScheduleDao sd = new ScheduleDao();
	
	int year = Schedule.year;
	int month = Schedule.month;
	String date = Schedule.date;
	String userno = ScheduleController.USERNO;
	String today;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblDate.setText(year + "³â " + month + "¿ù " + date + "ÀÏ");
		btnReg.setOnAction(event -> handleBtnRegAction());
		btnCancel.setOnAction(event -> handleBtnCancelAction());
		btnDel.setOnAction(event -> handleBtnDelAction());
		
		today = year + "-" + month + "-" + date;
		sd.loadPrivateSchedule(privateList, userno, today);
		sd.loadGroupSchedule(groupList, userno, today);
		
		createTbl(tblViewPrivateSch, privateList);
		createTbl(tblViewGroupSch, groupList);
		
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
	
	public void createTbl(TableView<Reg> table, ObservableList<Reg> list) {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Reg, String> schTitleCol = new TableColumn<Reg, String>();
		schTitleCol.setCellValueFactory(new PropertyValueFactory<Reg, String>("schTitle"));
		table.getColumns().add(schTitleCol);
		table.setItems(list);
		table.widthProperty().addListener((obs, oldValue, newValue) -> {
			Pane header = (Pane)table.lookup("TableHeaderRow");
			if(header.isVisible()) {
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
	}
	
	public void handleBtnRegAction() {
		if(tblViewPrivateSch.getSelectionModel().isEmpty()) {
			sd.entrySchedule(new Reg(userno, txtFieldTitle.getText(), txtAreaContent.getText(), today, null));
		}
		else {
			sd.updateSchedule(new Reg(userno, txtFieldTitle.getText(), txtAreaContent.getText(), today, null));
		}
		privateList.clear();
		sd.loadPrivateSchedule(privateList, userno, today);
		tblViewPrivateSch.setItems(privateList);
		txtFieldTitle.clear();
		txtAreaContent.clear();
	}
	
	public void handleBtnCancelAction() {
		btnCancel.getScene().getWindow().hide();
	}
	
	public void handleBtnDelAction() {
		sd.deleteSchedule(new Reg(userno, txtFieldTitle.getText(), txtAreaContent.getText(), today, "1"));
		privateList.clear();
		sd.loadPrivateSchedule(privateList, userno, today);
		tblViewPrivateSch.setItems(privateList);
		txtFieldTitle.clear();
		txtAreaContent.clear();
	}
}
