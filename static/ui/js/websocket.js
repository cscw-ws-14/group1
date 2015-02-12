
var App = {
	url: [
		"ws://localhost:8887/", // air quality
		"ws://localhost:1881/", // coffee
		"ws://localhost:7220/", // idle
		"ws://localhost:8118/", // kinect
	], 
	ws: [],
	user: 1,
	room: "R1",
  	channel: "",
	    
    Construct: function(){
        var hashtag = window.location.hash.replace('#','');
		try{
			var objHash = JSON.parse(hashtag);
			App.room = objHash.room;
			App.user = objHash.user; 
		} catch(ex){
			console.log('hash param error. correct:{"room":"cscw-bplus-04","user":1}')
		}
    }, 
	
	WebSocketBrowserCheck: function(){
		if (("WebSocket" in window) == false){
			alert("WebSocket is not supported by your Browser!");
			return false;	
		}			
	},
	
	OpenWebSocket: function(){
		for(var i in this.url){
			var wso = new WebSocket( this.url[i] + this.channel );
			wso.onopen  = this.OnOpen;
			wso.onclose = this.OnClose;
			wso.onmessage = this.OnMessage;	
			
			this.ws.push(wso);
		}
	},
	
	OnOpen: function(){
		console.log("connected to server");
	},
	
	OnMessage: function(event){
        if(event.data == "" || typeof(event.data) == "undefined")
            return;
		
		console.log("msg: %s", event.data);

        var data = JSON.parse(event.data);
  
        switch(data.type){
		case 'airquality':
			App.OnAirQuality(data);
			break;
		case 'coffee':
			App.OnCoffee(data);
			break;
		case 'movement':
			App.OnMovement(data);
			break;
		case 'idle':
			App.OnIdle(data);
			break;
        }
	}, 
	
	OnAirQuality: function(data){ 
		if(data.room != App.room)
			return;
			
        // change led to this level
		var led = $('#led');
		led.css("background-image", "url(images/lvl_" + data.level + ".png)");
		 
		// play beep
		if(data.beep){
			App.PlaySound(); 
			$('#speaker').css("background-image", "url(images/speaker_active.png)");
			
			// set time out on the next 1 second to turn the image off
			window.setTimeout(function(){
				$('#speaker').css("background-image", "url(images/speaker_deactive.png)");
			}, 1000);
		}
			
		// change door css images
		var door = $('#door');
		if(data.door){
			door.css("background-image", "url(images/door_active.gif)");
		}
		else{
			door.css("background-image", "url(images/door_deactive.gif)");
		}
		
		// change window css images
		var window_ = $('#window');
		if(data.window){
			window_.css("background-image", "url(images/window_active.gif)");
		}
		else{
			window_.css("background-image", "url(images/window_deactive.gif)"); 
		}
	},
	
	OnCoffee: function(data){
        // change bg to this coffee total
		var led = $('#coffee');
		if(data.UserId != App.user){
			console.log("no user id")
			return;
		}
		
		if(data.Intake <= 4)
			led.css("background-image", "url(images/coffee_" + data.Intake + ".png)"); 
		else 
		{
			led.css("background-image", "url(images/coffee_4.png)"); 
		}
		
			
		if(data.Intake){
			led.html(data.Intake + "X");
			
			if(data.Intake > 4){
				led.css("color", "#FF0000");
			} else {
				led.css("color", "#000");
			}
		}
		else
			led.html(""); 	
	},
	 
	OnMovement: function(data){
		// display the notification
		$('#movement #notification').html("No movement detected for 2 hours."); 
		$('#movement #dialog').css("display", "block"); 
	},
	
	OnIdle: function(data){
		// display the notification
		$('#idle').html("" + data.TimeIdle + " sec. Idle"); 		
	},
	
	PlaySound: function(){ 
		var audio = new Audio('images/beep.mp3');
		var sound_opt = $('#sound_option').is(':checked');
		if(sound_opt){
			audio.play(); 
		}
	},
	
	OnClose: function(){
		console.log("disconnected to server");		
	},
	
	Run: function(){
		if(this.WebSocketBrowserCheck){
			this.OpenWebSocket();			
		}
	},
	
	RunKinectApp: function(){
		alert("running kinect app")
	}
}
 
 
 /**
App.OnAirQuality({"timestamp":1422530580656,"type":"airquality","beep":false,"room":"cscw-bplus-04","level":0,"door":false,"window":false})
*/