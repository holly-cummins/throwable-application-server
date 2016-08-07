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

package com.ibm.wasdev.sphere.sensors;

import java.io.IOException;

public class Temperature {

	public static final double UNSET = -1;
	private Pcduino arduino = new Pcduino();

	public double getTemperature() {

		if (arduino.hasPins()) {
			int sensorPin = 5; // select the input pin for the sensor
			int sensorValue = 0; // variable to store the value coming from the

			try {
				sensorValue = arduino.analogRead(sensorPin);
			} catch (IOException e) {
				// Unlikely
				e.printStackTrace();
				return UNSET;
			}

			// Convert the analog value to a temperature in kelvin, based on the
			// calibration formula
			int denominator = 1023 - sensorValue;
			if (denominator != 0) {
				double temperature = Math.log(((10240000 / denominator) - 10000));
				temperature = 1
						/ (0.001129148 + (0.000234125 + (0.0000000876741 * temperature * temperature)) * temperature);
				temperature = temperature - 273.15; // Convert Kelvin to Celcius

				// Now do a further crude pcduino calibration, based on one data
				// point
				temperature += 25;

				return temperature;
			} else {
				return UNSET;
			}
		} else {
			return UNSET;
		}

	}

	public boolean hasPins() {
		return arduino.hasPins();
	}

}
