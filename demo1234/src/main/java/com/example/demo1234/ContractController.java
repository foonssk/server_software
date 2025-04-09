package com.example.demo1234;

import com.example.demo1234.model.Contract;
import com.example.demo1234.repository.ClientRepository;
import com.example.demo1234.repository.ContractRepository;
import com.example.demo1234.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo1234.model.Client;
import com.example.demo1234.model.Supplier;

import java.time.LocalDate;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    @GetMapping
    public String getAllContracts(Model model) {
        model.addAttribute("contracts", contractRepository.findAll());
        return "contracts"; // Должно соответствовать contracts.html
    }
}

@Controller
@RequestMapping("/add-contract")
class AddContractController {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public String showAddContractForm(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "add-contract";
    }

    @PostMapping
    public String addContract(
            @RequestParam String contractNumber,
            @RequestParam String date,
            @RequestParam double amount,
            @RequestParam Long clientId,
            @RequestParam Long supplierId) {

        // Находим клиента и поставщика по их ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + clientId));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id:" + supplierId));

        // Создаем и сохраняем контракт
        Contract contract = new Contract();
        contract.setContractNumber(contractNumber);
        contract.setDate(LocalDate.parse(date));
        contract.setAmount(amount);
        contract.setClient(client);  // Устанавливаем объект Client, а не ID
        contract.setSupplier(supplier);  // Устанавливаем объект Supplier, а не ID

        contractRepository.save(contract);
        return "redirect:/contracts";
    }
}