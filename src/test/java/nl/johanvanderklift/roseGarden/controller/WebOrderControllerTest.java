package nl.johanvanderklift.roseGarden.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.johanvanderklift.roseGarden.model.WebOrder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.is;
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
    @WithUserDetails("tester2")
    void getAllWebOrdersByUsername() throws Exception {
        String username = "tester";
        mockMvc.perform(get("/weborder"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should get all WebOrders from username performed by ROLE_ADMIN")
    @WithUserDetails("tester2")
    void testGetAllWebOrdersByUsername() throws Exception {
        mockMvc.perform(
                get("/weborder/admin?username=tester")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));

    }

    @Test
    @WithUserDetails("tester")
    void getWebOrderById() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/weborder/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.webOrderStatus", is("PENDING")));
    }

    @Test
    void testGetWebOrderById() {
    }

    @Test
    @DisplayName("Should create webOrder")
    @WithUserDetails("tester")
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