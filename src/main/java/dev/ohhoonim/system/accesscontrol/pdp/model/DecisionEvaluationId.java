package dev.ohhoonim.system.accesscontrol.pdp.model;

import java.util.UUID;
import com.github.f4b6a3.ulid.UlidCreator;
import dev.ohhoonim.component.model.unit.EntityId;
import dev.ohhoonim.component.model.unit.EntityId.Creator;

public record DecisionEvaluationId(UUID internalId, UUID externalId) implements EntityId<UUID>{

    public DecisionEvaluationId {
        if (externalId == null) {
            throw new PdpException("외부 식별자가 없습니다");
        }
    }

    public static Creator<UUID, DecisionEvaluationId> Creator = new Creator<>() {
        @Override
        public DecisionEvaluationId from(UUID internalId, UUID externalId) {
            if (internalId == null) throw new PdpException("내부 식별자가 누락되었습니다");
            return new DecisionEvaluationId(internalId, externalId);
        }

        @Override
        public DecisionEvaluationId fromPublic(String publicId) {
            return new DecisionEvaluationId(null, UUID.fromString(publicId));
        }

        @Override
        public DecisionEvaluationId generate() {
            return new DecisionEvaluationId(UlidCreator.getMonotonicUlid().toUuid(), UUID.randomUUID());
        }
    };

    @Override
    public UUID getRawValue() {
        if (internalId == null) {
            throw new PdpException("내부 식별자가 확인되지 않은 ID입니다. Resolve가 필요합니다.");
        }
        return internalId;
    }

    @Override
    public String getPublicValue() {
        return externalId.toString();
    }
}