#!/bin/ksh -p

java -classpath /vob/emanager/support/tibco71/lib/tibrvj.jar:/vob/emanager/sdk/client/tibco eMgrClientSender cisco.mgmt.emanager.inventory.request avMsgDeleteNode_AustinByFdn.xml
