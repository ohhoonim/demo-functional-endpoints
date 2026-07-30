package dev.ohhoonim.system.accesscontrol.pip.infra.activity;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import dev.ohhoonim.system.accesscontrol.pip.activity.AttributeFetchActivity;
import dev.ohhoonim.system.accesscontrol.pip.activity.out.ExternalApiClientPort;
import dev.ohhoonim.system.accesscontrol.pip.activity.out.HrDbClientPort;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeValueCollection;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;
import dev.ohhoonim.system.accesscontrol.pip.model.PipException;

@Component
public class AttributeFetchActivityActions implements AttributeFetchActivity {

    private final HrDbClientPort hrDbClientPort;
    private final ExternalApiClientPort externalApiClientPort;

    public AttributeFetchActivityActions(HrDbClientPort hrDbClientPort,
                                         ExternalApiClientPort externalApiClientPort) {
        this.hrDbClientPort = hrDbClientPort;
        this.externalApiClientPort = externalApiClientPort;
    }

    @Override
    public AttributeValueCollection fetchAttributes(AttributeSource attributeSource) {
        PipComponent.SourceInfo info = attributeSource.getInfo();
        PipComponent.SourceConnection connection = attributeSource.getConnection();

        List<PipComponent.AttributeItem> fetchedItems = new ArrayList<>();

        if ("HR_DB".equalsIgnoreCase(info.sourceType())) {
            fetchedItems.addAll(hrDbClientPort.fetchHrAttributes(connection.endpoint()));
        } else if ("API".equalsIgnoreCase(info.sourceType()) || "LDAP".equalsIgnoreCase(info.sourceType())) {
            fetchedItems.addAll(externalApiClientPort.fetchApiAttributes(connection.endpoint(), connection.timeoutMs()));
        } else {
            throw new PipException("지원하지 않는 데이터 원천 타입입니다: " + info.sourceType());
        }

        return new AttributeValueCollection(fetchedItems);
    }
}