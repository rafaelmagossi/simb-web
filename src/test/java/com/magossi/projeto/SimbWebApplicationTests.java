package com.magossi.projeto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.magossi.simbweb.SimbWebApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimbWebApplication.class)
@WebAppConfiguration
public class SimbWebApplicationTests {

	@Test
	public void contextLoads() {
	}

}
