package org.openjfx.DirecT.SenderReceivingPackage;

import org.pdfsam.ui.FillProgressIndicator;
import javafx.application.Platform;

public class ProgressIndicatorHandler {

	public static void setPorgress(FillProgressIndicator progressIndicator, double value) {

		// we get a scale of 100
		int i = (int) value;
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				progressIndicator.setProgress(i);

			}
		});


	}

}
