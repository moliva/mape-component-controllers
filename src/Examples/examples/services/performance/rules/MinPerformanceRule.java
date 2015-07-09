package examples.services.performance.rules;

import org.objectweb.proactive.extensions.autonomic.controllers.analysis.Alarm;
import org.objectweb.proactive.extensions.autonomic.controllers.analysis.Rule;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;

import examples.services.performance.metrics.CrackerMetric;


public class MinPerformanceRule extends Rule {

	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_NAME = "min-performance-rule";

	private double minSPM;


	public MinPerformanceRule(double minSPM) {
		this.minSPM = minSPM;
		this.subscribeToMetric(CrackerMetric.DEFAULT_NAME);
	}
	
	@Override
	public Alarm check(MonitorController monitor) {
		// WARN: Do not recalculate the metric value. Since this rule is subscribed to the metric,
		// a recalculation would call this method again, falling in a infinite loop.
		if (minSPM > (Double) monitor.getMetricValue(CrackerMetric.DEFAULT_NAME).getValue()) {
			return Alarm.VIOLATION;
		}
		return null;
	}

}
