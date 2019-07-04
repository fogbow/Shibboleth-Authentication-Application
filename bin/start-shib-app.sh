#!/bin/bash
DIRNAME=`dirname $0`
cd $DIRNAME/..
java -Dlog4j.configuration=file:log4j.properties -cp target/shib-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar:. cloud/fogbow/shibapp/Main shibboleth-authentication-application.conf > /dev/null &
