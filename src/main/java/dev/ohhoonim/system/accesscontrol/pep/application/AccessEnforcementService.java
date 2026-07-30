package dev.ohhoonim.system.accesscontrol.pep.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.ohhoonim.system.accesscontrol.pep.activity.GatewayForwardActivity;
import dev.ohhoonim.system.accesscontrol.pep.activity.PdpEvaluationActivity;
import dev.ohhoonim.system.accesscontrol.pep.activity.out.AccessEnforcementPersistencePort;
import dev.ohhoonim.system.accesscontrol.pep.model.AccessEnforcement;
import dev.ohhoonim.system.accesscontrol.pep.model.EnforcementId;
import dev.ohhoonim.system.accesscontrol.pep.model.PepComponent;
import dev.ohhoonim.system.accesscontrol.pep.model.PepException;

@Service
public class AccessEnforcementService {

    private final AccessEnforcementPersistencePort accessEnforcementPersistencePort;
    private final PdpEvaluationActivity pdpEvaluationActivity;
    private final GatewayForwardActivity gatewayForwardActivity;

    public AccessEnforcementService(AccessEnforcementPersistencePort accessEnforcementPersistencePort,
                                   PdpEvaluationActivity pdpEvaluationActivity,
                                   GatewayForwardActivity gatewayForwardActivity) {
        this.accessEnforcementPersistencePort = accessEnforcementPersistencePort;
        this.pdpEvaluationActivity = pdpEvaluationActivity;
        this.gatewayForwardActivity = gatewayForwardActivity;
    }

    @Transactional
    public PepComponent.EnforcementResult handleAccessRequest(PepComponent.InterceptedRequest request,
                                                              PepComponent.GatewayTarget target,
                                                              String operator) {
        EnforcementId id = EnforcementId.Creator.generate();
        AccessEnforcement enforcement = AccessEnforcement.create(id, request, target, operator);

        enforcement.startEvaluation(operator);
        accessEnforcementPersistencePort.save(enforcement);

        try {
            boolean isPermitted = pdpEvaluationActivity.evaluateAccess(request);

            if (isPermitted) {
                PepComponent.EnforcementResult result = gatewayForwardActivity.forwardToTarget(request, target);
                enforcement.enforcePermit(result, operator);
                accessEnforcementPersistencePort.save(enforcement);
                return result;
            } else {
                PepComponent.EnforcementResult result = gatewayForwardActivity.createDenyResponse(request);
                enforcement.enforceDeny(result, operator);
                accessEnforcementPersistencePort.save(enforcement);
                return result;
            }
        } catch (Exception e) {
            enforcement.failEnforcement("PEP 집행 중 오류 발생: " + e.getMessage(), operator);
            accessEnforcementPersistencePort.save(enforcement);
            throw new PepException("접근 제어 집행 실패");
        }
    }
}