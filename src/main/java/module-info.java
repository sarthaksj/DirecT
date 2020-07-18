module org.openjfx.DirecT {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.media;
	requires javafx.graphics;
	requires com.jfoenix;
	requires json.simple;
	
	requires java.desktop;
	requires AnimateFX;
	requires com.google.zxing;
	requires java.base;

 
	opens org.openjfx.DirecT to javafx.fxml;
	exports org.openjfx.DirecT;

	opens org.openjfx.DirecT.Controller to javafx.fxml;
	exports org.openjfx.DirecT.Controller;

}