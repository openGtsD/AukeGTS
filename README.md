# AukeGTS - How to run

1. build and run auke-service.jar by command:  java -jar auke-service.jar server src/main/resources/app-config.yaml
Notes: after services start we can test send Http request by browser e.g: localhost:8888/feed/get-feed

2. build and deploy auke-web for send request into web services.
 run for test by command: mvn jetty:run -Djetty.port=9999
