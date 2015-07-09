package records;

import org.junit.Before;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import tests.components.ServiceA;
import tests.components.ServiceAImpl;

public class RecordStress {

	protected ServiceA service;
	protected MonitorController serviceMonitor;

	@Before
	public void setUp() throws Exception {
		Component boot = Utils.getBootstrapComponent();
		PAGCMTypeFactory patf = Utils.getPAGCMTypeFactory(boot);
		PAGenericFactory pagf = Utils.getPAGenericFactory(boot);

		Remmos remmos = new Remmos(patf, pagf);

		PAGCMInterfaceType itfType = (PAGCMInterfaceType) patf.createGCMItfType("service-a", ServiceA.class.getName(), PAGCMTypeFactory.SERVER,
				PAGCMTypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY);

		Component serviceAProvider = remmos.newFcInstance(
				remmos.createFcType(new PAGCMInterfaceType[] { itfType }, Constants.COMPOSITE),
				new ControllerDescription("ServiceA", Constants.COMPOSITE),
				null, null);

		Component serviceAImplementor = remmos.newFcInstance(
				remmos.createFcType(new PAGCMInterfaceType[] { itfType }, Constants.PRIMITIVE),
				new ControllerDescription("ServiceAImplementor", Constants.PRIMITIVE),
				new ContentDescription(ServiceAImpl.class.getName()),
				null);
		
		Utils.getPAMembraneController(serviceAProvider).startMembrane();
		Utils.getPAMembraneController(serviceAImplementor).startMembrane();
		Remmos.addMonitoring(serviceAProvider);
		Remmos.addMonitoring(serviceAImplementor);
		
		Utils.getPAContentController(serviceAProvider).addFcSubComponent(serviceAImplementor);
		Utils.getPABindingController(serviceAProvider).bindFc("service-a", serviceAImplementor.getFcInterface("service-a"));
		Utils.getPAGCMLifeCycleController(serviceAProvider).startFc();
	
		service = (ServiceA) serviceAProvider.getFcInterface("service-a");
		serviceMonitor = Remmos.getMonitorController(serviceAImplementor);
		serviceMonitor.startGCMMonitoring();
		Thread.sleep(1000);
	}

	public static void main(String[] args) throws Exception {
		final RecordStress ro = new RecordStress();
		ro.setUp();
		ro.serviceMonitor.addMetric("metric", new TestMetric());
		ro.serviceMonitor.setRecordStoreCapacity(16);
	
		(new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					ro.service.foo();
				}
			}
			
		})).start();

		while (true) {
			long time = System.currentTimeMillis();
			//System.out.println ( Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
			
		
			if (ro.serviceMonitor.calculateMetric("metric").isValid()) {
				System.out.println (System.currentTimeMillis() - time);
			} else {
				System.out.println ("FAIL");
			}
			Thread.sleep(100);
		}
	}
}
