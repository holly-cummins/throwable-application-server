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

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("tempdata")
public class TempResultsRest {

	@Inject
	private PersistenceHelper service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getGetTemperatureData() throws Exception {

		System.out.println("Reading REST temperature data");

		List<TempReading> tlist = service.findAll();

		StringBuffer answers = new StringBuffer();
		answers.append("{ \"date\": [");
		for (TempReading t : tlist) {
			answers.append(t.getTime().getTime());
			answers.append(",");

		}
		// Trim trailing comma
		answers.deleteCharAt(answers.length() - 1);

		answers.append("],\"temperature\": [");
		for (TempReading t : tlist) {
			// answers.append(t.getTime().getTime());
			answers.append(t.getTemperature());
			answers.append(",");
		}
		// Trim trailing comma
		answers.deleteCharAt(answers.length() - 1);

		answers.append("] }");

		// return "{\"data1\": [220, 240, 270, 250, 280], \"data2\": [180, 150,
		// 300, 70, 120], \"data3\": [200, 310, 150, 100, 180]}";
		return answers.toString();

	}
}