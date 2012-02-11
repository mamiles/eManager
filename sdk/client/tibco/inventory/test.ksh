#!/bin/ksh -p

. ../../../../bin/emenv.ksh

CLASSPATH=$CLASSPATH:$EM_HOME/sdk/client/tibco
export CLASSPATH

$JRE_ROOT/bin/java -cp $CLASSPATH eMgrClientReceiver >eMgrClientReceiver.out 2>&1 &
receiverpid=$!

/usr/bin/sleep 3

# apps view messages
sender avMsgGetRoot.xml
sender avMsgCreateNode_Texas.xml
sender avMsgCreateNode_TexasAustin.xml
sender avMsgCreateNode_California.xml
sender avMsgGetChildren_RootByFdn.xml
sender avMsgFindNodes_AustinByName.xml
sender avMsgFindNodes_CaliforniaByFdn.xml
sender avMsgMoveNode_AustinToCalifornia.xml
sender avMsgFindNodes_AustinByName.xml
sender avMsgDeleteNode_AustinByFdn.xml

# physical view messages
sender pvMsgGetRoot.xml
sender pvMsgCreateNode_Usa.xml
sender pvMsgCreateNode_UsaTexas.xml
sender pvMsgCreateNode_Canada.xml
sender pvMsgGetChildren_RootByFdn.xml
sender pvMsgFindNodes_TexasByName.xml
sender pvMsgFindNodes_CanadaByFdn.xml
sender pvMsgMoveNode_TexasToCanada.xml
sender pvMsgFindNodes_TexasByName.xml
sender pvMsgDeleteNode_TexasByFdn.xml

registrationsender TestAppRegistration.xml
solutionregistrationsender SASolutionRegistration.xml

/usr/bin/sleep 3

kill $receiverpid
