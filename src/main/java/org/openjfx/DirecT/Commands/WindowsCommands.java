package org.openjfx.DirecT.Commands;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;

class Hotspot implements Runnable {

	
	@Override
	public void run() {
		try {
			

			String name = DetailsJsonHandler.getName() + "_DrT";
			String password = generatePassword();
			name += password;

			
			
			
			
			
			
			//space for easy to read
			String c="powershell.exe \"Add-Type -AssemblyName System.Runtime.WindowsRuntime; $asTaskGeneric = ([System.WindowsRuntimeSystemExtensions].GetMethods() | ? { $_.Name -eq 'AsTask' -and $_.GetParameters().Count -eq 1 -and $_.GetParameters()[0].ParameterType.Name -eq 'IAsyncOperation`1' })[0]; Function Await($WinRtTask, $ResultType) {     $asTask = $asTaskGeneric.MakeGenericMethod($ResultType);     $netTask = $asTask.Invoke($null, @($WinRtTask));     $netTask.Wait(-1) | Out-Null;     $netTask.Result; } Function AwaitAction($WinRtAction) {     $asTask = ([System.WindowsRuntimeSystemExtensions].GetMethods() | ? { $_.Name -eq 'AsTask' -and $_.GetParameters().Count -eq 1 -and !$_.IsGenericMethod })[0];     $netTask = $asTask.Invoke($null, @($WinRtAction));     $netTask.Wait(-1) | Out-Null; }   $connectionProfile = [Windows.Networking.Connectivity.NetworkInformation,Windows.Networking.Connectivity,ContentType=WindowsRuntime]::GetInternetConnectionProfile(); $tetheringManager=[Windows.Networking.NetworkOperators.NetworkOperatorTetheringManager,Windows.Networking.NetworkOperators,ContentType=WindowsRuntime]::createFromConnectionProfile($connectionProfile);  $configuration = new-object Windows.Networking.NetworkOperators.NetworkOperatorTetheringAccessPointConfiguration; $configuration.Ssid = 'test'; $configuration.Passphrase = '12345678';  [enum]::GetValues([Windows.Networking.NetworkOperators.TetheringWiFiBand]); $configuration | Get-Member ;   $tetheringManager.TetheringOperationalState;   AwaitAction ($tetheringManager.ConfigureAccessPointAsync($configuration));   Await ($tetheringManager.StartTetheringAsync()) ([Windows.Networking.NetworkOperators.NetworkOperatorTetheringOperationResult]);  \"\n" ;
			
					
					
					
					
					
					
					
					
					
					
					
			c = c.replace("test", name);
			c = c.replace("12345678", password);

			Process child = Runtime.getRuntime().exec(c);
			Thread.sleep(2000);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String generatePassword() {

		// name + _dir + 4digits (digits ranging from 1-9)
		String password = "";
		Random rn = new Random();

		for (int i = 1; i < 9; i++) {
			int j = rn.nextInt(10) + 1;
			password += j;

		}
		return password;
	}

}

public class WindowsCommands {

	public static String name = "";
	public static String password = "";

	public static void deleteProfile() throws Exception {

		String command = "netsh wlan delete profile name=\"" + name + "\"";
		Runtime.getRuntime().exec(command);

	}

	public static void disconnect() throws Exception {
		String command = "netsh wlan disconnect";
		Runtime.getRuntime().exec(command);
	}

	public static void connectProfile() throws Exception {

		// export file to resources/profile

		export();

		// now add profile

		String command = "netsh wlan add profile filename=\"src/main/resources/org/openjfx/Windows/wifi.xml\"";
		Runtime.getRuntime().exec(command);

	}

	public static void connect() throws Exception {

		String command = "netsh wlan connect name=" + name;

		Runtime.getRuntime().exec(command);
	}

	public static void export() {

		String xml = "<?xml version=\"1.0\"?>\r\n"
				+ "<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">\r\n" + "	<name>" + name
				+ "</name>\r\n" + "	<SSIDConfig>\r\n" + "		<SSID>\r\n" + "			\r\n" + "			<name>"
				+ name + "</name>\r\n" + "		</SSID>\r\n" + "	</SSIDConfig>\r\n"
				+ "	<connectionType>ESS</connectionType>\r\n" + "	<connectionMode>auto</connectionMode>\r\n"
				+ "	<MSM>\r\n" + "		<security>\r\n" + "			<authEncryption>\r\n"
				+ "				<authentication>WPA2PSK</authentication>\r\n"
				+ "				<encryption>AES</encryption>\r\n" + "				<useOneX>false</useOneX>\r\n"
				+ "			</authEncryption>\r\n" + "			<sharedKey>\r\n"
				+ "				<keyType>passPhrase</keyType>\r\n" + "				<protected>false</protected>\r\n"
				+ "				<keyMaterial>" + password + "</keyMaterial>\r\n" + "			</sharedKey>\r\n"
				+ "		</security>\r\n" + "	</MSM>\r\n"
				+ "	<MacRandomization xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v3\">\r\n"
				+ "		<enableRandomization>false</enableRandomization>\r\n"
				+ "		<randomizationSeed>2877454535</randomizationSeed>\r\n" + "	</MacRandomization>\r\n"
				+ "</WLANProfile>\r\n";

		try {

			FileWriter f = new FileWriter("src/main/resources/org/openjfx/Windows/wifi.xml");
			f.write(xml);
			f.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String[] availableNetworks() throws Exception {

		String command = "netsh wlan show networks";
		Process child = Runtime.getRuntime().exec(command);

		InputStream in = child.getInputStream();
		String s = "";
		int c;
		while ((c = in.read()) != -1) {

			s += Character.toString((char) c);

		}

		in.close();
		String[] networkList = trimNetworksList(s);

		return networkList;

	}

	private static String[] trimNetworksList(String str) {

		String s[] = str.split("\n");

		ArrayList<String> list = new ArrayList<>();

		for (String a : s) {
			if (a.length() > 5)
				if (a.substring(0, 4).equalsIgnoreCase("SSID")) {

					int l = 0;
					for (int i = 0; i < a.length(); i++) {
						if (a.charAt(i) == ':') {
							l = i;
							break;
						}
					}

					list.add(a.substring(l + 2));

				}
		}

		Object[] l = list.toArray();

		String[] networks = new String[l.length];

		for (int x = 0; x < l.length; x++) {

			String t = "";

			String string = l[x].toString();

			for (int j = 0; j < string.length(); j++) {

				char ch = string.charAt(j);

				boolean check = true;

				if (ch == '\n' || ch == '\r')
					check = false;

				if (check) {
					t += ch;
				} else {

				}

			}

			networks[x] = t;

		}

		return networks;

	}

	public static String hotspotIP() throws Exception {

		String command = "arp -a";
		Process child = Runtime.getRuntime().exec(command);
		InputStream in = child.getInputStream();
		String s = "";
		int c;
		while ((c = in.read()) != -1) {

			s += Character.toString((char) c);

		}

		in.close();
		String[] lines = s.split("[\r\n]+");
		String string = "";

		for (String str : lines) {

			if (str.contains("dynamic")) {
				string = str;
				break;
			}
		}

		String ip = "";
		for (int i = 2; i < string.length(); i++) {

			if (string.charAt(i) == ' ') {

				break;
			}
			ip = ip + string.charAt(i);
		}

		return ip;

	}
	
	public static void deleteWifiProfile() {
		try {
			
			File f=new File("src/main/resources/org/openjfx/Windows/wifi.xml");
			f.delete();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void openHotspot() throws Exception {

		new Thread(new Hotspot()).start();

	}
	
}
