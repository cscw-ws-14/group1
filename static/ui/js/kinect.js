var App = {
	url: [ 
		"ws://localhost:8118/", // kinect
	], 
	ws: [], 
	window_width:1280,
	window_height:800,
	first: true,
	channel: "",
	score: 0,
	    
    Construct: function(){  
		App.window_height = $( window ).height() ;
		App.window_width = $( window ).width() ;
		
		$( window ).resize(function() {
		  	App.window_height = $( window ).height() ;
			App.window_width = $( window ).width() ;
			console.log($( window ).height());
			console.log($( window ).width());
		});
		
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

        var data = JSON.parse(event.data);
  
        switch(data.type){
		case 'kinect':
		default:
			App.FirstMethod();
			App.OnKinect(data);
			break;
        }
	}, 
	FirstMethod: function(){
		if(!App.first)
			return
			
		if(App.first)
			App.first = false;
		
		// hide the calibrating text
		$('#calibrated').hide()
			
		// show the target box
		App.RandomizeTarget();
	},
	
	RandomizeTarget: function(jointName){
		var x = Math.floor((Math.random() * (App.window_width -240)));
		var y = Math.floor((Math.random() * (App.window_height +77)));
 
		if(jointName){
			$('#target_' + jointName).css('left', x + "px")
			$('#target_' + jointName).css('top', y + "px") 
			return	
		}
		
		var x2 = Math.floor((Math.random() * (App.window_width -240)));
		var y2 = Math.floor((Math.random() * (App.window_height +77)));
				
		$('#target_l_hand').css('left', x + "px")
		$('#target_l_hand').css('top', y + "px") 
		
		$('#target_r_hand').css('left', x2 + "px")
		$('#target_r_hand').css('top', y2 + "px") 
	},
	
	HideTarget: function(){ 
		$('#target_r_hand').css('left', "-300px")  
		$('#target_l_hand').css('left', "-300px") 
	},
	
	OnKinect: function(data){ 
		var x = Math.floor(data.x * App.window_width);
		var y = Math.floor(data.y * App.window_height);
		$('#' + data.jointName).css("top", y + "px");
		$('#' + data.jointName).css("left", x + "px");
		
		App.HasReachTarget(data);
	},
	
	HasReachTarget: function(data){
		var x = $('#' + data.jointName).position().left;
		var y = $('#' + data.jointName).position().top;
		
		// check bound
		var box = $('#target_' + data.jointName)
		var boxPos = box.position();
		if( x > boxPos.left && x < boxPos.left + box.width() &&
			y > boxPos.top && y < boxPos.top + box.height()
		)
		{
			console.log(data.jointName + " is in the box");
			//re positino target
			App.RandomizeTarget(data.jointName);
			// give points
			App.GiveScore();
		}
	},
	
	GiveScore: function(){
		console.log("scored");
		App.score += 10;
		$('#score').html("Score: " + App.score + " Pts.")
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
/*
$('#target_r_hand').css('left', '600px') ;
$('#target_r_hand').css('top', '400px')  ;
$('#target_l_hand').css('left', '850px') ;
$('#target_l_hand').css('top', '450px')  ;
*/