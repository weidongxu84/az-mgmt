package io.weidongxu.webapp.textnormalization;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.management.Azure;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AzureManagement {

    private final Azure client;
    private final String subscriptionId;
    private final String secret;

    public AzureManagement() {
        subscriptionId = System.getenv("SUBSCRIPTION_ID");
        secret = System.getenv("QUERY_SECRET");

        client = Azure.authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE))
                .withSubscription(subscriptionId);
    }

    public Azure getClient() {
        return client;
    }

    public String getSecret() {
        return secret;
    }
}
