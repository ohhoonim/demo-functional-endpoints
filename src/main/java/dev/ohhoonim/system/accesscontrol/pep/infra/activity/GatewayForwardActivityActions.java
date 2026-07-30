package dev.ohhoonim.system.accesscontrol.pep.infra.activity;

import org.springframework.stereotype.Component;
import dev.ohhoonim.system.accesscontrol.pep.activity.GatewayForwardActivity;
import dev.ohhoonim.system.accesscontrol.pep.activity.out.GatewayRoutingPort;
import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

@Component
public class GatewayForwardActivityActions implements GatewayForwardActivity {

    private final GatewayRoutingPort gatewayRoutingPort;

    public GatewayForwardActivityActions(GatewayRoutingPort gatewayRoutingPort) {
        this.gatewayRoutingPort = gatewayRoutingPort;
    }

    @Override
    public PepComponent.EnforcementResult forwardToTarget(PepComponent.InterceptedRequest request,
                                                           PepComponent.GatewayTarget target) {
        return gatewayRoutingPort.routeToBackend(request, target);
    }

    @Override
    public PepComponent.EnforcementResult createDenyResponse(PepComponent.InterceptedRequest request) {
        return new PepComponent.EnforcementResult(
                403,
                "Access Denied by Policy Enforcement Point",
                0L
        );
    }
}