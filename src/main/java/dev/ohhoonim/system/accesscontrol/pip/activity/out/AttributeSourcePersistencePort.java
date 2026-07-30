package dev.ohhoonim.system.accesscontrol.pip.activity.out;

import java.util.Optional;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSourceId;

public interface AttributeSourcePersistencePort {

    AttributeSource save(AttributeSource attributeSource);

    Optional<AttributeSource> findById(AttributeSourceId id);
}
