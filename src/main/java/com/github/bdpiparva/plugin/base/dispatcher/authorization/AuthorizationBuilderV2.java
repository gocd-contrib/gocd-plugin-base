package com.github.bdpiparva.plugin.base.dispatcher.authorization;

import com.github.bdpiparva.plugin.base.dispatcher.VersionedExtensionBuilder;
import com.github.bdpiparva.plugin.base.executors.*;
import com.github.bdpiparva.plugin.base.executors.authorization.CapabilitiesExecutor;
import com.github.bdpiparva.plugin.base.validation.DefaultValidator;
import com.github.bdpiparva.plugin.base.validation.Validator;

public class AuthorizationBuilderV2 extends VersionedExtensionBuilder<AuthorizationBuilderV2> {
    static final String REQUEST_GET_ICON = "go.cd.authorization.get-icon";
    static final String REQUEST_CAPABILITIES = "go.cd.authorization.get-capabilities";
    static final String REQUEST_GET_USER_ROLES = "go.cd.authorization.get-user-roles";
    static final String REQUEST_IS_VALID_USER = "go.cd.authorization.is-valid-user";
    static final String REQUEST_GET_AUTH_CONFIG_METADATA = "go.cd.authorization.auth-config.get-metadata";
    static final String REQUEST_GET_AUTH_CONFIG_VIEW = "go.cd.authorization.auth-config.get-view";
    static final String REQUEST_VALIDATE_AUTH_CONFIG = "go.cd.authorization.auth-config.validate";
    static final String REQUEST_VERIFY_CONNECTION = "go.cd.authorization.auth-config.verify-connection";
    static final String REQUEST_GET_ROLE_CONFIG_METADATA = "go.cd.authorization.role-config.get-metadata";
    static final String REQUEST_GET_ROLE_CONFIG_VIEW = "go.cd.authorization.role-config.get-view";
    static final String REQUEST_VALIDATE_ROLE_CONFIG = "go.cd.authorization.role-config.validate";
    static final String REQUEST_AUTHENTICATE_USER = "go.cd.authorization.authenticate-user";
    static final String REQUEST_SEARCH_USERS = "go.cd.authorization.search-users";
    static final String REQUEST_ACCESS_TOKEN = "go.cd.authorization.fetch-access-token";
    static final String REQUEST_AUTHORIZATION_SERVER_URL = "go.cd.authorization.authorization-server-url";

    public AuthorizationBuilderV2() {
        registry.put(REQUEST_VALIDATE_AUTH_CONFIG, new ValidationExecutor());
        registry.put(REQUEST_VALIDATE_ROLE_CONFIG, new ValidationExecutor());
    }

    public AuthorizationBuilderV2 icon(String iconPath, String contentType) {
        return register(REQUEST_GET_ICON, new IconRequestExecutor(iconPath, contentType));
    }

    public AuthorizationBuilderV2 capabilities(SupportedAuthType supportedAuthType, boolean canSearch, boolean canAuthorize, boolean canGetUserRoles) {
        return register(REQUEST_CAPABILITIES, new CapabilitiesExecutor(supportedAuthType, canSearch, canAuthorize, canGetUserRoles));
    }

    public AuthorizationBuilderV2 authConfigMetadata(Class<?> authConfigClass) {
        return authConfigMetadata(authConfigClass, true);
    }

    public AuthorizationBuilderV2 authConfigMetadata(Class<?> authConfigClass, boolean addDefaultValidator) {
        if (addDefaultValidator) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_AUTH_CONFIG)).addAll(new DefaultValidator(authConfigClass));
        }
        return register(REQUEST_GET_AUTH_CONFIG_METADATA, new MetadataExecutor(authConfigClass));
    }

    public AuthorizationBuilderV2 validateAuthConfig(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_AUTH_CONFIG)).addAll(validators);
        return this;
    }


    public AuthorizationBuilderV2 roleConfigMetadata(Class<?> roleConfigClass) {
        return roleConfigMetadata(roleConfigClass, true);
    }

    public AuthorizationBuilderV2 roleConfigMetadata(Class<?> roleConfigClass, boolean addDefaultValidator) {
        if (addDefaultValidator) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_ROLE_CONFIG)).addAll(new DefaultValidator(roleConfigClass));
        }
        return register(REQUEST_GET_ROLE_CONFIG_METADATA, new MetadataExecutor(roleConfigClass));
    }

    public AuthorizationBuilderV2 validateRoleConfig(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_ROLE_CONFIG)).addAll(validators);
        return this;
    }

    public AuthorizationBuilderV2 roleConfigView(String viewFile) {
        return register(REQUEST_GET_ROLE_CONFIG_VIEW, new ViewRequestExecutor(viewFile));
    }

    public AuthorizationBuilderV2 authConfigView(String viewFile) {
        return register(REQUEST_GET_AUTH_CONFIG_VIEW, new ViewRequestExecutor(viewFile));
    }

    public AuthorizationBuilderV2 getUserRoles(AbstractExecutor getRoleExecutor) {
        return register(REQUEST_GET_USER_ROLES, getRoleExecutor);
    }

    public AuthorizationBuilderV2 isValidUser(AbstractExecutor isValidUser) {
        return register(REQUEST_IS_VALID_USER, isValidUser);
    }

    public AuthorizationBuilderV2 authenticateUser(AbstractExecutor authenticateUser) {
        return register(REQUEST_AUTHENTICATE_USER, authenticateUser);
    }

    public AuthorizationBuilderV2 searchUser(AbstractExecutor searchUser) {
        return register(REQUEST_SEARCH_USERS, searchUser);
    }

    public AuthorizationBuilderV2 getAccessToken(AbstractExecutor getAccessTokenExecutor) {
        return register(REQUEST_ACCESS_TOKEN, getAccessTokenExecutor);
    }

    public AuthorizationBuilderV2 authorizationServerUrl(AbstractExecutor getAuthorizationUrl) {
        return register(REQUEST_AUTHORIZATION_SERVER_URL, getAuthorizationUrl);
    }

    public AuthorizationBuilderV2 verifyConnection(AbstractExecutor verifyConnectionExecutor) {
        return register(REQUEST_VERIFY_CONNECTION, verifyConnectionExecutor);
    }
}

