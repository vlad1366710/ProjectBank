package com.example.bank.service;

import com.example.bank.model.*;
import com.example.bank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    //Создание Bank и внесение его в бд
    public ResponseEntity<Bank> saveBank(Bank bank) {

        this.bankRepository.save(bank);
        return ResponseEntity.status(HttpStatus.OK).body(bank);

    }


    public boolean deleteBank(Long id) {
        Optional<Bank> foundClient = bankRepository.findById(id); // поиск в бд
        if (!foundClient.isEmpty()) {
            Bank bank = foundClient.get();// Если запись найдена, то присваиваем её переменной deposit
            this.bankRepository.delete(bank); // удаление
            return true;
        }else{
            return false;
        }
    }

    // метод применяющий фильтры к  результатам запроса на вывод
    public List<Bank> loadAllBankFiltered(String name, String bik) throws FileNotFoundException {
        Specification<Bank> spec = Specification.where(null);

        //проверка что задействован фильтра по наименованию банка
        if (!name.isEmpty()) {
            spec = spec.and(BankHasName(name));
        }
        //проверка что задействован фильтра по бик
        if (!bik.isEmpty()) {
            spec = spec.and(BankHasBik(bik));
        }

        // получение списка Сlients Deposit и Bank соответсвующих фильтрам
        List<Bank> bankDataList = bankRepository.findAll(spec);

        if (bankDataList.isEmpty()) {
            throw new FileNotFoundException("Файла с введенными фильтрами не найден");
        }
        return bankDataList;
    }

    // фильтр поиска записи Сlient по заданному наименованию банка
    private static Specification<Bank> BankHasName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    // фильтр поиска записи Сlient по заданному бик
    private static Specification<Bank> BankHasBik(String bik) {
        return (root, query, cb) -> cb.like(root.get("bik"), "%" + bik + "%");
    }

    //Метод получения только Банков
    public List<BankOnly> getBanks(String name, String bik) throws FileNotFoundException {
        List<Bank> banks = loadAllBankFiltered(name, bik);
        return banks.stream()
                .map(bank -> new BankOnly(bank.getId(), bank.getName(), bank.getBik()))
                .collect(Collectors.toList());
    }

    //Метод поиска банков по id
    public Bank findByIdBank(Long id) {
        return bankRepository.findById(id).orElse(null);
    }


}
