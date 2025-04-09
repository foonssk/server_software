package com.example.demo1234;

import com.example.demo1234.model.Supplier;
import com.example.demo1234.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public String getAllSuppliers(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "suppliers"; // Должно соответствовать suppliers.html
    }
}

@Controller
@RequestMapping("/add-supplier")
class AddSupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public String showAddSupplierForm() {
        return "add-supplier"; // Должно соответствовать add-supplier.html
    }

    @PostMapping
    public String addSupplier(
            @RequestParam String name,
            @RequestParam String address) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setAddress(address);
        supplierRepository.save(supplier);
        return "redirect:/suppliers";
    }
}