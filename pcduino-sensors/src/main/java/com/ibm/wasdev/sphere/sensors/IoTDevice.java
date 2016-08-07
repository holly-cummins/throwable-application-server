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

import java.io.File;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.ibm.iotf.client.device.DeviceClient;

public class IoTDevice implements Runnable {

	private Properties options = new Properties();
	protected DeviceClient client;
	private int interval = 1;

	public IoTDevice(String configFilePath, int interval) throws Exception {
		this.interval = interval;
		this.options = DeviceClient.parsePropertiesFile(new File(configFilePath));
		this.client = new DeviceClient(this.options);
		// TODO an ugly place for this?
		client.setCommandCallback(new LightFlasher());
	}

	public IoTDevice() throws Exception {
		this.options.put("org", "quickstart");
		this.options.put("id", "noid");
		this.client = new DeviceClient(this.options);
		// TODO an ugly place for this?
		client.setCommandCallback(new LightFlasher());
	}

	public void run() {
		try {
			client.connect(3);
		} catch (MqttException e1) {
			e1.printStackTrace();
			return;
		}

		Shock shock = new Shock(client);
		shock.listen();

		System.out.println("Will send a message with sensor readings every " + interval + " seconds.");
		System.out.println("Check the Internet of Things Platform dashboard for this device to see recent readings.");
		// Send a set of readings every [interval] seconds
		while (true) {
			try {

				ArduinoData reading = ArduinoData.create();
				client.publishEvent("reading", reading);
				Thread.sleep(interval * 1000);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Carrying on.");
			}
		}

		// It would be nice to disconnect cleanly, but that's for another day
		// client.disconnect();

	}
}