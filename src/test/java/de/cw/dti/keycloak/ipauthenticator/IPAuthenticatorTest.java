package de.cw.dti.keycloak.ipauthenticator;

import static de.cw.dti.keycloak.ipauthenticator.IPAuthenticator.IP_RANGE;

import de.cw.dti.keycloak.ipauthenticator.stubs.GroupModelStub;
import de.cw.dti.keycloak.ipauthenticator.stubs.UserModelStub;
import java.util.List;
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
    group1 = new GroupModelStub("IPX_TEST");

    GroupModel group2 = new GroupModelStub("TEST");
    UserModel user = new UserModelStub("user_id_1", "test@example.com", List.of(group1, group2));

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
    group1.setAttribute(IP_RANGE, List.of("192.168.1.0/28"));

    // start test
    IPAuthenticator authenticator = new IPAuthenticator();
    authenticator.authenticate(context);

    Mockito.verify(context, Mockito.times(1))
           .success();
  }

  @Test
  void testAuthenticationFlow_fail_invalid_user_ip() {
    // Mock Setup
    group1.setAttribute(IP_RANGE, List.of("192.168.2.0/28"));

    // start test
    IPAuthenticator authenticator = new IPAuthenticator();
    authenticator.authenticate(context);

    Mockito.verify(context, Mockito.times(1))
           .failure(Mockito.any(), Mockito.any());
  }

  @Test
  void testAuthenticationFlow_fail_invalid_ip_range() {
    // Mock Setup
    group1.setAttribute(IP_RANGE, List.of("192.168.256.0/28"));

    // start test
    IPAuthenticator authenticator = new IPAuthenticator();
    authenticator.authenticate(context);

    Mockito.verify(context, Mockito.times(1))
           .failure(Mockito.any(), Mockito.any());
  }
}