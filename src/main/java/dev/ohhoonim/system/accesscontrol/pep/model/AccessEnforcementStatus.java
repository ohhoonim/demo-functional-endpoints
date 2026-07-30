package dev.ohhoonim.system.accesscontrol.pep.model;

import dev.ohhoonim.component.model.state.Status;
import dev.ohhoonim.component.model.state.TransitionResult;

public sealed interface AccessEnforcementStatus extends Status<AccessEnforcementStatus, AccessEnforcementTransitionEvent, AccessEnforcement>
        permits AccessEnforcementStatus.Evaluating,
                AccessEnforcementStatus.Permitted,
                AccessEnforcementStatus.Denied,
                AccessEnforcementStatus.Failed {

    record Evaluating() implements AccessEnforcementStatus {
        @Override
        public TransitionResult<AccessEnforcementStatus, AccessEnforcement> trigger(AccessEnforcementTransitionEvent event) {
            return switch (event) {
                case AccessEnforcementTransitionEvent.PermitAccessEvent e -> new AccessEnforcementTransitionResult(new Permitted(), e.actions());
                case AccessEnforcementTransitionEvent.DenyAccessEvent e -> new AccessEnforcementTransitionResult(new Denied(), e.actions());
                case AccessEnforcementTransitionEvent.FailEnforcementEvent e -> new AccessEnforcementTransitionResult(new Failed(), e.actions());
            };
        }
    }

    record Permitted() implements AccessEnforcementStatus {
        @Override
        public TransitionResult<AccessEnforcementStatus, AccessEnforcement> trigger(AccessEnforcementTransitionEvent event) {
            throw new PepException("PERMITTED 상태는 최종 상태이므로 변경할 수 없습니다.");
        }
    }

    record Denied() implements AccessEnforcementStatus {
        @Override
        public TransitionResult<AccessEnforcementStatus, AccessEnforcement> trigger(AccessEnforcementTransitionEvent event) {
            throw new PepException("DENIED 상태는 최종 상태이므로 변경할 수 없습니다.");
        }
    }

    record Failed() implements AccessEnforcementStatus {
        @Override
        public TransitionResult<AccessEnforcementStatus, AccessEnforcement> trigger(AccessEnforcementTransitionEvent event) {
            throw new PepException("FAILED 상태는 최종 상태이므로 변경할 수 없습니다.");
        }
    }
}
