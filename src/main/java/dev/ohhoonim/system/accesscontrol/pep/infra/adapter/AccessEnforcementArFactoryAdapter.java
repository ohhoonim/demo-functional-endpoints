package dev.ohhoonim.system.accesscontrol.pep.infra.adapter;

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
import dev.ohhoonim.system.accesscontrol.pep.activity.out.AccessEnforcementArFactory;
import dev.ohhoonim.system.accesscontrol.pep.model.AccessEnforcement;
import dev.ohhoonim.system.accesscontrol.pep.model.EnforcementId;
import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;

@Component("accessEnforcementArFactory")
public class AccessEnforcementArFactoryAdapter implements AccessEnforcementArFactory {

    private final Map<Class<?>, java.util.function.Function<ResultSet, ? extends PepComponent>> registry = Map.of(
            // PepComponent.InterceptedRequest.class, wrap(rs -> new PepComponent.InterceptedRequest(
            //         rs.getString("request_uri"),
            //         rs.getString("http_method"),
            //         rs.getString("client_ip")
            // )),
            // PepComponent.GatewayTarget.class, wrap(rs -> new PepComponent.GatewayTarget(
            //         rs.getString("target_service_id"),
            //         rs.getString("target_path")
            // )),
            // PepComponent.EnforcementStatus.class, wrap(rs -> new PepComponent.EnforcementStatus(
            //         rs.getString("status"),
            //         rs.getObject("response_status", Integer.class),
            //         rs.getString("failure_reason")
            // ))
    );

    @Override
    public AccessEnforcement reconsitute(EnforcementId id, List<Class<? extends PepComponent>> requiredVos, ResultSet data) throws SQLException {
        Map<String, ? extends PepComponent> vos = composer(requiredVos, registry, data);

        return null;
        // return AccessEnforcement.reconstitute(
        //         id,
        //         data.getObject("external_id", UUID.class),
        //         PepComponent.narrow(vos.get("InterceptedRequest"), PepComponent.InterceptedRequest.class),
        //         PepComponent.narrow(vos.get("GatewayTarget"), PepComponent.GatewayTarget.class),
        //         PepComponent.narrow(vos.get("EnforcementStatus"), PepComponent.EnforcementStatus.class),
        //         data.getObject("created_at", Instant.class),
        //         data.getString("created_by"),
        //         data.getObject("modified_at", Instant.class),
        //         data.getString("modified_by")
        // );
    }

    @Override
    public String resolveRequiredColumns(List<Class<? extends PepComponent>> columnTypes) {
        List<String> defaultColumns = List.of(
                "enforcement_id", "external_id", "created_at", "created_by", "modified_at", "modified_by"
        );

        return Stream.concat(defaultColumns.stream(), dynamicColumns(columnTypes).stream())
                .collect(Collectors.joining(", "));
    }

    @Override
    public <T extends PepComponent> T narrow(PepComponent component, Class<T> targetType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'narrow'");
    }

    @Override
    public Map<Class<?>, Function<ResultSet, ? extends PepComponent>> registry() {
        return registry;
    }
}