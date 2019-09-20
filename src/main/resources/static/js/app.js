$(document).foundation()

// Set up map
var mymap = L.map('map', {fullscreenControl: true}).setView([51.505, -0.09], 13);

L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 14
}).addTo(mymap);

var markerLayer = L.featureGroup().addTo(mymap);

var gridLayer = L.maidenhead({ worked: [], confirmed: [] });
gridLayer._map = mymap;

var layerControl = new L.Control.Layers(null, {
	'Gridsquares': maidenhead = L.maidenhead()
}).addTo(mymap);

populate_map(markerLayer);

$("#view-tabs").on("change.zf.tabs", function (event) { 
	mymap.invalidateSize();
	mymap.fitBounds(markerLayer.getBounds());
});


// Handle callsign lookup

$(document).ready(function () {
    $("#callsign").keyup(function () {
	  clearTimeout($.data(this, 'timer'));
	  var wait = setTimeout(function() {
		  callsign_lookup($("#callsign").val());
	  }, 500);
	  $(this).data('timer', wait);
    });
    
    $("#liveSwitch").change(updateLiveEditMode);
    
    updateLiveEditMode();
});

function populate_map(layer) {
	$.ajax({
        type: "GET",
        url: "/location/all",
        cache: false,
        timeout: 600000,
        success: function (data) {
        	$.each(data, function(index, value) {
        		L.marker([value.lat, value.lon]).bindPopup(value.timestamp + "<br /><b>" + value.callsign + "</b><br />" + value.name).addTo(layer);
        	});
        	console.log("AJAX RESULT : ", data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        },
        complete: function() {
        	$("#callbook-status").html("");
        	console.log(layer);
        	mymap.fitBounds(layer.getBounds().pad(0.5));
    	}
    });	
}

function callsign_lookup(callsign) {
	if ( callsign.length < 3 ) {
		hide_info_box();
	} else {
		$("#callbook-status").html("Loading...");
		$("#previous-contacts-status").html("Loading...");

		$.ajax({
	        type: "GET",
	        url: "/callbook/" + callsign,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	        	
	            console.log("AJAX RESULT : ", data);
	        	
	        	if ( data.length > 0 ) {
	        	
		        	var address = !!data[0].addr1 && data[0].addr1.length > 0 ? data[0].addr1 + "<br />" : "";
		        	address = !!data[0].addr2 && data[0].addr2.length > 0 ? data[0].addr2 + "<br />" : "";
		        	address = !!data[0].county && data[0].county.length > 0 ? data[0].addrcounty + "<br />" : "";
		        	address = !!data[0].state && data[0].state.length > 0 ? data[0].state + "<br />" : "";
		        	address = !!data[0].zip && data[0].zip.length > 0 ? data[0].zip + "<br />" : "";
		        	address = !!data[0].country && data[0].country.length > 0 ? data[0].country : "";
		
		        	$('#callbook-name').html(data[0].fname + " " + data[0].name);
		        	$('#callbook-callsign').html(data[0].callsign);
		        	$('#callbook-aliases').html(data[0].aliases);
		        	$('#callbook-grid').html(data[0].grid);
		        	$('#callbook-address').html(address);
		            $('#callbook-image').attr('src', data[0].image);
		            
		            if ( data[0].qslmgr ) {
		            	$('#callbook-tag-qslmgr').html("QSL via: " + data[0].qslmgr);
		            	$('#callbook-tag-qslmgr').css('visibility', 'visible');
		            } else {
		            	$('#callbook-tag-qslmgr').html("");
		            	$('#callbook-tag-qslmgr').css('visibility', 'hidden');
		            }
		            
		            // Show the info box
		            show_info_box();
		            
		            // Populate empty input fields
		            if ( $('#location').val() == "" ) {
		            	if ( data[0].grid ) {
		            		$('#location').val(data[0].grid);
		            	} else {
		            		$('#location').val(data[0].address);
		            	}
		            }
		            
		            if ( $('#name').val() == "" ) {
		            	$('#name').val(data[0].fname);
		            }
		            
		            if ( $('#location').val() == "" ) {
		            	$('#location').val(data[0].grid);
		            }
		            
		        } else {
	        		// Hide the info box
		        	hide_info_box();
	        	}
	
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	        },
	        complete: function() {
	        	$("#callbook-status").html("");
	    	}
	    });
		$.ajax({
	        type: "GET",
	        url: "/lotw-user/" + callsign,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	        	if ( data.length > 0 ) {
	        		$("#callbook-tag-lotw").css("visibility", "visible");
	        	} else {
	        		$("#callbook-tag-lotw").css("visibility", "hidden");
	        	}
	            console.log("AJAX RESULT : ", data);
	
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	        },
	        complete: function() {
	        	$("#callbook-status").html("");
	    	}
	    });
		$.ajax({
	        type: "GET",
	        url: "/eqsl-user/" + callsign,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	        	if ( data.length > 0 ) {
	        		$("#callbook-tag-eqsl").css("visibility", "visible");
	        	} else {
	        		$("#callbook-tag-eqsl").css("visibility", "hidden");
	        	}
	            console.log("AJAX RESULT : ", data);
	
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	        },
	        complete: function() {
	        	$("#callbook-status").html("");
	    	}
	    });
	    $.ajax({
	        type: "GET",
	        url: "/log/search/" + callsign,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	        	var previous_contact_list = "<b>Previous Contacts </b>(" + data.length + ")<br />";
	        	$.each(data, function(index, value) {
	        		previous_contact_list = previous_contact_list + value.timestamp + '<br />';
	        	});
	        	$('#previous-contacts').html(previous_contact_list);
	        	$('#previous-contacts-count').html(data.length);
	            console.log("AJAX RESULT : ", data);
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	        },
	        complete: function() {
	        	$("#previous-contacts-status").html("");
	        }
	    });
	}
}

function show_info_box() {
	$('.info-box').animate({
		bottom: 0
	});
}

function hide_info_box() {
	$('.info-box').animate({
		bottom: 0 - $('.info-box').height() - 5
	});
}

function updateLiveEditMode() {
	if ( document.getElementById("liveSwitch").checked ) {
		// Live mode
		setInterval(function() {
			document.getElementById("timestamp").value = moment().utc().format('YYYY-MM-DD HH:mm:ss');
		}, 1000);
	} else {
		// Edit mode
		clearInterval();
	}
}