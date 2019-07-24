$(document).foundation()

// Set up map
var mymap = L.map('map').setView([51.505, -0.09], 13);

L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 14
}).addTo(mymap);

populate_map();


// Handle callsign lookup

$(document).ready(function () {
    $("#callsign").keyup(function () {
	  clearTimeout($.data(this, 'timer'));
	  var wait = setTimeout(function() {
		  callsign_lookup($("#callsign").val());
	  }, 500);
	  $(this).data('timer', wait);
    });
});

function populate_map() {
    $.ajax({
        type: "GET",
        url: "/location/all",
        cache: false,
        timeout: 600000,
        success: function (data) {
        	$.each(data, function(index, value) {
        		L.marker([value.lat, value.lon]).bindPopup(value.callsign).addTo(mymap);
        	});
        	console.log("AJAX RESULT : ", data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        },
        complete: function() {
        	$("#callbook-status").html("");
        	map.invalidateSize();
    	}
    });	
}

function callsign_lookup(callsign) {
	if ( callsign.length > 3 ) {
		$("#callbook-status").html("Loading...");
		$("#previous-contacts-status").html("Loading...");
	    $.ajax({
	        type: "GET",
	        url: "/callbook/" + callsign,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	        	
	        	var address = data[0].addr1.length > 0 ? data[0].addr1 + "<br />" : "";
	        	address = data[0].addr2.length > 0 ? data[0].addr2 + "<br />" : "";
	        	address = data[0].county.length > 0 ? data[0].addrcounty + "<br />" : "";
	        	address = data[0].state.length > 0 ? data[0].state + "<br />" : "";
	        	address = data[0].zip.length > 0 ? data[0].zip + "<br />" : "";
	        	address = data[0].country.length > 0 ? data[0].country : "";
	
	        	$('#callbook-name').html(data[0].fname + " " + data[0].name);
	        	$('#callbook-callsign').html(data[0].callsign);
	        	$('#callbook-aliases').html(data[0].aliases);
	        	$('#callbook-grid').html(data[0].grid);
	        	$('#callbook-address').html(address);
	            $('#callbook-image').attr('src', data[0].image);
	
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
	        url: "/search/" + callsign,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	        	$.each(data, function(index, value) {
	        		var row = '<tr><td>' + value.timestamp + '</td><td>' + value.rstr + '</td><td>' + value.rsts + '</td></tr>';
	        		console.log("Adding row", row);
	        		$('#previous-contacts > tbody:last-child').append(row);
	        	});
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