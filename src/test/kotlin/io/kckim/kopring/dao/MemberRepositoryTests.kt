package io.kckim.kopring.dao

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kckim.kopring.domain.Member
import io.kckim.kopring.util.genMember
import io.kckim.kopring.util.genMemberList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

// @DataJpaTest
@SpringBootTest
@DisplayName("Slice tests")
class MemberRepositoryTests(
    @Autowired var repository: MemberRepository,
) {
    @Test
    fun `주입 테스트`() {
        log.info { repository }
        assertThat(repository).isNotNull
    }

    @Test
    fun `회원을 생성해서 저장하면 id와 생성날짜, 수정날짜가 자동으로 등록된다`() {
        val targetName = "member1"
        val targetEmail = "member1@email.com"
        val member = genMember(targetName, targetEmail)

        val saved: Member = repository.save(member)

        assertThat(saved).isNotNull
        assertThat(saved.id).isNotNull
        assertThat(saved.createdAt).isNotNull
        assertThat(saved.updatedAt).isNotNull

        val now = LocalDateTime.now()

        assertThat(saved.createdAt).isBefore(now)
        assertThat(saved.updatedAt).isBefore(now)
    }

    @Test
    fun `회원 저장 후 findAllMemberView 메서드를 통해서 리스트를 불러오면 MemberView 타입으로 불러올 수 있다`() {
        val size = 10
        val members = genMemberList(size)
        repository.saveAll(members)

        val result = repository.findAllMemberView()

        assertThat(members.size).isEqualTo(10)
        assertThat(result).isNotNull
        assertThat(result.size).isEqualTo(size)

        result.forEachIndexed { index, memberView ->
            assertThat(memberView.name).isEqualTo(members[index].name)
        }
    }
}
