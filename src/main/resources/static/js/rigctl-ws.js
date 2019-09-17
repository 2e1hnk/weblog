var RigCtl = function() {
	
	this.config = {
		stompClient: null,
		currentRigId: null			
	};
	
	this.connect = function(rigId) {
		this.config.currentRigId = rigId;
	    var socket = new SockJS('/rigctl-update');
	    this.config.stompClient = Stomp.over(socket);
	    console.log("rigctl: connecting to rig " + rigId);
	    this.config.stompClient.connect({}, function (frame) {
	        setConnected(true);
	        console.log('rigctl: connected: ' + frame);
	        $("#rigctl-indicator").html("<i class='fi-check'></i>");
	        rigCtl.config.stompClient.subscribe('/topic/rigctl/' + rigId, function (rigctlMessage) {
	        	console.log("RigctlMessage", rigctlMessage);
	        	message = JSON.parse(rigctlMessage.body);
	        	console.log("RigctlMessage parsed", message);
	        	if ( message.frequency > 0 ) {
	        		$('[data-item-id="frequency"]').val(message.frequency);
	        	}
	        	if ( message.mode != "" ) {
	        		$('[data-item-id="mode"]').val(message.mode);
	        	}
	        	// $('#frequency').val(message.frequency);
	    		// $('#mode').val(message.mode);
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
    $( "#connect-1" ).click(function() { rigCtl.connect(1); });
    $( "#connect-2" ).click(function() { rigCtl.connect(2); });
    $( "#disconnect" ).click(function() { rigCtl.disconnect(); });
    $( "#send" ).click(function() { rigCtl.sendUpdate(); });
    // Use this to auto-connect
    //$( document ).ready(function() { rigCtl.connect(1); });
});

var rigCtl = new RigCtl();

function setConnected(connected) {
	$("#connect-1").prop("disabled", connected);
	$("#connect-2").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
    	$("#conversation").show();
    }
    else {
    	$("#conversation").hide();
    }
    $("#greetings").html("");
}