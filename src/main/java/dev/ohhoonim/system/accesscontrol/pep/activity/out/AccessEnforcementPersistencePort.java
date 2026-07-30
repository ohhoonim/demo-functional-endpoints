package dev.ohhoonim.system.accesscontrol.pep.activity.out;

import java.util.Optional;
import dev.ohhoonim.system.accesscontrol.pep.model.AccessEnforcement;
import dev.ohhoonim.system.accesscontrol.pep.model.EnforcementId;

public interface AccessEnforcementPersistencePort {

    AccessEnforcement save(AccessEnforcement accessEnforcement);

    Optional<AccessEnforcement> findById(EnforcementId id);
}