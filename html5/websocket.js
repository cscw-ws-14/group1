
var App = {
	url: "ws://127.0.0.1:1880/",
	channel: "simulator",
	ws: null,

    robots: {},

    charts: [],

    labels: [],
    initialData: [],

    timeSpan: 60,

    robotsInterval:{},

    createInterval: function(robotId){
        this.robotsInterval[robotId] = window.setInterval( function(){
            App.pushZeroData(robotId);
        }, 1000);
    },

    stopInterval: function(robotId){
        window.clearInterval(App.robotsInterval[robotId]);
    },

    pushZeroData: function(robotId){
        App.robots[robotId].power.push(0);
        App.robots[robotId].carId = 0;

        App.RefreshChart(robotId);
    },

    Construct: function(){
        for(var i = this.timeSpan;i > 0;i--){
            if(i%4 == 0){
                this.labels.push(i);
            }else
                this.labels.push("");

            this.initialData.push(0);
        }

        this.CreateChartInstance();
    },

    CreateChartInstance: function(){
        for(var i = 1;i < 5; i++){
            var ctx = document.getElementById("robot" + i).getContext("2d");
            var data = {
                labels : this.labels,
                datasets : [{
                    label: "robot " + i,
                    fillColor: "rgba(151,187,205,0.2)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: this.initialData
                }]
            };

            var options = {
                bezierCurve : false,
                animation: false,
                scaleBeginAtZero: true,
                scaleIntegersOnly: true,
            }

            this.charts["r" + i] = (new Chart(ctx).Line(data, options));
        }
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

        if(typeof(App.robots[data.robot]) == "undefined")
            App.robots[data.robot] = {
                state: true,
                power: [],
                carId: 0
            };

        var robot = App.robots[data.robot];

        switch(data.type){
            case "state":
                robot.state = data.value;
                if(data.value == "stopped"){
                    // start sending 0 data
                    App.createInterval(data.robot);
                } else if(data.value == "started") {
                    // stop sending 0 data
                    App.stopInterval(data.robot);
                }
                break;

            case "power":
                // stop sending 0 data (just in case)
                App.stopInterval(data.robot);

                //App.robots[data.robot].power.unshift(parseInt(data.value));
                robot.power.push(parseInt(data.value));
                robot.carId = data.carId;

                App.RefreshChart(data.robot);
                break;
				
			case "min":
                robot.min = data.value;
				break;

			case "max":
                robot.max = data.value;
				break;

			case "avg":
                robot.avg = data.value;
				break;
				
			case "car":
                robot.car = data.value;
				break;	
        }

        // calculate average
        document.getElementById("avg" + data.robot).innerHTML = App.robots[data.robot].avg;
		document.getElementById("min" + data.robot).innerHTML = App.robots[data.robot].min;
        document.getElementById("max" + data.robot).innerHTML = App.robots[data.robot].max;
        document.getElementById("car" + data.robot).innerHTML = App.robots[data.robot].car;
        document.getElementById("state" + data.robot).innerHTML = App.robots[data.robot].state;

	},

    RefreshChart: function(robotId){
        var robot = App.robots[robotId];

        // clean array so it wont be heavy
        while(robot.power.length > 60){
            robot.power.shift();
        }

        // reupdate the array
        for(var i = App.timeSpan - 1;i > 0; i--){
            if(typeof(robot.power[i]) != "undefined" && robot.power[i] >= 0)
                App.charts[robotId].datasets[0].points[i].value = robot.power[i];
        }

        // update the chart
        App.charts[robotId].update();
    },

    Average: function(arr){
        var sum = 0;
        for(var i = 0;i < arr.length;i++){
            sum += arr[i];
        }

        if(arr.length <= 0)
            return 0;

        return sum/arr.length;
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

/*
 var Robot  {
 state : false,
 power : [],
 carId : 0
 }
 */

/*
 { "robot": "r3", "type": "power", "value": "7", "carId": "_carId", "timestamp": 1415876392505 }
 */