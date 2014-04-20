var http = require('http');
var net = require('net');
var events = require('events');

// destination IP address and port
var HOST = '127.0.0.1';
var PORT = '10001';

// as a client, when http server receive data, the client relay the data
var socket = new net.Socket({allowHalfOpen: false, readable: true, writable: true});
socket.connect(PORT, HOST, function(){
	console.log("connect to: " + HOST + ": " + PORT);
});

socket.on('close', function(){
	console.log("connection closed");
});

socket.on('error', function(){
	console.log("connection failure!");
});

socket.setNoDelay(true);
socket.setKeepAlive(true);

var emitter = new events.EventEmitter();
emitter.on("relay", function(msg){
	socket.write(msg);
	socket.write("finished!");
});

emitter.on("error", function(msg){
	console.log("error: " + msg);
});
		

http.createServer(function(req, res) {
	//console.log("send");
	// parse client send information(post)
	var postData = '';
	//if(req.method == 'POST'){
		req.setEncoding("utf-8");
		
		req.addListener('data', function(msg){
			postData += msg;
		});
		
		req.addListener('end', function(){
			console.log(postData);
			emitter.emit("relay", postData);
			//socket.write(postData);
			//socket.write("finished!");
			//socket.end();
			res.end();
		});
	
}).listen(1111);
console.log('Server running at http://127.0.0.1:1111/');


