package org.jtwig.spring.boot.integration;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SimpleTest {
    @Test
    public void simpleTest() throws Exception {
        AnnotationConfigEmbeddedWebApplicationContext context = (AnnotationConfigEmbeddedWebApplicationContext) SpringApplication.run(Config.class, "--server.port=0");
        int port = context.getEmbeddedServletContainer().getPort();
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d", port));
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);
        String content = IOUtils.toString(response.getEntity().getContent());
        context.stop();

        assertThat(content, is("Hi Jtwig!"));
    }

    @EnableAutoConfiguration
    @EnableWebMvc
    @Controller
    public static class Config {
        @RequestMapping("/")
        public String example (ModelMap model) {
            model.put("name", "Jtwig");
            return "example";
        }
    }
}
