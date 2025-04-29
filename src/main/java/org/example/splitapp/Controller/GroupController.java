package org.example.splitapp.Controller;

import org.example.splitapp.model.Group;
import org.example.splitapp.model.GroupMember;
import org.example.splitapp.Service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @PutMapping("/{groupId}/name")
    public ResponseEntity<Group> updateGroupName(
            @PathVariable Long groupId,
            @RequestParam String newName) {
        return ResponseEntity.ok(groupService.updateGroupName(groupId, newName));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Group>> getGroupsByCreator(@PathVariable Long userId) {
        return ResponseEntity.ok(groupService.findGroupsByCreator(userId));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addGroupMembers(
            @PathVariable Long groupId,
            @RequestBody List<Long> memberIds) {
        groupService.addMembersToGroup(groupId, memberIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<?> removeMember(@PathVariable Long memberId) {
        groupService.removeMember(memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getGroupMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getGroupMembers(groupId));
    }
}