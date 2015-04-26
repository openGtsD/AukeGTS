# AukeGTS - How to run

1. build and run auke-service.jar by command:  java -jar auke-service.jar server src/main/resources/app-config.yaml
Notes: after services start we can test send Http request by browser e.g: localhost:8888/feed/get-feed

2. Package auke-web to war file and copy into tomcat


3. Make folder ROOT in webapp (e.g: webapp/ROOT) and make symbolic link by command 
  a. cd webapp/ROOT
  b. open cmn by administrator and run command mklink /D auke-js D:\pathtoyourrepo\AukeGTS\auke-js

this command will creates a symbolic link called auke-js, which points to the folder called auke-js
 
Optional for step 3 we can install apache or any http server then host auke-js  

4. Go to browser type >> localhost:8080/drone and see result or goto joomla folder click demo.html (notes: Need corrected domain or ip link to js files)
