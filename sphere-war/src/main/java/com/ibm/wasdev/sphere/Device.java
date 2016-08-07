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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A standard JPA entity, like in any other Java application.
 */
@Entity
public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String type;

	private String deviceId;

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getType() {
		return type;
	}

	public void setType(String type2) {
		this.type = type2;
	}

	/**
	 * Get the value of firstName
	 * 
	 * @return the value of firstName
	 */
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String id2) {
		this.deviceId = id2;
	}

}
