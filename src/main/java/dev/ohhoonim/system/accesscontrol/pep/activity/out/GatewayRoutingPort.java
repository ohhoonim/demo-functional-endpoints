package dev.ohhoonim.system.accesscontrol.pep.activity.out;

import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

public interface GatewayRoutingPort {

    PepComponent.EnforcementResult routeToBackend(PepComponent.InterceptedRequest request,
                                                  PepComponent.GatewayTarget target);
}