$user=$args[0]
$password=$args[1]
$ip=$args[2]
$securePassword = ConvertTo-SecureString -AsPlainText -Force $password
$cred = New-Object System.Management.Automation.PSCredential $user, $securePassword

Enable-PSRemoting -force
set-item WSMan:\localhost\Client\TrustedHosts -Value * -Force
$session = new-pssession $ip -credential $cred

Invoke-Command -Session $session -Scriptblock {
	$user=$args[0]
	$password=$args[1]
	$ip=$args[2]
	& cmd /c "puppet agent -t" 
	Write-Host "Puppet agent has been run sucessfully"
} -ArgumentList $user,$passwd,$ip

