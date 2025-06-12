package io.kckim.kopring.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MemberDescription(
    @field: NotBlank(message = "이름은 반드시 입력되어야 합니다.")
    override val name: String,
    @field: Email(message = "이메일 형식")
    @field: NotBlank(message = "이메일은 반드시 입력되어야 합니다.")
    override val email: String,
    @field: NotNull(message = "등급은 반드시 입력되어야 합니다.")
    override val role: Role,
): MemberView

data class MemberUpdateDescription(
    val name: String,
    val role: Role,
)