FROM ubuntu:16.04

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping openjdk-8-jdk tzdata && \
  rm -rf /var/lib/apt/lists/*

# Install apache/shibboleth dependencies
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get -y install apache2 libapache2-mod-php libapache2-mod-shib2 ntp dbus

# Apacha modules
RUN a2enmod shib2
RUN a2enmod ssl
RUN a2enmod rewrite
RUN a2enmod proxy
RUN a2enmod proxy_http
RUN a2enmod headers

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Installing Manager
RUN \
  git clone https://github.com/fogbow/shibboleth-authentication-application.git && \
  (cd shibboleth-authentication-application && git checkout master)

# Define working directory.
WORKDIR /root/shibboleth-authentication-application

RUN mkdir files

# Generates the build number based on the commit checksum
RUN \
    (build_number=$(git rev-parse --short 'HEAD') && echo "build_number=$build_number" > build)

RUN mvn dependency:sources
RUN mvn install -Dmaven.test.skip=true

RUN rm -rf /var/www/index.html
RUN rm -rf /etc/shibboleth/IQ-metadata.xml

RUN ln -sf /usr/share/zoneinfo/America/Recife /etc/localtime