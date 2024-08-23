package com.sample;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiKeyService {

    public String findAuthenticationIdByApiKey(String apiKey) {
        return "SECRET";
    }

    public String findApiKeyByAuthenticationId(String customerId) {
        return "SECRET";
    }

    public String createApiKey(String customerId) {
        return"SECRET";
    }

    public String recreateApiKey(String customerId) {
        deleteApiKey(customerId);
        return createApiKey(customerId);
    }

    public void deleteApiKey(String customerId) {
    }
}
