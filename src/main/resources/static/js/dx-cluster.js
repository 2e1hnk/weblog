	var DxClusterClient = function() {
		
		this.config = {
			stompClient: null,
			maxRows: 15
		};
		
		this.connect = function() {
			var socket = new SockJS('/dx-spots');
		    this.config.stompClient = Stomp.over(socket);
		    console.log("connecting to dx spots broker");
		    this.config.stompClient.connect({}, function (frame) {
		        //setConnected(true);
		        console.log('Connected: ' + frame);
		        dxClusterClient.config.stompClient.subscribe('/topic/dx-spots', function (dxSpotMessage) {
		        	console.log("dxSpotMessage", dxSpotMessage);
		        	console.log("dxSpotMessage parsed", JSON.parse(dxSpotMessage.body));
		        	dxClusterClient.showUpdate(JSON.parse(dxSpotMessage.body));
		        });
		    });		
		};
		
		this.disconnect = function() {
		    if (this.config.stompClient !== null) {
		    	this.config.stompClient.disconnect();
		    }
		    //setConnected(false);
		    console.log("Disconnected");
		};
		
		this.sendUpdate = function() {
			//this.config.stompClient.send("/app/update/" + this.config.currentRigId, {}, JSON.stringify({'rigId': this.config.currentRigId, 'frequency': $("#frequency").val(), 'mode': $("#mode").val()}));
		};
		
		this.showUpdate = function(message) {
		    $("#dx-spots > tr:first").before(
		    		"<tr onclick=\"toggleElement('" + message.time + "-" + message.spotter + "-" + message.dx + "');\">" +
		    		"<td class=\"monospace\">" + ('0000' + message.time).slice(-4) + 
		    		"</td><td class=\"monospace\">" + message.spotter + 
		    		"</td><td class=\"monospace\">" + message.dx + 
		    	//	"</td><td class=\"monospace\">" + message.grid + 
		    		"</td><td class=\"monospace\">" + message.frequency + 
		    		"</td></tr><tr id='" + message.time + "-" + message.spotter + "-" + message.dx + "' class=\"w3-hide\"><td colspan=\"4\" class=\"monospace\">" + message.comments + 
		    		"</td></tr>");
		    // Delete extra rows
		    var rowsToDelete = $('#dx-spots > tr').length - (this.config.maxRows * 2);
		    
		    console.log("Rows to delete", rowsToDelete);
		    
		    for ( i = rowsToDelete; i > 0; i-- ) {
		    	$('#dx-spots tr:last').remove();
		    }
		}
	
	}

	var dxClusterClient = new DxClusterClient();
	dxClusterClient.connect();