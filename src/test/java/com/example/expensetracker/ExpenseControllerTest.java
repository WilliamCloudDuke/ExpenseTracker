package com.example.expensetracker;


import com.example.expensetracker.controller.ExpenseController;
import com.example.expensetracker.dto.ExpenseDto;
import com.example.expensetracker.exception.ExpenseNotFoundException;
import com.example.expensetracker.model.ExpenseCategory;
import com.example.expensetracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(controllers = ExpenseController.class)
class ExpenseControllerTest {

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should create expense")
    void shouldCreateExpense() throws Exception {
        //Created the DTO that will be sent to POST method
        ExpenseDto expenseDto = ExpenseDto.builder()
                .id("123")
                .expenseCategory(ExpenseCategory.ENTERTAINMENT)
                .expenseName("Movies")
                .expenseAmount(BigDecimal.TEN)
                .build();
        String json = """
                    {
                      "id": "123",
                      "expenseName": "Movies",
                      "expenseCategory": "ENTERTAINMENT",
                      "expenseAmount": 10
                    }                
                """;

        //Mock the response of the service class
        Mockito.when(expenseService.addExpense(expenseDto)).thenReturn("123");

        MvcResult mvcResult = mockMvc.perform(post("/api/expense")
                        .contentType("application/json").content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
                .andReturn();
        assertTrue(Objects.requireNonNull(mvcResult.getResponse().getHeaderValue(HttpHeaders.LOCATION)).toString().contains("123"));
    }


    @Test
    @DisplayName("Should return 404 Not Found Exception when calling expense endpoint with invalid id")
    void shouldReturn404ErrorResponseForGETWithInvalidId() throws Exception {
        Mockito.when(expenseService.getExpense("123")).thenThrow(new ExpenseNotFoundException("Cannot find Expense By id - 123"));

        MvcResult mvcResult = mockMvc.perform(get("/api/expense/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("https://api.expenses.com/errors/not-found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Expense not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCategory").value("Generic")).andReturn();
    }

}
