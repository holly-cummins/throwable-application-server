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

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({ "/results" })
public class ResultsServlet extends SphereServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "spherepu")
	private EntityManager em;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setEnvironment(request);

		String query = "SELECT f FROM PollSubmission f";
		Query q = this.em.createQuery(query);

		@SuppressWarnings("unchecked")
		List<PollSubmission> list = q.getResultList();
		request.setAttribute("result", list);

		int hobbyists = 0;
		int pros = 0;

		for (PollSubmission o : list) {
			if (o.getReason().equals("fun")) {
				hobbyists++;
			} else {
				pros++;
			}
		}

		if (list.size() > 0) {

			request.setAttribute("summary", "So far, " + hobbyists + " of you are hobbyists, and " + pros
					+ " of you have a business application.");
		} else {
			request.setAttribute("summary", "No one has done the poll yet.");
		}
		request.getRequestDispatcher("Results.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}