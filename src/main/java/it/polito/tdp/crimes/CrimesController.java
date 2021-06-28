/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String categoria= boxCategoria.getValue();
    	Integer giorno= boxAnno.getValue();
    	if(categoria==null || giorno==null) {
    		txtResult.appendText("selezionare i parametri");
    		return;
    	}
    	
    	
    	txtResult.appendText("Crea grafo...\n");
    	model.creaGrafo(categoria, giorno);
    	txtResult.appendText("#vertici: "+model.numVertici()+"\n");
    	txtResult.appendText("#archi: "+model.numArchi()+"\n");
    	
    	txtResult.appendText("\n\n Elenco archi di peso massimo pari a "+model.pesoMassimo()+"\n");
    	for(Adiacenza a: model.getArchiPesoMassimo() ) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	boxArco.getItems().clear();
    	boxArco.getItems().addAll(model.getArchiPesoMassimo());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	if(!model.grafoCreato()) {
    		txtResult.appendText("craere prima il grafo");
    		return;
    	}
    	Adiacenza a= boxArco.getValue();
    	if(a==null) {
    		txtResult.appendText("selesionare arco");
    		return;
    	}
    	String partenza= a.getTipo1();
    	String arrivo= a.getTipo2();
    	List<String> soluzione =model.doRicorsione2(partenza, arrivo);
    	txtResult.appendText("Percorso trovato che tocca "+soluzione.size()+"vertici \n");
    	for(String s: soluzione) {
    		txtResult.appendText(s+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.getCategorie());
    	this.boxAnno.getItems().addAll(model.getGiorni());
    }
}
