package io.weidongxu.webapp.azmgmt;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.resourcemanager.Azure;
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

        client = Azure.configure().withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(new ManagedIdentityCredentialBuilder().build(),
                        new AzureProfile(null, subscriptionId, AzureEnvironment.AZURE))
                .withSubscription(subscriptionId);
    }

    public Azure getClient() {
        return client;
    }

    public String getSecret() {
        return secret;
    }
}
