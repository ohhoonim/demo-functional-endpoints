package dev.ohhoonim.system.accesscontrol.pap.application;

import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.ohhoonim.system.accesscontrol.pap.activity.PolicyVerificationActivity;
import dev.ohhoonim.system.accesscontrol.pap.activity.out.PolicyPersistencePort;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyComponent;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyComponent.PolicyRule;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyException;
import dev.ohhoonim.system.accesscontrol.pap.model.PolicyId;

@Service
public class PolicyManagementService {

    private final PolicyPersistencePort policyPersistencePort;
    private final PolicyVerificationActivity policyVerificationActivity;

    public PolicyManagementService(PolicyPersistencePort policyPersistencePort,
                                   PolicyVerificationActivity policyVerificationActivity) {
        this.policyPersistencePort = policyPersistencePort;
        this.policyVerificationActivity = policyVerificationActivity;
    }

    @Transactional
    public PolicyId createPolicyDraft(PolicyComponent.PolicyInfo info,
                                      PolicyComponent.PolicyTarget target,
                                      Set<PolicyRule> ruleSet,
                                      String operator) {
        PolicyId id = PolicyId.Creator.generate();
        Policy policy = Policy.create(id, info, target, ruleSet, operator);

        Policy savedPolicy = policyPersistencePort.save(policy);
        return savedPolicy.getId();
    }

    @Transactional
    public void updatePolicyDraft(PolicyId id,
                                  PolicyComponent.PolicyInfo newInfo,
                                  PolicyComponent.PolicyTarget newTarget,
                                  Set<PolicyRule> newRuleSet,
                                  String operator) {
        Policy policy = findPolicyOrThrow(id);
        policy.updateDraft(newInfo, newTarget, newRuleSet, operator);

        policyPersistencePort.save(policy);
    }

    @Transactional
    public void verifyPolicy(PolicyId id, String operator) {
        Policy policy = findPolicyOrThrow(id);

        String verificationResult = policyVerificationActivity.verifyPolicy(policy);
        policy.verify(verificationResult, operator);

        policyPersistencePort.save(policy);
    }

    @Transactional
    public void approvePolicy(PolicyId id, String approver, String operator) {
        Policy policy = findPolicyOrThrow(id);
        policy.approve(approver, operator);

        policyPersistencePort.save(policy);
    }

    @Transactional
    public void rejectPolicy(PolicyId id, String operator) {
        Policy policy = findPolicyOrThrow(id);
        policy.reject(operator);

        policyPersistencePort.save(policy);
    }

    @Transactional
    public void deployPolicy(PolicyId id, String deploymentTarget, String operator) {
        Policy policy = findPolicyOrThrow(id);
        policy.deploy(deploymentTarget, operator);

        policyPersistencePort.save(policy);
    }

    private Policy findPolicyOrThrow(PolicyId id) {
        return policyPersistencePort.findById(id)
                .orElseThrow(() -> new PolicyException("존재하지 않는 정책입니다: " + id.getPublicValue()));
    }
}