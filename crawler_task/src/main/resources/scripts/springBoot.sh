#!/bin/bash

#------  JAVA Env  ------#
#export JAVA_HOME="/opt/jdk1.8.0_20"
#export PATH=$JAVA_HOME/bin:$PATH

APP_NAME=$1
JAVA_OPTS="-Xmx1024m -Xms1024m"
JAVA_JMX="-Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=121.196.209.183 -Dcom.sun.management.jmxremote.port=9909 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
JAVA_DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8001"
#JAVA_OPTS="$JAVA_OPTS $JAVA_DEBUG"
#------  JMX RMI ------#
#JAVA_OPTS="$JAVA_OPTS $JAVA_JMX"
PID=""

function pid(){
    PID=`ps -ef | grep -i $APP_NAME | grep 'java' | grep -v grep | awk '{ print $2 }'`
}

function start {
    pid
    if [ -n "$PID" ]
    then
        echo "[$APP_NAME] is already running. PID $PID"
        exit 1
    fi

    nohup java $JAVA_OPTS -jar $APP_NAME stocksnap > /dev/null 2>&1 &

    sleep 1s
    pid
    if [ -n "$PID" ]
    then
        echo "$APP_NAME Starting . PID $PID"
    else
        echo "$APP_NAME failure start"
    fi
}

function stop {
    pid
    if [ -z "$PID" ]
    then
    	echo "[$APP_NAME] is already stopped"
    else
   	echo kill -9 $PID
    	kill -9 $PID
    fi
    sleep 3s
}

case $2 in
    start)
        start
        exit 0
    ;;
    stop)
        stop
        exit 0
    ;;
    restart)
        stop
        start
    ;;
    *)
    echo "Usage: name-version.jar {start|stop|restart}"
esac
