$user=$args[0]
$password=$args[1]
$ip=$args[2]
$securePassword = ConvertTo-SecureString -AsPlainText -Force $password
$cred = New-Object System.Management.Automation.PSCredential $user, $securePassword

Enable-PSRemoting -force
set-item WSMan:\localhost\Client\TrustedHosts -Value * -Force

$connected = $FALSE
$connectionAttempts = 0
DO {
	$connectionAttempts++
	$test = Test-NetConnection -ComputerName $ip -CommonTCPPort WINRM
	if ($test.TcpTestSucceeded -eq $TRUE) {
		$connected = $TRUE
	} else {
		Write-Host "Connection to $($ip) failed, retrying in 60s ..."
		Start-Sleep -Seconds 60
	}
} While ($connected -eq $FALSE -and $connectionAttempts -lt 10)

if ($connected -eq $FALSE) {
	Write-Error "PowerSehll connection to $($ip) failed - terminating ..."
	Exit
}

$session = new-pssession $ip -credential $cred

Invoke-Command -Session $session -Scriptblock {
	$user=$args[0]
	$password=$args[1]
	$ip=$args[2]
	& cmd /c "puppet agent -t" 
	Write-Host "Puppet agent has been run sucessfully"
} -ArgumentList $user,$password,$ip

