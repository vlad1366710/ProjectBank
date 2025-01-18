package com.example.bank;

import com.example.bank.controller.BankController;
import com.example.bank.model.ClientOnly;
import com.example.bank.model.Clients;
import com.example.bank.service.ClientService;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class BankApplicationClientsTests {

    @MockBean
    private ClientService clientService;
    @InjectMocks
    private BankController controller;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void createClientValidInput() throws URISyntaxException {
        Clients client = new Clients();
        doReturn(new ResponseEntity<>(HttpStatus.CREATED)).when(clientService).createClients(client);
        ResponseEntity<?> response = controller.createClient(client);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(clientService, times(1)).createClients(client);
    }

    @Test
    void deleteClientsReturnOk() {
        long id = 1L;
        when(clientService.deleteClients(id)).thenReturn(true);
        ResponseEntity<?> response = controller.deleteClients(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clientService, times(1)).deleteClients(id);
    }

    @Test
    void deleteClientsReturnNotModefied() {
        long id = 1L;
        when(clientService.deleteClients(id)).thenReturn(false);
        ResponseEntity<?> response = controller.deleteClients(id);
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        verify(clientService, times(1)).deleteClients(id);
    }

    @Test
    public void testUpdateClientShortName() throws NoSuchFieldException {
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fieldName", "shortName");
        requestBody.put("newValue", "TypeA");
        Clients client = new Clients();
        client.setShortName("TypeB");
        when(clientService.findByIdClient(id)).thenReturn(client);
        Clients updatedClient = controller.updateClientField(id, requestBody);
        assertEquals("TypeA", updatedClient.getShortName());
    }

    @Test
    public void testUpdateClientAddress() throws NoSuchFieldException {
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fieldName", "address");
        requestBody.put("newValue", "TypeA");
        Clients client = new Clients();
        client.setAddress("TypeB");
        when(clientService.findByIdClient(id)).thenReturn(client);
        Clients updatedClient = controller.updateClientField(id, requestBody);
        assertEquals("TypeA", updatedClient.getAddress());
    }

    @Test
    public void testUpdateClientForm() throws NoSuchFieldException {
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fieldName", "form");
        requestBody.put("newValue", "ОПФ");
        Clients client = new Clients();
        client.setForm(Clients.OrganizationalForm.valueOf("ИП"));
        when(clientService.findByIdClient(id)).thenReturn(client);
        Clients updatedClient = controller.updateClientField(id, requestBody);
        assertEquals(Clients.OrganizationalForm.valueOf("ОПФ"), updatedClient.getForm());
    }

    @Test
    public void testUpdateClientName() throws NoSuchFieldException {
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fieldName", "name");
        requestBody.put("newValue", "TypeA");
        Clients client = new Clients();
        client.setName("TypeB");
        when(clientService.findByIdClient(id)).thenReturn(client);
        Clients updatedClient = controller.updateClientField(id, requestBody);
        assertEquals("TypeA", updatedClient.getName());
    }

    @Test
    public void testUpdateClientNotFound() throws NoSuchFieldException {
        // Создание тестовых данных
        Long id = 1L;
        Map<String, Object> requestBody = new HashMap<>();

        when(clientService.findByIdClient(id)).thenReturn(null);
        Clients updatedClient = controller.updateClientField(id, requestBody);
        assertNull(updatedClient);
    }

    @Test
    void testShouldReturnClientsFound() throws IOException, IOException {
        String name = "Имя клиента";
        String shortName = "Краткое имя";
        String address = "Адрес";
        String form = "Форма собственности";
        List<String> types = Arrays.asList("retail", "investment");
        List<ClientOnly> expectedClients = new ArrayList<>();
        when(clientService.getClients(name, shortName, address, form))
                .thenReturn(expectedClients);
        ResponseEntity<List<ClientOnly>> response = controller.getClients(name, shortName, address, form, types);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedClients, response.getBody());
    }
}
