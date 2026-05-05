package com.smartfinance.service;

import com.smartfinance.BackendApplication;
import com.smartfinance.dto.request.TransactionRequest;
import com.smartfinance.dto.response.TransactionResponse;
import com.smartfinance.model.User;
import com.smartfinance.repository.CategoryRepository;
import com.smartfinance.repository.TransactionRepository;
import com.smartfinance.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("TransactionService Integration Tests")
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        transactionRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();

        testUser = User.builder()
                .email("test@hotmail.com")
                .fullName("Mock tester User")
                .password("password")
                .role("USER")
                .build();

        testUser = userRepository.saveAndFlush(testUser);

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                testUser.getEmail(), null, List.of()
                        )
                );
    }

    @Test
    @DisplayName("Should save INCOME transaction to database")
    void shouldSaveTransactionToDatabase() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("500.00"));
        request.setDescription("Monthly salary");
        request.setType("INCOME");
        request.setDate(LocalDate.now());

        TransactionResponse response =
                transactionService.createTransaction(request);

        assertNotNull(response.getId());
        assertEquals(new BigDecimal("500.00"), response.getAmount());
        assertEquals("INCOME", response.getType());
        assertEquals("Monthly salary", response.getDescription());
        assertTrue(transactionRepository
                .findById(response.getId()).isPresent());
    }

    @Test
    @DisplayName("Should save EXPENSE transaction to database")
    void shouldSaveExpenseTransactionToDatabase() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("150.00"));
        request.setDescription("Grocery shopping");
        request.setType("EXPENSE");
        request.setDate(LocalDate.now());

        TransactionResponse response =
                transactionService.createTransaction(request);

        assertNotNull(response.getId());
        assertEquals("EXPENSE", response.getType());
        assertTrue(transactionRepository
                .findById(response.getId()).isPresent());
    }

    @Test
    @DisplayName("Should get all user transactions from database")
    void shouldGetAllTransactionsFromDatabase() {
        for (int i = 1; i <= 3; i++) {
            TransactionRequest req = new TransactionRequest();
            req.setAmount(new BigDecimal(i * 100));
            req.setDescription("Transaction " + i);
            req.setType("EXPENSE");
            req.setDate(LocalDate.now());
            transactionService.createTransaction(req);
        }

        List<TransactionResponse> responses =
                transactionService.getAllTransactions(testUser);

        assertEquals(3, responses.size());
    }

    @Test
    @DisplayName("Should return empty list when user has no transactions")
    void shouldReturnEmptyListWhenNoTransactions() {
        List<TransactionResponse> responses =
                transactionService.getAllTransactions(testUser);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should delete transaction from database")
    void shouldDeleteTransactionFromDatabase() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("To be deleted");
        request.setType("EXPENSE");
        request.setDate(LocalDate.now());

        TransactionResponse created =
                transactionService.createTransaction(request);

        assertTrue(transactionRepository
                .findById(created.getId()).isPresent());

        transactionService.deleteTransaction(created.getId(), testUser);

        assertFalse(transactionRepository
                .findById(created.getId()).isPresent());
    }

    @Test
    @DisplayName("Should update transaction in database")
    void shouldUpdateTransactionInDatabase() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Original");
        request.setType("EXPENSE");
        request.setDate(LocalDate.now());

        TransactionResponse created =
                transactionService.createTransaction(request);

        TransactionRequest updateRequest = new TransactionRequest();
        updateRequest.setAmount(new BigDecimal("200.00"));
        updateRequest.setDescription("Updated");
        updateRequest.setType("INCOME");
        updateRequest.setDate(LocalDate.now());

        TransactionResponse updated =
                transactionService.updateTransaction(
                        created.getId(), updateRequest, testUser);

        assertNotNull(updated);
        assertEquals(new BigDecimal("200.00"), updated.getAmount());
        assertEquals("Updated", updated.getDescription());
        assertEquals("INCOME", updated.getType());
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }
}