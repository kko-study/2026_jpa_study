package homework.chapter_13.controller

import homework.chapter_13.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * OSIV 보너스 테스트용 컨트롤러.
 * OSIV=true일 때 컨트롤러에서도 Lazy 로딩이 동작하는지 확인합니다.
 */
@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @GetMapping("/members/{id}")
    fun getMember(@PathVariable("id") id: Long): ResponseEntity<String> {
        val member = memberService.findMember(id)
        // OSIV=true이면 여기서도 Lazy 로딩 가능
        // OSIV=false이면 LazyInitializationException 발생
        val teamName = member.team?.name
        return ResponseEntity.ok("${member.name} - $teamName")
    }
}