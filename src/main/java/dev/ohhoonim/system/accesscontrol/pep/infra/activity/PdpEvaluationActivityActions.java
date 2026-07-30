package dev.ohhoonim.system.accesscontrol.pep.infra.activity;

import org.springframework.stereotype.Component;
import dev.ohhoonim.system.accesscontrol.pep.activity.PdpEvaluationActivity;
import dev.ohhoonim.system.accesscontrol.pep.activity.out.PdpRemoteClientPort;
import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

@Component
public class PdpEvaluationActivityActions implements PdpEvaluationActivity {

    private final PdpRemoteClientPort pdpRemoteClientPort;

    public PdpEvaluationActivityActions(PdpRemoteClientPort pdpRemoteClientPort) {
        this.pdpRemoteClientPort = pdpRemoteClientPort;
    }

    @Override
    public boolean evaluateAccess(PepComponent.InterceptedRequest request) {
        try {
            String decision = pdpRemoteClientPort.evaluate(request);
            return "PERMIT".equalsIgnoreCase(decision);
        } catch (Exception e) {
            return false;
        }
    }
}