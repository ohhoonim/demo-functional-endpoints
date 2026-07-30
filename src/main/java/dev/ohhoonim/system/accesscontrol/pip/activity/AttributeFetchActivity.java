package dev.ohhoonim.system.accesscontrol.pip.activity;

import dev.ohhoonim.system.accesscontrol.pip.model.AttributeSource;
import dev.ohhoonim.system.accesscontrol.pip.model.AttributeValueCollection;

public interface AttributeFetchActivity {

    AttributeValueCollection fetchAttributes(AttributeSource attributeSource);
}
