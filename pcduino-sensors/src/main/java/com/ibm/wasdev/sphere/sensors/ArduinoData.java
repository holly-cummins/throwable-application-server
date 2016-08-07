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

public class ArduinoData {

	private String name;
	private double temperature;
	private double light;
	private boolean isDark;

	public ArduinoData(String name, double temperature, double light, boolean isDark) {
		this.name = name;
		this.temperature = round(temperature, 2);
		this.light = round(light * 100, 2);
		this.isDark = isDark;
	}

	private double round(double number, int places) {
		double multiplier = Math.pow(10, places);
		return Math.round(number * multiplier) / multiplier;
	}

	public String getName() {
		return name;
	}

	public double getTemperature() {
		return temperature;
	}

	public double getLight() {
		return light;
	}

	public boolean getIsDark() {
		return isDark;
	}

	public String toString() {
		return this.name + ":" + "/" + this.temperature + "/" + this.light;
	}

	public static ArduinoData create() throws InterruptedException, IOException {
		try {
			return new ArduinoData("pcduino", new Temperature().getTemperature(), new Light().lightValue(),
					new Light().isDark());
		} catch (IOException e) {
			return new ArduinoData("uhoh", 0, 0, true);

		}
	}
}
