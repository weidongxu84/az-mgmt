package io.weidongxu.webapp.azmgmt;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.util.Configuration;
import com.azure.identity.ChainedTokenCredentialBuilder;
import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AzureManagement {

    private final AzureResourceManager client;
    private final String secret;

    private static final String AZ_CLI_CLIENT_ID = "04b07795-8ddb-461a-bbee-02f9e1bf7b46";

    public AzureManagement() {
        Configuration configuration = Configuration.getGlobalConfiguration();

        // either clientId or tenantId need to be provided, for InteractiveBrowserCredentialBuilder
        String clientId = configuration.get(Configuration.PROPERTY_AZURE_CLIENT_ID, AZ_CLI_CLIENT_ID);
        String tenantId = configuration.get(Configuration.PROPERTY_AZURE_TENANT_ID);

        String subscriptionId = Objects.requireNonNull(configuration.get(Configuration.PROPERTY_AZURE_SUBSCRIPTION_ID));
        secret = Objects.requireNonNull(configuration.get("QUERY_SECRET"));

        TokenCredential credential = new ChainedTokenCredentialBuilder()
                .addFirst(new ManagedIdentityCredentialBuilder().build())
                .addLast(new InteractiveBrowserCredentialBuilder()
                        .redirectUrl("http://localhost:3124")
                        .clientId(clientId)
                        .tenantId(tenantId).build())  // as backup auth for local dev
                .build();

        client = AzureResourceManager.configure().withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(credential, new AzureProfile(null, subscriptionId, AzureEnvironment.AZURE))
                .withSubscription(subscriptionId);
    }

    public AzureResourceManager getClient() {
        return client;
    }

    public String getSecret() {
        return secret;
    }
}
