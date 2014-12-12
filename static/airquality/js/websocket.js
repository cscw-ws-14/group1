
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
  
        // change led to this level
		document.getElementById("led").css(data.level);
		
		// play beep
		if(data.beep)
			App.PlaySound();
			
		// change door css images
		document.getElementById("door").css(data.door);
		
		// change window css images
		document.getElementById("window").css(data.window); 
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
 