package dev.ohhoonim.system.accesscontrol.pip.model;

import dev.ohhoonim.component.model.unit.ValueObject;

@ValueObject
public sealed interface PipComponent {

    record SourceInfo(
        String name,
        String sourceType,
        String description
    ) implements PipComponent {
        public SourceInfo {
            if (name == null || name.isBlank()) {
                throw new PipException("데이터 원천 이름은 필수입니다");
            }
            if (sourceType == null || sourceType.isBlank()) {
                throw new PipException("데이터 원천 타입(HR_DB, LDAP, API 등)은 필수입니다");
            }
        }
    }

    record SourceConnection(
        String endpoint,
        String timeoutMs,
        boolean isEncrypted
    ) implements PipComponent {
        public SourceConnection {
            if (endpoint == null || endpoint.isBlank()) {
                throw new PipException("연결 엔드포인트는 필수입니다");
            }
        }
    }

    record AttributeItem(
        String attributeKey,
        String attributeValue
    ) implements PipComponent {
        public AttributeItem {
            if (attributeKey == null || attributeKey.isBlank()) {
                throw new PipException("속성 키는 필수입니다");
            }
        }
    }
}
