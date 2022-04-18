package de.cofinpro.codeshare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.codeshare.domain.CodeSnippet;
import de.cofinpro.codeshare.persistence.CodeSnippetRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CodeSharingPlatformAppTests {

	private static final String HTML_FORM = """
			<form>
			    <textarea class="ui-width" id="code_snippet">// write your code here</textarea>
			    <br>
			    <div class="ui-width" id="div_button">
			        <button id="send_snippet" type="button" onclick="send()">Submit</button>
			    </div>
			</form>""";
	@Autowired
	CodeSnippet codeSnippet;

	@Autowired
	CodeSnippetRepository codeRepository;
	@Autowired
	MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		codeRepository.setCodeSnippet(codeSnippet);
	}

	@Test
	void whenGetCode_HtmlReturnedWith200ContainsCodeSnippet() throws Exception {
		mockMvc.perform(get("/code"))
				.andExpect(content().string(containsStringIgnoringCase("<title>Code</title>")))
				.andExpect(content().string(containsString(StringEscapeUtils.escapeHtml4(codeSnippet.getCode()))))
				.andExpect(content().string(containsString(codeSnippet.getDate())))
				.andExpect(status().isOk());
	}

	@Test
	void whenGetNewCode_createDotHtmlReturnedWith200() throws Exception {
		mockMvc.perform(get("/code/new"))
				.andExpect(header().string("content-type", "text/html;charset=UTF-8"))
				.andExpect(content().string(stringContainsInOrder(HTML_FORM.split("\\s+"))))
				.andExpect(content().string(containsString("/js/send.js")))
				.andExpect(content().string(containsString("/css/create.css")))
				.andExpect(status().isOk());
	}

	@Test
	void whenPostNewCode_emptyJsonReturnedWith200AndNextGetRetrieves() throws Exception {
		mockMvc.perform(post("/api/code/new")
						.header("content-type", "application/json")
						.content("{\"code\":\"test code\"}"))
				.andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().string("{}"))
				.andExpect(status().isOk());
		assertEquals("test code", codeRepository.getCodeSnippet().getCode());
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
				.andExpect(header().string("content-type", "text/html;charset=UTF-8"))
				.andExpect(status().isOk());
	}

	private String toJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}
}
