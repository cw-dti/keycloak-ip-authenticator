package de.cw.dti.keycloak.ipauthenticator;

import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

import java.util.Collections;
import java.util.List;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

public class IPAuthenticatorFactory implements AuthenticatorFactory {

    public static final String ID = "ipauthenticator";

    private static final Authenticator AUTHENTICATOR_INSTANCE = new IPAuthenticator();

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return AUTHENTICATOR_INSTANCE;
    }

    @Override
    public String getDisplayType() {
        return "IP Authenticator";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[] { AuthenticationExecutionModel.Requirement.REQUIRED };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Limits access to only allowed IP Address";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public void init(Config.Scope scope) {
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return ID;
    }
}
