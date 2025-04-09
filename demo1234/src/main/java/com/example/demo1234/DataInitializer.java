package com.example.demo1234;

import com.example.demo1234.model.Client;
import com.example.demo1234.model.Contract;
import com.example.demo1234.model.Supplier;
import com.example.demo1234.repository.ClientRepository;
import com.example.demo1234.repository.ContractRepository;
import com.example.demo1234.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создаем поставщиков
        Supplier supplier1 = new Supplier();
        supplier1.setName("ООО Поставщик 1");
        supplier1.setAddress("г. Тюмень, ул. Поставщиков, 1");
        supplierRepository.save(supplier1);

        Supplier supplier2 = new Supplier();
        supplier2.setName("ООО Поставщик 2");
        supplier2.setAddress("г. Тюмень, ул. Поставщиков, 2");
        supplierRepository.save(supplier2);

        // Создаем клиентов
        Client client1 = new Client();
        client1.setName("Иванов Иван");
        client1.setEmail("ivanov@example.com");
        client1.setPhone("+79991234567");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Петров Петр");
        client2.setEmail("petrov@example.com");
        client2.setPhone("+79997654321");
        clientRepository.save(client2);

        // Создаем договоры
        Contract contract1 = new Contract();
        contract1.setContractNumber("C001");
        contract1.setDate(LocalDate.of(2023, 1, 15));
        contract1.setAmount(100000.0);
        contract1.setClient(client1);
        contract1.setSupplier(supplier1);
        contractRepository.save(contract1);

        Contract contract2 = new Contract();
        contract2.setContractNumber("C002");
        contract2.setDate(LocalDate.of(2023, 2, 20));
        contract2.setAmount(150000.0);
        contract2.setClient(client1);
        contract2.setSupplier(supplier2);
        contractRepository.save(contract2);

        Contract contract3 = new Contract();
        contract3.setContractNumber("C003");
        contract3.setDate(LocalDate.of(2023, 3, 10));
        contract3.setAmount(200000.0);
        contract3.setClient(client2);
        contract3.setSupplier(supplier1);
        contractRepository.save(contract3);
    }
}