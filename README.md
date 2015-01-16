group 1
======
0. clone this repo

1. install mosquitto, or run it.

2. download the dashboard: 

a. cd to your patchwork directory, 
   mkdir static
   cd static
   
b. git clone https://github.com/patchwork-toolkit/dashboard.git

3. run bin/win/device-gateway or bin/linux/device-gateway or bin/osx/device-gateway
use browser to http://localhost:8080/static/dashboard

------
To run the coffee DB server,

$ cd coffee

$ sudo node cscw_final_server_red.js
