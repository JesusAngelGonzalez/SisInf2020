#!/bin/bash
sudo docker build -t sisinf/tomcat:latest ./tomcat
sudo docker build -t sisinf/postgres:latest ./postgres
sudo docker run --name "sisinf-database" -e ALLOW_EMPTY_PASSWORD=yes -p 5432:5432 -d sisinf/postgres:latest
sudo docker run -d --name "sisinf-tomcat" --link "sisinf-database" -p 8080:8080 sisinf/tomcat:latest
