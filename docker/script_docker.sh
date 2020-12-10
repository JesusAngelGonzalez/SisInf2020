#!/bin/bash
cd postgres && unzip main.zip && cd ..
sudo docker stop sisinf-database
sudo docker rm sisinf-database
sudo docker build -t sisinf/postgres:latest ./postgres

sudo docker stop sisinf-tomcat
sudo docker rm sisinf-tomcat
sudo docker build -t sisinf/tomcat:latest ./tomcat
sudo docker run --name "sisinf-database" -e ALLOW_EMPTY_PASSWORD=yes -p 5432:5432 -d sisinf/postgres:latest
sudo docker run -d --name "sisinf-tomcat" --link "sisinf-database" -p 8080:8080 sisinf/tomcat:latest

echo
echo

sudo docker ps
