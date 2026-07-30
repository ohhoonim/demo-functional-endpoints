package dev.ohhoonim.system.accesscontrol.pap.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionResult;

public record PolicyTransitionResult(
        PolicyStatus status,
        List<PostAction<Policy>> actions
) implements TransitionResult<PolicyStatus, Policy> {
}