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
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton(name = "initializer")
public class Initializer {

	@Inject
	PersistenceHelper service;

	@Inject
	BounceMonitorHolder holder;

	@Inject
	public Initializer() {
	}

	// A business method we can call to trigger post-construct
	public void stuff() {
		// DO nothing
		System.out.println("I AM HERE ");
	}

	@PostConstruct
	public void go() {
		System.out.println("Listening to MQTT queue");
		try {
			new MessageListener(service, holder.getMonitor()).listen();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
