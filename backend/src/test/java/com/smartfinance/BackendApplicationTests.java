package com.smartfinance;

import com.smartfinance.service.GroqService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest(classes = BackendApplication.class)
class BackendApplicationTests {
	@MockitoBean
	private GroqService groqService;

	@Test
	void contextLoads() {
	}

}
