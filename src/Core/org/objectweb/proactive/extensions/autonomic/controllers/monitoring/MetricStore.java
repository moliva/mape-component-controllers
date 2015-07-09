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
package org.objectweb.proactive.extensions.autonomic.controllers.monitoring;

import java.io.Serializable;
import java.util.HashSet;

import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.metrics.Metric;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;


/**
 * Interface for a component that stores and updates Metrics.
 * @author cruz
 *
 */
public interface MetricStore {

	// default metric store interface name
	public final static String ITF_NAME = "metric-store-nf";

	public Wrapper<String> getMetricState(String metricName);
	public Wrapper<Boolean> enableMetric(String metricName);
	public Wrapper<Boolean> disableMetric(String metricName);

	public Wrapper<Boolean> addMetric(String name, Metric<?> metric);
	
	public Wrapper<Boolean> removeMetric(String name);

	/**
	 * Updates the metric value using the arguments stored
	 * @param name
	 * @return
	 */
	public <T extends Serializable> Wrapper<T> calculate(String name);
	
	public <T extends Serializable> Wrapper<T> getValue(String name);
	
	void setValue(String name, Object v);
	
	public Wrapper<HashSet<String>> getMetricList();
	
	// EXTERNAL METRICS API: The same, but for an external monitor indicated by "itfPath"
	public <T extends Serializable> Wrapper<T> calculate(String name, String itfPath);
	public <T extends Serializable> Wrapper<T> getValue(String name, String itfPath);
	void setValue(String name, Object v, String itfPath);
	public Wrapper<HashSet<String>> getMetricList(String itfPath);

}
