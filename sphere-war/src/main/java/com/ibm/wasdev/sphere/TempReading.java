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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 * A standard JPA entity, like in any other Java application.
 */
@Entity
public class TempReading implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private double temperature;

	private int lightLevel;

	private boolean isDark;

	private String reporter;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date time;

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String s) {
		this.reporter = s;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the value of lastName
	 * 
	 * @return the value of lastName
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Set the value of lastName
	 * 
	 * @param lastName
	 *            new value of lastName
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Get the value of firstName
	 * 
	 * @return the value of firstName
	 */
	public int getLightLevel() {
		return lightLevel;
	}

	/**
	 * Set the value of firstName
	 * 
	 * @param firstName
	 *            new value of firstName
	 */
	public void setLightLevel(int lightLevel) {
		this.lightLevel = lightLevel;
	}

	public boolean getIsDark() {
		return isDark;
	}

	public void setIsDark(Boolean isDark) {
		this.isDark = isDark;
	}

	public boolean isPersisted() {
		return id > 0;
	}

	public void setTime(Date timestamp) {
		this.time = timestamp;
	}

	public Date getTime() {
		return time;

	}

	public String getDisplayTime() {
		return new SimpleDateFormat("hh:mm:ss").format(time);
	}
}
