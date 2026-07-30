package dev.ohhoonim.system.accesscontrol.pdp.model;

import java.util.List;
import dev.ohhoonim.component.model.state.PostAction;
import dev.ohhoonim.component.model.state.TransitionEvent;

public sealed interface DecisionEvaluationTransitionEvent extends TransitionEvent<DecisionEvaluation>
        permits DecisionEvaluationTransitionEvent.StartEvaluationEvent,
                DecisionEvaluationTransitionEvent.CompleteEvaluationEvent,
                DecisionEvaluationTransitionEvent.FailEvaluationEvent {

    record StartEvaluationEvent(String operator) implements DecisionEvaluationTransitionEvent {
        @Override
        public List<PostAction<DecisionEvaluation>> actions() {
            return List.of(evaluation -> evaluation.touchModified(operator));
        }
    }

    record CompleteEvaluationEvent(String combiningAlgorithm, String decisionResult, String reason, String operator) implements DecisionEvaluationTransitionEvent {
        @Override
        public List<PostAction<DecisionEvaluation>> actions() {
            return List.of(evaluation -> evaluation.updateResult(combiningAlgorithm, decisionResult, reason, operator));
        }
    }

    record FailEvaluationEvent(String reason, String operator) implements DecisionEvaluationTransitionEvent {
        @Override
        public List<PostAction<DecisionEvaluation>> actions() {
            return List.of(evaluation -> evaluation.updateResult("UNKNOWN", "INDETERMINATE", reason, operator));
        }
    }
}