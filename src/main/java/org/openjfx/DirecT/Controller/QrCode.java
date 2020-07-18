package org.openjfx.DirecT.Controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import org.openjfx.DirecT.FlowControl.BackHandler;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class QrCode implements Initializable {
	@FXML
	private ImageView qrcodeImg;
	@FXML
	private StackPane mainPane;
	@FXML
	private Label username;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		username.setText(DetailsJsonHandler.getName());
		new FadeIn(mainPane).play();

		String qrCodeText = "//Enter Text Here 7060621259 7906831276";
		String filePath = "src/main/resources/org/openjfx/Code.png";
		int size = 150;
		String fileType = "png";
		File qrFile = new File(filePath);
		try {
			createQRImage(qrFile, qrCodeText, size, fileType);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
		try {
			FileInputStream input = new FileInputStream(filePath);
			Image image = new Image(input);
			qrcodeImg.setImage(image);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
			throws WriterException, IOException {
		// Create the ByteMatrix for the QR-Code that encodes the given String
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		ImageIO.write(image, fileType, qrFile);
	}

	public static void deleteQr() {
		//Platform.exit();
		try {
			File f;
			f = new File("src/main/resources/org/openjfx/Code.png");
			f.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void back() throws IOException {

		BackHandler.UsersSelection();
	}

}
