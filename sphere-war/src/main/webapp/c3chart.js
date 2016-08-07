var chart = c3.generate({
	data : {
		x : 'date',
		y : 'temperature',
		url : 'rest/tempdata',
		mimeType : 'json'
	},
	axis : {
		x : {
			type : 'timeseries',
			tick : {
				format : '%H:%M:%S'
			}
		},
		y : {
			tick : {}
		}

	}
});
