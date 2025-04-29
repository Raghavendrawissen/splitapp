package org.example.splitapp.Service;

import org.example.splitapp.model.Group;
import org.example.splitapp.model.GroupMember;

import java.util.List;

public interface GroupService {
    Group createGroup(Group group);
    Group updateGroupName(Long groupId, String newName);
    List<Group> findGroupsByCreator(Long userId);
    void addMembersToGroup(Long groupId, List<Long> memberIds);
    void removeMember(Long memberId);

    List<GroupMember> getGroupMembers(Long groupId);
}