package dev.ohhoonim.system.accesscontrol.pap.infra.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import dev.ohhoonim.system.accesscontrol.pap.activity.out.PolicyArFactory;
import dev.ohhoonim.system.accesscontrol.pap.activity.out.PolicyPersistencePort;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyComponent;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyId;

@Repository
public class PolicyRepositoryAdapter implements PolicyPersistencePort {

    private final JdbcClient jdbcClient;
    private final PolicyArFactory factory;

    public PolicyRepositoryAdapter(JdbcClient jdbcClient, PolicyArFactory factory) {
        this.jdbcClient = jdbcClient;
        this.factory = factory;
    }

    @Override
    public Policy save(Policy policy) {
        var sql = """
                insert into policies (
                    policy_id, external_id, name, description, status, content, version, verification_result,
                    created_at, created_by, modified_at, modified_by
                ) values (
                    :policyId, :externalId, :name, :description, :status, :content, :version, :verificationResult,
                    :createdAt, :createdBy, :modifiedAt, :modifiedBy
                )
                on conflict (policy_id) do update set
                    name = :name,
                    description = :description,
                    status = :status,
                    content = :content,
                    version = :version,
                    verification_result = :verificationResult,
                    modified_at = :modifiedAt,
                    modified_by = :modifiedBy
                """;

        jdbcClient.sql(sql)
                .param("policyId", policy.getId().getPublicValue())
                // .param("externalId", policy.getExternalId())
                // .param("name", policy.getInfo().name())
                // .param("description", policy.getInfo().description())
                // .param("status", policy.getInfo().status())
                // .param("content", policy.getContent().content())
                // .param("version", policy.getContent().version())
                // .param("verificationResult", policy.getContent().verificationResult())
                // .param("createdAt", policy.getAudit().createdAt())
                // .param("createdBy", policy.getAudit().createdBy())
                // .param("modifiedAt", policy.getAudit().modifiedAt())
                // .param("modifiedBy", policy.getAudit().modifiedBy())
                .update();

        return policy;
    }

    @Override
    public Optional<Policy> findById(PolicyId id) {
        var columns = factory.forDefault();
        var sql = """
                select %s from policies where policy_id = :policyId
                """.formatted(factory.resolveRequiredColumns(columns));

        return jdbcClient.sql(sql)
                .param("policyId", id.getPublicValue())
                .query(policyMapper.apply(factory, columns))
                .optional();
    }

    private final java.util.function.BiFunction<PolicyArFactory, List<Class<? extends PolicyComponent>>, RowMapper<Policy>> policyMapper =
            (factory, columns) -> (rs, _) -> factory.reconsitute(
                    null, //new PolicyId(rs.getObject("policy_id", UUID.class)),
                    columns,
                    rs
            );
}