#!/bin/ksh -p

echo "Get Event Details"
./getEventDetails.ksh

sleep 10

echo "Retrieve Events"
./getEvents.ksh

sleep 10

echo "Ack Event"
./ackEvent.ksh

sleep 10

echo "Unack Event"
./unackEvent.ksh

sleep 10

echo "Register SNMP Client"
./registerSNMPClient.ksh

sleep 10

echo "Unregister SNMP Client"
./unregisterSNMPClient.ksh

sleep 10

echo "Delete events"
./deleteEvents.ksh
