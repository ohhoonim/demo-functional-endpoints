package dev.ohhoonim.system.accesscontrol.pip.infra.adapter;

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
import dev.ohhoonim.system.accesscontrol.pip.activity.out.AttributeSourceArFactory;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSourceId;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;

@Component("attributeSourceArFactory")
public class AttributeSourceArFactoryAdapter implements AttributeSourceArFactory {

    private final Map<Class<?>, java.util.function.Function<ResultSet, ? extends PipComponent>> registry = Map.of(
            PipComponent.SourceInfo.class, AttributeSourceArFactory.wrap(rs -> new PipComponent.SourceInfo(
                    rs.getString("name"),
                    rs.getString("source_type"),
                    rs.getString("source_type")
            ))
            // ,
            // PipComponent.SourceConnection.class, wrap(rs -> new PipComponent.SourceConnection(
            //         rs.getString("endpoint"),
            //         rs.getString("timeout_ms")
            // )),
            // PipComponent.SyncStatus.class, wrap(rs -> new PipComponent.SyncStatus(
            //         rs.getString("status"),
            //         rs.getObject("last_synced_at", Instant.class),
            //         rs.getString("last_sync_result")
            // ))
    );

    @Override
    public AttributeSource reconsitute(AttributeSourceId id, List<Class<? extends PipComponent>> requiredVos, ResultSet data) throws SQLException {
        Map<String, ? extends PipComponent> vos = composer(requiredVos, registry, data);

        return null;
        // return AttributeSource.reconstitute(
        //         id,
        //         data.getObject("external_id", UUID.class),
        //         PipComponent.narrow(vos.get("SourceInfo"), PipComponent.SourceInfo.class),
        //         PipComponent.narrow(vos.get("SourceConnection"), PipComponent.SourceConnection.class),
        //         PipComponent.narrow(vos.get("SyncStatus"), PipComponent.SyncStatus.class),
        //         data.getObject("created_at", Instant.class),
        //         data.getString("created_by"),
        //         data.getObject("modified_at", Instant.class),
        //         data.getString("modified_by")
        // );
    }

    @Override
    public String resolveRequiredColumns(List<Class<? extends PipComponent>> columnTypes) {
        List<String> defaultColumns = List.of(
                "attribute_source_id", "external_id", "created_at", "created_by", "modified_at", "modified_by"
        );

        return Stream.concat(defaultColumns.stream(), dynamicColumns(columnTypes).stream())
                .collect(Collectors.joining(", "));
    }

    @Override
    public <T extends PipComponent> T narrow(PipComponent component, Class<T> targetType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'narrow'");
    }

    @Override
    public Map<Class<?>, Function<ResultSet, ? extends PipComponent>> registry() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registry'");
    }
}