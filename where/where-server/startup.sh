#!/bin/sh
### ====================================================================== ###
##                                                                          ##
##  DB PROXY Bootstrap Script                                               ##
##                                                                          ##
### ====================================================================== ###

DIRNAME=`dirname $0`

#Specify JAVA_HOME
JAVA_HOME=/usr/java/jdk1.6.0_18

# Setup SERVER_HOME
if [ "x$SERVER_HOME" = "x" ];
then
    # get the full path (without any relative bits)
    SERVER_HOME=`cd $DIRNAME;cd ..;pwd`
fi

#Specify SERVER_ENV
SERVER_CONF=$SERVER_HOME/conf
SERVER_LIB=$SERVER_HOME/lib
SERVER_BIN=$SERVER_HOME/bin

# Configure JAVA_HOME
PATH=$PATH:$JAVA_HOME/bin

# Configure CLASSPATH
CLASSPATH=$SERVER_BIN/riders-server.jar
cd $SERVER_LIB
for l in `ls`
do
    CLASSPATH=$CLASSPATH:$SERVER_LIB/$l
done
CLASSPATH=$CLASSPATH:$SERVER_CONF


#echo "JAVA_HOME="$JAVA_HOME
#echo ""
#echo "SERVER_HOME="$SERVER_HOME
#echo ""
#echo "SERVER_BIN="$SERVER_BIN
#echo ""
#echo "SERVER_LIB="$SERVER_LIB
#echo ""
#echo "SERVER_CONF="$SERVER_CONF
#echo ""
#echo "CLASSPATH="$CLASSPATH
#echo ""
#echo "PATH="$PATH
#echo ""

export JAVA_HOME CLASSPATH PATH SERVER_HOME

JAVA_OPTS="-Xms1536m -Xmx1536m -Xmn384m -XX:PermSize=512M -XX:MaxPermSize=1024m -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -verbose:gc -Xloggc:../logs/gc.log"

#if [ $SERVER_HOME != "" ]
#then
#    JAVA_OPTS="$JAVA_OPTS -Djava.library.path=$SERVER_HOME/lib"
#fi

#echo $JAVA_OPTS
#export JAVA_OPTS

#echo ================================
#echo =
#echo = SERVER_HOME = $SERVER_HOME
#echo =
#echo ================================

ulimit -n 8192

$JAVA_HOME/bin/java $JAVA_OPTS  com.leadtone.riders.RidersServer $1 1>/dev/null 2>/dev/null

