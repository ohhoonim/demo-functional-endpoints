package dev.ohhoonim.system.accesscontrol.pep.model;

import java.util.Map;
import dev.ohhoonim.component.model.unit.ValueObject;

@ValueObject
public sealed interface PepComponent {

    record InterceptedRequest(
        String clientIp,
        String requestUri,
        String httpMethod,
        Map<String, String> headers
    ) implements PepComponent {
        public InterceptedRequest {
            if (requestUri == null || requestUri.isBlank()) {
                throw new PepException("요청 URI는 필수입니다");
            }
            if (httpMethod == null || httpMethod.isBlank()) {
                throw new PepException("HTTP 메서드는 필수입니다");
            }
            headers = headers == null ? Map.of() : Map.copyOf(headers);
        }
    }

    record GatewayTarget(
        String targetServiceId,
        String destinationUrl
    ) implements PepComponent {
        public GatewayTarget {
            if (targetServiceId == null || targetServiceId.isBlank()) {
                throw new PepException("대상 서비스 식별자는 필수입니다");
            }
        }
    }

    record EnforcementResult(
        int httpStatusCode,
        String responseMessage,
        long latencyMs
    ) implements PepComponent {
        public EnforcementResult {
            if (latencyMs < 0) {
                throw new PepException("지연 시간은 음수일 수 없습니다");
            }
        }
    }
}