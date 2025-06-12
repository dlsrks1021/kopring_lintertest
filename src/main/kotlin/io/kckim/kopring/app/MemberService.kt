package io.kckim.kopring.app

import io.kckim.kopring.dao.MemberRepository
import io.kckim.kopring.domain.Member
import io.kckim.kopring.dto.MemberDescription
import io.kckim.kopring.dto.MemberUpdateDescription
import io.kckim.kopring.dto.MemberView
import io.kckim.kopring.exception.MemberNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val repository: MemberRepository,
) {
    @Transactional
    fun save(desc: MemberDescription): MemberDescription {
        val member =
            Member(
                name = desc.name,
                email = desc.email,
                role = desc.role,
            )
        repository.save(member)
        return desc
    }

    fun findByEmail(email: String): Member = repository.findByEmail(email) ?: throw MemberNotFoundException()

    fun getDescByEmail(email: String): MemberDescription {
        val find = findByEmail(email)
        return MemberDescription(
            name = find.name,
            email = find.email,
            role = find.role,
        )
    }

    @Transactional
    fun update(
        email: String,
        updateDesc: MemberUpdateDescription,
    ): MemberUpdateDescription {
        val find = findByEmail(email)
        find.update(updateDesc)
        return MemberUpdateDescription(
            name = find.name,
            role = find.role,
        )
    }

    @Transactional
    fun delete(email: String) {
        val find = findByEmail(email)
        repository.delete(find)
    }

    fun getAllDescView(): List<MemberView> = repository.findAllMemberView()
}
