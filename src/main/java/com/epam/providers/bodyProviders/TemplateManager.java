package com.epam.providers.bodyProviders;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class TemplateManager {

    private final static Logger logger = LoggerFactory.getLogger(TemplateManager.class);
    private static final String TEMPLATE_DIRECTORY = "src/test/resources/ftl/";
    private final Configuration config;

    public TemplateManager() {
        config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateLoader(new StringTemplateLoader());
    }

    private Template loadTemplate(String templateName, String templatePath) {
        try {
            String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));
            ((StringTemplateLoader) config.getTemplateLoader()).putTemplate(templateName, templateContent);
            return config.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String processTemplate(String templateName, Map<String, Object> data) {
        Template template = loadTemplate(templateName, TEMPLATE_DIRECTORY + templateName + ".ftl");
        try (StringWriter writer = new StringWriter()) {
            template.process(data, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
