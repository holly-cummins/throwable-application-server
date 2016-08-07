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

import com.ibm.iotf.client.device.Command;
import com.ibm.iotf.client.device.CommandCallback;

public class LightFlasher implements CommandCallback {
	int pin = 3;

	public void flash() {
		GpioPin thing = getPcDuino();
		if (thing.hasPins()) {
			// Flash twice in succession so we know it's the MQTT flasher
			quickFlash(thing, 50);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			quickFlash(thing, 50);
		}

	}

	protected GpioPin getPcDuino() {
		GpioPin thing = new GpioPin(pin);
		if (thing.hasPins()) {
			thing.setMode(GpioPin.OUTPUT);
		}
		return thing;
	}

	protected void quickFlash(GpioPin thing, int time) {
		thing.setHigh();

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thing.setLow();
	}

	@Override
	public void processCommand(Command cmd) {
		System.out.println("Flashing lights: " + cmd);
		flash();

	}

}
