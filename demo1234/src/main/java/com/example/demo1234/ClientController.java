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
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "clients"; // Должно соответствовать clients.html
    }
}

@Controller
@RequestMapping("/add-client")
class AddClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public String showAddClientForm() {
        return "add-client"; // Должно соответствовать add-client.html
    }

    @PostMapping
    public String addClient(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone) {
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setPhone(phone);
        clientRepository.save(client);
        return "redirect:/clients";
    }
}