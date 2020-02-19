#!/bin/sh

# This script checks files in the environment and exists with non-zero status
# if files are not found.

if [ ! -d ./lib ] ; then
 echo "The folder ./lib does not exist. Are you running this script from the right folder?"
 exit 1
fi

if [ ! -f ./scripts/jar.sh ] ; then
 echo "The file ./scripts/jar.sh does not exist. Are you running this script from the right folder?"
 exit 1
fi

JAR=`./scripts/jar.sh`
if [ ! -f $JAR ] ; then
 echo "The file $JAR does not exist, but we did find the lib folder and scripts/jar.sh."
 echo "Make sure that ./scripts/jar.sh echos the proper jar name."
 exit 1
fi

