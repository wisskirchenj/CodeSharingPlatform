package de.cofinpro.codeshare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.codeshare.domain.CodeSnippet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CodeSharingPlatformAppTests {

	@Autowired
	CodeSnippet codeSnippet;

	@Autowired
	MockMvc mockMvc;

	HttpHeaders htmlHeader;
	HttpHeaders jsonHeader;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		htmlHeader = new HttpHeaders();
		htmlHeader.setContentType(MediaType.TEXT_HTML);
		jsonHeader = new HttpHeaders();
		jsonHeader.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	void whenCode_HtmlStringCodeReturnedWith200() throws Exception {
		mockMvc.perform(get("/code"))
				.andExpect(content().string(codeSnippet.toHtml()))
				.andExpect(status().isOk());
	}

	@Test
	void whenApiCode_JsonCodeReturnedWith200() throws Exception {
		mockMvc.perform(get("/api/code"))
				.andExpect(content().json(toJson(codeSnippet)))
				.andExpect(status().isOk());
	}

	@Test
	void whenApiCode_HeaderIsJson() throws Exception {
		mockMvc.perform(get("/api/code"))
				.andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}

	@Test
	void whenCode_HeaderIsHtml() throws Exception {
		mockMvc.perform(get("/code"))
				.andExpect(header().string("content-type", MediaType.TEXT_HTML_VALUE))
				.andExpect(status().isOk());
	}

	private String toJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}
}
