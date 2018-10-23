#!/bin/bash
DIRNAME=`dirname $0`
cd $DIRNAME/..
java -Dlog4j.configuration=file:log4j.properties -cp target/shib-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar:. org/fogbowcloud/shipapp/Main shib-app.conf > /dev/null &
