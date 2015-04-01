set SFTP="C:\Program Files (x86)\PuTTY\psftp"

%SFTP% -b deploy.txt -pw radiat0r cmgr@31.222.191.71

del /F *.jar

pause
