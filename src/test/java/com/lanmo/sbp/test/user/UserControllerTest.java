package com.lanmo.sbp.test.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lanmo.sbp.rpc.Rpc;
import com.lanmo.sbp.service.UserService;
import com.lanmo.sbp.test.SbpTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class UserControllerTest extends SbpTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    protected MockMvc getMockMvc() {
        return mockMvc;
    }

    @BeforeAll
    public void beforeWorker() {
        System.out.println("i am before worker");
        userService.addUser();
    }

    @Test
    @Order(1)
    public void testGetId(){
        try {
            ResultActions resultActions = this.getMockMvc()
                .perform(get("/user/get?id=1", "xxx")
                    .header("X-Token", "test")
                    .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
            resultActions.andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void testGetList(){
        try {
            ResultActions resultActions = this.getMockMvc()
                .perform(get("/user/list", "xxx")
                    .header("X-Token", "test")
                    .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andDo(print());
            resultActions.andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
