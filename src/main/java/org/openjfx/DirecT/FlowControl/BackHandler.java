package org.openjfx.DirecT.FlowControl;

import java.io.IOException;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Controller.WifiDevices;

public class BackHandler {
	public static void UsersSelection() throws IOException {
		App.setRoot("UsersSelection");
	}

	// thread here
	public static void ThreadBreak() throws IOException {

		WifiDevices.toRefresh = false;// don't refresh available network list now
		App.setRoot("DeviceSelection");
	}
	

}
