FROM openjdk:8

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping && \
  rm -rf /var/lib/apt/lists/*

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

# Generates the build number based on the commit checksum
RUN \
    (build_number=$(git rev-parse --short 'HEAD') && echo "build_number=$build_number" > build)

RUN \
  mvn dependency:sources

CMD /bin/bash start-shib-app.sh > log.out && tail -f /dev/null
