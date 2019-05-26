package com.assignment.shareapp.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.assignment.shareapp.repository.MemberRepository;
import com.assignment.shareapp.exception.ResourceNotFoundException;
import com.assignment.shareapp.model.Member;

@RestController
@RequestMapping("/api/v1")
public class MemberController {
	
	@Autowired
	MemberRepository memberRepository;
	
	// Get All Members
	@GetMapping("/members")
	public List<Member> getAllMembers() {
	    return memberRepository.findAll();
	}
	
	// Get a Single Member
	@GetMapping("/members/{id}")
	public Member getMemberById(@PathVariable(value = "id") Integer memberId) {
	    return memberRepository.findById(memberId)
	            .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
	}

	// Create a new Member
	@PostMapping("/members")
	public Member createMember(@Valid @RequestBody Member member) {
	    return memberRepository.save(member);
	}
	
	// Update a Member
	@PutMapping("/members/{id}")
	public Member updateMember(@PathVariable(value = "id") Integer memberId,
	                                        @Valid @RequestBody Member memberDetails) {

		Member member = memberRepository.findById(memberId)
	            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", memberId));

		member.setMemberName(memberDetails.getMemberName());

		Member updatedGroup = memberRepository.save(member);
	    return updatedGroup;
	}
	
	// Delete a Member
	@DeleteMapping("/members/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable(value = "id") Integer memberId) {
		Member member = memberRepository.findById(memberId)
	            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", memberId));

		memberRepository.delete(member);

	    return ResponseEntity.ok().build();
	}
}
