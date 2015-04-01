set JAR="C:\Program Files\Java\jdk1.7.0_25\bin\jar"

del *jar

%JAR% -cf cmcommon.jar -C CmCommon\Bin .
%JAR% -cf cmserver.jar -C CmServer\Bin .
%JAR% -cf cmclient.jar -C CmClient\Bin .

pause
