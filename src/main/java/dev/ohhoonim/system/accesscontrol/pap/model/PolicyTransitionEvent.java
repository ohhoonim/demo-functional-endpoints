package dev.ohhoonim.system.accesscontrol.pap.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionEvent;

public sealed interface PolicyTransitionEvent extends TransitionEvent<Policy>
        permits PolicyTransitionEvent.VerifyEvent,
                PolicyTransitionEvent.PublishEvent,
                PolicyTransitionEvent.ArchiveEvent {

    record VerifyEvent(String verificationResult, String operator) implements PolicyTransitionEvent {
        @Override
        public List<PostAction<Policy>> actions() {
            return List.of(policy -> policy.updateVerificationResult(verificationResult, operator));
        }
    }

    record PublishEvent(String operator) implements PolicyTransitionEvent {
        @Override
        public List<PostAction<Policy>> actions() {
            return List.of(policy -> policy.touchModified(operator));
        }
    }

    record ArchiveEvent(String operator) implements PolicyTransitionEvent {
        @Override
        public List<PostAction<Policy>> actions() {
            return List.of(policy -> policy.touchModified(operator));
        }
    }
}