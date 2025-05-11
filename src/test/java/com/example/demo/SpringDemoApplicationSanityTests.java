package com.example.demo;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZoneId;

import static com.example.demo.TestAppConfig.FIXED_INSTANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
@AutoConfigureMockMvc
class SpringDemoApplicationSanityTests {
	@Autowired
	ManagementController managementController;
	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
		assertThat(managementController).isNotNull();
	}

	@Test
	void testManagementController() throws Exception {
		val fixedTime = FIXED_INSTANT.atZone(ZoneId.systemDefault()).toString();
		mockMvc.perform(MockMvcRequestBuilders.get("/management/aliveCheck"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.isAlive").value("true"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(fixedTime))
				.andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(fixedTime))
				.andExpect(MockMvcResultMatchers.jsonPath("$.upTime").isNotEmpty());
	}

}
