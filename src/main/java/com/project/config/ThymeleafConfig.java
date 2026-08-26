package com.project.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;

public class ThymeleafConfig {

    private static TemplateEngine templateEngine;

    public static synchronized TemplateEngine getTemplateEngine(ServletContext servletContext) {
        if (templateEngine == null) {
            
            // 1. 
            JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);

            // 2.
            WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

            templateResolver.setPrefix("/WEB-INF/templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode("HTML");
            templateResolver.setCharacterEncoding("UTF-8");

            templateResolver.setCacheable(false); // Disable caching for development

            // 3.
            templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
        }
        return templateEngine;
    }
    
}
