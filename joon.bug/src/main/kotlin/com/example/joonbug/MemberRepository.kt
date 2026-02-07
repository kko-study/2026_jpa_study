package com.example.joonbug

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, String>