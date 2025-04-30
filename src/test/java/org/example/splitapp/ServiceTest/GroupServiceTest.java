package org.example.splitapp.ServiceTest;

import org.example.splitapp.model.Group;
import org.example.splitapp.model.GroupMember;
import org.example.splitapp.model.User;
import org.example.splitapp.repository.GroupMemberRepository;
import org.example.splitapp.repository.GroupRepository;
import org.example.splitapp.repository.UserRepository;
import org.example.splitapp.Service.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;

    private GroupServiceImpl groupService;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        groupService = new GroupServiceImpl();
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Sample User");
        sampleUser.setEmail("sample@example.com");
        sampleUser.setPasswordHash("sampleHash");
        // Set mocked repositories
        org.springframework.test.util.ReflectionTestUtils.setField(groupService, "groupRepository", groupRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(groupService, "userRepository", userRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(groupService, "groupMemberRepository", groupMemberRepository);
    }

    @Test
    void createGroup_Success() {
        // Arrange
        User creator = new User();
        creator.setId(1L);
        creator.setName("Test User");

        Group group = new Group();
        group.setGroupName("Test Group");
        group.setCreatedBy(creator);

        Group savedGroup = new Group();
        savedGroup.setId(1L);
        savedGroup.setGroupName("Test Group");
        savedGroup.setCreatedBy(creator);

        when(userRepository.findById(creator.getId())).thenReturn(Optional.of(creator));
        when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
        when(groupMemberRepository.save(any(GroupMember.class))).thenReturn(new GroupMember());

        // Act
        Group result = groupService.createGroup(group);

        // Assert
        assertNotNull(result);
        assertEquals("Test Group", result.getGroupName());
        assertEquals(creator.getId(), result.getCreatedBy().getId());
        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    @Test
    void createGroup_EmptyName() {
        // Arrange
        Group group = new Group();
        group.setGroupName("");
        group.setCreatedBy(new User(sampleUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> groupService.createGroup(group));
    }

    @Test
    void updateGroupName_Success() {
        // Arrange
        Long groupId = 1L;
        String newName = "Updated Group Name";
        Group existingGroup = new Group();
        existingGroup.setId(groupId);
        existingGroup.setGroupName("Old Name");

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(groupRepository.save(any(Group.class))).thenReturn(existingGroup);

        // Act
        Group result = groupService.updateGroupName(groupId, newName);

        // Assert
        assertNotNull(result);
        assertEquals(newName, result.getGroupName());
    }

    @Test
    void findGroupsByCreator_Success() {
        // Arrange
        Long userId = 1L;
        List<Group> expectedGroups = Arrays.asList(new Group(), new Group());
        when(groupRepository.findByCreatedByUserId(userId)).thenReturn(expectedGroups);

        // Act
        List<Group> result = groupService.findGroupsByCreator(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void addMembersToGroup_Success() {
        // Arrange
        Long groupId = 1L;
        List<Long> memberIds = Arrays.asList(2L, 3L);
        Group group = new Group();
        group.setId(groupId);

        User user1 = new User(sampleUser);
        user1.setId(2L);
        User user2 = new User(sampleUser);
        user2.setId(3L);
        List<User> users = Arrays.asList(user1, user2);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findAllById(memberIds)).thenReturn(users);
        when(groupMemberRepository.existsByGroupAndUser(any(), any())).thenReturn(false);

        // Act
        groupService.addMembersToGroup(groupId, memberIds);

        // Assert
        verify(groupMemberRepository, times(2)).save(any(GroupMember.class));
    }

    @Test
    void removeMember_Success() {
        // Arrange
        Long memberId = 1L;
        GroupMember member = new GroupMember();
        User user = new User(sampleUser);
        user.setId(2L);
        Group group = new Group();
        User creator = new User(sampleUser);
        creator.setId(1L);
        group.setCreatedBy(creator);
        member.setUser(user);
        member.setGroup(group);

        when(groupMemberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // Act
        groupService.removeMember(memberId);

        // Assert
        verify(groupMemberRepository).delete(member);
    }

    @Test
    void removeMember_CreatorAttempt() {
        // Arrange
        Long memberId = 1L;
        GroupMember member = new GroupMember();
        User creator = new User(sampleUser);
        creator.setId(1L);
        Group group = new Group();
        group.setCreatedBy(creator);
        member.setUser(creator);
        member.setGroup(group);

        when(groupMemberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> groupService.removeMember(memberId));
    }

    @Test
    void getGroupMembers_Success() {
        // Arrange
        Long groupId = 1L;
        Group group = new Group();
        group.setId(groupId);
        List<GroupMember> expectedMembers = Arrays.asList(new GroupMember(), new GroupMember());

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMemberRepository.findByGroup(group)).thenReturn(expectedMembers);

        // Act
        List<GroupMember> result = groupService.getGroupMembers(groupId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}