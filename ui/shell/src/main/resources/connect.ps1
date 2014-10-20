$passwd=$args[0]
$ip=$args[1]
$hostname=$args[2]
$master=$args[3]
$securePassword = ConvertTo-SecureString -AsPlainText -Force $passwd
$cred = New-Object System.Management.Automation.PSCredential administrator, $securePassword

Enable-PSRemoting -force
set-item WSMan:\localhost\Client\TrustedHosts -Value * -Force
$session = new-pssession $ip -credential $cred


#enter-pssession $session
Invoke-Command -Session $session -Scriptblock {Write-Host "Hello, World (from $env:COMPUTERNAME)"}
Invoke-Command -Session $session -Scriptblock {
	$client = new-object System.Net.WebClient
	$client.DownloadFile( "http://downloads.puppetlabs.com/windows/puppet-3.6.2.msi", "C:\Users\administrator\Desktop\puppet-3.6.2.msi" )
	& cmd /c "msiexec.exe /l*v install.txt /qn /i C:\Users\administrator\Desktop\puppet-3.6.2.msi PUPPET_MASTER_SERVER=puppet-master-01" 
	Write-Host "Puppet has been sucessfully installed"
	Write-Host "Restarting $($args[1])"
	$computer = Get-WmiObject Win32_ComputerSystem -computername $env:COMPUTERNAME
    $computer.Rename($($args[2]))
	ADD-content -path C:\Windows\System32\drivers\etc\hosts -value "$($args[3])    puppet-master-01"
} -ArgumentList $passwd,$ip,$hostname,$master

#Remove-PSSession $session
#Rename-Computer -ComputerName $ip -NewName $hostname -DomainCredential $cred -Force
Invoke-Command -Session $session -Scriptblock {
shutdown -r -t 0
}