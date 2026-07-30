package dev.ohhoonim.system.accesscontrol.pap.model;

import java.time.Instant;
import java.util.Set;
import dev.ohhoonim.component.model.unit.BaseEntity;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyComponent.PolicyRule;
import io.jsonwebtoken.lang.Collections;

public class Policy extends BaseEntity<PolicyId> {

    private PolicyComponent.PolicyInfo info;
    private PolicyComponent.PolicyTarget target;
    private Set<PolicyRule> ruleSet;
    private PolicyStatus status;

    protected Policy(PolicyId id, PolicyComponent.PolicyInfo info, PolicyComponent.PolicyTarget target,
                     Set<PolicyRule> ruleSet, String operator) {
        super(id, operator);
        if (info == null) throw new PolicyException("정책 정보가 누락되었습니다");
        if (target == null) throw new PolicyException("정책 대상이 누락되었습니다");
        if (ruleSet == null) throw new PolicyException("정책 규칙 세트가 누락되었습니다");

        this.info = info;
        this.target = target;
        this.ruleSet = ruleSet;
        this.status = new PolicyStatus.Draft();
    }

    private Policy(PolicyId id, PolicyComponent.PolicyInfo info, PolicyComponent.PolicyTarget target,
                   Set<PolicyRule> ruleSet, PolicyStatus status,
                   Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        super(id, createdAt, createdBy, modifiedAt, modifiedBy);
        this.info = info;
        this.target = target;
        this.ruleSet = ruleSet;
        this.status = status;
    }

    public static Policy create(PolicyId id, PolicyComponent.PolicyInfo info, PolicyComponent.PolicyTarget target,
                                Set<PolicyRule> ruleSet, String operator) {
        return new Policy(id, info, target, ruleSet, operator);
    }

    public static Policy reconstitute(PolicyId id, PolicyComponent.PolicyInfo info, PolicyComponent.PolicyTarget target,
                                       Set<PolicyRule> ruleSet, PolicyStatus status,
                                       Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        return new Policy(id, info, target, ruleSet, status, createdAt, createdBy, modifiedAt, modifiedBy);
    }



    public void updateDraft(PolicyComponent.PolicyInfo newInfo, PolicyComponent.PolicyTarget newTarget,
                            Set<PolicyRule> newRuleSet, String operator) {
        if (!status.isDraft()) {
            throw new PolicyException("초안 상태에서만 정책을 수정할 수 있습니다");
        }
        this.info = newInfo;
        this.target = newTarget;
        this.ruleSet = newRuleSet;
        recordModification(operator);
    }

    public void verify(String verificationResult, String operator) {
        if (!status.isDraft()) {
            throw new PolicyException("초안 상태의 정책만 검증할 수 있습니다");
        }
        this.status = new PolicyStatus.Verified(verificationResult);
        recordModification(operator);
    }

    public void approve(String approver, String operator) {
        if (!status.isVerified()) {
            throw new PolicyException("검증 완료된 정책만 승인할 수 있습니다");
        }
        this.status = new PolicyStatus.Approved(approver);
        recordModification(operator);
    }

    public void reject(String operator) {
        if (!status.isVerified()) {
            throw new PolicyException("검증 상태의 정책만 반려할 수 있습니다");
        }
        this.status = new PolicyStatus.Draft();
        recordModification(operator);
    }

    public void deploy(String deploymentTarget, String operator) {
        if (!status.isApproved()) {
            throw new PolicyException("승인된 정책만 PDP에 배포할 수 있습니다");
        }
        this.info = this.info.nextVersion();
        this.status = new PolicyStatus.Deployed(deploymentTarget);
        recordModification(operator);
    }

    public PolicyComponent.PolicyInfo getInfo() {
        return info;
    }

    public PolicyComponent.PolicyTarget getTarget() {
        return target;
    }

    public Set<PolicyRule> getRuleSet() {
        return Collections.immutable(ruleSet);
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public Object updateVerificationResult(String verificationResult, String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateVerificationResult'");
    }

    public Object touchModified(String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'touchModified'");
    }
}