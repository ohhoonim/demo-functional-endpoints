package dev.ohhoonim;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class DemoFunctionalEndpointsApplicationTests {

	@Test
	void contextLoads() {
		var modules = ApplicationModules.of(DemoFunctionalEndpointsApplication.class);

		for( var module : modules) {
			IO.println(module);
		}
	}

}
