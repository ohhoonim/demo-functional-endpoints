package dev.ohhoonim.system.accesscontrol.pap.activity.out;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dev.ohhoonim.component.model.factory.ArFactory;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyComponent;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyException;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyId;

public interface PolicyArFactory extends ArFactory<Policy, PolicyId, PolicyComponent> {

    default List<Class<? extends PolicyComponent>> forDefault() {
        return List.of(PolicyComponent.PolicyInfo.class);
    }

    public static java.util.function.Function<ResultSet, ? extends PolicyComponent> wrap(PolicyArMapper mapper) {
        return rs -> {
            try {
                return mapper.map(rs);
            } catch (SQLException e) {
                throw new PolicyException("처리할 수 없는 컬럼이 존재합니다.", e);
            }
        };
    }
}