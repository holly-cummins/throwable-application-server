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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.wasdev.sphere.sensors.BounceMonitor;

public class MessageListener implements MqttCallback {

	private PersistenceHelper service;
	// We use the monitor here - even though that's a bit ugly - since it's
	// tracking the listeners
	private BounceMonitor bounceMonitor;

	public MessageListener(PersistenceHelper service, BounceMonitor bounceMonitor) {
		this.service = service;
		this.bounceMonitor = bounceMonitor;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Message received: " + topic);
		if (topic.contains("shock")) {
			System.out.println("notifying shock from mqtt");
			bounceMonitor.notifyListeners();
		}
		service.create(message);
		service.recordDevice(topic);
	}

	public void listen() throws Exception {

		// parse VCAP_SERVICES
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

		if (VCAP_SERVICES != null) {
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
			client.setCallback(this);
			client.subscribe("iot-2/type/+/id/+/evt/+/fmt/+");
		}

	}

}
