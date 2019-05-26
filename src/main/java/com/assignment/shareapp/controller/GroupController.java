package com.assignment.shareapp.controller;

import com.assignment.shareapp.exception.ResourceNotFoundException;
import com.assignment.shareapp.model.Group;
import com.assignment.shareapp.model.Member;
import com.assignment.shareapp.repository.GroupRepository;
import com.assignment.shareapp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    MemberRepository memberRepository;

    // Get All Groups
    @GetMapping("/groups")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // Get a Single Group
    @GetMapping("/groups/{id}")
    public Group getGroupById(@PathVariable(value = "id") Integer groupId) {
        return groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));
    }

    // Create a new Group
    @PostMapping("/groups")
    public Group createGroup(@Valid @RequestBody Group group) {
        return groupRepository.save(group);
    }

    // Update a Group
    @PutMapping("/groups/{id}")
    public Group updateGroup(@PathVariable(value = "id") Integer groupId,
                             @Valid @RequestBody Group groupDetails) {

        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));

        group.setGroupName(groupDetails.getGroupName());


        Group updatedGroup = groupRepository.save(group);
        return updatedGroup;
    }

    // Delete a Group
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") Integer groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));

        groupRepository.delete(group);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/groups/{id}/members")
    public Group linkMemebers(@PathVariable(value = "id") Integer groupId, @Valid @RequestBody Set<Integer> members) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));

        Set<Member> hsMem = new HashSet<Member>();

        for (Integer id: members) {
            Member member =
                memberRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

            hsMem.add(member);
        }

        group.setMembers(hsMem);

        Group updatedGroup = groupRepository.save(group);

        return updatedGroup;

    }
}
