package dev.ohhoonim.system.accesscontrol.pip.activity.out;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dev.ohhoonim.component.model.factory.ArFactory;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSourceId;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;
import dev.ohhoonim.system.accesscontrol.pip.model.PipException;

public interface AttributeSourceArFactory extends ArFactory<AttributeSource, AttributeSourceId, PipComponent> {

    default List<Class<? extends PipComponent>> forDefault() {
        return List.of(PipComponent.SourceInfo.class, PipComponent.SourceConnection.class);
    }

    public static java.util.function.Function<ResultSet, ? extends PipComponent> wrap(AttributeSourceArMapper mapper) {
        return rs -> {
            try {
                return mapper.map(rs);
            } catch (SQLException e) {
                throw new PipException("처리할 수 없는 컬럼이 존재합니다.", e);
            }
        };
    }
}
