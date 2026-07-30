package dev.ohhoonim.system.accesscontrol.pip.activity.out;

import java.util.List;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;

public interface ExternalApiClientPort {

    List<PipComponent.AttributeItem> fetchApiAttributes(String endpoint, String timeoutMs);
}
