package dev.ohhoonim.system.accesscontrol.pdp.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionResult;

public record DecisionEvaluationTransitionResult(
        DecisionEvaluationStatus status,
        List<PostAction<DecisionEvaluation>> actions
) implements TransitionResult<DecisionEvaluationStatus, DecisionEvaluation> {
}