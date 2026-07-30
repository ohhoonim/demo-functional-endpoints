package dev.ohhoonim.system.accesscontrol.pdp.infra.activity;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import dev.ohhoonim.system.accesscontrol.pdp.activity.PipAttributeQueryActivity;
import dev.ohhoonim.system.accesscontrol.pdp.activity.out.PipAttributeRepositoryPort;
import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;

@Component
public class PipAttributeQueryActivityActions implements PipAttributeQueryActivity {

    private final PipAttributeRepositoryPort pipAttributeRepositoryPort;

    public PipAttributeQueryActivityActions(PipAttributeRepositoryPort pipAttributeRepositoryPort) {
        this.pipAttributeRepositoryPort = pipAttributeRepositoryPort;
    }

    @Override
    public PdpComponent.SubjectContext enrichSubjectContext(PdpComponent.SubjectContext subjectContext) {
        Map<String, String> existingAttributes = subjectContext.attributes();
        Map<String, String> pipAttributes = pipAttributeRepositoryPort.findSubjectAttributes(subjectContext.subjectId());

        Map<String, String> mergedAttributes = new HashMap<>(existingAttributes);
        if (pipAttributes != null) {
            mergedAttributes.putAll(pipAttributes);
        }

        return new PdpComponent.SubjectContext(subjectContext.subjectId(), mergedAttributes);
    }

    @Override
    public PdpComponent.ResourceContext enrichResourceContext(PdpComponent.ResourceContext resourceContext) {
        Map<String, String> existingAttributes = resourceContext.attributes();
        Map<String, String> pipAttributes = pipAttributeRepositoryPort.findResourceAttributes(resourceContext.resourceId());

        Map<String, String> mergedAttributes = new HashMap<>(existingAttributes);
        if (pipAttributes != null) {
            mergedAttributes.putAll(pipAttributes);
        }

        return new PdpComponent.ResourceContext(resourceContext.resourceId(), mergedAttributes);
    }
}