package com.example.bank.controller;


import com.example.bank.model.*;
import com.example.bank.service.BankService;
import com.example.bank.service.ClientService;
import com.example.bank.service.DepositService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import java.text.ParseException;

import java.util.List;
import java.util.Map;

@RestController
public class BankController {


    @Autowired
    private ClientService clientService;
    @Autowired
    private BankService bankService;
    @Autowired
    private DepositService depositService;


    //Запрос на создание нового клиента
    @PostMapping("/clients")

    public ResponseEntity<?> createClient(@RequestBody Clients client) throws URISyntaxException {
        clientService.createClients(client);
        return ResponseEntity.created(new URI("/clients/" + client)).build();
    }

    //Запрос на удаление клиента
    @DeleteMapping(value = "/clients/{id}") // добавить проверку существования

    public ResponseEntity<?> deleteClients(@PathVariable Long id) {
        final boolean deleted = clientService.deleteClients(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    //Запрос на изменение данных Клиента(Проверить) // добавить проверку существования

    @PutMapping(value = "/clients/{id}")
    public Clients updateClientField(@PathVariable Long id, @RequestBody Map<String, Object> body) throws NoSuchFieldException {
        // Получаем значение поля, которое нужно обновить
        String fieldName = (String) body.get("fieldName");
        Object newValue = body.get("newValue");
        Clients client = clientService.findByIdClient(id);
        if (client != null) {
            if (fieldName.equals("form")){
                Clients.OrganizationalForm newForm = Clients.OrganizationalForm.valueOf((String) newValue);
                 newValue = newForm;
            }
            // Обновляем поле в сущности


            // Устанавливаем новое значение для указанного поля
            try {
            Field field = client.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

                field.set(client, newValue);
                clientService.createClients(client);
                return client;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    //Запрос на ввывод только клиентов с фильтрацией(ДОБАВИТЬ ВЫВОД ID)
    @GetMapping("/clientsAll")
    public ResponseEntity<List<ClientOnly>> getClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String form,
            @RequestParam(required = false) List<String> types) throws IOException {


        List<ClientOnly> clients = clientService.getClients(name, shortName, address, form);
        return ResponseEntity.ok(clients);
    }


    // Запрос на вывод всех сущностей с фильтрацией()
    @GetMapping("/allbank")
    public ResponseEntity<?> getClientsNames(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String form,
            @RequestParam(required = false) String sortAscending,
            @RequestParam(required = false) String sortDescending,
            @RequestParam(required = false) List<String> types) throws IOException {

        List<Clients> clients = ClientService.loadAllClientFiltered(name, shortName, address, form, sortAscending, sortDescending);
        return ResponseEntity.ok(clients);

    }



//Запрос на создание нового банка

    @PostMapping(value = "/banks")
    public ResponseEntity<?> createBank(@RequestBody Bank bank) throws URISyntaxException {
        bankService.saveBank(bank);
        return ResponseEntity.created(new URI("/banks/" + bank.getId())).build();
    }

    //Запрос на удаление банка
    @DeleteMapping(value = "/bank/{id}")
    public ResponseEntity<?> deleteBank(@PathVariable(name = "id") Long id) {
        final boolean deleted = bankService.deleteBank(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
// Запрос на вывод всех банков с фильтрацией(проверить)

    @GetMapping("/bank")
    public ResponseEntity<?> getBankName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String bik,
            @RequestParam(required = false) List<String> types) throws IOException {


        List<BankOnly> banks = bankService.getBanks(name, bik);
        return ResponseEntity.ok(banks);


    }



    //Запрос на изменение данных Клиента(Проверить)

    @PutMapping(value = "/banks/{id}")
    public Bank updateBankField(@PathVariable Long id, @RequestBody Map<String, Object> body) throws NoSuchFieldException {
        // Получаем значение поля, которое нужно обновить
        String fieldName = (String) body.get("fieldName");
        Object newValue = body.get("newValue");

        // Обновляем поле в сущности
        Bank bank = bankService.findByIdBank(id);
        if (bank != null) {
            // Устанавливаем новое значение для указанного поля
            Field field = bank.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            try {
                field.set(bank, newValue);
                bankService.saveBank(bank);
                return bank;
            } catch (IllegalAccessException e) {
                // Обработка исключений, если поле не найдено или доступ к нему запрещен
                e.printStackTrace();
            }
        }
        return null;
    }


    // Запрос на добавление дипазита
    @PostMapping("/deposits")
    public ResponseEntity<?> createDeposit(@RequestBody Deposit deposit) throws URISyntaxException {
        depositService.saveDeposit(deposit);
        return ResponseEntity.created(new URI("/deposits/" + deposit.getId())).build();
    }


//запрос на удаление дипазита

    @DeleteMapping(value = "/deposits/{id}")
    public ResponseEntity<?> deleteDeposits(@PathVariable Long id) {
        boolean isDeleted = depositService.deleteDeposits(id);

        return isDeleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }


    @GetMapping("/deposit")
    public ResponseEntity<?> getDepositName(

            @RequestParam(required = false) String openingDate,
            @RequestParam(required = false) Float percent,
            @RequestParam(required = false) Integer termMonths,
            @RequestParam(required = false) String sortAscending,
            @RequestParam(required = false) String sortDescending,
            @RequestParam(required = false) List<String> types) throws IOException, ParseException {

        List<DepositOnly> depositDTOS = depositService.getClients(openingDate, percent, termMonths, sortAscending, sortDescending);

        return ResponseEntity.ok(depositDTOS);

    }


//Запрос на изменение данных Депозита(Проверить)

    @PutMapping(value = "/deposit/{id}")
    public Deposit updateDepositField(@PathVariable Long id, @RequestBody Map<String, Object> body) throws NoSuchFieldException {
        // Получаем значение поля, которое нужно обновить
        String fieldName = (String) body.get("fieldName");
        Object newValue = body.get("newValue");

        // Обновляем поле в сущности
        Deposit deposit = DepositService.findByIdDeposit(id);

        if (deposit != null) {
            try {
            // Устанавливаем новое значение для указанного поля
            Field field = deposit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

                field.set(deposit, newValue);
                depositService.saveDeposit(deposit);
                return deposit;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
