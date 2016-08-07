/*******************************************************************************
 * Copyright (c) 2014 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.ibm.wasdev.sphere;

import java.io.IOException;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.ibm.wasdev.sphere.sensors.BounceListener;
import com.ibm.wasdev.sphere.sensors.BounceMonitor;

@ServerEndpoint(value = "/BounceEndpoint")
public class BounceEndpoint implements BounceListener {
	@Inject
	BounceMonitorHolder monitorHolder;

	int bounceCount = 0;

	private Session currentSession = null;

	// message prefix for the number of bounces.
	private final String bounceCountCommand = "/BOUNCECOUNT ";

	private long startTime = System.currentTimeMillis();

	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		try {
			// Store the WebSocket session for later use.
			currentSession = session;
			// We may also get notifications driven by an MQTT channel
			System.out.println("Registering " + monitorHolder);
			BounceMonitor monitor = monitorHolder.getMonitor();
			monitor.registerListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public void receiveMessage(String message) {
		// Unexpected, for this application
		System.out.println(this.getClass() + " received websocket message: " + message);
	}

	/**
	 * Send a message to all clients.
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		for (Session session : currentSession.getOpenSessions()) {
			try {
				if (session.isOpen()) {
					session.getBasicRemote().sendText(message);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {

		System.out.println("Closing endpoint.");
	}

	@OnError
	public void onError(Throwable t) {
		// no error processing will be done for this sample
		t.printStackTrace();
	}

	@Override
	public void notifyBounce() {
		bounceCount++;
		System.out.println(bounceCount + " bounces after " + (System.currentTimeMillis() - startTime) / 1000 + " s.");
		System.out.println("BOING" + bounceCount);
		sendMessage(bounceCountCommand + bounceCount);
		// TODO can we (and would we want to?) make the message get cleared?
		sendMessage("BOUNCE!    ");
	}
}