package io.kckim.kopring.api

import io.kckim.kopring.app.MemberService
import io.kckim.kopring.dto.GenericResponse
import io.kckim.kopring.dto.MemberDescription
import io.kckim.kopring.dto.MemberView
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin("*")
@RestController
@Validated
@RequestMapping("/api/members")
class MemberApiController(
    private val service: MemberService,
) {
    @PostMapping
    fun saveMember(
        @RequestBody saveRequest: MemberDescription,
    ): GenericResponse<MemberDescription> =
        GenericResponse(
            data = service.save(desc = saveRequest),
            message = "회원이 성공적으로 등록되었습니다!",
        )

    @GetMapping("/{email}")
    fun findMember(
        @PathVariable email: String,
    ): GenericResponse<MemberDescription> =
        GenericResponse(
            data = service.getDescByEmail(email),
            message = "dsd",
        )

    @GetMapping
    fun getMemberViews(): GenericResponse<List<MemberView>> =
        GenericResponse(
            service.getAllDescView(),
            "",
        )
}
