package dev.ohhoonim.system.accesscontrol.pip.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionEvent;

public sealed interface AttributeSourceTransitionEvent extends TransitionEvent<AttributeSource>
        permits AttributeSourceTransitionEvent.ActivateEvent,
                AttributeSourceTransitionEvent.DeactivateEvent,
                AttributeSourceTransitionEvent.SyncSuccessEvent,
                AttributeSourceTransitionEvent.SyncFailEvent {

    record ActivateEvent(String operator) implements AttributeSourceTransitionEvent {
        @Override
        public List<PostAction<AttributeSource>> actions() {
            return List.of(source -> source.touchModified(operator));
        }
    }

    record DeactivateEvent(String operator) implements AttributeSourceTransitionEvent {
        @Override
        public List<PostAction<AttributeSource>> actions() {
            return List.of(source -> source.touchModified(operator));
        }
    }

    record SyncSuccessEvent(String resultMessage, String operator) implements AttributeSourceTransitionEvent {
        @Override
        public List<PostAction<AttributeSource>> actions() {
            return List.of(source -> source.updateSyncResult(resultMessage, operator));
        }
    }

    record SyncFailEvent(String errorMessage, String operator) implements AttributeSourceTransitionEvent {
        @Override
        public List<PostAction<AttributeSource>> actions() {
            return List.of(source -> source.updateSyncResult("FAIL: " + errorMessage, operator));
        }
    }
}