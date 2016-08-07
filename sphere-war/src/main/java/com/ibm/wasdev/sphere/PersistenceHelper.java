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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

@Stateless
@LocalBean
public class PersistenceHelper {

	private static final Pattern COMMAND_PATTERN = Pattern.compile("iot-2/type/(.+)/id/(.+)/evt/.+");

	// A limit for the number of readings
	private static final int MAX_RESULTS = 2000;

	@PersistenceContext(unitName = "spherepu")
	private EntityManager entityManager;

	public PersistenceHelper() {
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveOrPersist(TempReading entity) {
		try {
			if (entity.getId() > 0) {
				entityManager.merge(entity);
			} else {
				entityManager.persist(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveOrPersist(Device entity) {
		try {
			entityManager.merge(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(PollSubmission order) {
		entityManager.persist(order);

	}

	public void deleteEntity(TempReading entity) {
		if (entity.getId() > 0) {
			// reattach to remove
			entity = entityManager.merge(entity);
			entityManager.remove(entity);
		}
	}

	public List<TempReading> findAll() {
		return findAll(MAX_RESULTS);
	}

	public List<TempReading> findAll(int maxResults) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TempReading> cq = criteriaBuilder.createQuery(TempReading.class);
		Root<TempReading> root = cq.from(TempReading.class);
		cq.select(root);
		cq.orderBy(criteriaBuilder.desc(root.get("id")));
		return entityManager.createQuery(cq).setMaxResults(maxResults).getResultList();
	}

	public List<Device> findAllDevices() {
		CriteriaQuery<Device> cq = entityManager.getCriteriaBuilder().createQuery(Device.class);
		cq.select(cq.from(Device.class));
		return entityManager.createQuery(cq).getResultList();
	}

	public List<TempReading> findByName(String filter) {
		if (filter == null || filter.isEmpty()) {
			return findAll();
		}
		filter = filter.toLowerCase();
		return entityManager.createNamedQuery("TempReading.findByName", TempReading.class)
				.setParameter("filter", filter + "%").getResultList();
	}

	public void resetTestData() {
		entityManager.createQuery("DELETE FROM TempReading c WHERE c.id > 0").executeUpdate();
		entityManager.createQuery("DELETE FROM Device c").executeUpdate();
		entityManager.createQuery("DELETE FROM PollSubmission c").executeUpdate();

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(MqttMessage message) {
		System.out.println("Blip! " + message);
		TempReading c = parse(message);

		// Write to the database
		saveOrPersist(c);
	}

	public void recordReading(double temperature) {
		TempReading c = new TempReading();
		c.setTemperature(temperature);
		c.setTime(new Date());
		// Write to the database
		saveOrPersist(c);
	}

	private TempReading parse(MqttMessage message) {
		TempReading c = new TempReading();
		try {
			// message will be of the form
			// "d":{"myName":"myPi","cputemp":41.70,"cpuload":0.30,"sine":1.00}
			JSONObject parsedMessage = new JSONObject(message.toString());
			JSONObject fields = parsedMessage.getJSONObject("d");

			// Read data into the entity from the message
			c.setReporter(fields.getString("name"));
			double temperature = fields.getDouble("temperature");
			c.setTemperature(temperature);
			int rawLightLevel = (int) fields.getDouble("light");
			// Calibrate light data
			int lightLevel = (200000 - rawLightLevel) / 1000;
			c.setLightLevel(lightLevel);
			c.setTime(new Timestamp(System.currentTimeMillis()));
			c.setIsDark(lightLevel < 110);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return c;
	}

	public void recordDevice(String topic) {
		Matcher matcher = COMMAND_PATTERN.matcher(topic);
		if (matcher.matches()) {
			String type = matcher.group(1);
			String id = matcher.group(2);
			Device device = new Device();
			device.setType(type);
			device.setDeviceId(id);
			device.setId(type + ":" + id);
			saveOrPersist(device);
		}

	}

}
