package dev.ohhoonim.system.accesscontrol.pap.activity.out;

import java.util.Optional;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyId;

public interface PolicyPersistencePort {

    Policy save(Policy policy);

    Optional<Policy> findById(PolicyId id);
}
