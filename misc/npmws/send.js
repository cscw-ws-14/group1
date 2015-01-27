var host = process.argv[2]
var port = process.argv[3] 
var message = process.argv[4]

if (!port || !host) {
	console.log("please give me a host and a port number to connect to");
	process.exit(1);
}

if(!message){
	console.log("what are you sending?")
	process.exit(1)
}

var WebSocket = require('ws'),
	ws = new WebSocket('ws://' + host + ':' + port);
	
ws.on('open', function() {
	console.log("connected")
	var msg = {
		type:'server_sender',
		message: message
	}
	
	// send the message to broadcast
	ws.send(JSON.stringify(msg)); 
});

ws.on('message', function(message) {
	console.log('sent: %s', message);
	
	// exit the program because we are done
	process.exit();
});

process.on('exit', function(code) { 	
	ws.close();
});
 
process.on('SIGINT', function() { 
  	process.exit();
});

/**
Examples

node send localhost 8888 "{\"type\":\"coffee\",\"Intake\":3,\"UserId\":1}"



*/