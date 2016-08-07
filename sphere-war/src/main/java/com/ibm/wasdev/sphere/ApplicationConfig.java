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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class ApplicationConfig extends Application {

	private Set<Class<?>> clazzes = null;

	@Override
	public Set<Class<?>> getClasses() {
		return getRestResourceClasses();
	}

	private Set<Class<?>> getRestResourceClasses() {
		if (clazzes == null) {
			synchronized (this) {
				clazzes = new HashSet<Class<?>>();
				clazzes.add(LatestReadingsRest.class);
				clazzes.add(TempResultsRest.class);
				clazzes.add(NameResultsRest.class);
			}
		}
		return clazzes;
	}

}
