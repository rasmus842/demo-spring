package com.example.demo.management;

import com.example.demo.BaseTest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZoneId;

import static com.example.demo.TestAppConfig.FIXED_INSTANT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ManagementControllerTests extends BaseTest {
	@Autowired
	MockMvc mockMvc;

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
