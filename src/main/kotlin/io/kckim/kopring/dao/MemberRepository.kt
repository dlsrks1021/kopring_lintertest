package io.kckim.kopring.dao

import io.kckim.kopring.domain.Member
import io.kckim.kopring.dto.MemberView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

    @Query(
        """
        SELECT
          m.name as name,
          m.email as email,
          m.role as role
        FROM Member m
    """,
    )
    fun findAllMemberView(): List<MemberView>
}
