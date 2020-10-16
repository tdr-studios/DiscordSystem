package de.tdrstudios.discordsystem.example;

import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;

@CreateService
public class ExampleService implements Service {
    public void initialize() throws Exception {
        System.out.println("ExampleService initialized");
    }
}
