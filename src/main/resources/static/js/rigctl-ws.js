var RigCtl = function() {
	
	this.config = {
		stompClient: null,
		currentRigId: null			
	};
	
	this.connect = function(brokerUrl) {
		this.config.currentRigId = 0;
	    var socket = new SockJS('/rigctl-update');
	    this.config.stompClient = Stomp.over(socket);
	    console.log("rigctl: connecting...");
	    this.config.stompClient.connect({}, function (frame) {
	        setConnected(true);
	        console.log('rigctl: connected: ' + frame);
	        $("#rigctl-indicator").html("<i class='fas fa-link'></i>");
	        rigCtl.config.stompClient.subscribe(brokerUrl, function (rigctlMessage) {
	        	console.log("RigctlMessage", rigctlMessage);
	        	message = JSON.parse(rigctlMessage.body);
	        	console.log("RigctlMessage parsed", message);
	        	if ( message.frequency > 0 ) {
	        		$('[data-item-id="frequency"]').val(message.frequency);
	        	}
	        	if ( message.mode ) {
	        		$('[data-item-id="mode"]').val(message.mode);
	        	}
	        });
	    });		
	};
	
	this.disconnect = function() {
	    if (this.config.stompClient !== null) {
	    	this.config.stompClient.disconnect();
	    }
	    setConnected(false);
	    $("#rigctl-indicator").html("");
	    console.log("Disconnected");
	};
	
	this.sendUpdate = function() {
		this.config.stompClient.send("/app/update/" + this.config.currentRigId, {}, JSON.stringify({'rigId': this.config.currentRigId, 'frequency': $("#frequency").val(), 'mode': $("#mode").val()}));
	};
	
	this.showUpdate = function(message) {
		$('frequency').val(message.frequency);
		$('mode').val(message.mode);
		//$("#greetings").append("<tr><td>" + message.rigId + "</td><td>" + message.frequency + "</td><td>" + message.mode + "</td></tr>");
	}
}

$(function () {
    $( ".websocket-connect" ).each(function(){
    	$(this).click(function() {
    		rigCtl.connect($(this).attr('data-broker-url'));
    	});
    });
    //$( ".websocket-connect" ).click(function() { rigCtl.connect(2); });
    $( "#rigctl-disconnect" ).click(function() { rigCtl.disconnect(); });
    $( "#send" ).click(function() { rigCtl.sendUpdate(); });
    // Use this to auto-connect
    //$( document ).ready(function() { rigCtl.connect(1); });
});

var rigCtl = new RigCtl();

function setConnected(connected) {
	$(".websocket-connect").prop("disabled", connected);
    $("#rigctl-disconnect").prop("disabled", !connected);
    if (connected) {
    	$("#conversation").show();
    }
    else {
    	$("#conversation").hide();
    }
    $("#greetings").html("");
}