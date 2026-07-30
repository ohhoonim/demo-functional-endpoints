package dev.ohhoonim.system.accesscontrol.pdp.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.ohhoonim.system.accesscontrol.pdp.activity.PipAttributeQueryActivity;
import dev.ohhoonim.system.accesscontrol.pdp.activity.out.DecisionEvaluationPersistencePort;
import dev.ohhoonim.system.accesscontrol.pdp.activity.out.PolicyProviderPort;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluation;
import dev.ohhoonim.system.accesscontrol.pdp.model.DecisionEvaluationId;
import dev.ohhoonim.system.accesscontrol.pdp.model.EvaluatedRuleCollection;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;

@Service
public class PolicyDecisionService {

    private final DecisionEvaluationPersistencePort decisionEvaluationPersistencePort;
    private final PolicyProviderPort policyProviderPort;
    private final PipAttributeQueryActivity pipAttributeQueryActivity;

    public PolicyDecisionService(DecisionEvaluationPersistencePort decisionEvaluationPersistencePort,
                                 PolicyProviderPort policyProviderPort,
                                 PipAttributeQueryActivity pipAttributeQueryActivity) {
        this.decisionEvaluationPersistencePort = decisionEvaluationPersistencePort;
        this.policyProviderPort = policyProviderPort;
        this.pipAttributeQueryActivity = pipAttributeQueryActivity;
    }

    @Transactional
    public DecisionEvaluation evaluateAccessRequest(PdpComponent.SubjectContext subjectContext,
                                                    PdpComponent.ResourceContext resourceContext,
                                                    PdpComponent.ActionContext actionContext,
                                                    PdpComponent.EnvironmentContext environmentContext,
                                                    String operator) {
        PdpComponent.SubjectContext enrichedSubject = pipAttributeQueryActivity.enrichSubjectContext(subjectContext);
        PdpComponent.ResourceContext enrichedResource = pipAttributeQueryActivity.enrichResourceContext(resourceContext);

        String combiningAlgorithm = policyProviderPort.getCombiningAlgorithm();
        DecisionEvaluationId id = DecisionEvaluationId.Creator.generate();

        DecisionEvaluation evaluation = DecisionEvaluation.create(
                id,
                enrichedSubject,
                enrichedResource,
                actionContext,
                environmentContext,
                combiningAlgorithm,
                operator
        );

        try {
            EvaluatedRuleCollection rules = policyProviderPort.loadApplicableRules(enrichedSubject, enrichedResource, actionContext);
            evaluation.evaluate(rules, operator);
        } catch (Exception e) {
            evaluation.markIndeterminate("정책 평가 중 오류 발생: " + e.getMessage(), operator);
        }

        return decisionEvaluationPersistencePort.save(evaluation);
    }
}
