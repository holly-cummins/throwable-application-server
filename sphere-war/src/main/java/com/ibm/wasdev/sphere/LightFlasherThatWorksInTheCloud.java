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

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.wasdev.sphere.sensors.GpioPin;
import com.ibm.wasdev.sphere.sensors.LightFlasher;

public class LightFlasherThatWorksInTheCloud extends LightFlasher {
	private final PersistenceHelper service;
	/**
	 * A formatter for ISO 8601 compliant timestamps.
	 */
	protected final DateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public LightFlasherThatWorksInTheCloud(PersistenceHelper service) {
		this.service = service;
	}

	public void flash() {

		GpioPin pcDuino = getPcDuino();
		if (pcDuino.hasPins()) {
			// Flash once for 'local' flashes
			quickFlash(pcDuino, 100);
		} else {
			try {
				// send a command via MQTT

				// TODO THIS IS SO AWFUL

				// parse VCAP_SERVICES
				String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

				if (VCAP_SERVICES != null) {
					System.out.println("Flashing a light on the remote device.");
					JSONObject vcap = new JSONObject(VCAP_SERVICES);
					JSONArray jsonObject = vcap.getJSONArray("iotf-service");
					JSONObject credentials = jsonObject.getJSONObject(0).getJSONObject("credentials");
					String host = (String) credentials.get("mqtt_host");
					Integer port = (Integer) credentials.get("mqtt_u_port");
					String org = credentials.getString("org");
					String id = "a:" + org + ":" + MqttClient.generateClientId();
					String username = credentials.getString("apiKey");
					String password = credentials.getString("apiToken");
					String uri = "tcp://" + host + ":" + port;

					MqttClient client = new MqttClient(uri, id);
					MqttConnectOptions opts = new MqttConnectOptions();
					opts.setUserName(username);
					opts.setPassword(password.toCharArray());
					client.connect(opts);

					JSONObject payload = new JSONObject();

					String timestamp = ISO8601_DATE_FORMAT.format(new Date());
					payload.put("ts", timestamp);

					payload.put("d", new JSONObject());

					String event = "flash";

					List<Device> devices = service.findAllDevices();
					System.out.println("Publishing flash event to " + devices.size() + " devices.");
					for (Device device : devices) {
						System.out.println(
								"Publishing flash event to " + device.getType() + ":" + device.getDeviceId() + ".");

						String topic = "iot-2/type/" + device.getType() + "/id/" + device.getDeviceId() + "/cmd/"
								+ event + "/fmt/json";

						MqttMessage msg = new MqttMessage(payload.toString().getBytes(Charset.forName("UTF-8")));
						msg.setQos(0);
						msg.setRetained(false);

						client.publish(topic, msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
