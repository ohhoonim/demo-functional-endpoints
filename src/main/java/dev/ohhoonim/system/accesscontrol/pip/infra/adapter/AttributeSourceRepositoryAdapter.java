package dev.ohhoonim.system.accesscontrol.pip.infra.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import dev.ohhoonim.system.accesscontrol.pip.activity.out.AttributeSourceArFactory;
import dev.ohhoonim.system.accesscontrol.pip.activity.out.AttributeSourcePersistencePort;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSourceId;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;

@Repository
public class AttributeSourceRepositoryAdapter implements AttributeSourcePersistencePort {

    private final JdbcClient jdbcClient;
    private final AttributeSourceArFactory factory;

    public AttributeSourceRepositoryAdapter(JdbcClient jdbcClient, AttributeSourceArFactory factory) {
        this.jdbcClient = jdbcClient;
        this.factory = factory;
    }

    @Override
    public AttributeSource save(AttributeSource attributeSource) {
        var sql = """
                insert into attribute_sources (
                    attribute_source_id, external_id, name, source_type, endpoint, timeout_ms,
                    status, last_synced_at, last_sync_result,
                    created_at, created_by, modified_at, modified_by
                ) values (
                    :attributeSourceId, :externalId, :name, :sourceType, :endpoint, :timeoutMs,
                    :status, :lastSyncedAt, :lastSyncResult,
                    :createdAt, :createdBy, :modifiedAt, :modifiedBy
                )
                on conflict (attribute_source_id) do update set
                    name = :name,
                    source_type = :sourceType,
                    endpoint = :endpoint,
                    timeout_ms = :timeoutMs,
                    status = :status,
                    last_synced_at = :lastSyncedAt,
                    last_sync_result = :lastSyncResult,
                    modified_at = :modifiedAt,
                    modified_by = :modifiedBy
                """;

        jdbcClient.sql(sql)
                .param("attributeSourceId", attributeSource.getId().getPublicValue())
                // .param("externalId", attributeSource.getExternalId())
                // .param("name", attributeSource.getInfo().name())
                // .param("sourceType", attributeSource.getInfo().sourceType())
                // .param("endpoint", attributeSource.getConnection().endpoint())
                // .param("timeoutMs", attributeSource.getConnection().timeoutMs())
                // .param("status", attributeSource.getSyncStatus().status())
                // .param("lastSyncedAt", attributeSource.getSyncStatus().lastSyncedAt())
                // .param("lastSyncResult", attributeSource.getSyncStatus().lastSyncResult())
                // .param("createdAt", attributeSource.getAudit().createdAt())
                // .param("createdBy", attributeSource.getAudit().createdBy())
                // .param("modifiedAt", attributeSource.getAudit().modifiedAt())
                // .param("modifiedBy", attributeSource.getAudit().modifiedBy())
                .update();

        return attributeSource;
    }

    @Override
    public Optional<AttributeSource> findById(AttributeSourceId id) {
        var columns = factory.forDefault();
        var sql = """
                select %s from attribute_sources where attribute_source_id = :attributeSourceId
                """.formatted(factory.resolveRequiredColumns(columns));

        return jdbcClient.sql(sql)
                .param("attributeSourceId", id.getPublicValue())
                .query(attributeSourceMapper.apply(factory, columns))
                .optional();
    }

    private final java.util.function.BiFunction<AttributeSourceArFactory, List<Class<? extends PipComponent>>, RowMapper<AttributeSource>> attributeSourceMapper =
            (factory, columns) -> (rs, _) -> factory.reconsitute(
                    null, //new AttributeSourceId(rs.getObject("attribute_source_id", UUID.class)),
                    columns,
                    rs
            );
}
