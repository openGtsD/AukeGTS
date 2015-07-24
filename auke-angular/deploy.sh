#!/bin/bash

# Assume at here /opt/demo
echo "Starting install and restart server"
before="$(date +%s)"

# Stop all services
sudo pkill java
sleep 10s

rm -fr ./opt/demo/*.jar ./opt/demo/nohup.out

cd AukeGTS
git pull

# Build jar file
cd ./auke-service
mvn clean install -DskipTests
sleep 10s
cp -f ./target/auke-service.jar /opt/demo/

# Build auke Component
cd ../auke-angular
npm install

# Issues: got answer when install. Need check the first. Fixed
bower install

# Remove old minified and build again
rm -f ./app/*.min.js

# Issues: Need install and check uglifyjs. Install by  sudo npm install -g uglify-js
chmod a+x ./*.sh
dos2unix ./*.sh

./combile.sh f
./combile.sh m
./combile.sh b

./combile-third-party.sh

# Start service
cd /opt/demo

# Start tomcat
./tomcat7/bin/startup.sh

nohup ./start.sh &



after="$(date +%s)"
elapsed_seconds="$(expr $after - $before)"

echo "End install and restart server in: $elapsed_seconds"
