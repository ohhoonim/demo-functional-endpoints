package dev.ohhoonim.system.accesscontrol.pap.activity.out;

import java.util.List;
import dev.ohhoonim.system.accesscontrol.pap.model.Policy;

public interface PolicyStaticAnalysisPort {

    List<String> analyzeSyntaxAndConflict(Policy policy);
}