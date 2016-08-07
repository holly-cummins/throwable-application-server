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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Set the EMULATE_PINS system property to true on the command line to simulate
 * pins.
 *
 */

public class Pcduino {

	private static final Random RANDOM = new Random();

	private static boolean doneEmulationWarning = false;

	private int pin = 0;
	private String pinURI = "/proc/";

	public int getPin() {
		return pin;
	}

	public String getPinURI() {
		return pinURI;
	}

	public int analogRead(int sensorPin) throws IOException {
		if (isEmulationMode()) {
			return RANDOM.nextInt(1023);
		}
		String pinURI = this.pinURI + "adc" + sensorPin;
		FileReader fr = new FileReader(pinURI);
		BufferedReader br = new BufferedReader(fr);
		String string = br.readLine();
		fr.close();
		// Strip off the name of the pin
		int value = Integer.parseInt(string.substring(5));
		// The pcduino values seem to have a range 4x arduino, so do a crude
		// calibration
		int arduinoValue = value / 4;
		return arduinoValue;
	}

	/*
	 * TODO merge with GpioPin public boolean digitalRead(int sensorPin) throws
	 * IOException { String pinURI = this.pinURI + "adc" + sensorPin; FileReader
	 * fr = new FileReader(pinURI); BufferedReader br = new BufferedReader(fr);
	 * String string = br.readLine(); fr.close(); // TODO this may not work :)
	 * return Boolean.valueOf(string); }
	 */

	public boolean hasPins() {
		if (isEmulationMode()) {
			return true;
		} else {
			// Assume we'll have an analog pin 0
			String pinURI = this.pinURI + "adc0";
			File f = new File(pinURI);
			return f.exists();
		}

	}

	protected boolean isEmulationMode() {
		boolean isDebug = System.getProperty("EMULATE_PINS") != null;
		if (isDebug && !doneEmulationWarning) {
			System.out.println("WARNING: Running in emulation mode. All values are fake.");
			doneEmulationWarning = true;
		}
		return isDebug;
	}

}
