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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Set the EMULATE_PINS system property to true on the command line to simulate
 * pins.
 *
 */
public class Launcher {

	public static class LauncherOptions {
		@Option(name = "-c", aliases = { "--config" }, usage = "The path to a configuration file for this device")
		public String configFilePath = null;

		@Option(name = "-i", aliases = { "--interval" }, usage = "The interval (in seconds) between messages")
		public int interval = 5;

		public LauncherOptions() {
		}
	}

	public static void main(String[] args) throws Exception {
		// Load custom logging properties file
		try {
			FileInputStream fis = new FileInputStream("logging.properties");
			LogManager.getLogManager().readConfiguration(fis);
		} catch (SecurityException e) {
		} catch (IOException e) {
		}

		LauncherOptions opts = new LauncherOptions();
		CmdLineParser parser = new CmdLineParser(opts);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			// Caused by wrong arguments
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
			System.exit(1);
		}

		// Start the device off on a thread
		final IoTDevice d;

		if (opts.configFilePath != null) {
			d = new IoTDevice(opts.configFilePath, opts.interval);
		} else {
			d = new IoTDevice();
		}
		d.run();

	}
}