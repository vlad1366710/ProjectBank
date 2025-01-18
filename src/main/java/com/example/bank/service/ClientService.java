package com.example.bank.service;

import com.example.bank.model.*;
import com.example.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private static ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    //Создание Clienta и внесение его в бд 
    public ResponseEntity<Clients> createClients(Clients client) {

        clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.OK).body(client);

    }

    // Удалени клиента из бд  
    public boolean deleteClients(Long id) {
        // Проверка что такой клиент существует 
        Optional<Clients> foundClient = clientRepository.findById(id); // поиск в бд
        if (!foundClient.isEmpty()) {
            Clients clients = foundClient.get();// Если запись найдена, то присваиваем её переменной deposit
            this.clientRepository.delete(clients); // удаление
            return true;
        }else{
            return false;
        }









    }
    // метод применяющий фильтры к  результатам запроса на вывод
    public static List<Clients> loadAllClientFiltered(String name, String shortName, String address, String form, String sortAscending, String sortDescending) throws FileNotFoundException {
        Specification<Clients> spec = Specification.where(null);
        //проверка что задействован фильтра по наименованию
        if (!name.isEmpty()) {
            spec = spec.and(ClientHasName(name));
        }
        //проверка что задействован фильтра по короткому наименованию
        if (!shortName.isEmpty()) {
            spec = spec.and(ClientHasShortName(shortName));
        }
        //проверка что задействован фильтра по адресу
        if (!address.isEmpty()) {
            spec = spec.and(ClientHasAddress(address));
        }
        //проверка что задействован фильтра по Организационно-правовая форме
        if (form != null) {
            Clients.OrganizationalForm newForm = Clients.OrganizationalForm.valueOf( form);

            spec = spec.and(ClientHasForm(newForm));
        }

        // получение списка Сlients Deposit и Bank соответсвующих фильтрам
        List<Clients> clientDataList = clientRepository.findAll(spec);

        if (sortAscending != null || sortDescending != null) {
            // Сортировка по дате открытия
            for (Clients client : clientDataList) {
                List<Deposit> deposits = client.getDeposits();
                if (!deposits.isEmpty()) {
                    Collections.sort(deposits, Comparator.comparing(Deposit::getOpeningDate));
                    if (sortDescending.equals("true")) {
                        Collections.reverse(deposits);
                    }

                }
            }
        }



        if (clientDataList.isEmpty()) {
            throw new FileNotFoundException("Клиента с введенными фильтрами не найден");
        }
        return clientDataList;
    }

    // фильтр поиска записи Сlient по заданному наименованию

    private static Specification<Clients> ClientHasName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    // фильтр поиска записи Сlient по заданному короткому наименованию
    private static Specification<Clients> ClientHasShortName(String shortName) {
        return (root, query, cb) -> cb.like(root.get("short_name"), "%" + shortName + "%");
    }

    // фильтр поиска записи Сlient по заданному адрессу
    private static Specification<Clients> ClientHasAddress(String address) {
        return (root, query, cb) -> cb.like(root.get("address"), "%" + address + "%");
    }

    // фильтр поиска записи Сlient по заданной Организационно-правовая форме
    private static Specification<Clients> ClientHasForm(Enum form) {
        return (root, query, cb) -> cb.equal(root.<Clients>get("form"), form);
    }


    //Метод получения только клиентов
    public List<ClientOnly> getClients(String name, String shortName, String address, String form) throws FileNotFoundException {
        List<Clients> clients = loadAllClientFiltered(name, shortName, address, form, null, null);
        return clients.stream().map(client -> new ClientOnly(client.getId(), client.getName(), client.getShortName(), client.getAddress(), client.getForm())).collect(Collectors.toList());
    }

// Метод поиска клиента по id
    public Clients findByIdClient(Long id) {
        return clientRepository.findById(id).orElse(null);
    }




}
