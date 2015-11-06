$user=$args[0]
$password=$args[1]
$ip=$args[2]
$hostname=$args[3]
$masterIp=$args[4]
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

#enter-pssession $session
Invoke-Command -Session $session -Scriptblock {
	$user=$args[0]
	$password=$args[1]
	$ip=$args[2]
	$hostname=$args[3]
	$masterIp=$args[4]
	$client = new-object System.Net.WebClient
	$client.DownloadFile( "http://downloads.puppetlabs.com/windows/puppet-3.8.1.msi", "C:\Users\$($user)\Downloads\puppet-3.8.1.msi" )
	& cmd /c "msiexec.exe /l*v install.txt /qn /i C:\Users\$($user)\Downloads\puppet-3.8.1.msi PUPPET_MASTER_SERVER=puppet-master-01 PUPPET_AGENT_STARTUP_MODE=Disabled" 
	Write-Host "Puppet has been sucessfully installed"
	Write-Host "Restarting $($ip)"
	$computer = Get-WmiObject Win32_ComputerSystem -computername $env:COMPUTERNAME
    $computer.Rename($hostname)
	ADD-content -path C:\Windows\System32\drivers\etc\hosts -value "$($masterIp)    puppet-master-01"
} -ArgumentList $user,$password,$ip,$hostname,$masterIp

Invoke-Command -Session $session -Scriptblock {
shutdown -r -t 0
}