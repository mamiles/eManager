#!/usr/bin/ksh

echo $1
FILELST=$1
shift
SEARCHVAL=$*
echo ${SEARCHVAL}

/usr/bin/ls -l `/usr/bin/egrep -i -l "${SEARCHVAL}" $FILELST || echo null`