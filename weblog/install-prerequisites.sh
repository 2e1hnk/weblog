#!/bin/bash

sudo apt-get install mysql-server openjdk-8-jre openjdk-8-jdk-headless screen gradle nginx
mysqladmin --user=root password "password"
mysql -u root -p -e "CREATE DATABASE weblog;"
mysql -u root -p -e "CREATE USER 'weblog'@'localhost' IDENTIFIED BY 'password';"
mysql -u root -p -e "GRANT ALL PRIVILEGES ON weblog.* TO 'weblog'@'localhost';"

echo "Remember to set the following environment variables:"
echo "  DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME"
