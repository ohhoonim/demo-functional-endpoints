package dev.ohhoonim.system.accesscontrol.pap.activity.out;

import dev.ohhoonim.system.accesscontrol.pap.model.Policy;

public interface PolicySimulationPort {

    boolean runSandboxSimulation(Policy policy);
}