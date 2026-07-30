package dev.ohhoonim.system.accesscontrol.pip.activity.out;

import java.util.List;
import dev.ohhoonim.system.accesscontrol.pip.model.PipComponent;

public interface HrDbClientPort {

    List<PipComponent.AttributeItem> fetchHrAttributes(String endpoint);
}