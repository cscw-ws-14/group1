#group 1
==============

Assuming all programs are running on localhost
 
#Patchwork, NodeRed and MQTT
1. install mosquitto, or run Mosquitto server. 
2. run patchwork by executing: bin/device-gateway
(the binary which is in this package is for OSX)
3. install nodejs and node red.
4. for running sqlite REST server flow, please install sqlite3 via npm: 
	npm install sqlite3
5. install node-red sqlite wrapper: 
	npm install node-red-node-sqlite

#UI
1. change the WebSocket URL setting in: /static/js/kinect.js and /static/js/websocket.js
2. UI: open with your websocket enable browser and visit this url (please also include the hashtag part)
http://localhost:8080/static/ui/index.html#{"room":"cscw-bplus-04","user":1}

## Codes Explanation:
###/static/ui/index.html
It is the dashboard html code.
###/static/ui/js/kinect.js 
It is a js file that contains code that reads kinect body node coordinates sent from Physical activity advisor
###/static/ui/js/websocket.js 
It is a js file that contains code that reads websocket feed of Air Quality, Coffee Intake, and Physical Activity Advisor

#Air Quality Advisor
1. open your favorite java IDE
2. create a new project and import the source code inside /IndoorAirQuality
3. add these reference to the project: 
	a. libraries under /IndoorAirQuality/*.jar 

	b. libraries under /websocket_server/websocket_component/lib/*.jar 
	
	c. codes under /websocket_server/websocket_component/src/*  

4. compile and run the IndoorAirQuality.java
 
## Codes Explanation:
###/IndoorAirQuality/ 
contain components that form the flow. each explanations are written on each file.
###/IndoorAirQuality/IndoorAirQuality.java
The network flow of Air Quality Advisor
###/websocket_server/websocket_component/src
component that can instantiate a websocket server, and publish messages to its subscriber

#Coffee Intake Advisor
1. go to /coffee 
$ cd coffee

2. run coffee advisor node flow:
$ sudo node coffee_red.js
 
## Codes Explanation:
###/coffee/coffee_red.js
Contains flow of Coffee Intake Advisor Logic, and SQLite REST interface

#Physical Activity Advisor
1. open your favorite java IDE

2. create a new project and import the source code inside: 
		/Movement/Movement/src/

3. add these reference to the project:
	a. /MessageBoxComponent/MessageBoxComponent.java
	
	b. libraries under /IndoorAirQuality/*.jar
	
	c. libraries under /websocket_server/websocket_component/lib/*.jar
	
	d. codes under /websocket_server/websocket_component/src/*
	  
4. compile and run the MovementControl.java
 
## Codes Explanation:
###/Movement/Movement/ 
contain components that form the flow. each explanations are written on each file 
###/MessageBoxComponent/src
component that can pop up a AWT message box 