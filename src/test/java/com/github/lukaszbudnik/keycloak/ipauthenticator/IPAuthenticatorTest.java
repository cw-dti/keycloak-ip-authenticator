package com.github.lukaszbudnik.keycloak.ipauthenticator;

import static com.github.lukaszbudnik.keycloak.ipauthenticator.IPAuthenticator.IP_RANGE;
import static com.github.lukaszbudnik.keycloak.ipauthenticator.IPAuthenticator.IP_URL;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.GroupModel;
import org.keycloak.models.UserModel;
import org.mockito.Mockito;

class IPAuthenticatorTest {

  private GroupModel group1;
  private AuthenticationFlowContext context;

  @BeforeEach
  void setup() {
    group1 = Mockito.mock(GroupModel.class);
    Mockito.when(group1.getName())
           .thenReturn("IPX_TEST");

    Mockito.when(group1.getAttributeStream(IP_URL))
           .thenReturn(Stream.empty());
    GroupModel group2 = Mockito.mock(GroupModel.class);
    Mockito.when(group2.getName())
           .thenReturn("TEST");

    UserModel user = Mockito.mock(UserModel.class);
    Mockito.when(user.getGroupsStream())
           .thenAnswer(invocationOnMock -> Stream.of(group1, group2));
    Mockito.when(user.getEmail()).thenReturn("test@example.com");
    Mockito.when(user.getId()).thenReturn("user_id_1");

    context = Mockito.mock(AuthenticationFlowContext.class,
                                                     Mockito.RETURNS_DEEP_STUBS);
    Mockito.when(context.getUser())
           .thenReturn(user);
    Mockito.when(context.getConnection()
                        .getRemoteAddr())
           .thenReturn("192.168.1.1");
  }

  @Test
  void testAuthenticationFlow_success() {
    // Mock Setup
    Mockito.when(group1.getAttributeStream(IP_RANGE))
           .thenReturn(Stream.of("192.168.1.0/28"));

    // start test
    IPAuthenticator authenticator = new IPAuthenticator();
    authenticator.authenticate(context);

    Mockito.verify(context, Mockito.times(1))
           .success();
  }

  @Test
  void testAuthenticationFlow_fail_invalid_user_ip() {
    // Mock Setup
    Mockito.when(group1.getAttributeStream(IP_RANGE))
           .thenReturn(Stream.of("192.168.2.0/28"));

    // start test
    IPAuthenticator authenticator = new IPAuthenticator();
    authenticator.authenticate(context);

    Mockito.verify(context, Mockito.times(1))
           .failure(Mockito.any(), Mockito.any());
  }

  @Test
  void testAuthenticationFlow_fail_invalid_ip_range() {
    // Mock Setup
    Mockito.when(group1.getAttributeStream(IP_RANGE))
           .thenReturn(Stream.of("192.168.256.0/28"));

    // start test
    IPAuthenticator authenticator = new IPAuthenticator();
    authenticator.authenticate(context);

    Mockito.verify(context, Mockito.times(1))
           .failure(Mockito.any(), Mockito.any());
  }
}