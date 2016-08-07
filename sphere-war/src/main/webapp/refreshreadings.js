
function updateReadings() {
	var url = "rest/latestreadings";
	var client = new XMLHttpRequest();

	client.onreadystatechange = function() {
		if (client.readyState == 4 && client.status == 200) {
			var readings = JSON.parse(client.responseText);
			document.getElementById('temperature-information').innerHTML = "The WebSphere Sphere is currently " + readings.mood + " (temperature is "+readings.temperature+"&deg;).";
			document.getElementById('location').innerHTML = "The pcDuino is "+ readings.location + "." ;
		}
	};

	client.open("GET", url, true);

	client.setRequestHeader("Content-Type", "text/plain");

	client.send();


};

updateReadings();
setInterval(updateReadings, 2000);