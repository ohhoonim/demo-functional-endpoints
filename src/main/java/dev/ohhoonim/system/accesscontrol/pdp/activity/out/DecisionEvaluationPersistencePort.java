package dev.ohhoonim.system.accesscontrol.pdp.activity.out;

import java.util.Optional;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluation;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluationId;

public interface DecisionEvaluationPersistencePort {

    DecisionEvaluation save(DecisionEvaluation decisionEvaluation);

    Optional<DecisionEvaluation> findById(DecisionEvaluationId id);
}