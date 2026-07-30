package dev.ohhoonim.system.accesscontrol.pep.model;

import java.time.Instant;
import dev.ohhoonim.component.model.unit.BaseEntity;

public class AccessEnforcement extends BaseEntity<EnforcementId> {

    private PepComponent.InterceptedRequest request;
    private PepComponent.GatewayTarget target;
    private PepStatus status;

    protected AccessEnforcement(EnforcementId id, PepComponent.InterceptedRequest request,
                                PepComponent.GatewayTarget target, String operator) {
        super(id, operator);
        if (request == null) throw new PepException("가로챈 요청 정보가 누락되었습니다");
        if (target == null) throw new PepException("게이트웨이 대상 정보가 누락되었습니다");

        this.request = request;
        this.target = target;
        this.status = new PepStatus.Intercepted();
    }

    private AccessEnforcement(EnforcementId id, PepComponent.InterceptedRequest request,
                               PepComponent.GatewayTarget target, PepStatus status,
                               Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        super(id, createdAt, createdBy, modifiedAt, modifiedBy);
        this.request = request;
        this.target = target;
        this.status = status;
    }

    public static AccessEnforcement create(EnforcementId id, PepComponent.InterceptedRequest request,
                                           PepComponent.GatewayTarget target, String operator) {
        return new AccessEnforcement(id, request, target, operator);
    }

    public static AccessEnforcement reconstitute(EnforcementId id, PepComponent.InterceptedRequest request,
                                                 PepComponent.GatewayTarget target, PepStatus status,
                                                 Instant createdAt, String createdBy, Instant modifiedAt, String modifiedBy) {
        return new AccessEnforcement(id, request, target, status, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public void startEvaluation(String operator) {
        if (!status.isIntercepted()) {
            throw new PepException("요청 인터셉트 상태에서만 평가 상태로 전환할 수 있습니다");
        }
        this.status = new PepStatus.Evaluating();
        recordModification(operator);
    }

    public void enforcePermit(PepComponent.EnforcementResult result, String operator) {
        if (!status.isEvaluating()) {
            throw new PepException("평가 진행 중인 상태에서만 집행 결과를 반영할 수 있습니다");
        }
        this.status = new PepStatus.Permitted(result);
        recordModification(operator);
    }

    public void enforceDeny(PepComponent.EnforcementResult result, String operator) {
        if (!status.isEvaluating()) {
            throw new PepException("평가 진행 중인 상태에서만 집행 결과를 반영할 수 있습니다");
        }
        this.status = new PepStatus.Denied(result);
        recordModification(operator);
    }

    public void failEnforcement(String cause, String operator) {
        this.status = new PepStatus.Failed(cause);
        recordModification(operator);
    }

    public PepComponent.InterceptedRequest getRequest() {
        return request;
    }

    public PepComponent.GatewayTarget getTarget() {
        return target;
    }

    public PepStatus getStatus() {
        return status;
    }

    public Object updateEnforcementResult(int responseStatus, Object object, String operator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEnforcementResult'");
    }
}