package dev.ohhoonim.system.accesscontrol.pep.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionResult;

public record AccessEnforcementTransitionResult(
        AccessEnforcementStatus status,
        List<PostAction<AccessEnforcement>> actions
) implements TransitionResult<AccessEnforcementStatus, AccessEnforcement> {
}