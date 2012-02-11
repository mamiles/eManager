#!/bin/ksh -p

. ../../../../bin/emenv.ksh

CLASSPATH=$CLASSPATH:$EM_HOME/sdk/client/tibco
export CLASSPATH

ans=`ckyorn -d y \
	-p "Have you updated the TestAppRegistration.xml with the localhost? Default=y "` || exit $?
if [ "$ans" = "n" -o "$ans" = "no" -o "$ans" = "N" -o "$ans" = "NO" ]; then
	exit
fi

$JRE_ROOT/bin/java -cp $CLASSPATH eMgrClientReceiver >eMgrClientReceiver.out 2>&1 &
receiverpid=$!

/usr/bin/sleep 3

pmsender pmStatus.xml
pmsender pmGetGroups.xml
pmsender pmGetProcessesForGroup.xml
pmsender pmGetGroupState.xml
pmsender pmGetHealth.xml

registrationsender TestAppRegistration.xml
/usr/bin/sleep 4
solutionregistrationsender SASolutionRegistration.xml

$EM_HOME/bin/cmd -u admin -p cisco123 -a TestApp -i TestAppInst start

/usr/bin/sleep 5

echo "status"
$EM_HOME/bin/cmd -a TestApp -i TestAppInst status
echo "\nhealth"
$EM_HOME/bin/cmd -a TestApp -i TestAppInst health
echo "\ngroups"
$EM_HOME/bin/cmd -a TestApp -i TestAppInst groups
echo "\ngroup fullApplication"
$EM_HOME/bin/cmd -a TestApp -i TestAppInst group fullApplication
echo "\ngrpstatus fullApplication"
$EM_HOME/bin/cmd -a TestApp -i TestAppInst grpstatus fullApplication

echo "\nsolstatus SA"
$EM_HOME/bin/cmd -a TestApp -i TestAppInst solstatus SA
echo "\nsolstop SA"
$EM_HOME/bin/cmd -u admin -p cisco123 -a TestApp -i TestAppInst solstop SA
echo "\nsolstart SA"
$EM_HOME/bin/cmd -u admin -p cisco123 -a TestApp -i TestAppInst solstart SA

#$EM_HOME/bin/cmd -u admin -p cisco123 -a TestApp -i TestAppInst stop

logsender logGetLogFiles.xml
logsender logGetLogURL.xml

/usr/bin/sleep 5

kill $receiverpid
