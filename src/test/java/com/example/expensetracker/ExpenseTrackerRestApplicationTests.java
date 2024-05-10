package com.example.expensetracker;

import com.example.expensetracker.dto.ExpenseDto;
import com.example.expensetracker.model.ExpenseCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ExpenseTrackerRestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4");


    static {
        mongoDBContainer.start();
    }


    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Test
    @DisplayName("Should create expense, and verify the expense by GET")
    void shouldCreateExpenseAndGetTheExpense() throws Exception {
        String json = """
                    {
                      "id": "123",
                      "expenseName": "Movies",
                      "expenseCategory": "ENTERTAINMENT",
                      "expenseAmount": 10
                    }                
                """;
        ExpenseDto expenseDTO = ExpenseDto.builder()
                .expenseCategory(ExpenseCategory.ENTERTAINMENT)
                .expenseName("Movies")
                .expenseAmount(BigDecimal.TEN)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("api/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION)).andReturn();

        String expenseUrl = mvcResult.getResponse().getHeaderValue(HttpHeaders.LOCATION).toString();

        //Fetch the object by using the GET endpoint

        mockMvc.perform(get(expenseUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expenseAmount").value(BigDecimal.TEN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expenseCategory").value(ExpenseCategory.ENTERTAINMENT.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expenseName").value("Movies"));
    }


}
