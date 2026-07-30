package dev.ohhoonim.system.accesscontrol.pdp.activity.out;

import dev.ohhoonim.system.accesscontrol.pdp.model.EvaluatedRuleCollection;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;

// PAP 정책 조회 포트
public interface PolicyProviderPort {

    EvaluatedRuleCollection loadApplicableRules(PdpComponent.SubjectContext subjectContext,
                                                 PdpComponent.ResourceContext resourceContext,
                                                 PdpComponent.ActionContext actionContext);

    String getCombiningAlgorithm();
}