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

import java.util.HashSet;
import java.util.Set;

public class BounceMonitor {
	private static int instanceCount = 0;

	private int sensorPin = 2; // select the input pin for the sensor
	private Set<BounceListener> listeners = new HashSet<BounceListener>();

	public BounceMonitor() {
		instanceCount++;
		System.out.println("Bounce monitor getting ready to rock (" + instanceCount + " instances).");

	}

	public void monitorSensor(final LightFlasher flasher) {
		System.out.println("Monitoring shock sensor.");
		final GpioPin pcduino = new GpioPin(sensorPin);
		if (pcduino.hasPins()) {

			pcduino.setModeInput();

			System.out.println("Preparing to read");
			// TODO this is an anti-pattern, but not sure of right pattern
			Thread thread = new Thread() {
				public void run() {
					while (true) {
						// The pin will be 0 or 1
						String reading = pcduino.getPinStatus();
						// The sensor reads low for a bounce
						if ("0".equals(reading)) {
							// System.out.println(
							// notifyCount + "-" + listeners.size() + "Bounce
							// monitor read -> " + reading);
							flasher.flash();

							notifyListeners();
							// Notifications might happen fast, so don't
							// look for another bounce for a little while
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}
				}

			};

			thread.start();
		}
	}

	public void notifyListeners() {
		for (BounceListener listener : listeners) {
			listener.notifyBounce();
		}
	}

	public void registerListener(BounceListener bounceEndpoint) {
		System.out.println("Registering a bounce listener " + bounceEndpoint + ".");
		listeners.add(bounceEndpoint);
	}

	public void clearListeners() {
		listeners.clear();

	}

}
