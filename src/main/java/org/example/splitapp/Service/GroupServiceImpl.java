package org.example.splitapp.Service;

import org.example.splitapp.model.Group;
import org.example.splitapp.model.GroupMember;
import org.example.splitapp.model.User;
import org.example.splitapp.repository.GroupMemberRepository;
import org.example.splitapp.repository.GroupRepository;
import org.example.splitapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Override
    @Transactional
    public Group createGroup(Group group) {
        // Validate input
        if (group.getGroupName() == null || group.getGroupName().trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        if (group.getCreatedBy() == null || group.getCreatedBy().getUserId() == null) {
            throw new IllegalArgumentException("Creator user must be specified");
        }

        // Fetch and set the creator
        User creator = userRepository.findById(group.getCreatedBy().getUserId())
                .orElseThrow(() -> new RuntimeException("Creator user not found"));
        
        group.setCreatedBy(creator);
        // createdByUserName will be set automatically in the entity

        // Save the group
        Group savedGroup = groupRepository.save(group);

        // Add creator as a member
        GroupMember creatorMember = new GroupMember();
        creatorMember.setGroup(savedGroup);
        creatorMember.setUser(creator);
        groupMemberRepository.save(creatorMember);

        return savedGroup;
    }

    @Override
    @Transactional
    public Group updateGroupName(Long groupId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("New group name cannot be empty");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        group.setGroupName(newName.trim());
        return groupRepository.save(group);
    }

    @Override
    public List<Group> findGroupsByCreator(Long userId) {
        return groupRepository.findByCreatedByUserId(userId);
    }

    @Override
    @Transactional
    public void addMembersToGroup(Long groupId, List<Long> memberIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<User> users = userRepository.findAllById(memberIds);
        if (users.size() != memberIds.size()) {
            throw new RuntimeException("One or more users not found");
        }

        for (User user : users) {
            if (!groupMemberRepository.existsByGroupAndUser(group, user)) {
                GroupMember member = new GroupMember();
                member.setGroup(group);
                member.setUser(user);
                groupMemberRepository.save(member);
            }
        }
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        GroupMember member = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Group member not found"));
        
        // Prevent removing the group creator
        if (member.getUser().getUserId().equals(member.getGroup().getCreatedBy().getUserId())) {
            throw new IllegalStateException("Cannot remove the group creator");
        }
        
        groupMemberRepository.delete(member);
    }

    @Override
    public List<GroupMember> getGroupMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMemberRepository.findByGroup(group);
    }
}