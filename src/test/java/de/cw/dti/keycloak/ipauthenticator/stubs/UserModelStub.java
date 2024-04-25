package de.cw.dti.keycloak.ipauthenticator.stubs;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;

public class UserModelStub implements UserModel {

  private final String id;
  private final String email;
  private final List<GroupModel> groups;

  public UserModelStub(String id, String email, List<GroupModel> groups) {
    this.id = id;
    this.email = email;
    this.groups = groups;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return "";
  }

  @Override
  public void setUsername(String s) {

  }

  @Override
  public Long getCreatedTimestamp() {
    return 0L;
  }

  @Override
  public void setCreatedTimestamp(Long aLong) {

  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public void setEnabled(boolean b) {

  }

  @Override
  public void setSingleAttribute(String s, String s1) {

  }

  @Override
  public void setAttribute(String s, List<String> list) {

  }

  @Override
  public void removeAttribute(String s) {

  }

  @Override
  public String getFirstAttribute(String s) {
    return "";
  }

  @Override
  public Stream<String> getAttributeStream(String s) {
    return Stream.empty();
  }

  @Override
  public Map<String, List<String>> getAttributes() {
    return Map.of();
  }

  @Override
  public Stream<String> getRequiredActionsStream() {
    return Stream.empty();
  }

  @Override
  public void addRequiredAction(String s) {

  }

  @Override
  public void removeRequiredAction(String s) {

  }

  @Override
  public String getFirstName() {
    return "";
  }

  @Override
  public void setFirstName(String s) {

  }

  @Override
  public String getLastName() {
    return "";
  }

  @Override
  public void setLastName(String s) {

  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String s) {

  }

  @Override
  public boolean isEmailVerified() {
    return false;
  }

  @Override
  public void setEmailVerified(boolean b) {

  }

  @Override
  public Stream<GroupModel> getGroupsStream() {
    return groups.stream();
  }

  @Override
  public void joinGroup(GroupModel groupModel) {

  }

  @Override
  public void leaveGroup(GroupModel groupModel) {

  }

  @Override
  public boolean isMemberOf(GroupModel groupModel) {
    return false;
  }

  @Override
  public String getFederationLink() {
    return "";
  }

  @Override
  public void setFederationLink(String s) {

  }

  @Override
  public String getServiceAccountClientLink() {
    return "";
  }

  @Override
  public void setServiceAccountClientLink(String s) {

  }

  @Override
  public SubjectCredentialManager credentialManager() {
    return null;
  }

  @Override
  public Stream<RoleModel> getRealmRoleMappingsStream() {
    return Stream.empty();
  }

  @Override
  public Stream<RoleModel> getClientRoleMappingsStream(ClientModel clientModel) {
    return Stream.empty();
  }

  @Override
  public boolean hasRole(RoleModel roleModel) {
    return false;
  }

  @Override
  public void grantRole(RoleModel roleModel) {

  }

  @Override
  public Stream<RoleModel> getRoleMappingsStream() {
    return Stream.empty();
  }

  @Override
  public void deleteRoleMapping(RoleModel roleModel) {

  }
}
