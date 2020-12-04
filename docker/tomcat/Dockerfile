FROM bitnami/tomcat
MAINTAINER covid_free

ADD *.jar /opt/bitnami/tomcat/lib/
ADD context.xml /opt/bitnami/tomcat/conf/
ADD server.xml /opt/bitnami/tomcat/conf/

ADD CovidFree.war /opt/bitnami/tomcat/webapps/
