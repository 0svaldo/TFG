package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.controller.Controller;
import application.model.Opciones;
import application.model.Pregunta;
import application.model.Tema;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;

import com.github.rjeschke.txtmark.*;
import javafx.scene.web.*;

public class Main extends Application {
	
	private Stage primaryStage;
	private VBox root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Controller c= new Controller();
			c.cargaModelo("paGuarrearPreguntas");
			Tema t= c.getTema();
			PegDownProcessor processor= new PegDownProcessor();
			// Este objeto se deberia pasar a la vista para que lo muestre
			//temporal para pruebas
			
			//obtenemos los atributos
			String tituloPreProcesado= t.getTitulo();
			String introPreProcesado= t.getIntroduccion();
			//pasamos txtmark
			String tituloProcesado= Processor.process(tituloPreProcesado);
			String introProcesado= Processor.process(introPreProcesado);
			
			//otro
			String tituloLeccionPreProc = t.getLecciones().get(0).getTitulo();
			String explicacionLeccionPreProc= t.getLecciones().get(0).getExplicacion();
			
			String tituloLeccionProc= Processor.process(tituloLeccionPreProc);
			String explicacionLeccionProc= Processor.process(explicacionLeccionPreProc);
			//html ejemplos varios
			String htmlProcesado= tituloProcesado+introProcesado;
			String htmlProcesado2= tituloLeccionProc+explicacionLeccionProc;
	
			//pregunta con imagen
			String preguntaPreProc = t.getLecciones().get(0).getPreguntas().get(2).getEnunciado();
			
			// String current = new java.io.File( "." ).getCanonicalPath();
		    //    System.out.println("Current dir:"+current);
			
		/*	BufferedReader reader = new BufferedReader( new FileReader ("test.txt"));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }

		    String moretest= stringBuilder.toString();
			
			String preguntaProc = processor.markdownToHtml(moretest);*/
			
			//fin temporal para pruebas
			this.primaryStage=primaryStage;
			this.primaryStage.setTitle("Prueba");
			
			//initLayout();
			//showTemas();
			
			Pregunta p= t.getLecciones().get(0).getPreguntas().get(1)  ;
			showOptions(p);
			
			
			int num= new File("resources").list().length;
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void showOptions(Pregunta p){
		Opciones o= (Opciones) p;
		Scene scene= new Scene(new Group());
		root= new VBox();
		WebView browser= new WebView();
		WebEngine engine= browser.getEngine();
		
		ScrollPane panelTexto = new ScrollPane();
		panelTexto.setContent(browser);
		panelTexto.setMaxHeight(50);
		int i=0;
		
		List<RadioButton> l= new ArrayList <RadioButton>();
		List<String> opciones= o.getOpciones();
		for(Object op: opciones){
			RadioButton rb= new RadioButton();
			
			rb.setText(op.toString());
			l.add(rb);
			
			
		}
		
		Button envio = new Button("ENVIAR");
		envio.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
                
            }
        });
		
		engine.loadContent(p.getEnunciado());
		root.getChildren().addAll(panelTexto);
		root.getChildren().addAll(l);
		root.getChildren().addAll(envio);
		scene.setRoot(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	private void showIntroTema(String html) {
		System.out.println(html);
		Scene scene= new Scene(new Group());
		root= new VBox();
		WebView browser= new WebView();
		WebEngine engine= browser.getEngine();
		
		ScrollPane panelTexto = new ScrollPane();
		panelTexto.setContent(browser);
		engine.loadContent(html);
		root.getChildren().addAll(panelTexto);
		scene.setRoot(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	private void showTemas() {
		try{
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Initial.fxml"));
			AnchorPane initial = loader.load();
			
			
			
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}

	private void initLayout() {
		try{
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Raiz.fxml"));
		root = (VBox) loader.load();
		
		Scene scene= new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}