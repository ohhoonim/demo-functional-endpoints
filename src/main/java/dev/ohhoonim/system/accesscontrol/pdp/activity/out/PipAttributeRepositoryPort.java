package dev.ohhoonim.system.accesscontrol.pdp.activity.out;

import java.util.Map;

public interface PipAttributeRepositoryPort {

    Map<String, String> findSubjectAttributes(String subjectId);

    Map<String, String> findResourceAttributes(String resourceId);
}