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

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.ibm.wasdev.sphere.sensors.BounceMonitor;
import com.ibm.wasdev.sphere.sensors.Temperature;

@Singleton
@Startup
public class SensorListener {

	@Inject
	private PersistenceHelper helper;
	Temperature temperatureSensor;

	public SensorListener() {
		System.out.println("Constructing timed sensor listener.");
		temperatureSensor = new Temperature();
		BounceMonitor b = new BounceMonitor();
		System.out.println("Constructing bounce monitor " + b);

	}

	@Schedule(hour = "*", minute = "*", second = "5", persistent = false)
	public void listen() {

		// TODO cancel right out if we haven't got a sensor
		if (temperatureSensor.hasPins()) {
			System.out.println("Reading sensor data.");
			double temperature = temperatureSensor.getTemperature();
			helper.recordReading(temperature);
		}

	}

}
