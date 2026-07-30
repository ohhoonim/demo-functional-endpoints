package dev.ohhoonim.system.accesscontrol.pip.model;

import dev.ohhoonim.component.model.state.Status;
import dev.ohhoonim.component.model.state.TransitionResult;

public sealed interface AttributeSourceStatus extends Status<AttributeSourceStatus, AttributeSourceTransitionEvent, AttributeSource>
        permits AttributeSourceStatus.Inactive,
                AttributeSourceStatus.Active,
                AttributeSourceStatus.SyncFailed {

    record Inactive() implements AttributeSourceStatus {
        @Override
        public TransitionResult<AttributeSourceStatus, AttributeSource> trigger(AttributeSourceTransitionEvent event) {
            return switch (event) {
                case AttributeSourceTransitionEvent.ActivateEvent e -> new AttributeSourceTransitionResult(new Active(), e.actions());
                default -> throw new PipException("비활성화(INACTIVE) 상태에서는 활성화(Activate)만 수행할 수 있습니다.");
            };
        }
    }

    record Active() implements AttributeSourceStatus {
        @Override
        public TransitionResult<AttributeSourceStatus, AttributeSource> trigger(AttributeSourceTransitionEvent event) {
            return switch (event) {
                case AttributeSourceTransitionEvent.DeactivateEvent e -> new AttributeSourceTransitionResult(new Inactive(), e.actions());
                case AttributeSourceTransitionEvent.SyncSuccessEvent e -> new AttributeSourceTransitionResult(new Active(), e.actions());
                case AttributeSourceTransitionEvent.SyncFailEvent e -> new AttributeSourceTransitionResult(new SyncFailed(), e.actions());
                default -> throw new PipException("유효하지 않은 전이입니다.");
            };
        }
    }

    record SyncFailed() implements AttributeSourceStatus {
        @Override
        public TransitionResult<AttributeSourceStatus, AttributeSource> trigger(AttributeSourceTransitionEvent event) {
            return switch (event) {
                case AttributeSourceTransitionEvent.ActivateEvent e -> new AttributeSourceTransitionResult(new Active(), e.actions());
                case AttributeSourceTransitionEvent.SyncSuccessEvent e -> new AttributeSourceTransitionResult(new Active(), e.actions());
                case AttributeSourceTransitionEvent.DeactivateEvent e -> new AttributeSourceTransitionResult(new Inactive(), e.actions());
                default -> throw new PipException("유효하지 않은 전이입니다.");
            };
        }
    }
}
