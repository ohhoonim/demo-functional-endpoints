package dev.ohhoonim.system.accesscontrol.pdp.model;

import java.time.Instant;
import dev.ohhoonim.component.model.unit.BaseEntity;

public class DecisionEvaluation extends BaseEntity<DecisionEvaluationId> {

    private PdpComponent.SubjectContext subjectContext;
    private PdpComponent.ResourceContext resourceContext;
    private PdpComponent.ActionContext actionContext;
    private PdpComponent.EnvironmentContext environmentContext;
    private EvaluatedRuleCollection evaluatedRules;
    private String combiningAlgorithm;
    private PdpStatus status;

    protected DecisionEvaluation(DecisionEvaluationId id,
                                 PdpComponent.SubjectContext subjectContext,
                                 PdpComponent.ResourceContext resourceContext,
                                 PdpComponent.ActionContext actionContext,
                                 PdpComponent.EnvironmentContext environmentContext,
                                 String combiningAlgorithm,
                                 String operator) {
        super(id, operator);
        if (subjectContext == null) throw new PdpException("주체 컨텍스트가 누락되었습니다");
        if (resourceContext == null) throw new PdpException("자원 컨텍스트가 누락되었습니다");
        if (actionContext == null) throw new PdpException("행위 컨텍스트가 누락되었습니다");
        if (combiningAlgorithm == null || combiningAlgorithm.isBlank()) {
            throw new PdpException("결합 알고리즘 설정이 누락되었습니다");
        }

        this.subjectContext = subjectContext;
        this.resourceContext = resourceContext;
        this.actionContext = actionContext;
        this.environmentContext = environmentContext == null ? new PdpComponent.EnvironmentContext(null) : environmentContext;
        this.evaluatedRules = new EvaluatedRuleCollection(null);
        this.combiningAlgorithm = combiningAlgorithm;
        this.status = new PdpStatus.NotApplicable("평가 전 상태입니다");
    }

    private DecisionEvaluation(DecisionEvaluationId id,
                               PdpComponent.SubjectContext subjectContext,
                               PdpComponent.ResourceContext resourceContext,
                               PdpComponent.ActionContext actionContext,
                               PdpComponent.EnvironmentContext environmentContext,
                               EvaluatedRuleCollection evaluatedRules,
                               String combiningAlgorithm,
                               PdpStatus status,
                               Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        super(id, createdAt, createdBy, modifiedAt, modifiedBy);
        this.subjectContext = subjectContext;
        this.resourceContext = resourceContext;
        this.actionContext = actionContext;
        this.environmentContext = environmentContext;
        this.evaluatedRules = evaluatedRules;
        this.combiningAlgorithm = combiningAlgorithm;
        this.status = status;
    }

    public static DecisionEvaluation create(DecisionEvaluationId id,
                                            PdpComponent.SubjectContext subjectContext,
                                            PdpComponent.ResourceContext resourceContext,
                                            PdpComponent.ActionContext actionContext,
                                            PdpComponent.EnvironmentContext environmentContext,
                                            String combiningAlgorithm,
                                            String operator) {
        return new DecisionEvaluation(id, subjectContext, resourceContext, actionContext, environmentContext, combiningAlgorithm, operator);
    }

    public static DecisionEvaluation reconstitute(DecisionEvaluationId id,
                                                  PdpComponent.SubjectContext subjectContext,
                                                  PdpComponent.ResourceContext resourceContext,
                                                  PdpComponent.ActionContext actionContext,
                                                  PdpComponent.EnvironmentContext environmentContext,
                                                  EvaluatedRuleCollection evaluatedRules,
                                                  String combiningAlgorithm,
                                                  PdpStatus status,
                                                  Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        return new DecisionEvaluation(id, subjectContext, resourceContext, actionContext, environmentContext, evaluatedRules, combiningAlgorithm, status, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public void evaluate(EvaluatedRuleCollection rules, String operator) {
        if (rules == null || rules.rules().isEmpty()) {
            this.evaluatedRules = new EvaluatedRuleCollection(null);
            this.status = new PdpStatus.NotApplicable("적용 가능한 보안 정책 규칙이 없습니다");
            recordModification(operator);
            return;
        }

        this.evaluatedRules = rules;

        if ("Deny-Override".equalsIgnoreCase(combiningAlgorithm)) {
            applyDenyOverride(rules);
        } else {
            applyPermitOverride(rules);
        }

        recordModification(operator);
    }

    public void markIndeterminate(String cause, String operator) {
        this.status = new PdpStatus.Indeterminate(cause);
        recordModification(operator);
    }

    private void applyDenyOverride(EvaluatedRuleCollection rules) {
        boolean hasPermit = false;
        for (PdpComponent.EvaluatedRule rule : rules.rules()) {
            if (rule.isMatched()) {
                if ("Deny".equalsIgnoreCase(rule.effect())) {
                    this.status = new PdpStatus.Deny("Deny-Override 정책에 의해 거부되었습니다 (규칙 ID: " + rule.ruleId() + ")");
                    return;
                }
                if ("Permit".equalsIgnoreCase(rule.effect())) {
                    hasPermit = true;
                }
            }
        }

        if (hasPermit) {
            this.status = new PdpStatus.Permit("매칭된 정책 규칙에 따라 접근이 허용되었습니다");
        } else {
            this.status = new PdpStatus.NotApplicable("매칭되는 허용 규칙이 없습니다");
        }
    }

    private void applyPermitOverride(EvaluatedRuleCollection rules) {
        boolean hasDeny = false;
        for (PdpComponent.EvaluatedRule rule : rules.rules()) {
            if (rule.isMatched()) {
                if ("Permit".equalsIgnoreCase(rule.effect())) {
                    this.status = new PdpStatus.Permit("Permit-Override 정책에 의해 허용되었습니다 (규칙 ID: " + rule.ruleId() + ")");
                    return;
                }
                if ("Deny".equalsIgnoreCase(rule.effect())) {
                    hasDeny = true;
                }
            }
        }

        if (hasDeny) {
            this.status = new PdpStatus.Deny("매칭된 정책 규칙에 따라 접근이 거부되었습니다");
        } else {
            this.status = new PdpStatus.NotApplicable("매칭되는 규칙이 없습니다");
        }
    }

    public PdpComponent.SubjectContext getSubjectContext() {
        return subjectContext;
    }

    public PdpComponent.ResourceContext getResourceContext() {
        return resourceContext;
    }

    public PdpComponent.ActionContext getActionContext() {
        return actionContext;
    }

    public PdpComponent.EnvironmentContext getEnvironmentContext() {
        return environmentContext;
    }

    public EvaluatedRuleCollection getEvaluatedRules() {
        return evaluatedRules;
    }

    public String getCombiningAlgorithm() {
        return combiningAlgorithm;
    }

    public PdpStatus getStatus() {
        return status;
    }

    public Object touchModified(String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchModified'");
    }

    public Object updateResult(String combiningAlgorithm2, String decisionResult, String reason,
            String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateResult'");
    }
}