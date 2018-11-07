#!/bin/bash
DIR_PATH=$(pwd)
CONTAINER_BASE_PATH="/root/shibboleth-authentication-application"
CONTAINER_FILES_DIR_PATH=$CONTAINER_BASE_PATH"/files"

CONF_FOLDER_PATH=$DIR_PATH"/conf-files"
LOG4J_FILE_NAME="log4j.properties"
CONF_FILE_NAME="shibboleth-authentication-application.conf"

IMAGE_NAME="fogbow/shibboleth-authentication-application"
CONTAINER_NAME="shibboleth-authentication-application"

IMAGE_BASE_NAME=$(basename $IMAGE_NAME)
SERVICES_CONF=services.conf
TAG=$(grep $IMAGE_BASE_NAME $SERVICES_CONF | awk -F "=" '{print $2}')

echo "Shibboleth authentication application port: $SHIB_AUTH_APP_HOST_PORT"

sudo docker pull $IMAGE_NAME
sudo docker stop $CONTAINER_NAME
sudo docker rm $CONTAINER_NAME

sudo docker run -idt --name $CONTAINER_NAME \
	-p 80:80 \
	-p 443:443 \
	-v $DIR_PATH/$CONF_FILE_NAME:$CONTAINER_BASE_PATH/$CONF_FILE_NAME \
	-v $DIR_PATH/$LOG4J_FILE_NAME:$CONTAINER_BASE_PATH/$LOG4J_FILE_NAME:ro \
	-v $CONF_FOLDER_PATH:$CONTAINER_FILES_DIR_PATH \
	$IMAGE_NAME:$TAG /bin/bash

# --------------- Service Provider Configuration ---------------#

GENERAL_CONF_FILE_PATH=$CONF_FOLDER_PATH/"general.conf"
GENERAL_SERVICE_PROVIDER_DOMAIN_PATTERN="service_provider_domain"
GENERAL_SERVICE_PROVIDER_DOMAIN_VALUE=$(grep $GENERAL_SERVICE_PROVIDER_DOMAIN_PATTERN $GENERAL_CONF_FILE_PATH | awk -F "=" '{print $2}')

DEFAULT_SP_FILE_CONF_NAME="default.conf"
SHIBBOLETH_SP2_SP_FILE_CONF_NAME="shibboleth-sp2.conf"
SHIBBOLETH_XML_SP_FILE_CONF_NAME="shibboleth2.xml"
ATTRIBUTE_MAP_XML_NAME="attribute-map.xml"
ATTRIBUTE_POLICY_XML_NAME="attribute-policy.xml"
CERTIFICATE_APACHE=$GENERAL_SERVICE_PROVIDER_DOMAIN_VALUE".crt"
KEY_APACHE=$GENERAL_SERVICE_PROVIDER_DOMAIN_VALUE".key"

echo "Moving Server Provider file."

## Moving files
COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$SHIBBOLETH_XML_SP_FILE_CONF_NAME" /etc/shibboleth/"$SHIBBOLETH_XML_SP_FILE_CONF_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$ATTRIBUTE_MAP_XML_NAME" /etc/shibboleth/"$ATTRIBUTE_MAP_XML_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$ATTRIBUTE_POLICY_XML_NAME" /etc/shibboleth/"$ATTRIBUTE_POLICY_XML_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$SHIBBOLETH_SP2_SP_FILE_CONF_NAME" /etc/apache2/sites-available/"$SHIBBOLETH_SP2_SP_FILE_CONF_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$DEFAULT_SP_FILE_CONF_NAME" /etc/apache2/sites-available/"$DEFAULT_SP_FILE_CONF_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"

COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$CERTIFICATE_APACHE" /etc/ssl/certs/"$CERTIFICATE_APACHE
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="chmod 600 /etc/ssl/certs/"$CERTIFICATE_APACHE
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="yes | cp -f "$CONTAINER_FILES_DIR_PATH"/"$KEY_APACHE" /etc/ssl/private/"$KEY_APACHE
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="chmod 600 /etc/ssl/private/"$KEY_APACHE
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"

## Dissite apache
COMMAND="a2dissite 000-default"
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="a2dissite default-ssl"
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"

## Ensite apache
COMMAND='a2ensite '$DEFAULT_SP_FILE_CONF_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND='a2ensite '$SHIBBOLETH_SP2_SP_FILE_CONF_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"

## Restart apache and shib module
COMMAND="service apache2 restart"
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"
COMMAND="service shibd restart"
sudo docker exec $CONTAINER_NAME /bin/bash -c "$COMMAND"

# --------------- Shibboleth-Authenticatio-Application --------------- #

# Add build value into shibboleth-authentication-application.conf
BUILD_FILE_NAME="build"
SHIB_AUTH_APP_CONF_PATH="./"$CONF_FILE_NAME
sudo docker exec $CONTAINER_NAME /bin/bash -c "cat $BUILD_FILE_NAME >> $SHIB_AUTH_APP_CONF_PATH"

# Run Shibboleth Authentication Application
sudo docker exec $CONTAINER_NAME /bin/bash -c "bash bin/start-shib-app.sh > log.out 2> log.err" &
