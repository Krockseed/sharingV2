package hello.sharingv2.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void register_success() throws Exception {
        //given
        MemberSignUpDto signUpDto = new MemberSignUpDto("w332w@mail", "1234", "skkkeed", "Kkkim");

        String json = mapper.writer().writeValueAsString(signUpDto);

        //then
        mockMvc.perform(post("/test/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void register_null_fail() throws Exception {
        //given
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "", "seed", "Kim");

        String json = mapper.writer().writeValueAsString(signUpDto);

        //then
        mockMvc.perform(post("/test/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}