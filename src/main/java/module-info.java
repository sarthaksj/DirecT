module org.openjfx.DirecT {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires java.sql;
	requires com.jfoenix;
	requires json.simple;
	requires com.google.zxing;
	requires AnimateFX;
	requires commons.net;
	requires javafx.graphics;

	opens org.openjfx.DirecT to javafx.fxml;

	exports org.openjfx.DirecT;

	opens org.openjfx.DirecT.Controller to javafx.fxml;

	exports org.openjfx.DirecT.Controller;

}