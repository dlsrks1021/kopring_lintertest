package io.kckim.kopring.app

import io.kckim.kopring.dao.MemberRepository
import io.kckim.kopring.dto.Role
import io.kckim.kopring.util.genMember
import io.kckim.kopring.util.genMemberDesc
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MemberServiceUnitTest {
    val repository = mockk<MemberRepository>()
    val service = MemberService(repository)

    @Test
    fun `적절한 데이터가 들어온다면 저장된 후 MemberDescription을 반환`() {
        val actualName = "member1"
        val actualEmail = "member@example.com"
        val actualRole = Role.BRONZE

        val memberDescription = genMemberDesc(actualName, actualEmail, actualRole)
        val member = genMember(actualName, actualEmail, actualRole)

        every { repository.save(member) } returns member

        val expectedMemberDescription = service.save(memberDescription)
        expectedMemberDescription.name shouldBe actualName
        expectedMemberDescription.email shouldBe actualEmail
        expectedMemberDescription.role shouldBe actualRole
    }

    @Test
    fun `회원이 저장되어있고 findByEmail을 호출하면 회원을 정상적으로 조회한다`() {
        val actualName = "member1"
        val actualEmail = "member@example.com"
        val actualRole = Role.BRONZE

        val expected = genMember(actualName, actualEmail, actualRole)
        every { repository.findByEmail(actualEmail) } returns expected

        val actual = service.findByEmail(actualEmail)

        actual.name shouldBe expected.name
        actual.email shouldBe expected.email
        actual.role shouldBe expected.role
    }

    @Test
    fun `없는 회원의 이메일로 findByEmail을 호출하면 NoSuchElementException이 발생`() {
        val unavailableEmail = "<EMAIL>"

        every { repository.findByEmail(unavailableEmail) } returns null

        val actual =
            assertThrows<NoSuchElementException> { service.findByEmail(unavailableEmail) }
        actual.message shouldBe "회원이 존재하지 않습니다"

        verify(exactly = 1) { repository.findByEmail(unavailableEmail) }
    }

    @Test
    fun `회원이 저장되어 있고 getDescByEmail을 호출하면 MemberDescription을 정상적으로 반환한다`() {
        val targetEmail = "member1@test.com"
        val expected = genMember("member1", targetEmail, Role.BRONZE)

        every { repository.findByEmail(targetEmail) } returns expected

        val actual = service.getDescByEmail(targetEmail)

        actual.name shouldBe expected.name
        actual.email shouldBe expected.email
        actual.role shouldBe expected.role
    }
}

// #1 BDD
class KopringMemberServiceUnitTest :
    BehaviorSpec({
        val repository = mockk<MemberRepository>()
        val service = MemberService(repository)

        // Kotest
        Given("적절한 데이터가 주어지고") {
            val actualName = "member1"
            val actualEmail = "member@example.com"
            val actualRole = Role.BRONZE

            val memberDescription = genMemberDesc(actualName, actualEmail, actualRole)
            val member = genMember(actualName, actualEmail, actualRole)

            every { repository.save(member) } returns member

            When("memberService의 save 호출하면") {
                val expected = service.save(memberDescription)

                Then("반환된 memberDescription의 값은 주어진 값과 동일할 것이다") {
                    expected.name shouldBe actualName
                    expected.email shouldBe actualEmail
                    expected.role shouldBe actualRole
                }

                Then("repository의 save가 한 번 호출될 것이다") {
                    verify(exactly = 1) { repository.save(member) }
                }
            }
        }
    })

// #2 함수 기반 스타일
class MemberServiceUnitTestFunSpec :
    FunSpec({
        val repository = mockk<MemberRepository>()
        val service = MemberService(repository)

        test("적절한 데이터가 들어온다면 저장한 후 MemberDescription을 반환한다") {
            val actualName = "member1"
            val actualEmail = "member@example.com"
            val actualRole = Role.BRONZE

            val memberDescription = genMemberDesc(actualName, actualEmail, actualRole)
            val member = genMember(actualName, actualEmail, actualRole)

            every { repository.save(member) } returns member

            val expectedMemberDescription = service.save(memberDescription)
            expectedMemberDescription.name shouldBe actualName
            expectedMemberDescription.email shouldBe actualEmail
            expectedMemberDescription.role shouldBe actualRole
        }
    })

// #3 StringSpec
class MemberServiceUnitTestStringSpec :
    StringSpec({
        val repository = mockk<MemberRepository>()
        val service = MemberService(repository)

        "적절한 데이터가 들어온다면 저장된 후 MemberDescription을 반환" {
            val actualName = "member1"
            val actualEmail = "member@example.com"
            val actualRole = Role.BRONZE

            val memberDescription = genMemberDesc(actualName, actualEmail, actualRole)
            val member = genMember(actualName, actualEmail, actualRole)

            every { repository.save(member) } returns member

            val expectedMemberDescription = service.save(memberDescription)
            expectedMemberDescription.name shouldBe actualName
            expectedMemberDescription.email shouldBe actualEmail
            expectedMemberDescription.role shouldBe actualRole
        }
    })

class MemberServiceUnitTestWithDescriveSpec :
    DescribeSpec({

        val repository = mockk<MemberRepository>()
        val service = MemberService(repository)

        describe("MemberService의 save 메서드는") {
            context("적절한 데이터가 주어진다면") {
                val actualName = "member1"
                val actualEmail = "member@example.com"
                val actualRole = Role.BRONZE

                val memberDescription = genMemberDesc(actualName, actualEmail, actualRole)
                val member = genMember(actualName, actualEmail, actualRole)

                beforeTest {
                    every { repository.save(member) } returns member
                }

                it("데이터를 저장하고, 반환한다.") {
                    val expectedMemberDescription = service.save(memberDescription)
                    expectedMemberDescription.name shouldBe actualName
                    expectedMemberDescription.email shouldBe actualEmail
                    expectedMemberDescription.role shouldBe actualRole
                }

                it("repository를 통해 데이터를 저장한다.") {
                    verify(exactly = 1) { repository.save(member) }
                }
            }
        }
    })
