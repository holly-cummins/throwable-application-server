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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import com.ibm.wasdev.sphere.sensors.BounceMonitor;
import com.ibm.wasdev.sphere.sensors.LightFlasher;

@Singleton
public class BounceMonitorHolder {
	BounceMonitor monitor;

	@PostConstruct
	public void go() {
		monitor = new BounceMonitor();
		monitor.monitorSensor(new LightFlasher());

	}

	@PreDestroy
	public void tearDown() {
		// If we clear the listeners here, any code change causes bounce
		// notifications to no longer be received
		// TODO even if we don't, that's the case
		// monitor.clearListeners();
	}

	public BounceMonitor getMonitor() {
		return monitor;
	}
}
