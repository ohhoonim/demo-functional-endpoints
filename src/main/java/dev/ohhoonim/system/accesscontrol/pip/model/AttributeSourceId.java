package dev.ohhoonim.system.accesscontrol.pip.model;

import java.util.UUID;
import com.github.f4b6a3.ulid.UlidCreator;
import dev.ohhoonim.component.model.unit.EntityId;
import dev.ohhoonim.component.model.unit.EntityId.Creator;

public record AttributeSourceId(UUID internalId, UUID externalId) implements EntityId<UUID> {

    public AttributeSourceId {
        if (externalId == null) {
            throw new PipException("외부 식별자가 없습니다");
        }
    }

    public static Creator<UUID, AttributeSourceId> Creator = new Creator<>() {
        @Override
        public AttributeSourceId from(UUID internalId, UUID externalId) {
            if (internalId == null) throw new PipException("내부 식별자가 누락되었습니다");
            return new AttributeSourceId(internalId, externalId);
        }

        @Override
        public AttributeSourceId fromPublic(String publicId) {
            return new AttributeSourceId(null, UUID.fromString(publicId));
        }

        @Override
        public AttributeSourceId generate() {
            return new AttributeSourceId(UlidCreator.getMonotonicUlid().toUuid(), UUID.randomUUID());
        }
    };

    @Override
    public UUID getRawValue() {
        if (internalId == null) {
            throw new PipException("내부 식별자가 확인되지 않은 ID입니다. Resolve가 필요합니다.");
        }
        return internalId;
    }

    @Override
    public String getPublicValue() {
        return externalId.toString();
    }
}