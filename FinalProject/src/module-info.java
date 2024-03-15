

module FinalProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.base;
	requires javafx.media;
	requires javafx.swing;
	requires javafx.web;
	
	opens application to javafx.graphics, javafx.fxml, javafx.controls, javafx.desktop, javafx.base, javafx.media, javafx.swing, javafx.web;
}
