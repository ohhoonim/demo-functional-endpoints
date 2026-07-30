package dev.ohhoonim.system.accesscontrol.pdp.model;

import java.util.List;

public record EvaluatedRuleCollection(
    List<PdpComponent.EvaluatedRule> rules
) {
    public EvaluatedRuleCollection {
        rules = rules == null ? List.of() : List.copyOf(rules);
    }
}