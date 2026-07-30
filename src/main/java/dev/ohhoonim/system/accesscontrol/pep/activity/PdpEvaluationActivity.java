package dev.ohhoonim.system.accesscontrol.pep.activity;

import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

public interface PdpEvaluationActivity {

    boolean evaluateAccess(PepComponent.InterceptedRequest request);
}