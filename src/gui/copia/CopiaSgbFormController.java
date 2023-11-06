package gui.copia;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CopiaSgbFormController implements Initializable {
	
	@FXML
	private Button btOK;

	@FXML
	private ComboBox<Unidade> comboUnid;

	private ObservableList<Unidade> obsListUnid;

	String file = "";
	String unid = null;
	String path = unid +"\\ARQS\\SGB\\" + file;

 	public void recebeUnidAction(ActionEvent event) {
 		try {
			unid = comboUnid.getValue().getLetraUnid();
			CopiaSgbController.unid = unid;
			String pathA = unid + ":\\ARQS";
			@SuppressWarnings("unused")
			String pathB = unid + ":\\ARQS\\Backup";
			@SuppressWarnings("unused")
			String pathC = unid + ":\\ARQS\\Backup\\SGB";
			@SuppressWarnings("unused")
			String pathD = unid + ":\\ARQS\\Backup\\SGBCP";
//			File pathf = new File(pathA);
	 		@SuppressWarnings("unused")
			boolean exist = false;
	 		
	 		switch (unid) {
	 			case "A": new File (pathA).mkdir();
	 			break;
	 			case "B": new File (pathA).mkdir();
	 			break;
	 			case "C": new File (pathA).mkdir();
	 			break;
	 			case "D": new File (pathA).mkdir();
	 			break;
	 		}
	 		
/////			exist = new File (pathA).mkdir();
////			exist = new File (pathB).mkdir();
////			exist = new File (pathC).mkdir();
////			exist = new File (pathD).mkdir();
//			File[] folders = pathf.listFiles(File::isDirectory);
//			for(File f : folders) {
//				System.out.println(f);
//			}	
			Utils.currentStage(event).close();
			CopiaSgbController.unid = unid;
		} catch (NullPointerException e) {
			Alerts.showAlert("IO Exception", "unidade inválida - sem backUp " , "BackForm ", AlertType.ERROR);
		}
 	}	
 	
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

	public void loadAssociatedObjects() {
		UnidadeService unidService = new UnidadeService();
//		List<String> list = Arrays.asList("C", "E", "F", "G", "H", "I");
		List<Unidade> listUnid = unidService.findAll(); 
		obsListUnid = FXCollections.observableArrayList(listUnid);
		comboUnid.setItems(obsListUnid);
	}
	
 	private void initializeNodes() {
		initializeComboBoxFuncionario();
 	}
 	
	private void initializeComboBoxFuncionario() {
		Callback<ListView<Unidade>, ListCell<Unidade>> factory = lv -> new ListCell<Unidade>() {
			@Override
			protected void updateItem(Unidade item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getLetraUnid());
			}
		};

		comboUnid.setCellFactory(factory);
		comboUnid.setButtonCell(factory.call(null));
	}
}