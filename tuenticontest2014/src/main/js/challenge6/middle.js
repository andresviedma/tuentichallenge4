#!/usr/bin/env node

if ((process.version.split('.')[1]|0) < 10) {
	console.log('Please, upgrade your node version to 0.10+');
	process.exit();
}

var net = require('net');
var util = require('util');
var crypto = require('crypto');

var options = {
	'port': 6969,
	'host': '54.83.207.90',
}


var lastClientKeyphrase;

var myKeyphrase = 'HotMagentaImpossibleGnuIsOpening';


process.stdin.setEncoding('utf8');

process.stdin.on('readable', function(chunk) {
  var chunk = process.stdin.read();
  if (chunk !== null) {
  	myKeyphrase = chunk.trim();
  }
});

process.stdin.on('end', function() {
  runWebSockets();
});

function runWebSockets() {
	var socket = net.connect(options, function() {
	});


	socket.on('data', function(data) {
		data = data.toString().trim().split(':');
		if(data[0] == 'CLIENT->SERVER') {
			res = processCliMessage(data[1]);
		} else {
			res = processSrvMessage(data[1]);
		}
		if (res != null) {
			socket.write(res);
		} else
			socket.write(data[1]);
	});
 }


var client = {
	dh: null,
	secret: null,

	createKey: function() {
		this.dh = crypto.createDiffieHellman(256);
		this.dh.generateKeys();
	},

	getPrime: function() {
		return this.dh.getPrime('hex');
	},
	getPublicKey: function() {
		return this.dh.getPublicKey('hex');
	},

	processServerKey: function(cryptoSecret) {
		this.secret = this.dh.computeSecret(cryptoSecret, 'hex');
	},

	encryptMessage: function(phrase) {
		return cypher(this.secret, phrase);
	},
	decryptMessage: function(phrase) {
		return decypher(this.secret, phrase);
	}
};

var server = {
	dh: null,
	secret: null,

	createCompatibleKey: function(prime, key) {
		this.dh = crypto.createDiffieHellman(prime, 'hex');
		this.dh.generateKeys();
		this.secret = this.dh.computeSecret(key, 'hex');
		return this.dh.getPublicKey('hex');
	},

	getPublicKey: function() {
		return this.dh.getPublicKey('hex');
	},

	processCryptMessage: function(msg, msgProcessor) {
		var keyphrase = decypher(this.secret, msg);
		var res = msgProcessor(keyPhrase);
		return cypher(res);
	},

	encryptMessage: function(phrase) {
		return cypher(this.secret, phrase);
	},
	decryptMessage: function(phrase) {
		return decypher(this.secret, phrase);
	}
};


function processCliMessage(data) {
	data = data.toString().trim().split('|');
	if(data[0] == 'key') {
		server.createCompatibleKey(data[1], data[2]);
		client.createKey();
		return util.format('key|%s|%s\n', client.getPrime(), client.getPublicKey());

	} else if (data[0] == 'keyphrase') {
		lastClientKeyphrase = server.decryptMessage(data[1]);
		if(myKeyphrase == null)  myKeyphrase = lastClientKeyphrase;
		var keyphrase = client.encryptMessage(myKeyphrase);
		return util.format('keyphrase|%s\n', keyphrase);
	}
}


function processSrvMessage(data) {
	data = data.toString().trim().split('|');
	if (data[0] == 'key') {
		client.processServerKey(data[1]);
		return util.format('key|%s\n', server.getPublicKey());
	} else if (data[0] == 'result') {
		var result = client.decryptMessage(data[1]);
		process.stdout.write(result + '\n');

		var result = server.encryptMessage(lastClientKeyphrase);
		return util.format('result|%s\n', result);
	}
}


function cypher(onesecret, msg) {
	var cipher = crypto.createCipheriv('aes-256-ecb', onesecret, '');
	var result = cipher.update(msg, 'utf8', 'hex') + cipher.final('hex');
	return result;
}

function decypher(onesecret, msg) {
	var decipher = crypto.createDecipheriv('aes-256-ecb', onesecret, '');
	var keyphrase = decipher.update(msg, 'hex', 'utf8') + decipher.final('utf8');
	return keyphrase;
}
