package com.example.exam_proj

import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Order(1)
class RepositoryRunner(
    private val memberRepository: MemberRepository
) : CommandLineRunner {
    @Transactional
    override fun run(vararg args: String) {
        val member = Member(id = "id1", name = "Alice", age = 25)
        val savedMember = memberRepository.save(member)
        savedMember.age = 40


        println("====RepositoryMember=====")
        val found = memberRepository.findById("id1").orElse(null)
        println("findMember = ${found.name}, age = ${found.age}")

        val members = memberRepository.findAll()
        val memberSize = members.size
        println(memberSize)

        memberRepository.delete(member)
    }
}
