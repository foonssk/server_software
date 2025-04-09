package com.example.demo1234;

import com.example.demo1234.model.Client;
import com.example.demo1234.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/add-client")
public class AddClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public String showAddClientForm() {
        return "add-client";
    }

    @PostMapping
    public String addClient(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            Model model) {
        try {
            Client client = new Client();
            client.setName(name);
            client.setEmail(email);
            client.setPhone(phone);
            clientRepository.save(client);
            return "redirect:/clients";
        } catch (Exception e) {
            model.addAttribute("error", "Error adding client: " + e.getMessage());
            return "add-client";
        }
    }
}