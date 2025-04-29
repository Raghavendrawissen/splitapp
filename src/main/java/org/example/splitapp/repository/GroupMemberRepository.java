package org.example.splitapp.repository;

import org.example.splitapp.model.Group;
import org.example.splitapp.model.GroupMember;
import org.example.splitapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupAndUser(Group group, User user);
    List<GroupMember> findByGroup(Group group);
}