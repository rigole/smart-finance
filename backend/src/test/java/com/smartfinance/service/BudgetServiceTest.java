package com.smartfinance.service;

import com.smartfinance.dto.request.BudgetRequest;
import com.smartfinance.dto.response.BudgetResponse;
import com.smartfinance.model.Budget;
import com.smartfinance.model.User;
import com.smartfinance.repository.BudgetRepository;
import com.smartfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Unit Tests")
@MockitoSettings(strictness = Strictness.LENIENT)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    private User mockUser;
    private Budget mockBudget;
    private BudgetRequest mockBudgetRequest;

    @BeforeEach
    void setUp(){
        mockUser =  User.builder()
                .id(UUID.randomUUID())
                .email("test@hotmail.com")
                .fullName("Mock tester User")
                .password("password")
                .role("USER")
                .build();

        mockBudget =  Budget.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("159.00"))
                .spent(new BigDecimal("34.56"))
                .category("INTERNET")
                .user(mockUser)
                .build();

        mockBudgetRequest = new BudgetRequest();
        mockBudgetRequest.setAmount(new BigDecimal("159.00"));
        mockBudgetRequest.setSpent(new BigDecimal("34.56"));
        mockBudgetRequest.setCategory("INTERNET");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@hotmail.com");
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    @DisplayName("Should Create Budget Successfully")
    void shouldCreateBudget(){
        when(userRepository.findByEmail("test@hotmail.com")).thenReturn(Optional.of(mockUser));
        when(budgetRepository.save(any(Budget.class))).thenReturn(mockBudget);

        BudgetResponse response = budgetService.createBudget(mockBudgetRequest);

        assertNotNull(response);
        assertEquals(new BigDecimal("159.00"), response.getAmount());
        assertEquals(new BigDecimal("34.56"), response.getSpent());
        assertEquals("INTERNET", response.getCategory());

        verify(budgetRepository, times(1)).save(any(Budget.class));
    }
}
