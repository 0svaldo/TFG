package TFG.TutorialesInteractivos.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Lenguaje que se está utilizando actualmente
 * 
 * @authors Carlos, Rafa
 *
 */

public class Lenguaje {
	private final SimpleStringProperty language;
	private final SimpleStringProperty path;

	public Lenguaje() {
		this.language = null;
		this.path = null;
	}

	public Lenguaje(String language, String path) {
		this.language = new SimpleStringProperty(language);
		this.path = new SimpleStringProperty(path);
	}

	public String getLanguage() {
		return language.get();
	}

	public String getPath() {
		return path.get();
	}

	public void setLanguage(String lang) {
		this.language.set(lang);
	}

	public void setPath(String path) {
		this.path.set(path);
	}

}