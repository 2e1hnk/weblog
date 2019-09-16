#!/bin/bash

sudo apt-get install mysql-server openjdk-8-jre openjdk-8-jdk-headless screen gradle nginx
sudo mysqladmin --user=root password "password"
sudo mysql -u root -p -e "CREATE DATABASE weblog;"
sudo mysql -u root -p -e "CREATE USER 'weblog'@'localhost' IDENTIFIED BY 'password';"
sudo mysql -u root -p -e "GRANT ALL PRIVILEGES ON weblog.* TO 'weblog'@'localhost';"

echo "Remember to set the following environment variables:"
echo "  DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME"
