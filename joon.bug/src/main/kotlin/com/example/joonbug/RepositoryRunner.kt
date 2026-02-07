package com.example.joonbug

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
        val member = Member(id = "id1", name = "JoonBug", age = 30)
        memberRepository.save(member)

        member.age = 31

        println("==== RepositoryRunner =====")
        val found = memberRepository.findById("id1").orElse(null)
        println("findMember = ${found?.name}, age = ${found?.age}")

        val members = memberRepository.findAll()
        println("member.size = ${members.size}")

        memberRepository.delete(member)
    }
}