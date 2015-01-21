
var App = {
	url: "ws://localhost:8887/",
	channel: "", 
	ws: null,
  
	//{timestamp:1, beep:true, level:1, door:true, window:true} 
    
    Construct: function(){
         
    }, 
	
	WebSocketBrowserCheck: function(){
		if (("WebSocket" in window) == false){
			alert("WebSocket is not supported by your Browser!");
			return false;	
		}			
	},
	
	OpenWebSocket: function(){
		this.ws = new WebSocket( this.url + this.channel );
		this.ws.onopen  = this.OnOpen;
		this.ws.onclose = this.OnClose;
		this.ws.onmessage = this.OnMessage;
	},
	
	OnOpen: function(){
		console.log("connected to server");
	},
	
	OnMessage: function(event){
        if(event.data == "" || typeof(event.data) == "undefined")
            return;

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
        }
	}, 
	
	OnAirQuality: function(data){
        // change led to this level
		var led = $('#led');
		led.css("background-image", "url(images/lvl_" + data.level + ".png)");
		 
		// play beep
		if(data.beep){
			App.PlaySound(); 
			$('#speaker').css("background-image", "url(images/speaker_active.gif)");
			
			// set time out on the next 1 second to turn the image off
			window.setTimeout(function(){
				$('#speaker').css("background-image", "url(images/speaker_deactive.gif)");
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
        // change bg to this coffee level
		var led = $('#coffee');
		led.css("background-image", "url(images/coffee_" + data.level + ".png)"); 
	},
	 
	OnMovement: function(data){
		// display the notification
		$('#movement #notification').html("No movement detected for 2 hours."); 
		$('#movement #dialog').css("display", "block"); 
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
 