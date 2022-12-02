package com.epam.providers.bodyProviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class BodyProvider {
    private final static Logger logger = LoggerFactory.getLogger(BodyProvider.class);

    public static String getBody(String ftlFileName, Map<String, Object> params) {
        TemplateManager templateManager = new TemplateManager();
        logger.info("Get body with provided parameters -> {}", params);
        return templateManager.processTemplate(ftlFileName, params);
    }
}
