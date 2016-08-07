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
/**
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

import java.io.IOException;

public class Light {

	// Pins 0, 1, and 4 all seem to be broken, which is a bit worrying.
	private static final int LIGHT_PIN = 2;

	public boolean isDark() throws IOException {
		double lightValue = lightValue();
		boolean isDark = lightValue > 600;
		return isDark;
	}

	public double lightValue() {
		try {
			Pcduino arduino = new Pcduino();

			if (arduino.hasPins()) {
				int lightValue = 0;

				// Value is higher when there is light
				lightValue = arduino.analogRead(LIGHT_PIN);
				return lightValue;

			} else {
				// TODO mqtt way
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// We don't have a sensor. Let's assume it's dark.
			return 0;
		}
	}
}
