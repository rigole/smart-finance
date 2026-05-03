package com.smartfinance.controller;

import com.smartfinance.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ExportController {
    private final ExportService exportService;

    @GetMapping("/transactions/csv")
    public ResponseEntity<byte[]> transactionCsv() throws IOException {
        byte[] data = exportService.exportTransactionCSV();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/transactions/pdf")
    public ResponseEntity<byte[]> transactionsPDF() throws IOException {
        byte[] data = exportService.exportTransactionPDF();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=transactions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    @GetMapping("/budgets/csv")
    public ResponseEntity<byte[]> budgetsCSV() throws IOException {
        byte[] data = exportService.exportBudgetCSV();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=budgets.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/budgets/pdf")
    public ResponseEntity<byte[]> budgetsPdf() throws IOException {
        byte[] data = exportService.exportBudgetsPDF();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=budgets.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}
