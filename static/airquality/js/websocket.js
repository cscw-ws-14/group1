
var App = {
	url: "ws://localhost:1880/",
	channel: "airquality", 
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
        }
	}, 
	
	OnAirQuality: function(data){
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
			door.css("background-image", "url(images/door_active.png)");
		}
		else{
			door.css("background-image", "url(images/door_deactive.png)");
		}
		
		// change window css images
		var window_ = $('#window');
		if(data.window){
			window_.css("background-image", "url(images/window_active.png)");
		}
		else{
			window_.css("background-image", "url(images/window_deactive.png)"); 
		}
	},
	
	OnCoffee: function(data){
        // change bg to this coffee level
		var led = $('#coffee');
		led.css("background-image", "url(images/coffee_" + data.level + ".png)"); 
	},
	
	PlaySound: function(){
		return;
		var audio = new Audio('images/beep.mp3');
		audio.play(); 
	},
	
	OnClose: function(){
		console.log("disconnected to server");		
	},
	
	Run: function(){
		if(this.WebSocketBrowserCheck){
			this.OpenWebSocket();			
		}
	}	
}
 