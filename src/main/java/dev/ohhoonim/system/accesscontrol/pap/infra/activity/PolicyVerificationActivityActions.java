package dev.ohhoonim.system.accesscontrol.pap.infra.activity;

import java.util.List;
import org.springframework.stereotype.Component;
import dev.ohhoonim.system.accesscontrol.pap.activity.PolicyVerificationActivity;
import dev.ohhoonim.system.accesscontrol.pap.activity.out.PolicySimulationPort;
import dev.ohhoonim.system.accesscontrol.pap.activity.out.PolicyStaticAnalysisPort;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;

@Component
public class PolicyVerificationActivityActions implements PolicyVerificationActivity {

    private final PolicyStaticAnalysisPort staticAnalysisPort;
    private final PolicySimulationPort simulationPort;

    public PolicyVerificationActivityActions(PolicyStaticAnalysisPort staticAnalysisPort,
                                             PolicySimulationPort simulationPort) {
        this.staticAnalysisPort = staticAnalysisPort;
        this.simulationPort = simulationPort;
    }

    @Override
    public String verifyPolicy(Policy policy) {
        List<String> staticIssues = staticAnalysisPort.analyzeSyntaxAndConflict(policy);
        if (!staticIssues.isEmpty()) {
            return "정적 분석 실패: " + String.join(", ", staticIssues);
        }

        boolean simulationSuccess = simulationPort.runSandboxSimulation(policy);
        if (!simulationSuccess) {
            return "샌드박스 모의 테스트 실패: 정책 수행 결과 불일치";
        }

        return "검증 성공: 정적 분석 및 샌드박스 시뮬레이션통과";
    }
}
