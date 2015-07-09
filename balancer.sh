#!/bin/bash
USER=/user/moliva/home
REPOS=$USER/repos
export JCP="$REPOS/mape-component-controllers/build/*"
JCP="$JCP:$REPOS/mape-component-controllers/lib/*"
JCP="$JCP:$REPOS/gcmscript/dist/lib/*"
JCP="$JCP:$REPOS/programming-multiactivities/dist/lib/*"

export VM="-Dgcm.provider=org.objectweb.proactive.core.component.Fractive"
VM="$VM -Dfractal.provider=org.objectweb.proactive.core.component.Fractive"
VM="$VM -Djava.security.manager"
VM="$VM -Djava.security.policy=$REPOS/programming-multiactivities/dist/proactive.java.policy"
VM="$VM -Dlog4j.configuration=file:$REPOS/programming-multiactivities/dist/proactive-log4j"
VM="$VM -Duser.home=$REPOS"
VM="$VM -Dproactive.home=$REPOS/programming-multiactivities"
VM="$VM -Djline.terminal=jline.UnsupportedTerminal"

export TEST=cl.niclabs.autonomic.examples.balancer.Test

java -cp $JCP $VM $TEST "$@"
