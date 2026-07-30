package dev.ohhoonim.system.accesscontrol.pdp.activity.out;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dev.ohhoonim.component.model.factory.ArFactory;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluation;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluationId;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpException;

public interface DecisionEvaluationArFactory extends ArFactory<DecisionEvaluation, DecisionEvaluationId, PdpComponent> {

    default List<Class<? extends PdpComponent>> forDefault() {
        return null;
        // return List.of(PdpComponent.EvaluationTarget.class, PdpComponent.EvaluationResult.class);
    }

    public static java.util.function.Function<ResultSet, ? extends PdpComponent> wrap(DecisionEvaluationArMapper mapper) {
        return rs -> {
            try {
                return mapper.map(rs);
            } catch (SQLException e) {
                throw new PdpException("처리할 수 없는 컬럼이 존재합니다.", e);
            }
        };
    }
}
