package com.example.bank;


import com.example.bank.controller.BankController;
import com.example.bank.model.Bank;
import com.example.bank.model.BankOnly;
import com.example.bank.service.BankService;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class BankApplicationBankTest {

    @MockBean
    private BankService bankService;

    @InjectMocks
    private BankController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateClientValidInput() throws URISyntaxException {
        Bank bank = new Bank();
        doReturn(new ResponseEntity<>(HttpStatus.CREATED)).when(bankService).saveBank(bank);
        ResponseEntity<?> response = controller.createBank(bank);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bankService, times(1)).saveBank(bank);
    }
    @Test
    void testDeleteClientsReturnOk() {
        long id = 1L;
        when(bankService.deleteBank(id)).thenReturn(true);
        ResponseEntity<?> response = controller.deleteBank(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bankService, times(1)).deleteBank(id);
    }

    @Test
    void testDeleteClientsReturnNot_Modified() {
        long id = 1L;
        when(bankService.deleteBank(id)).thenReturn(false);
        ResponseEntity<?> response = controller.deleteBank(id);
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        verify(bankService, times(1)).deleteBank(id);
    }

    @Test
    public void testUpdateName() throws NoSuchFieldException {
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fieldName", "name");
        requestBody.put("newValue", "TypeA");
        Bank bank = new Bank();
        bank.setName("TypeB");
        when(bankService.findByIdBank(id)).thenReturn(bank);
        Bank updateBank = controller.updateBankField(id, requestBody);
        assertEquals("TypeA", updateBank.getName());
    }
    @Test
    public void testUpdateBik() throws NoSuchFieldException {
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fieldName", "bik");
        requestBody.put("newValue", "9854685");
        Bank bank = new Bank();
        bank.setBik("TypeB");
        when(bankService.findByIdBank(id)).thenReturn(bank);
        Bank updateBank = controller.updateBankField(id, requestBody);
        assertEquals("9854685", updateBank.getBik());
    }
    @Test
    void testShouldReturnOk() throws IOException {
        String name = "Название банка";
        String bik = "04012345";
        List<String> types = Arrays.asList("retail", "investment");
        List<BankOnly> expectedBanks = new ArrayList<>();
        when(bankService.getBanks(name, bik)).thenReturn(expectedBanks);
        ResponseEntity<?> response = controller.getBankName(name, bik, types);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedBanks, response.getBody());
    }

}
