package com.smartfinance.service;

import com.smartfinance.dto.request.TransactionRequest;
import com.smartfinance.dto.response.TransactionResponse;
import com.smartfinance.model.Category;
import com.smartfinance.model.Transaction;
import com.smartfinance.model.User;
import com.smartfinance.repository.CategoryRepository;
import com.smartfinance.repository.TransactionRepository;
import com.smartfinance.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<TransactionResponse> getAllTransactions(User user) {
        return transactionRepository
                .findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User managedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .type(request.getType())
                .date(request.getDate())
                .category(category)
                .user(managedUser)
                .build();

        return mapToResponse(
                transactionRepository.save(transaction));
    }

    public TransactionResponse updateTransaction(UUID id, TransactionRequest request, User user ) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .ifPresent(transaction::setCategory);
        }

        return mapToResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(UUID id, User user) {
        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        transactionRepository.delete(transaction);
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .date(t.getDate())
                .type(t.getType())
                .categoryName(t.getCategory() != null
                        ? t.getCategory().getName() : null)
                .createdAt(t.getCreatedAt())
                .build();
    }
}
