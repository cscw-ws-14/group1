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
To run the coffee-intake-APP,

$ cd coffee

$ sudo node coffee_red.js


## demo to do 
pi#cscw-bplus-04 username:pi password -----
IP: 
mosquitto: 1883
device-gateway: 8080

cd ~
mkdir ridho
git clone https://github.com/cscw-ws-14/group1.git
cd patchwork-toolkit-v0.2_linux_arm
mkdir agents
cp ~/ridho/group1/agents/outside_temperature.py agents
cp ~/ridho/group1/conf/devices/outside_temperature.json conf/devices

./device-gateway

pi# number 1 or 2
change mosquitto pub IP and port in 
cd ~/patchwork-toolkit-v0.2_linux_arm
nano conf/device-gateway.json


laptop#shekoufe
air quality flow
change mosquitto subs IP in java fbp src
mosquittosubscribe.java

laptop#dian
node red: sqlite and coffee flow
change CoffeeFlow's mosquitto subs IP in node red

laptop#ashish
movement flow, UI 
change mosquitto subs IP in java fbp
mosquittosubscribe.java

laptop#ridho UI
change ws IP  
http://device-gateway:8080/static/ui/index.html#{"room":"R2","user":2}
