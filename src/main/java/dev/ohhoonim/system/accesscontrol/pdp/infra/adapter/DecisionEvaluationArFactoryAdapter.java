package dev.ohhoonim.system.accesscontrol.pdp.infra.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import dev.ohhoonim.system.accesscontrol.pdp.activity.out.DecisionEvaluationArFactory;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluation;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluationId;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;

@Component("decisionEvaluationArFactory")
public class DecisionEvaluationArFactoryAdapter implements DecisionEvaluationArFactory {

    private final Map<Class<?>, java.util.function.Function<ResultSet, ? extends PdpComponent>> registry = Map.of(
            // PdpComponent.EvaluationTarget.class, DecisionEvaluationArFactory.wrap(rs -> new PdpComponent.EvaluationTarget(
            //         rs.getString("subject_id"),
            //         rs.getString("resource_id"),
            //         rs.getString("action")
            // )),
            // PdpComponent.EvaluationResult.class, wrap(rs -> new PdpComponent.EvaluationResult(
            //         rs.getString("combining_algorithm"),
            //         rs.getString("decision_result"),
            //         rs.getString("reason")
            // ))
    );

    @Override
    public DecisionEvaluation reconsitute(DecisionEvaluationId id, List<Class<? extends PdpComponent>> requiredVos, ResultSet data) throws SQLException {
        Map<String, ? extends PdpComponent> vos = composer(requiredVos, registry, data);

        return null;
        // return DecisionEvaluation.reconstitute(
        //         id,
        //         data.getObject("external_id", UUID.class),
        //         PdpComponent.narrow(vos.get("EvaluationTarget"), PdpComponent.EvaluationTarget.class),
        //         PdpComponent.narrow(vos.get("EvaluationResult"), PdpComponent.EvaluationResult.class),
        //         data.getObject("created_at", Instant.class),
        //         data.getString("created_by"),
        //         data.getObject("modified_at", Instant.class),
        //         data.getString("modified_by")
        // );
    }

    @Override
    public String resolveRequiredColumns(List<Class<? extends PdpComponent>> columnTypes) {
        List<String> defaultColumns = List.of(
                "decision_evaluation_id", "external_id", "created_at", "created_by", "modified_at", "modified_by"
        );

        return Stream.concat(defaultColumns.stream(), dynamicColumns(columnTypes).stream())
                .collect(Collectors.joining(", "));
    }

    @Override
    public <T extends PdpComponent> T narrow(PdpComponent component, Class<T> targetType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'narrow'");
    }

    @Override
    public Map<Class<?>, Function<ResultSet, ? extends PdpComponent>> registry() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registry'");
    }
}