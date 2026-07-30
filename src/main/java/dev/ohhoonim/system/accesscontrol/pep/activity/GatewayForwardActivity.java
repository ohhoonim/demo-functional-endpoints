package dev.ohhoonim.system.accesscontrol.pep.activity;

import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

public interface GatewayForwardActivity {

    PepComponent.EnforcementResult forwardToTarget(PepComponent.InterceptedRequest request,
                                                    PepComponent.GatewayTarget target);

    PepComponent.EnforcementResult createDenyResponse(PepComponent.InterceptedRequest request);
}