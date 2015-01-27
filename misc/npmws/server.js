var port = process.argv[2]
if (!port) {
	console.log("please give me a port number to run on");
	process.exit(1);
}

var WebSocketServer = require('ws').Server,
	wss = new WebSocketServer({
		port: port
	});

wss.on('connection', function connection(ws) {
	console.log("someone connected")
	ws.on('message', function incoming(message) { 
		try {
			var msgObj = JSON.parse(message);
			if (msgObj.type == "server_sender") {
				console.log("broadcasting at " + Date())
				wss.broadcast(msgObj.message);
			}
		} catch (ex) {
			// not in json format.
			// ignore
		}
	}); 

});

// send to all client
wss.broadcast = function broadcast(data) {
	wss.clients.forEach(function each(client) {
		client.send(data);
	});
};

process.on('exit', function(code) { 	
  	console.log('killing server');
	wss.close();
  	console.log('server killed');
});

process.on('SIGINT', function() { 
  	process.exit();
});

