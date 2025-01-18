package com.example.bank.service;

import com.example.bank.model.*;
import com.example.bank.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepositService {
    private static DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    // Внесение Депозита и занесение в бд
    public ResponseEntity<Deposit> saveDeposit(Deposit deposit) {

        depositRepository.save(deposit); // внесение сущности в бд
        return ResponseEntity.status(HttpStatus.OK).body(deposit);

    }

    // Удаление сущности Deposit

    public boolean deleteDeposits(Long id) {
        // Проверка есть ли такая запись в бд
        Optional<Deposit> foundDeposit = depositRepository.findById(id); // поиск в бд
        if (!foundDeposit.isEmpty()) {
            Deposit deposit = foundDeposit.get();// Если запись найдена, то присваиваем её переменной deposit
            this.depositRepository.delete(deposit); // удаление
            return true;
        }else{
            return false;
        }


    }

    // метод применяющий фильтры к  результатам запроса на вывод
    public static List<Deposit> loadAllDepositFiltered(String openingDate, Float percent, Integer termMonths, String sortAscending, String sortDescending) throws FileNotFoundException, ParseException {
        Specification<Deposit> spec = Specification.where(null);


        //проверка что задействован фильтра по дате открытия
        if (openingDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(openingDate);
            spec = spec.and(depositHasOpeningDate(date));
        }
        //проверка что задействован фильтра по проценттам
        if (percent != null) {
            spec = spec.and(depositHasPercent(percent));
        }
        //проверка что задействован фильтра по месяцам
        if (termMonths != null) {
            spec = spec.and(depositHasTermMonths(termMonths));
        }

        // получение списка Deposit соответсвующих фильтрам
        List<Deposit> depositDataList = depositRepository.findAll(spec);

        //Сортировка по дате открытия
        if (sortAscending != null || sortDescending != null) {


            Collections.sort(depositDataList, Comparator.comparing(Deposit::getOpeningDate));
            if (sortDescending.equals("true")) {
                Collections.reverse(depositDataList);
            }

        }


        if (depositDataList.isEmpty()) {
            throw new FileNotFoundException("Депозита с введенными фильтрами не найдено");
        }
        return depositDataList;
    }

    // фильтр поиска записи Deposit по заданной дате открытия
    private static Specification<Deposit> depositHasOpeningDate(Date openingDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("openingDate").as(Date.class), openingDate);
    }

    // фильтр поиска записи Deposit по заданному проценту
    private static Specification<Deposit> depositHasPercent(Float percent) {
        return (root, query, cb) -> cb.equal(root.get("percent").as(Float.class), percent);
    }

    // фильтр поиска записи Deposit по сроку в месяцах
    private static Specification<Deposit> depositHasTermMonths(Integer termMonths) {
        return (root, query, cb) -> cb.equal(root.get("termMonths").as(Integer.class), termMonths);
    }

    //Метод получения только депозитов
    public List<DepositOnly> getClients(String opening_date, Float percent, Integer term_months, String sortAscending, String sortDescending) throws FileNotFoundException, ParseException {
        List<Deposit> deposits = loadAllDepositFiltered(opening_date, percent, term_months, sortAscending, sortDescending);
        return deposits.stream()
                .map(deposit -> new DepositOnly(deposit.getId(), deposit.getOpeningDate(), deposit.getPercent(), deposit.getTermInMonths()))
                .collect(Collectors.toList());
    }
    
    //Метод поиска депозитов по id
    public static Deposit findByIdDeposit(Long id) {
        return depositRepository.findById(id).orElse(null);
    }


}
