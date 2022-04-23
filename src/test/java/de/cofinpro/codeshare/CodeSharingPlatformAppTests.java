package de.cofinpro.codeshare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.codeshare.persistence.CodeSnippet;
import de.cofinpro.codeshare.domain.CodeSnippetStorage;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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
	CodeSnippetStorage snippetStorage;
	@Autowired
	MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	private CodeSnippet codeSnippet;

	@Value("${codesharing.latest.amount:10}")
	private int amountLatest;

	@BeforeAll
	static void cleanDB() {
		try {
			Files.deleteIfExists(Path.of("./src/test/resources/db/snippets.mv.db"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void whenGetCode_HtmlReturnedWith200ContainsCodeSnippet() throws Exception {
		long id = snippetStorage.addCode("void method(<? extends T>) {\n  //blah\n}");
		assertTrue(snippetStorage.findById(id).isPresent());
		codeSnippet = snippetStorage.findById(id).get();
		mockMvc.perform(get("/code/%d".formatted(id)))
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
	void whenPostNewCode_JsonReturnedWith200AndIdAndNextGetRetrieves() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(post("/api/code/new")
						.header("content-type", "application/json")
						.content("{\"code\":\"another test code\"}"))
				.andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().string(matchesRegex("\\{\\s*\"id\":\\s*\"\\d+\"\\s*}")))
				.andExpect(status().isOk()).andReturn().getResponse();
		Optional<CodeSnippet> retrieved = snippetStorage.findById(idFromResponse(response));
		assertTrue(retrieved.isPresent());
		assertEquals("another test code", retrieved.get().getCode());
	}

	@Test
	void whenApiCode_JsonCodeReturnedWith200() throws Exception {
		long id = snippetStorage.addCode("void method(<? extends T>) {\n  //blah\n}");
		assertTrue(snippetStorage.findById(id).isPresent());
		codeSnippet = snippetStorage.findById(id).get();
		mockMvc.perform(get("/api/code/%d".formatted(id)))
				.andExpect(content().json(toJson(codeSnippet.toDTO())))
				.andExpect(status().isOk());
	}

	@Test
	void whenApiCode_HeaderIsJson() throws Exception {
		snippetStorage.addCode("void method(<? extends T>) {\n  //blah\n}");
		mockMvc.perform(get("/api/code/1"))
				.andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}

	@Test
	void whenCode_HeaderIsHtml() throws Exception {
		snippetStorage.addCode("void method(<? extends T>) {\n  //blah\n}");
		mockMvc.perform(get("/code/1"))
				.andExpect(header().string("content-type", "text/html;charset=UTF-8"))
				.andExpect(status().isOk());
	}

	@Test
	void whenLatest_RightAmountReturned() throws Exception {
		for (int i = 0; i < amountLatest + 3; i++) {
			snippetStorage.addCode("void method(<? extends T>) {\n  //blah\n}");
		}
		MockHttpServletResponse response =  mockMvc.perform(get("/api/code/latest"))
				.andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse();
		String content = response.getContentAsString();
		Matcher matcher = Pattern.compile("\"date\":\\s*\"2022").matcher(content);
		for (int i = 0; i < amountLatest; i++) {
			assertTrue(matcher.find());
		}
		assertFalse(matcher.find());
	}

	private String toJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	private long idFromResponse(MockHttpServletResponse response) throws Exception {
		System.out.println(response.getContentAsString());
		Matcher regexMatcher = Pattern.compile("id\\s*\"\\s*:\\s*\"(\\d+)\\s*")
				.matcher(response.getContentAsString());
		assertTrue(regexMatcher.find());
		return Integer.parseInt(regexMatcher.group(1));
	}
}
