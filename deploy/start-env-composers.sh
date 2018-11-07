#!/bin/bash
DIR=$(pwd)
SERVICES_DIR="services"

SHIB_AUTH_APP_SERVICE_DIR=$SERVICES_DIR/"shibboleth-authentication-application"

SERVICES_LIST="$SHIB_AUTH_APP_SERVICE_DIR"

for service in $SERVICES_LIST; do
	echo ""
	echo "Running $service/env-composer.sh"
	echo ""
	bash $service/"env-composer.sh"
done
