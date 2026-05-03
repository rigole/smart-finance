package com.smartfinance.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.smartfinance.model.Budget;
import com.smartfinance.model.Transaction;
import com.smartfinance.model.User;
import com.smartfinance.repository.BudgetRepository;
import com.smartfinance.repository.TransactionRepository;
import com.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public byte[] exportTransactionCSV() throws IOException {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByDateDesc(user.getId());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter  writer = new OutputStreamWriter(outputStream);

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "Date", "Description", "Type", "Amount","Category"
        ));

        for (Transaction transaction : transactions) {
            csvPrinter.printRecord(
                    transaction.getDate(),
                    transaction.getDescription(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getCategory() != null ? transaction.getCategory().getName() : "N/A"
            );
        }
        csvPrinter.flush();
        return outputStream.toByteArray();
    }

    public byte[] exportBudgetCSV() throws IOException {
        User user = getCurrentUser();
        List<Budget> budgets = budgetRepository.findByUserId(user.getId());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter  writer = new OutputStreamWriter(outputStream);

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "Category","Limit","Spent","Remaining"
        ));

        for (Budget budget : budgets) {
            csvPrinter.printRecord(
                    budget.getCategory(),
                    budget.getAmount(),
                    budget.getSpent() != null ? budget.getSpent() : 0,
                    budget.getAmount().subtract(
                            budget.getSpent() != null ? budget.getSpent() : java.math.BigDecimal.ZERO)
            );
        }
        csvPrinter.flush();
        return outputStream.toByteArray();
    }

    public byte[] exportTransactionPDF() throws IOException {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByDateDesc(user.getId());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titleParagraph = new Paragraph("Smart Finance - Transaction Report", titleFont);
            titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            titleParagraph.setSpacingBefore(20);
            document.add(titleParagraph);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph subTitleParagraph = new Paragraph("Generated for: " + user.getEmail(), subFont);
            subTitleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            subTitleParagraph.setSpacingBefore(20);
            document.add(subTitleParagraph);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            addTableHeader(table, "Date", "Description", "Type", "Amount", "Category");

            for (Transaction transaction : transactions) {
                table.addCell(transaction.getDate().toString());
                table.addCell(transaction.getDescription() != null ? transaction.getDescription() : "");
                table.addCell(transaction.getType());
                table.addCell(transaction.getAmount().toString());
                table.addCell(transaction.getCategory() != null ? transaction.getCategory().getName() : "N/A");
            }

            document.add(table);

            document.add(new Paragraph(""));
            Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            document.add(new Paragraph("Total transactions " + transactions.size(), summaryFont));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    public byte[] exportBudgetsPDF() throws IOException {
        User user = getCurrentUser();
        List<Budget> budgets = budgetRepository.findByUserId(user.getId());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18);

            Paragraph titleParagraph = new Paragraph("Smart Finance - Budget Report", titleFont);

            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingAfter(20);
            document.add(titleParagraph);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph sub = new Paragraph("Generated for: " + user.getEmail(), subFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            sub.setSpacingAfter(20);
            document.add(sub);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            addTableHeader(table, "Category", "Limit", "Spent", "Remaining");

            for  (Budget budget : budgets) {
                java.math.BigDecimal spent = budget.getSpent() !=  null ? budget.getSpent() : java.math.BigDecimal.ZERO;
                java.math.BigDecimal remaining = budget.getAmount().subtract(spent);

                table.addCell(budget.getCategory());
                table.addCell("$" + budget.getAmount());
                table.addCell("$" + spent);
                table.addCell("$" + remaining);
            }
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();

        }
        return outputStream.toByteArray();
    }
    private void addTableHeader(PdfPTable table, String... headers) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(new java.awt.Color(0, 188, 212));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(8);
            table.addCell(headerCell);
        }
    }
}


