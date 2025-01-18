package com.example.bank;


import com.example.bank.controller.BankController;
import com.example.bank.model.Deposit;
import com.example.bank.model.DepositOnly;
import com.example.bank.service.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankApplicationDepositTest {

    @MockBean
    private DepositService depositService;

    @InjectMocks
    private BankController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateClientValidInput() throws URISyntaxException {
        Deposit deposit = new Deposit();
        doReturn(new ResponseEntity<>(HttpStatus.CREATED)).when(depositService).saveDeposit(deposit);
        ResponseEntity<?> response = controller.createDeposit(deposit);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(depositService, times(1)).saveDeposit(deposit);
    }

    @Test
    void testDeleteClientsReturnOk() {
        long id = 1L;
        when(depositService.deleteDeposits(id)).thenReturn(true);
        ResponseEntity<?> response = controller.deleteDeposits(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(depositService, times(1)).deleteDeposits(id);
    }

    @Test
    void testDeleteClientsNot_Modified() {
        long id = 1L;
        when(depositService.deleteDeposits(id)).thenReturn(false);
        ResponseEntity<?> response = controller.deleteDeposits(id);
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        verify(depositService, times(1)).deleteDeposits(id);
    }

    @Test
    void testShouldReturnOkFound() throws IOException, ParseException, IOException, ParseException {
        String openingDate = "Дата открытия";
        Float percent = 10F;
        Integer termMonths = 12;
        String sortAscending = "true";
        String sortDescending = "false";
        List<String> types = Arrays.asList("retail", "investment");
        List<DepositOnly> expectedDeposits = new ArrayList<>();
        when(depositService.getClients(openingDate, percent, termMonths, sortAscending, sortDescending))
                .thenReturn(expectedDeposits);
        ResponseEntity<?> response = controller.getDepositName(openingDate, percent, termMonths, sortAscending, sortDescending, types);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedDeposits, response.getBody());
    }
}
