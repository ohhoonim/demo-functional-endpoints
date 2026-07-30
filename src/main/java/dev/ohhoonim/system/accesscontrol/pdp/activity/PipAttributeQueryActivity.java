package dev.ohhoonim.system.accesscontrol.pdp.activity;

import dev.ohhoonim.system.accesscontrol.pdp.model.PdpComponent;

public interface PipAttributeQueryActivity {

    PdpComponent.SubjectContext enrichSubjectContext(PdpComponent.SubjectContext subjectContext);

    PdpComponent.ResourceContext enrichResourceContext(PdpComponent.ResourceContext resourceContext);
}
