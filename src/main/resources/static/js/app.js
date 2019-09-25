// Set up map
var mymap = L.map('map', {fullscreenControl: true}).setView([51.505, -0.09], 13);

var maxZoom = 8;

L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 14
}).addTo(mymap);

var markerLayer = L.featureGroup().addTo(mymap);

// Add Maidenhead Grid Layer
var gridLayer = L.maidenhead({ worked: [], confirmed: [] });
gridLayer._map = mymap;

var layerControl = new L.Control.Layers(null, {
	'Gridsquares': maidenhead = L.maidenhead()
}).addTo(mymap);

// Add auto-update custom control
	var autoupdate = true;
	var isProgramaticMove = false;
	var latest_id = 0;
	var autoUpdateControl = L.Control
		.extend({

			options : {
				position : 'topleft'
			//control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
			},

			onAdd : function(map) {
				var container = L.DomUtil
						.create('div',
								'leaflet-bar leaflet-control leaflet-control-custom auto-update-container');
				container.innerHTML = '<a href="#" title="Automatically update map when new contacts are logged" role="button" aria-label="Automatically pan and zoom to show all contacts"><i id="autoupdateIcon" class="fas fa-search-location" style="padding-top: 5px"></i></a>'
				container.style.fontSize = '20px';
				container.style.backgroundColor = 'white';
				container.style.borderColor = 'red';

				container.onclick = function() {
					toggleAutoUpdate();
				}
				return container;
			},

		});
	mymap.addControl(new autoUpdateControl());

// Disable auto update when user pans/zooms the map (but not on a programatic pan/zoom)
	mymap.on('zoomstart', function() {
		console.log("isProgramaticMove", isProgramaticMove);
		if (!isProgramaticMove) {
			disableAutoUpdate();
		}
	})
	
	mymap.on('dragstart', function() {
		console.log("isProgramaticMove", isProgramaticMove);
		if (!isProgramaticMove) {
			disableAutoUpdate();
		}
	})
	
// Auto-update control functions
	function toggleAutoUpdate() {
		if (autoupdate) {
			disableAutoUpdate();
		} else {
			enableAutoUpdate();
		}
	}

	function enableAutoUpdate() {
		autoupdate = true;
		$('.auto-update-container').css('border-color', 'red');
		console.log("Auto update enabled");

		// Move map to correct position
		isProgramaticMove = true;
		if (markerClusterGroup.getBounds().length > 0) {
			if (map.getBoundsZoom(markerClusterGroup.getBounds()) > maxZoom) {
				map.setView(markerClusterGroup.getBounds().getCenter(),
						maxZoom);
			} else {
				map.fitBounds(markerClusterGroup.getBounds());
			}
		}
		isProgramaticMove = false;
	}

	function disableAutoUpdate() {
		autoupdate = false;
		$('.auto-update-container').css('border-color',
				'rgba(0, 0, 0, 0.2)');
		console.log("Auto update disabled");
	}

// Update map markers
	function update() {
		var jqxhr = $.get("/location/from/" + latest_id,
				function(data) {
					if (data.length > 0) {
						$.each(data, function(index, value) {
							console.log("Adding", value);
							
							L.marker([ value.lat, value.lon ]).bindPopup(
									"<br /><b>"	+ value.callsign + "</b><br />" + value.name + "<br />"
											+ value.timestamp).addTo(markerLayer);
														
							latest_id = value.id;
						});
						
						if (autoupdate) {
							isProgramaticMove = true;
							if (mymap.getBoundsZoom(markerLayer.getBounds()) > maxZoom) {
								mymap.setView(markerLayer.getBounds().getCenter(), maxZoom);
							} else {
								mymap.fitBounds(markerLayer.getBounds());
							}
						}
					}
					isProgramaticMove = false;

				});
	}

	update();
	setInterval(update, 10000);

	
//populate_map(markerLayer);


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
    
    $("#frequency").change(function() {
    	var freq = $("#frequency").val();
    	
    	console.log("New frequency", freq);
    	
    	var band = "N/A";
    	if ( freq >= 135700 && freq <= 137800 ) {
    		band = "2200M";
    	}
    	if ( freq >= 472000 && freq <= 479000 ) {
    		band = "630M";
    	}
    	if ( freq >= 1810000 && freq <= 2000000 ) {
    		band = "160M";
    	}
    	if ( freq >= 3500000 && freq <= 3800000 ) {
    		band = "80M";
    	}
    	if ( freq >= 5258500 && freq <= 5406500 ) {
    		band = "60M";
    	}
    	if ( freq >= 7000000 && freq <= 7200000 ) {
    		band = "40M";
    	}
    	if ( freq >= 10100000 && freq <= 10150000 ) {
    		band = "30M";
    	}
    	if ( freq >= 14000000 && freq <= 14350000 ) {
    		band = "20M";
    	}
    	if ( freq >= 18068000 && freq <= 18168000 ) {
    		band = "17M";
    	}
    	if ( freq >= 21000000 && freq <= 21450000 ) {
    		band = "15M";
    	}
    	if ( freq >= 24890000 && freq <= 24990000 ) {
    		band = "12M";
    	}
    	if ( freq >= 28000000 && freq <= 29700000 ) {
    		band = "10M";
    	}
    	if ( freq >= 50000000 && freq <= 52000000 ) {
    		band = "6M";
    	}
    	if ( freq >= 70000000 && freq <= 70500000 ) {
    		band = "4M";
    	}
    	if ( freq >= 144000000 && freq <= 147000000 ) {
    		band = "2M";
    	}
    	if ( freq >= 430000000 && freq <= 440000000 ) {
    		band = "70CM";
    	}
    	if ( freq >= 1240000000 && freq <= 1325000000 ) {
    		band = "23CM";
    	}
    	if ( freq >= 2310000000 && freq <= 2450000000 ) {
    		band = "13CM";
    	}
    	if ( freq >= 3400000000 && freq <= 3410000000 ) {
    		band = "9CM";
    	}
    	if ( freq >= 5650000000 && freq <= 5850000000 ) {
    		band = "6CM";
    	}
    	if ( freq >= 10000000000 && freq <= 10500000000 ) {
    		band = "3CM";
    	}
    	if ( freq >= 24000000000 && freq <= 24250000000 ) {
    		band = "12MM";
    	}
    	if ( freq >= 47000000000 && freq <= 47200000000 ) {
    		band = "6MM";
    	}
    	if ( freq >= 75500000000 && freq <= 81000000000 ) {
    		band = "4MM";
    	}
    	if ( freq >= 134000000000 && freq <= 136000000000 ) {
    		band = "2MM";
    	}
    	$("#band").val(band);
    });
    
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
		            
		            // Populate input fields
	            	if ( data[0].grid ) {
	            		$('#location').val(data[0].grid);
	            	} else {
	            		$('#location').val(data[0].address);
	            	}
	            	$('#name').val(data[0].fname);
	            	$('#location').val(data[0].grid);
		            
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
	        	var previous_contact_list = "<b>Previous Contacts </b><span class='w3-badge w3-scout'>" + data.length + "</span><br />";
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

function openTab(panelBlock, tabName) {
	  var i;
	  var x = document.getElementsByClassName(panelBlock);
	  for (i = 0; i < x.length; i++) {
	    x[i].style.display = "none";
	  }
	  document.getElementById(tabName).style.display = "block";
}

var leftMenuOpen = false;	// Default to closed
var rightMenuOpen = false;	// Default to closed


function toggleRightMenu() {
	if ( rightMenuOpen ) {
		closeRightMenu();
	} else {
		openRightMenu();
	}
}

function openRightMenu() {
  document.getElementById("rightMenu").style.display = "block";
  document.getElementById("right-menu-button").innerHTML = "<i class=\"fas fa-times w3-large\"></i>";
  rightMenuOpen = true;
}

function closeRightMenu() {
  document.getElementById("rightMenu").style.display = "none";
  document.getElementById("right-menu-button").innerHTML = "<i class=\"fa fa-globe w3-large\"></i>";
  rightMenuOpen = false;
}

function toggleLeftMenu() {
	if ( leftMenuOpen ) {
		closeLeftMenu();
	} else {
		openLeftMenu();
	}
}

function openLeftMenu() {
	document.getElementById("page-overlay").style.display = "block";
	document.getElementById("leftMenu").style.display = "block";
	document.getElementById("left-menu-button").innerHTML = "<i class=\"fas fa-times w3-large\"></i>";
	leftMenuOpen = true;
}

function closeLeftMenu() {
	document.getElementById("page-overlay").style.display = "none";
	document.getElementById("leftMenu").style.display = "none";
	document.getElementById("left-menu-button").innerHTML = "<i class=\"fas fa-bars w3-large\"></i>";
	leftMenuOpen = false;
}

function toggleSmallPanel(header_id, target_id) {
	
	  var targetPanel = document.getElementById(target_id);
	  var targetHeader = document.getElementById(header_id);
	
	  if (targetPanel.className.indexOf("w3-hide-small") == -1) {
		  targetPanel.className += " w3-hide-small";
		  targetHeader.innerHtml = "<i class=\"fas fa-chevron-up\"></i> Hide Log Input";
	  } else {
		  targetPanel.className = targetPanel.className.replace(" w3-hide-small", "");
		  targetHeader.innerHtml = "<i class=\"fas fa-chevron-down\"></i> Show Log Input";
	  }
	}