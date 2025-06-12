package io.kckim.kopring.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.kckim.kopring.app.MemberService
import io.kckim.kopring.dto.Role
import io.kckim.kopring.exception.MemberNotFoundException
import io.kckim.kopring.util.genMember
import io.kckim.kopring.util.genMemberDesc
import io.kckim.kopring.util.genMemberDescList
import io.kckim.kopring.util.genMemberList
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.Mockito.`when`

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(MemberApiController::class)
class MemberApiControllerSliceTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var service: MemberService

    @Autowired
    lateinit var om: ObjectMapper

    @Test
    fun `회원 저장 요청을 보내면 성공적으로 저장하고 회원 정보가 포함되어있는 응답을 200 OK로 내린다`() {
        val expectedName = "member1"
        val expectedEmail = "member1@test.com"
        val expectedRole = Role.BRONZE

        val expectedDesc = genMemberDesc(expectedName, expectedEmail, expectedRole)

        `when` ( service.save(expectedDesc)).thenReturn(expectedDesc)

        mockMvc.post("/api/members") {
            contentType = APPLICATION_JSON
            content = om.writeValueAsString(expectedDesc)
        }.andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$.data.name") { value(expectedName) }
            jsonPath("$.data.email") { value(expectedEmail) }
            jsonPath("$.data.role") { value(expectedRole.name) }
        }
    }

    @Test
    fun `존재하는 email로 회원을 조회하면 규격에 맞는 응답을 반황한다`() {
        val expectedName = "member1"
        val expectedEmail = "member1@test.com"
        val expectedRole = Role.BRONZE
        val expectedDesc = genMemberDesc(expectedName, expectedEmail, expectedRole)

        `when` ( service.getDescByEmail(expectedEmail)).thenReturn(expectedDesc)

        mockMvc.get("/api/members/${expectedEmail}")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.data.name") { value(expectedName) }
                jsonPath("$.data.email") { value(expectedEmail) }
                jsonPath("$.data.role") { value(expectedRole.name) }
            }
    }

    @Test
    fun `존재하지 않는 email로 회원을 조회하면 규격에 맞는 응답을 반환한다`() {
        val targetEmail = "unknown@test.com"

        `when` ( service.getDescByEmail(targetEmail)).thenThrow(MemberNotFoundException())

        mockMvc.get("/api/members/$targetEmail")
            .andExpect {
                status { isNotFound() }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.message"){ value(MemberNotFoundException().message!!) }
            }
    }

    @Test
    fun `회원 목록을 요청하면 MemberView로 목록을 반환한다`() {
        val size = 10

        val memberDescList = genMemberDescList(size)

        `when` ( service.getAllDescView()).thenReturn(memberDescList)


        val result = mockMvc.get("/api/members")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.data.size()") { value(size) }
            }
            .andReturn()

        val respStr = result.response.contentAsString
        val respJson = om.readTree(respStr)

        val respData = respJson["data"]

        for (i in 0 until size) {
            val expected = memberDescList[i]
            val actual = respData[i]

            assertThat(expected.name).isEqualTo(actual["name"].asText())
            assertThat(expected.email).isEqualTo(actual["email"].asText())
            assertThat(expected.role.name).isEqualTo(actual["role"].asText())
        }
    }
}
