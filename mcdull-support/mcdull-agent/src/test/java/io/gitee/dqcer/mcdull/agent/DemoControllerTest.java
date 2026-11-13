package io.gitee.dqcer.mcdull.agent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void chat() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/chat?message=如何评价ollama？")).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void sendUserChatAndGetReply() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/send-user-chat-and-get-reply?message=如何评价ollama？&ext=要求必须带有强烈的主观评价"))
            .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }
}
