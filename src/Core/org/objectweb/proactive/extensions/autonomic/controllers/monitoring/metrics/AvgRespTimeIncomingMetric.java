/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.extensions.autonomic.controllers.monitoring.metrics;

import java.util.List;

import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.event.RemmosEventType;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.records.Condition;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.records.IncomingRequestRecord;

/**
 * Calculates the Average Response Time of all the requests that have been served by the component.
 * 
 * @author cruz
 *
 */

public class AvgRespTimeIncomingMetric extends Metric<Double> {

	private static final long serialVersionUID = 1L;

	private Double value;


	public AvgRespTimeIncomingMetric() {
		this.value = 0.0;
		this.subscribeTo(RemmosEventType.INCOMING_REQUEST_EVENT);
	}

	public Double calculate() {

		List<IncomingRequestRecord> recordList = null;
		recordList = recordStore.getIncomingRequestRecords(new Condition<IncomingRequestRecord>(){
			private static final long serialVersionUID = 1L;
			public boolean evaluate(IncomingRequestRecord irr) {
				return irr.isFinished();
			}
		});

		// and calculates the average
		double sum = 0.0;
		double nRecords = recordList.size();
		for(IncomingRequestRecord irr : recordList) {
			sum += (double)(irr.getReplyTime() - irr.getArrivalTime());
		}

		return (value = nRecords > 0 ? sum/nRecords : 0.0);
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

}


