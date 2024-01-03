/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Bilancio;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	Album a = this.cmbA1.getValue();
    	if (a == null) {
    		this.txtResult.setText("Inserire un elemento dalla combo box! \n");
    		return;
    	}
    	
    	List<Bilancio> bilanciAlbum = model.getAdiacenti(a);
    	this.txtResult.setText("Successori del nodo " + a + "\n");
    	
    	for (Bilancio b : bilanciAlbum) {
    		this.txtResult.appendText(b + "\n");
    	}
    	
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	if (!(model.getNumVertici()>0)) {
    		txtResult.setText("Graph not created.");
    		return;
    	}
    	
    	String input = this.txtX.getText();
    	
    	if (input == "") {
    		txtResult.setText("Inserire un numero! ");
    		return;
    	}
    	try {
    		int inputNum = Integer.parseInt(input);
    		Album source = this.cmbA1.getValue();
    		Album target = this.cmbA2.getValue();
    		
    		if (source == null || target == null) {
    			this.txtResult.setText("Seleziona un album dalla combo box. ");
    			return;
    		}
    		
    		List<Album> path = model.getPath(source, target, inputNum);
    		
    		if (path.isEmpty()) {
    			this.txtResult.setText("Nessun percorso tra " + source + " e " + target +"\n");
    			return;
    		}
    		this.txtResult.setText("Stampo il percorso tra " + source + "e "+ target );
    		
    		
    		for (Album a: path) {
    			this.txtResult.appendText("" + a + "\n");
    		}
    		
    	}catch (NumberFormatException e) {
    		txtResult.setText("Non valido, devi inserire un numero!" );
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String input = txtN.getText();
    	if (input == "") {
    		txtResult.setText("Inserire un numero! ");
    		return;
    	}
    	try {
    		int inputNum = Integer.parseInt(input);
    		model.creaGrafo(inputNum);
    		int numV = model.getNumVertici();
    		int numE = model.getNumArchi();
    		
    		txtResult.setText("Grafo creato correttamente! \n");
    		txtResult.appendText("#vertici " + numV + "\n" + "#archi " + numE + "\n");
    		
    		this.cmbA1.getItems().setAll(model.getVertici());
        	this.cmbA2.getItems().setAll(model.getVertici());
    		
    	}catch (NumberFormatException e) {
    		txtResult.setText("Non valido, devi inserire un numero!" );
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
