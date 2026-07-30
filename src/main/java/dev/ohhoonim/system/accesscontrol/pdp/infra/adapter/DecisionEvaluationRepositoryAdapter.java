package dev.ohhoonim.system.accesscontrol.pdp.infra.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import dev.ohhoonim.system.accesscontrol.pdp.activity.out.DecisionEvaluationArFactory;
import dev.ohhoonim.system.accesscontrol.pdp.activity.out.DecisionEvaluationPersistencePort;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluation;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluationId;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;

@Repository
public class DecisionEvaluationRepositoryAdapter implements DecisionEvaluationPersistencePort {

    private final JdbcClient jdbcClient;
    private final DecisionEvaluationArFactory factory;

    public DecisionEvaluationRepositoryAdapter(JdbcClient jdbcClient, DecisionEvaluationArFactory factory) {
        this.jdbcClient = jdbcClient;
        this.factory = factory;
    }

    @Override
    public DecisionEvaluation save(DecisionEvaluation decisionEvaluation) {
        var sql = """
                insert into decision_evaluations (
                    decision_evaluation_id, external_id, subject_id, resource_id, action,
                    combining_algorithm, decision_result, reason,
                    created_at, created_by, modified_at, modified_by
                ) values (
                    :decisionEvaluationId, :externalId, :subjectId, :resourceId, :action,
                    :combiningAlgorithm, :decisionResult, :reason,
                    :createdAt, :createdBy, :modifiedAt, :modifiedBy
                )
                on conflict (decision_evaluation_id) do update set
                    subject_id = :subjectId,
                    resource_id = :resourceId,
                    action = :action,
                    combining_algorithm = :combiningAlgorithm,
                    decision_result = :decisionResult,
                    reason = :reason,
                    modified_at = :modifiedAt,
                    modified_by = :modifiedBy
                """;

        jdbcClient.sql(sql)
                .param("decisionEvaluationId", decisionEvaluation.getId().getPublicValue())
                // .param("externalId", decisionEvaluation.getExternalId())
                // .param("subjectId", decisionEvaluation.getTarget().subjectId())
                // .param("resourceId", decisionEvaluation.getTarget().resourceId())
                // .param("action", decisionEvaluation.getTarget().action())
                // .param("combiningAlgorithm", decisionEvaluation.getResult().combiningAlgorithm())
                // .param("decisionResult", decisionEvaluation.getResult().decisionResult())
                // .param("reason", decisionEvaluation.getResult().reason())
                // .param("createdAt", decisionEvaluation.getAudit().createdAt())
                // .param("createdBy", decisionEvaluation.getAudit().createdBy())
                // .param("modifiedAt", decisionEvaluation.getAudit().modifiedAt())
                // .param("modifiedBy", decisionEvaluation.getAudit().modifiedBy())
                .update();

        return decisionEvaluation;
    }

    @Override
    public Optional<DecisionEvaluation> findById(DecisionEvaluationId id) {
        var columns = factory.forDefault();
        var sql = """
                select %s from decision_evaluations where decision_evaluation_id = :decisionEvaluationId
                """.formatted(factory.resolveRequiredColumns(columns));

        return jdbcClient.sql(sql)
                .param("decisionEvaluationId", id.getPublicValue())
                .query(decisionEvaluationMapper.apply(factory, columns))
                .optional();
    }

    private final java.util.function.BiFunction<DecisionEvaluationArFactory, List<Class<? extends PdpComponent>>, RowMapper<DecisionEvaluation>> decisionEvaluationMapper =
            (factory, columns) -> (rs, _) -> factory.reconsitute(
                    null, //new DecisionEvaluationId(rs.getObject("decision_evaluation_id", UUID.class)),
                    columns,
                    rs
            );
}