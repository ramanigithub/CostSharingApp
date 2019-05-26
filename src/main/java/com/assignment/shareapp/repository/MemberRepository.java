package com.assignment.shareapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.shareapp.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

}
