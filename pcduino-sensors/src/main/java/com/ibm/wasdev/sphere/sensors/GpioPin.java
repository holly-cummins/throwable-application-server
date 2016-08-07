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
import java.io.FileWriter;
import java.io.IOException;

public class GpioPin extends Pcduino {
	private String modeUri = "/sys/devices/virtual/misc/gpio/mode/";
	private String statusUri = "/sys/devices/virtual/misc/gpio/pin/";
	private int pin = 0;
	public static final String HIGH = "1", LOW = "0", INPUT = "0", OUTPUT = "1", INPUT_PU = "8";

	public GpioPin(int pin) {
		modeUri += "gpio" + pin;
		statusUri += "gpio" + pin;
		this.pin = pin;
	}

	public GpioPin(String pin) {
		modeUri += "gpio" + pin;
		statusUri += "gpio" + pin;
		this.pin = Integer.parseInt(pin);
	}

	public int getPin() {
		return pin;
	}

	public void setMode(String mode) {
		writeToFile(getModeUri(), mode);
	}

	public void set(String state) {
		writeToFile(getStatusUri(), state);
	}

	public void setHigh() {
		// set pin as high level
		writeToFile(getStatusUri(), HIGH);
	}

	public void setLow() {
		// set pin as low level
		writeToFile(getStatusUri(), LOW);
	}

	public void setModeInput() {

		writeToFile(getModeUri(), INPUT);
	}

	public void setModeOutput() {
		writeToFile(getModeUri(), OUTPUT);
	}

	public void setModeInputPullup() {
		writeToFile(getModeUri(), INPUT_PU);
	}

	private String getModeUri() {
		return modeUri;
	}

	private String getStatusUri() {
		return statusUri;
	}

	public String getPinMode() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(getModeUri()));
			String data = reader.readLine();
			reader.close();
			return data;
		} catch (IOException e) {
		}
		return "";
	}

	public String getPinStatus() {
		if (isEmulationMode()) {
			// Bias the results towards 1
			if (Math.random() > 0.00000001) {
				return "1";
			} else {
				return "0";
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(getStatusUri()));
			String data = reader.readLine();
			reader.close();
			return data;
		} catch (IOException e) {
		}
		return "no pins";
	}

	private void writeToFile(String uri, String data) {
		if (hasPins() && !isEmulationMode()) {
			try {
				File file = new File(uri);
				file.delete();
				File newFile = new File(uri);
				newFile.createNewFile();
				FileWriter writer = new FileWriter(uri);
				writer.write(data);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
