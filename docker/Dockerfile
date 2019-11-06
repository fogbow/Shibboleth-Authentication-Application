# This Dockerfile generates image fogbow/apache-shibboleth-server
# It follows RNP tutorial for Shibboleth-enabled HTTP server
## https://wiki.rnp.br/pages/viewpage.action?pageId=69969868

# Ubuntu 14.04 is required because da dependency 'libapache2-mod-php5'
FROM ubuntu:14.04

RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y byobu curl git htop man unzip vim nano wget && \
  apt-get install -y net-tools iputils-ping && \
  apt-get install -y apache2 && \ 
  apt-get install -y libapache2-mod-php5 libapache2-mod-shib2 maven

# Avoid Poddle vulnerability
RUN sed -i "s#SSLProtocol all#SSLProtocol All -SSLv2 -SSLv3#g" /etc/apache2/mods-available/ssl.conf

RUN rm -rf /var/www/index.html
RUN rm -rf /etc/shibboleth/IQ-metadata.xml

# Set the correct time 
RUN apt-get install -y ntp ntpdate && \ 
  service ntp restart

# Install Shibboleth Authentication Application
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

RUN apt-get -y install software-properties-common
RUN add-apt-repository ppa:openjdk-r/ppa 
RUN apt-get update 
RUN apt-get install -y --no-install-recommends openjdk-8-jdk 
RUN rm -rf /var/lib/apt/lists/* 
RUN apt-get install -y git

RUN update-java-alternatives --set /usr/lib/jvm/java-1.8.0-openjdk-amd64

WORKDIR /home/ubuntu/

RUN git clone https://github.com/fogbow/common.git

WORKDIR /home/ubuntu/common

RUN mvn install -DskipTests

WORKDIR /home/ubuntu/

RUN git clone https://github.com/fogbow/shibboleth-authentication-application.git

WORKDIR /home/ubuntu/shibboleth-authentication-application

# Generates the build number based on the commit checksum
RUN \
    (build_number=$(git rev-parse --short 'HEAD') && echo "build_number=$build_number" > build)

RUN mvn install -DskipTests

EXPOSE 80 443

WORKDIR /home/ubuntu/

CMD tail -f /dev/null