package dev.ohhoonim.system.accesscontrol.pep.activity.out;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dev.ohhoonim.component.model.factory.ArFactory;
import dev.ohhoonim.system.accesscontrol.pep.model.AccessEnforcement;
import dev.ohhoonim.system.accesscontrol.pep.model.EnforcementId;
import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;
import dev.ohhoonim.system.accesscontrol.pep.model.PepException;

public interface AccessEnforcementArFactory extends ArFactory<AccessEnforcement, EnforcementId, PepComponent> {

    default List<Class<? extends PepComponent>> forDefault() {
        return List.of(PepComponent.InterceptedRequest.class, PepComponent.GatewayTarget.class);
    }

    public static java.util.function.Function<ResultSet, ? extends PepComponent> wrap(AccessEnforcementArMapper mapper) {
        return rs -> {
            try {
                return mapper.map(rs);
            } catch (SQLException e) {
                throw new PepException("처리할 수 없는 컬럼이 존재합니다.", e);
            }
        };
    }
}