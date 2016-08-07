/**
 * (C) Copyright IBM Corporation 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.wasdev.sphere;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.ibm.wasdev.sphere.sensors.Light;
import com.ibm.wasdev.sphere.sensors.Pcduino;
import com.ibm.wasdev.sphere.sensors.Temperature;

public class TemperatureReading {

	public Object temperature;
	public boolean runningEmbedded;
	public String mood;
	public String location;
	private boolean isDark;

	public TemperatureReading(PersistenceHelper service) throws IOException {

		Temperature temperatureSensor = new Temperature();
		double temp = temperatureSensor.getTemperature();
		if (temp != Temperature.UNSET) {
			temperature = Math.round(temp);
			runningEmbedded = new Pcduino().hasPins();

			Light light = new Light();
			isDark = light.isDark();

		} else {
			runningEmbedded = false;

			List<TempReading> ts = service.findAll(1);
			if (!ts.isEmpty()) {
				TempReading r = ts.get(0);
				temp = r.getTemperature();
				temperature = r.getTemperature();
				isDark = r.getIsDark();
			} else {
				temperature = "unknown";

			}
		}

		int calmThreshold = 22;
		int workingThreshold = 25;
		int panickedThreshold = 30;
		try {
			Properties props = new Properties();
			InputStream stream = new FileInputStream("/tmp/washat.properties");
			props.load(stream);
			calmThreshold = Integer.valueOf(props.getProperty("calm.threshold")).intValue();

			workingThreshold = Integer.valueOf(props.getProperty("working.threshold")).intValue();

			panickedThreshold = Integer.valueOf(props.getProperty("panicked.threshold")).intValue();

			stream.close();
		} catch (IOException localIOException) {
		}

		if (temp > panickedThreshold) {
			mood = "melting";
		} else if (temp > workingThreshold) {
			mood = "pretty warm";
		} else if (temp > calmThreshold) {
			mood = "cool";
		} else if (temp != Temperature.UNSET) {
			mood = "cold";
		} else {
			mood = "disconnected";
		}

		if (!isDark) {
			location = "outside the sphere";
		} else {
			location = "in the sphere";

		}
	}

}
