package dev.ohhoonim.system.accesscontrol.pip.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionResult;

public record AttributeSourceTransitionResult(
        AttributeSourceStatus status,
        List<PostAction<AttributeSource>> actions
) implements TransitionResult<AttributeSourceStatus, AttributeSource> {
}
