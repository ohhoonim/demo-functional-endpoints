package dev.ohhoonim.system.accesscontrol.pap.infra.adapter;

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
import dev.ohhoonim.system.accesscontrol.pap.activity.out.PolicyArFactory;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyComponent;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyId;

@Component("policyArFactory")
public class PolicyArFactoryAdapter implements PolicyArFactory {

    private final Map<Class<?>, java.util.function.Function<ResultSet, ? extends PolicyComponent>> registry = Map.of(
            PolicyComponent.PolicyInfo.class, PolicyArFactory.wrap(rs -> new PolicyComponent.PolicyInfo(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("status")
            ))
    );

    @Override
    public Policy reconsitute(PolicyId id, List<Class<? extends PolicyComponent>> requiredVos, ResultSet data) throws SQLException {
        Map<String, ? extends PolicyComponent> vos = composer(requiredVos, registry, data);

        return null;
        // return Policy.reconstitute(
        //         id,
        //         data.getObject("external_id", UUID.class),
        //         PolicyComponent.narrow(vos.get("PolicyInfo"), PolicyComponent.PolicyInfo.class),
        //         PolicyComponent.narrow(vos.get("PolicyContent"), PolicyComponent.PolicyContent.class),
        //         data.getObject("created_at", Instant.class),
        //         data.getString("created_by"),
        //         data.getObject("modified_at", Instant.class),
        //         data.getString("modified_by")
        // );
    }

    @Override
    public String resolveRequiredColumns(List<Class<? extends PolicyComponent>> columnTypes) {
        List<String> defaultColumns = List.of(
                "policy_id", "external_id", "created_at", "created_by", "modified_at", "modified_by"
        );

        return Stream.concat(defaultColumns.stream(), dynamicColumns(columnTypes).stream())
                .collect(Collectors.joining(", "));
    }

    @Override
    public <T extends PolicyComponent> T narrow(PolicyComponent component, Class<T> targetType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'narrow'");
    }

    @Override
    public Map<Class<?>, Function<ResultSet, ? extends PolicyComponent>> registry() {
        return registry;
    }
}