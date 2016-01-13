package application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.yaml.snakeyaml.Yaml;

import application.Main;
import application.model.Codigo;
import application.model.Elemento;
import application.model.Explicacion;
import application.model.Leccion;
import application.model.Opciones;
import application.model.Pregunta;
import application.model.Sintaxis;
import application.model.Tema;
import application.model.YamlReaderClass;
import application.view.Contenido;
import application.view.MenuLeccion;
import application.view.MenuTema;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller {

	private Tema tema;
	private Stage primaryStage;
	private Pane root;
	private VBox buttons;
	private Scene scene;
	private ArrayList<Elemento> elems;
	private int actual; //contador de el elemento del contenido en el que estamos
	
	public Controller(Stage primaryStage) {
		this.tema = null;
		this.primaryStage=primaryStage;
	}

	public void cargaModelo(String cargaTema) {
		tema=YamlReaderClass.cargaTema(cargaTema);
	
			
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	
	public boolean corrige(ArrayList<Integer> resp, Pregunta p ) {
		return p.corrige(resp);
		
	}

	public void launch() {

		
		ArrayList<String> files = new ArrayList<String>();
		
		File folder = new File("resources/yaml");
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        files.add(listOfFiles[i].getName());
		      }
		    }
		showStart(files);
		
	}
	
	private void showStart(ArrayList<String> files) {
		
		
		primaryStage.setTitle("Python"); //el titulo se podria poner de la app, o del lenguaje, pero obteniendo en la primera lectura de ficheros...
		//este es el encargado de hacer el setroot que tiene los contenidos necesarios
		Pane p = new MenuTema();
		
		changeView(p, files, 0);
		
	}

	private void changeView(Pane p, ArrayList<String> files, int selected){
		scene = new Scene(new Group());
		root= new Pane();
		if (p instanceof MenuTema){
			root.getChildren().addAll(((MenuTema) p).menuTema(files, this));
		}else if (p instanceof MenuLeccion){
			root.getChildren().addAll(((MenuLeccion) p).menuLeccion(tema, this));
		}else if (p instanceof Contenido){
			Elemento e;
			//TODO añadir preguntas de ambos tipos aquí
			if (actual==-1){
				e= new Explicacion(tema.getLecciones().get(selected).getExplicacion());
				
			}else e= elems.get(actual);
			
			root.getChildren().addAll(((Contenido) p).contenido( e, this,selected));
		}
		scene.setRoot(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//TODO borrar
	private void showOptions(final Pregunta p, PegDownProcessor proc) {
		final Opciones o = (Opciones) p;
		Scene scene = new Scene(new Group());
		//scene.getStylesheets().add("resources/css/prueba");
		
		//root();
		buttons = new VBox();
		
		WebView browser = new WebView();
		WebEngine engine = browser.getEngine();
		if (!o.getMulti()) {
			List<RadioButton> l = new ArrayList<RadioButton>();
			List<String> opciones = o.getOpciones();
			final ToggleGroup group = new ToggleGroup();
			for (Object op : opciones) {
				RadioButton cb = new RadioButton();
				cb.setText(op.toString());
				cb.setToggleGroup(group);
				l.add(cb);
				
			}
			buttons.getChildren().addAll(l);
			
		} else {
			List<CheckBox> l = new ArrayList<CheckBox>();
			List<String> opciones = o.getOpciones();
			for (Object op : opciones) {
				CheckBox cb = new CheckBox();
				cb.setText(op.toString());
				l.add(cb);
			}
			buttons.getChildren().addAll(l);
		}
		Button envio = new Button("Resolver");
		envio.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				buttons.getChildren();
				ArrayList<Integer> resp = new ArrayList<Integer>();

				int i = 0;
				for (Node n : buttons.getChildren()) {
					i++;
					if (o.getSolucion().size() == 1) {
						if (((RadioButton) n).isSelected()) {

							resp.add(i);
							// meter respuestas elegidas en array
						}
					}else if (((CheckBox) n).isSelected()) {

						resp.add(i);
						// meter respuestas elegidas en array
					}
				}
				System.out.println(corrige(resp, p));
				// comprobar respuestas correctas y escribir en ventana
			}
		});

		engine.loadContent(proc.markdownToHtml(p.getEnunciado()));
		root.setPrefSize(200, 200);
		root.setMaxWidth(200);
		browser.setMaxHeight(100);
		root.getChildren().addAll(browser);
		root.getChildren().addAll(buttons);
		root.getChildren().addAll(envio);
		scene.setRoot(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public void selectedTema(String selectedItem) {
		
		this.tema= YamlReaderClass.cargaTema(selectedItem);
		changeView(new MenuLeccion(), null, 0);
	}

	public void selectedLeccion(int selectedItem) {
		this.elems=(ArrayList<Elemento>) tema.getLecciones().get(selectedItem).getElementos();
		actual=-1;
		changeView(new Contenido(), null, selectedItem);
		
	}
	public void nextElem(int l){
		//TODO ojo, hay que desactivar y activar botones para que esto no pete
		actual++;
		changeView(new Contenido(), null, l);
	}
	public void prevElem(int l){
		actual--;
		changeView(new Contenido(), null, l);
	}
}
