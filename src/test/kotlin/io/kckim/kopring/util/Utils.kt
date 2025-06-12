package io.kckim.kopring.util

import io.kckim.kopring.domain.Member
import io.kckim.kopring.dto.MemberDescription
import io.kckim.kopring.dto.Role

fun genMember(targetName: String, targetEmail: String, targetRole: Role = Role.BRONZE
) = Member(null, targetName, targetEmail, targetRole)

fun genMemberDesc(
    actualName: String,
    actualEmail: String,
    actualRole: Role
) = MemberDescription(actualName, actualEmail, actualRole)

fun genMemberList(size: Int) : List<Member>{
    val members: MutableList<Member> = mutableListOf()
    for (i in 1..size) {
        members.add(genMember("member$i", "member${i}@email.com"))
    }
    return members
}

fun genMemberDescList(size: Int) : List<MemberDescription>{
    val memberDescList: MutableList<MemberDescription> = mutableListOf()
    for (i in 1..size) {
        memberDescList.add(genMemberDesc("member$i", "member${i}@email.com", Role.BRONZE))
    }
    return memberDescList
}