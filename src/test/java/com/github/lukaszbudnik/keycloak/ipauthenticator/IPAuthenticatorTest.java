package com.github.lukaszbudnik.keycloak.ipauthenticator;


import inet.ipaddr.IPAddressString;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IPAuthenticatorTest {

  @Test
  void testIPRangeCheck5() {
    String testIp = "255.255.255.255";
    String testIp2 = "0.0.0.0";
    List<String> allowedIPs = List.of("0.0.0.0/0");

    Assertions.assertFalse(allowedIPs.stream()
                                     .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                         testIp))));
    Assertions.assertFalse(allowedIPs.stream()
                                     .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                         testIp2))));
  }

  @Test
  void testIPRangeCheck6() {
    String testIp = "192.168.0.256";
    String testIp2 = "255.255.255.300";
    List<String> allowedIPs = List.of("0.0.0.0/0");

    /*Assertions.assertThrows(IllegalArgumentException.class,
                            () -> allowedIPs.stream()
                                            .anyMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                                testIp))));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> allowedIPs.stream()
                                            .anyMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                                testIp2))));*/
  }

  @Test
  void testIPRangeCheck7() {
    String testIp = "::1";
    String testIp2 = "fe80::1";
    List<String> allowedIPs = List.of("0.0.0.0/0");

    Assertions.assertTrue(allowedIPs.stream()
                                    .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                        testIp))));
    Assertions.assertTrue(allowedIPs.stream()
                                    .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                        testIp2))));
  }

  @Test
  void testIPRangeCheck() {
    String currentIp = "192.168.0.1";
    String currentIp2 = "127.0.0.2";
    List<String> allowedIPs = List.of("127.0.0.0/30");

    Assertions.assertTrue(allowedIPs.stream().noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(currentIp))));
    Assertions.assertFalse(allowedIPs.stream().noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(currentIp2))));
  }

  @Test
  void testIPRangeCheck2() {
    String testIp = "127.0.0.5";
    String testIp2 = "127.0.0.1";
    List<String> allowedIPs = List.of("127.0.0.0/30");

    Assertions.assertTrue(allowedIPs.stream()
                                    .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                        testIp))));
    Assertions.assertFalse(allowedIPs.stream()
                                     .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                         testIp2))));
  }

  @Test
  void testIPRangeCheck3() {
    String testIp = "255.255.255.0";
    String testIp2 = "10.0.1.3";
    List<String> allowedIPs = List.of("10.0.1.0/24");

    Assertions.assertTrue(allowedIPs.stream()
                                    .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                        testIp))));
    Assertions.assertFalse(allowedIPs.stream()
                                     .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                         testIp2))));
  }

  @Test
  void testIPRangeCheck4() {
    String testIp = "255.255.255.0";
    String testIp2 = "10.0.1.3";
    List<String> allowedIPs = List.of("0.0.0.0/32");

    Assertions.assertTrue(allowedIPs.stream()
                                    .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                        testIp))));
    Assertions.assertTrue(allowedIPs.stream()
                                     .noneMatch(s -> new IPAddressString(s).contains(new IPAddressString(
                                         testIp2))));
  }
}