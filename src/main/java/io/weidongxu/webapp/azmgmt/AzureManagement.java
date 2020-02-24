package io.weidongxu.webapp.azmgmt;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.rest.LogLevel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AzureManagement {

    private final Azure client;
    private final String secret;

    public AzureManagement() {
        String subscriptionId = Objects.requireNonNull(System.getenv("SUBSCRIPTION_ID"));
        secret = Objects.requireNonNull(System.getenv("QUERY_SECRET"));

        client = Azure.configure().withLogLevel(LogLevel.BASIC)
                .authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE))
                .withSubscription(subscriptionId);
    }

    public Azure getClient() {
        return client;
    }

    public String getSecret() {
        return secret;
    }
}
