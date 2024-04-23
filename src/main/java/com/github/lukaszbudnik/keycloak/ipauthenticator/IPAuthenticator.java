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

import java.util.function.Supplier;
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

  private static final String GROUP_IP_PREFIX = "IPX_";
  private static final String IP_RANGE = GROUP_IP_PREFIX + "RANGE";
  private static final String IP_URL = GROUP_IP_PREFIX + "RANGE_URL";

  private static final String INVALID_IP_ADDRESS_ERROR_MESSAGE = "invalid_ip_address";

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    UserModel user = context.getUser();
    String remoteIPAddress = context.getConnection()
                                    .getRemoteAddr();

    Supplier<Stream<GroupModel>> supplier = () -> user.getGroupsStream()
                                                      .filter(g -> g.getName()
                                                                    .startsWith(GROUP_IP_PREFIX));

    if (supplier.get()
                .findAny()
                .isPresent()) {
      try {
        List<IPAddressString> allowedIPAddress = getAllowedIPAddresses(supplier.get());
        if (logger.isDebugEnabled()) {
          logger.debug("Access from " + remoteIPAddress);
          logger.debug("Allowed IPs " + allowedIPAddress);
        }

        if (allowedIPAddress.isEmpty()) {
          throw new RuntimeException(
              "User " + user.getEmail() + " (id: " + user.getId() + ") is member of an "
                  + GROUP_IP_PREFIX + " group, but no valid IP addresses are provided!");
        }

        IPAddressString currentIp = new IPAddressString(remoteIPAddress);
        if (allowedIPAddress.stream()
                            .noneMatch(s -> s.contains(currentIp))) {
          context.failure(AuthenticationFlowError.INVALID_USER,
                          context.form()
                                 .setError(INVALID_IP_ADDRESS_ERROR_MESSAGE)
                                 .createErrorPage(Status.FORBIDDEN));
        } else {
          context.success();
        }
      } catch (Exception e) {
        logger.error("Failed to login user with IPX flow", e);
        context.failure(AuthenticationFlowError.INVALID_USER,
                        context.form()
                               .setError(INVALID_IP_ADDRESS_ERROR_MESSAGE)
                               .createErrorPage(Status.FORBIDDEN));
      }
    } else {
      context.success();
    }
  }

  private List<IPAddressString> getAllowedIPAddresses(Stream<GroupModel> stream) {
    return stream.flatMap(this::getIPsForGroup)
                 .map(IPAddressString::new)
                 .filter(IPAddressString::isValid)
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
      throw new RuntimeException(
          "Failed to fetch valid IPs for group " + name + " and url " + urlString, e);
    }
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
