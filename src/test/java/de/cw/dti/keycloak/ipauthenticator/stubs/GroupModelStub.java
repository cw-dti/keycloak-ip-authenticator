package de.cw.dti.keycloak.ipauthenticator.stubs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;

public class GroupModelStub implements GroupModel {

  private final String name;
  private Map<String, List<String>> attributes;

  public GroupModelStub(String name) {
    this.name = name;
    this.attributes = new HashMap<>();
  }

  @Override
  public String getId() {
    return "";
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String s) {

  }

  @Override
  public void setSingleAttribute(String s, String s1) {

  }

  @Override
  public void setAttribute(String s, List<String> list) {
    this.attributes.put(s, list);
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
    return attributes.computeIfAbsent(s, k -> List.of()).stream();
  }

  @Override
  public Map<String, List<String>> getAttributes() {
    return Map.of();
  }

  @Override
  public GroupModel getParent() {
    return null;
  }

  @Override
  public String getParentId() {
    return "";
  }

  @Override
  public Stream<GroupModel> getSubGroupsStream() {
    return Stream.empty();
  }

  @Override
  public void setParent(GroupModel groupModel) {

  }

  @Override
  public void addChild(GroupModel groupModel) {

  }

  @Override
  public void removeChild(GroupModel groupModel) {

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
