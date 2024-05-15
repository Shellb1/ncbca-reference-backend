#!/bin/bash

# Assumes existence of secrets.env file with PRIVATE_KEY variable being the filename of private ssh key
source secrets.env

# EC2 Info
EC2_HOST=ec2-18-191-148-121.us-east-2.compute.amazonaws.com

# Run Maven clean install
echo "Running Maven clean install..."
mvn clean install

# Copy JAR file to EC2 instance
echo "Copying JAR file to EC2 instance..."
scp -i ssh-key.pem target/ncbca-reference-backend-1.0.0-SNAPSHOT.jar ubuntu@$EC2_HOST:~/ncbca-reference-backend/

# Commands to run on EC2 server
ssh -i $PRIVATE_KEY ubuntu@$EC2_HOST << 'EOF'
    echo "Running commands on EC2 instance..."
    echo "Stopping systemctl service"
    sudo systemctl stop ncbca-reference-backend.service

    cd ncbca-reference-backend
    rm ncbca-reference.jar
    mv ncbca-reference-backend-1.0.0-SNAPSHOT.jar ncbca-reference.jar

    # starting ncbca-reference-backend.service
    echo "Starting ncbca-reference-backend.service"
    sudo systemctl start ncbca-reference-backend.service

    # sanity check to see status started successfully
    sudo systemctl status ncbca-reference-backend.service
EOF
exit
