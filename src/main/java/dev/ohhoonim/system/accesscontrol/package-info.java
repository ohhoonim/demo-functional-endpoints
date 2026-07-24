@org.springframework.modulith.ApplicationModule(id = "system-access", displayName = "접근제어", 
    allowedDependencies = {"component::*", "user::*", "system-audit"})
package dev.ohhoonim.system.accesscontrol;
