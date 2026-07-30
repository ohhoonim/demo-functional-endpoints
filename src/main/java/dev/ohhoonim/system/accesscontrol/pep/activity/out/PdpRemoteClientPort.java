package dev.ohhoonim.system.accesscontrol.pep.activity.out;

import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

public interface PdpRemoteClientPort {

    String evaluate(PepComponent.InterceptedRequest request);
}
