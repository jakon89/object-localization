# Multiple objects detection using Google Vision


Google published [objects detection](https://cloud.google.com/vision/docs/object-localizer) feature in Google Vision (as for now it's BETA)

This is very sample demo that detects objects on uploaded image and then draws rectangles on valid objects.


### How to

Requirements:
* maven
* Google Cloud service account with access to Vision API


#### Local compilation

`git clone https://github.com/jakon89/object-localization.git`

`cd object-localization`

`export $GCP_PROJECT=gcp project you use`

`export $GCP_SERVICE_ACCOUNT_KEY_PATH=path to service account key`

` mvn spring-boot:run -Dspring-boot.run.arguments=--spring.cloud.gcp.project-id=$GCP_PROJECT,--spring.cloud.gcp.credentials.location=file:$GCP_SERVICE_ACCOUNT_KEY_PATH`


Then go to http://localhost:8080/ and upload image. Result should be visible in few seconds.


#### Demo

##### Input
[logo]: demo/input.jpg "input"

##### Result
[logo]: demo/result.jpg "result"



