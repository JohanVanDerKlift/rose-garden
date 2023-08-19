package nl.johanvanderklift.roseGarden.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class WebOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllWebOrdersByUsername() {
    }

    @Test
    @DisplayName("Should get all WebOrders from username performed by ROLE_ADMIN")
    void testGetAllWebOrdersByUsername() throws Exception {
//        mockMvc.perform(
//                get("/weborder/admin?username=tester")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
    }

    @Test
    void getWebOrderById() {

    }

    @Test
    void testGetWebOrderById() {
    }

    @Test
    void createWebOrder() throws Exception {
        String requestJson = """
                {
                    "quantity": "2",
                    "productId": "2"
                }
                """;

        mockMvc
                .perform(post("/weborder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addWebOrderDetailToWebOrder() {
    }

    @Test
    void confirmWebOrder() {
    }

    @Test
    void changeWebOrderStatus() {
    }

    @Test
    void deleteWebOrder() {
    }
}