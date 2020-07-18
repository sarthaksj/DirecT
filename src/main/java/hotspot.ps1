Add-Type -AssemblyName System.Runtime.WindowsRuntime;
$asTaskGeneric = ([System.WindowsRuntimeSystemExtensions].GetMethods() | ? { $_.Name -eq 'AsTask' -and $_.GetParameters().Count -eq 1 -and $_.GetParameters()[0].ParameterType.Name -eq 'IAsyncOperation`1' })[0];
Function Await($WinRtTask, $ResultType) {
    $asTask = $asTaskGeneric.MakeGenericMethod($ResultType);
    $netTask = $asTask.Invoke($null, @($WinRtTask));
    $netTask.Wait(-1) | Out-Null;
    $netTask.Result;
}
Function AwaitAction($WinRtAction) {
    $asTask = ([System.WindowsRuntimeSystemExtensions].GetMethods() | ? { $_.Name -eq 'AsTask' -and $_.GetParameters().Count -eq 1 -and !$_.IsGenericMethod })[0];
    $netTask = $asTask.Invoke($null, @($WinRtAction));
    $netTask.Wait(-1) | Out-Null;
}
 
$connectionProfile = [Windows.Networking.Connectivity.NetworkInformation,Windows.Networking.Connectivity,ContentType=WindowsRuntime]::GetInternetConnectionProfile();
$tetheringManager=[Windows.Networking.NetworkOperators.NetworkOperatorTetheringManager,Windows.Networking.NetworkOperators,ContentType=WindowsRuntime]::createFromConnectionProfile($connectionProfile);

$configuration = new-object Windows.Networking.NetworkOperators.NetworkOperatorTetheringAccessPointConfiguration;
$configuration.Ssid = 'test';
$configuration.Passphrase = '12345678';

[enum]::GetValues([Windows.Networking.NetworkOperators.TetheringWiFiBand]);
$configuration | Get-Member ;


$tetheringManager.TetheringOperationalState;


AwaitAction ($tetheringManager.ConfigureAccessPointAsync($configuration));


Await ($tetheringManager.StartTetheringAsync()) ([Windows.Networking.NetworkOperators.NetworkOperatorTetheringOperationResult]);

