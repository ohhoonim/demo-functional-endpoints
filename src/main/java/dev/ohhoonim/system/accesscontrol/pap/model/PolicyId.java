package dev.ohhoonim.system.accesscontrol.pap.model;

import java.util.UUID;
import com.github.f4b6a3.ulid.UlidCreator;
import dev.ohhoonim.component.model.unit.EntityId;
import dev.ohhoonim.component.model.unit.EntityId.Creator;

public record PolicyId(UUID internalId, UUID externalId) implements EntityId<UUID> {

    public PolicyId {
        if (externalId == null) {
            throw new PolicyException("외부 식별자가 없습니다");
        }
    }

    public static Creator<UUID, PolicyId> Creator = new Creator<>() {
        @Override
        public PolicyId from(UUID internalId, UUID externalId) {
            if (internalId == null) throw new PolicyException("내부 식별자가 누락되었습니다");
            return new PolicyId(internalId, externalId);
        }

        @Override
        public PolicyId fromPublic(String publicId) {
            return new PolicyId(null, UUID.fromString(publicId));
        }

        @Override
        public PolicyId generate() {
            return new PolicyId(UlidCreator.getMonotonicUlid().toUuid(), UUID.randomUUID());
        }
    };

    @Override
    public UUID getRawValue() {
        if (internalId == null) {
            throw new PolicyException("내부 식별자가 확인되지 않은 ID입니다. Resolve가 필요합니다.");
        }
        return internalId;
    }

    @Override
    public String getPublicValue() {
        return externalId.toString();
    }
}