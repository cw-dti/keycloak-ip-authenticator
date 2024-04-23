package com.github.lukaszbudnik.keycloak.ipauthenticator;

import inet.ipaddr.IPAddressString;
import jakarta.ws.rs.core.Response.Status;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;
import org.keycloak.utils.StringUtil;

public class IPAuthenticator implements Authenticator {

  private static final Logger logger = Logger.getLogger(IPAuthenticator.class);

  public static final String IP_RANGE = "IP_RANGE";
  public static final String IP_URL = "IP_RANGE_URL";

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    try {
      UserModel user = context.getUser();
      String remoteIPAddress = context.getConnection()
                                      .getRemoteAddr();
      List<String> allowedIPAddress = getAllowedIPAddresses(user);
      if (logger.isDebugEnabled()) {
        logger.debug("Access from " + remoteIPAddress);
        logger.debug("Allowed IPs " + allowedIPAddress);
      }

      if (allowedIPAddress.stream()
                          .map(IPAddressString::new)
                          .filter(IPAddressString::isValid)
                          .noneMatch(s -> s.contains(new IPAddressString(remoteIPAddress)))) {
        context.failure(AuthenticationFlowError.INVALID_USER,
                        context.form()
                               .setError("invalid_ip_address")
                               .createErrorPage(Status.FORBIDDEN));
      } else {
        context.success();
      }
    } catch (Exception e) {
      logger.error("Failed to login: ", e);
      context.failure(AuthenticationFlowError.IDENTITY_PROVIDER_ERROR);
    }
  }

  private List<String> getAllowedIPAddresses(UserModel user) {
    return user.getGroupsStream()
               .flatMap(this::getIPsForGroup)
               .collect(Collectors.toList());
  }

  private Stream<String> getIPsForGroup(GroupModel group) {
    List<String> ips = new ArrayList<>();

    ips.addAll(getIPsFromUrl(group.getName(), group.getAttributeStream(IP_URL)));
    ips.addAll(getIPsFromAttributes(group.getAttributeStream(IP_RANGE)));
    return ips.stream();
  }

  private List<String> getIPsFromAttributes(Stream<String> attributeStream) {
    return attributeStream.flatMap(a -> Arrays.stream(a.split(",")))
                          .filter(StringUtil::isNotBlank)
                          .map(String::trim)
                          .collect(Collectors.toList());
  }

  private List<String> getIPsFromUrl(String name, Stream<String> attributeStream) {
    return attributeStream.filter(StringUtil::isNotBlank)
                          .flatMap(url -> getIPsFromUrl(name, url).stream())
                          .collect(Collectors.toList());
  }

  private List<String> getIPsFromUrl(String name, String urlString) {
    try {
      URL url = new URL(urlString);
      List<String> lines = new ArrayList<>();

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          line = line.trim();
          if (!line.isEmpty()) {
            lines.add(line);
          }
        }
      }
      return lines;
    } catch (IOException e) {
      logger.error("Failed to fetch valid IPs for group " + name + " and url " + urlString, e);
    }
    throw new RuntimeException();
  }

  @Override
  public void action(AuthenticationFlowContext context) {
  }

  @Override
  public boolean requiresUser() {
    return true;
  }

  @Override
  public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
  }

  @Override
  public void close() {
  }

}
