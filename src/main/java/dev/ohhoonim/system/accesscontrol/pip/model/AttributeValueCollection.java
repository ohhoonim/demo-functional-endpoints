package dev.ohhoonim.system.accesscontrol.pip.model;

import java.util.List;

public record AttributeValueCollection(
    List<PipComponent.AttributeItem> items
) {
    public AttributeValueCollection {
        items = items == null ? List.of() : List.copyOf(items);
    }
}