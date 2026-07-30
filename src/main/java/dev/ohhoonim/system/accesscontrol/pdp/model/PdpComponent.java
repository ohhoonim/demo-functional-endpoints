package dev.ohhoonim.system.accesscontrol.pdp.model;

import java.util.Map;

public sealed interface PdpComponent {

    record SubjectContext(
        String subjectId,
        Map<String, String> attributes
    ) implements PdpComponent {
        public SubjectContext {
            if (subjectId == null || subjectId.isBlank()) {
                throw new PdpException("주체 식별자는 필수입니다");
            }
            attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        }
    }

    record ResourceContext(
        String resourceId,
        Map<String, String> attributes
    ) implements PdpComponent {
        public ResourceContext {
            if (resourceId == null || resourceId.isBlank()) {
                throw new PdpException("자원 식별자는 필수입니다");
            }
            attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        }
    }

    record ActionContext(
        String actionName
    ) implements PdpComponent {
        public ActionContext {
            if (actionName == null || actionName.isBlank()) {
                throw new PdpException("행위 이름은 필수입니다");
            }
        }
    }

    record EnvironmentContext(
        Map<String, String> environmentAttributes
    ) implements PdpComponent {
        public EnvironmentContext {
            environmentAttributes = environmentAttributes == null ? Map.of() : Map.copyOf(environmentAttributes);
        }
    }

    record EvaluatedRule(
        String ruleId,
        String effect,
        boolean isMatched
    ) implements PdpComponent {
        public EvaluatedRule {
            if (ruleId == null || ruleId.isBlank()) {
                throw new PdpException("규칙 ID는 필수입니다");
            }
        }
    }
}