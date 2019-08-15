package weblog.config;

import org.springframework.stereotype.Component;

import weblog.TenantNameFilter;
import weblog.controller.ContactController;

import org.glassfish.jersey.server.ResourceConfig;
/**
 * Jersey Configuration (Resources, Modules, Filters, ...)
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        // Register the Filters:
        register(TenantNameFilter.class);

        // Register the Resources:
        register(ContactController.class);

        // Uncomment to disable WADL Generation:
        //property("jersey.config.server.wadl.disableWadl", true);

        // Uncomment to add Request Tracing:
        property("jersey.config.server.tracing.type", "ALL");
        property("jersey.config.server.tracing.threshold", "TRACE");
    }
}