package dev.ohhoonim.system.accesscontrol.pdp.model;

import dev.ohhoonim.component.model.state.Status;
import dev.ohhoonim.component.model.state.TransitionResult;

public sealed interface DecisionEvaluationStatus extends Status<DecisionEvaluationStatus, DecisionEvaluationTransitionEvent, DecisionEvaluation>
        permits DecisionEvaluationStatus.Pending,
                DecisionEvaluationStatus.Evaluated,
                DecisionEvaluationStatus.Error {

    record Pending() implements DecisionEvaluationStatus {
        @Override
        public TransitionResult<DecisionEvaluationStatus, DecisionEvaluation> trigger(DecisionEvaluationTransitionEvent event) {
            return switch (event) {
                case DecisionEvaluationTransitionEvent.CompleteEvaluationEvent e -> new DecisionEvaluationTransitionResult(new Evaluated(), e.actions());
                case DecisionEvaluationTransitionEvent.FailEvaluationEvent e -> new DecisionEvaluationTransitionResult(new Error(), e.actions());
                default -> throw new PdpException("PENDING 상태에서는 평가 완료(Complete) 또는 실패(Fail)만 가능합니다.");
            };
        }
    }

    record Evaluated() implements DecisionEvaluationStatus {
        @Override
        public TransitionResult<DecisionEvaluationStatus, DecisionEvaluation> trigger(DecisionEvaluationTransitionEvent event) {
            throw new PdpException("EVALUATED 상태는 최종 상태이므로 변경할 수 없습니다.");
        }
    }

    record Error() implements DecisionEvaluationStatus {
        @Override
        public TransitionResult<DecisionEvaluationStatus, DecisionEvaluation> trigger(DecisionEvaluationTransitionEvent event) {
            return switch (event) {
                case DecisionEvaluationTransitionEvent.StartEvaluationEvent e -> new DecisionEvaluationTransitionResult(new Pending(), e.actions());
                case DecisionEvaluationTransitionEvent.CompleteEvaluationEvent e -> new DecisionEvaluationTransitionResult(new Evaluated(), e.actions());
                default -> throw new PdpException("ERROR 상태에서는 재평가 시작(Start) 또는 완료(Complete) 전이만 가능합니다.");
            };
        }
    }
}