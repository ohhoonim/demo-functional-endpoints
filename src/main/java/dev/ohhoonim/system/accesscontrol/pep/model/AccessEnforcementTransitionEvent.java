package dev.ohhoonim.system.accesscontrol.pep.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionEvent;

public sealed interface AccessEnforcementTransitionEvent extends TransitionEvent<AccessEnforcement>
        permits AccessEnforcementTransitionEvent.PermitAccessEvent,
                AccessEnforcementTransitionEvent.DenyAccessEvent,
                AccessEnforcementTransitionEvent.FailEnforcementEvent {

    record PermitAccessEvent(int responseStatus, String operator) implements AccessEnforcementTransitionEvent {
        @Override
        public List<PostAction<AccessEnforcement>> actions() {
            return List.of(enforcement -> enforcement.updateEnforcementResult(responseStatus, null, operator));
        }
    }

    record DenyAccessEvent(String reason, String operator) implements AccessEnforcementTransitionEvent {
        @Override
        public List<PostAction<AccessEnforcement>> actions() {
            return List.of(enforcement -> enforcement.updateEnforcementResult(403, reason, operator));
        }
    }

    record FailEnforcementEvent(int responseStatus, String failureReason, String operator) implements AccessEnforcementTransitionEvent {
        @Override
        public List<PostAction<AccessEnforcement>> actions() {
            return List.of(enforcement -> enforcement.updateEnforcementResult(responseStatus, failureReason, operator));
        }
    }
}