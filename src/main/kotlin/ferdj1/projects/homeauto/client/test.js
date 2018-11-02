#!/usr/bin/env node
var WebSocketClient = require('websocket').client;

var client = new WebSocketClient();

client.on('connectFailed', function(error) {
    console.log('Connect Error: ' + error.toString());
});

client.on('connect', function(connection) {
    console.log('WebSocket Client Connected');
    connection.on('error', function(error) {
        console.log("Connection Error: " + error.toString());
    });
    connection.on('close', function() {
        console.log('echo-protocol Connection Closed');
    });
    connection.on('message', function(message) {
        if (message.type === 'utf8') {
            console.log("Received: '" + message.utf8Data + "'");
        }
    });

    function sendNumber() {
        if (connection.connected) {
            var number = Math.round(Math.random() * 0xFFFFFF);
            connection.sendUTF(number.toString());
            setTimeout(sendNumber, 1000);
        }
    }
    function sendJSON() {
        if(connection.connected) {
            var json = {
                "messageType": "description",
                "id" : "sony-smartTV43HD",
                "name" : "Smart TV 43 HD",
                "manufacturer" : "Sony",
                "type": "TV",
                "commands" : [
                    {
                        "id" : "turnOn",
                        "name" : "Turn On",
                        "description" : "Turns TV on",
                        "getSetType": "SET",
                        "displayText": null,
                        "parameterDescriptions": [],
                        "returnType": null
                    },
                    {
                        "id" : "turnOff",
                        "name" : "Turn Off",
                        "description" : "Turns TV off",
                        "getSetType": "SET",
                        "displayText": null,
                        "parameterDescriptions": [],
                        "returnType": null
                    },
                    {
                        "id" : "changeVolume",
                        "name" : "Change Volume",
                        "description" : "Changes TVs volume",
                        "getSetType": "SET",
                        "displayText": null,
                        "parameterDescriptions": [{
                            "type": "number",
                            "limitType": "MIN_MAX",
                            "min": "0",
                            "max": "100",
                            "values": null
                        }],
                        "returnType": null
                    },
                    {
                        "id" : "changeSource",
                        "name" : "Change Source",
                        "description" : "Changes TVs source",
                        "getSetType": "SET",
                        "displayText": null,
                        "parameterDescriptions": [{
                            "type": "string",
                            "limitType": "LIMITED_SET",
                            "min": null,
                            "max": null,
                            "values": ["HDMI1", "HDMI2", "AV", "TV"]
                        }],
                        "returnType": null
                    },
                    {
                        "id" : "changeChannel",
                        "name" : "Change Channel",
                        "description" : "Changes current channel",
                        "getSetType": "SET",
                        "displayText": null,
                        "parameterDescriptions": [{
                            "type": "number",
                            "limitType": "MIN_MAX",
                            "min": "0",
                            "max": "999",
                            "values": null
                        }],
                        "returnType": null
                    },
                    {
                        "id" : "getVolume",
                        "name" : "Get Volume",
                        "description" : "Gets currently set volume",
                        "getSetType": "GET",
                        "displayText": "Volume",
                        "parameterDescriptions": [],
                        "returnType": "number"
                    },
                    {
                        "id" : "getChannel",
                        "name" : "Get Channel",
                        "description" : "Gets currently set channel",
                        "getSetType": "GET",
                        "displayText": "Channel",
                        "parameterDescriptions": [],
                        "returnType": "number"
                    }
                ]
            };

            console.log(JSON.stringify(json));

            connection.sendUTF(JSON.stringify(json));
        }
    }

    sendJSON();
});

client.connect('ws://localhost:8080/ws');