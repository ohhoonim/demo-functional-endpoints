package dev.ohhoonim.system.accesscontrol.pep.infra.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import dev.ohhoonim.system.accesscontrol.pep.activity.out.AccessEnforcementArFactory;
import dev.ohhoonim.system.accesscontrol.pep.activity.out.AccessEnforcementPersistencePort;
import dev.ohhoonim.system.accesscontrol.pep.model.AccessEnforcement;
import dev.ohhoonim.system.accesscontrol.pep.model.EnforcementId;
import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

@Repository
public class AccessEnforcementRepositoryAdapter implements AccessEnforcementPersistencePort {

    private final JdbcClient jdbcClient;
    private final AccessEnforcementArFactory factory;

    public AccessEnforcementRepositoryAdapter(JdbcClient jdbcClient, AccessEnforcementArFactory factory) {
        this.jdbcClient = jdbcClient;
        this.factory = factory;
    }

    @Override
    public AccessEnforcement save(AccessEnforcement accessEnforcement) {
        var sql = """
                insert into access_enforcements (
                    enforcement_id, external_id, request_uri, http_method, client_ip,
                    target_service_id, target_path, status, response_status, failure_reason,
                    created_at, created_by, modified_at, modified_by
                ) values (
                    :enforcementId, :externalId, :requestUri, :httpMethod, :clientIp,
                    :targetServiceId, :targetPath, :status, :responseStatus, :failureReason,
                    :createdAt, :createdBy, :modifiedAt, :modifiedBy
                )
                on conflict (enforcement_id) do update set
                    request_uri = :requestUri,
                    http_method = :httpMethod,
                    client_ip = :clientIp,
                    target_service_id = :targetServiceId,
                    target_path = :targetPath,
                    status = :status,
                    response_status = :responseStatus,
                    failure_reason = :failureReason,
                    modified_at = :modifiedAt,
                    modified_by = :modifiedBy
                """;

        jdbcClient.sql(sql)
                .param("enforcementId", accessEnforcement.getId().getPublicValue())
                // .param("externalId", accessEnforcement.getExternalId())
                // .param("requestUri", accessEnforcement.getRequest().requestUri())
                // .param("httpMethod", accessEnforcement.getRequest().httpMethod())
                // .param("clientIp", accessEnforcement.getRequest().clientIp())
                // .param("targetServiceId", accessEnforcement.getTarget().targetServiceId())
                // .param("targetPath", accessEnforcement.getTarget().targetPath())
                // .param("status", accessEnforcement.getStatus().status())
                // .param("responseStatus", accessEnforcement.getStatus().responseStatus())
                // .param("failureReason", accessEnforcement.getStatus().failureReason())
                // .param("createdAt", accessEnforcement.getAudit().createdAt())
                // .param("createdBy", accessEnforcement.getAudit().createdBy())
                // .param("modifiedAt", accessEnforcement.getAudit().modifiedAt())
                // .param("modifiedBy", accessEnforcement.getAudit().modifiedBy())
                .update();

        return accessEnforcement;
    }

    @Override
    public Optional<AccessEnforcement> findById(EnforcementId id) {
        var columns = factory.forDefault();
        var sql = """
                select %s from access_enforcements where enforcement_id = :enforcementId
                """.formatted(factory.resolveRequiredColumns(columns));

        return jdbcClient.sql(sql)
                .param("enforcementId", id.getPublicValue())
                .query(accessEnforcementMapper.apply(factory, columns))
                .optional();
    }

    private final java.util.function.BiFunction<AccessEnforcementArFactory, List<Class<? extends PepComponent>>, RowMapper<AccessEnforcement>> accessEnforcementMapper =
            (factory, columns) -> (rs, _) -> factory.reconsitute(
                    null, //new EnforcementId(rs.getObject("enforcement_id", UUID.class)),
                    columns,
                    rs
            );
}
