package application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

public abstract class Elemento {
	
	protected String texto; //Enunciado en preguntas o explicacion en explicacion
	
	public Elemento(String texto)
	{
		this.texto = texto;
	}
	
	public String getTexto(){
		return this.texto;
	}

	public abstract void setOpciones(ArrayList<String> opc);

	public abstract void setMulti(Boolean is);

	public abstract void setSolucion(ArrayList<Integer> correctasAux);

	public abstract void setTexto(String explicacion);
	
	
		

}
