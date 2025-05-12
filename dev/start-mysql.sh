#!/bin/bash

CONTAINER_NAME="mysql-dev"
MYSQL_ROOT_PASSWORD="root"
MYSQL_DATABASE="spring-demo"
MYSQL_USER="spring-demo"
MYSQL_PASSWORD="spring-demo"
MYSQL_PORT="3306"

# Check if container already exists
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  echo "Container $CONTAINER_NAME already exists. Starting it..."
  docker start $CONTAINER_NAME
else
  echo "Creating and starting new MySQL container..."
  docker run -d \
    --name $CONTAINER_NAME \
    -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
    -e MYSQL_DATABASE=$MYSQL_DATABASE \
    -e MYSQL_USER=$MYSQL_USER \
    -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
    -p $MYSQL_PORT:3306 \
    -v mysql_dev_data:/var/lib/mysql \
    mysql:8.0
fi