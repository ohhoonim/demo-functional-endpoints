package dev.ohhoonim.system.accesscontrol.pap.model;

import java.util.List;
import dev.ohhoonim.component.model.unit.ValueObject;

@ValueObject
public sealed interface PolicyComponent {

    record PolicyInfo(
        String name,
        String description,
        int version
    ) implements PolicyComponent {
        public PolicyInfo {
            if (name == null || name.isBlank()) {
                throw new PolicyException("정책 이름은 필수입니다");
            }
            if (version < 1) {
                throw new PolicyException("정책 버전을 올바르게 설정해야 합니다");
            }
        }

        public PolicyInfo nextVersion() {
            return new PolicyInfo(name, description, version + 1);
        }
    }

    record PolicyTarget(
        List<String> subjectAttributes,
        List<String> resourceAttributes,
        List<String> actionAttributes
    ) implements PolicyComponent {
        public PolicyTarget {
            subjectAttributes = subjectAttributes == null ? List.of() : List.copyOf(subjectAttributes);
            resourceAttributes = resourceAttributes == null ? List.of() : List.copyOf(resourceAttributes);
            actionAttributes = actionAttributes == null ? List.of() : List.copyOf(actionAttributes);
        }
    }

    record PolicyRule(
        String ruleId,
        String effect,
        String condition
    ) implements PolicyComponent {
        public PolicyRule {
            if (ruleId == null || ruleId.isBlank()) {
                throw new PolicyException("규칙 ID는 필수입니다");
            }
            if (effect == null || (!effect.equalsIgnoreCase("Permit") && !effect.equalsIgnoreCase("Deny"))) {
                throw new PolicyException("올바른 Effect(Permit/Deny)를 지정해야 합니다");
            }
        }
    }
}