# Debian based image that is able to run an akka app
FROM debian:latest
MAINTAINER Manuel Ignacio Franco Galeano <maigfrga@gmail.com>

#install oracle jdk
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true |  /usr/bin/debconf-set-selections 

RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list

RUN echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
RUN   apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN   apt-get update -y
RUN   apt-get install -y oracle-java8-installer 
RUN   apt-get install -y oracle-java8-set-default

USER daemon

# Adding fatjar file
ADD ReactiveTCP-assembly-0.0.1.jar /opt/

# Exppose port where server will be running
EXPOSE 6001

CMD [ "java", "-jar", "/opt/ReactiveTCP-assembly-0.0.1.jar", "server", "0.0.0.0",  "6001" ]
