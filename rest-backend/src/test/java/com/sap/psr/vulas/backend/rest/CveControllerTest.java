package com.sap.psr.vulas.backend.rest;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.sap.psr.vulas.backend.cve.Cve;
import com.sap.psr.vulas.backend.cve.CveReader;
import com.sap.psr.vulas.shared.categories.RequiresNetwork;
import com.sap.psr.vulas.shared.json.JacksonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainController.class,webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class CveControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter<?> mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                new Predicate<HttpMessageConverter<?>>() {
					@Override
					public boolean test(HttpMessageConverter<?> hmc) {
						return hmc instanceof MappingJackson2HttpMessageConverter;
					}
				}).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    @Category(RequiresNetwork.class)
    public void testPostCommonsFileUpload() throws Exception {
    	Cve cve = CveReader.read("CVE-2014-0050");
    	String json = JacksonUtil.asJsonString(cve);
    	System.out.println(json);
    }
}