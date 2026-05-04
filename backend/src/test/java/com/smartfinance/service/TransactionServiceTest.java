package com.smartfinance.service;


import com.smartfinance.BackendApplication;
import com.smartfinance.dto.request.TransactionRequest;
import com.smartfinance.dto.response.TransactionResponse;
import com.smartfinance.model.Transaction;
import com.smartfinance.model.User;
import com.smartfinance.repository.CategoryRepository;
import com.smartfinance.repository.TransactionRepository;
import com.smartfinance.repository.UserRepository;
import org.h2.engine.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Unit Tests")
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private CategoryRepository  categoryRepository;

    @Mock
    private UserRepository userRepository;

    private User mockUser;
    private Transaction mockTransaction;
    private TransactionRequest mockTransactionRequest;

    @BeforeEach
    void setUp(){
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@hotmail.com")
                .fullName("Mock tester User")
                .password("password")
                .role("USER")
                .build();

        mockTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("150.00"))
                .description("Test description Mock")
                .type("EXPENSE")
                .date(LocalDate.now())
                .user(mockUser)
                .build();
        mockTransactionRequest = new TransactionRequest();
        mockTransactionRequest.setAmount(new BigDecimal("150.00"));
        mockTransactionRequest.setDescription("Test description Mock");
        mockTransactionRequest.setType("EXPENSE");
        mockTransactionRequest.setDate(LocalDate.now());

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@hotmail.com");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void shouldCreateTransaction(){
        when(userRepository.findByEmail("test@hotmail.com")).thenReturn(Optional.of(mockUser));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

        TransactionResponse response = transactionService.createTransaction(mockTransactionRequest);

        assertNotNull(response);
        assertEquals(new BigDecimal("150.00"), response.getAmount());
        assertEquals("EXPENSE", response.getType());
        assertEquals("Test description Mock", response.getDescription());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found on create")
    void shouldThrowExceptionWhenUserNotFoundOnCreate(){
        when(userRepository.findByEmail("test@hotmail")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> transactionService.createTransaction(mockTransactionRequest));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should return all transactions for user")
    void shouldReturnAllTransactionsForUser(){
        when(userRepository.findByEmail("test@hotmail")).thenReturn(Optional.of(mockUser));

        when(transactionRepository.findByUserIdOrderByDateDesc(mockUser.getId())).thenReturn(List.of(mockTransaction));


        List<TransactionResponse> responses = transactionService.getAllTransactions(mockUser);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("EXPENSE", responses.get(0).getType());
        verify(transactionRepository, times(1)).findByUserIdOrderByDateDesc(mockUser.getId());
    }

    @Test
    @DisplayName("Should return empty list when no transactions")
    void shouldReturnEmptyListWhenNoTransactions(){
        when(userRepository.findByEmail("test@hotjhmail")).thenReturn(Optional.of(mockUser));
        when(transactionRepository.findByUserIdOrderByDateDesc(mockUser.getId())).thenReturn(List.of());
        List<TransactionResponse> responses = transactionService.getAllTransactions(mockUser);
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }





}
