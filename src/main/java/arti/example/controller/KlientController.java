package arti.example.controller;

import arti.example.model.Klient;
import arti.example.service.KlientService;
import arti.example.service.KlientZTransakcjami;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;

import java.util.Optional;

@Controller("/klienci")
public class KlientController {

    private final KlientService klientService;

    public KlientController(KlientService klientService) {
        this.klientService = klientService;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Klient dodaj(@Body Klient klient) {
        return klientService.zapiszKlienta(klient);
    }

    @Get
    public Iterable<Klient> listuj() {
        return klientService.pobierzWszystkich();
    }
    @Get("/{id}")
    public Optional<Klient> pobierzPodstawoweDane(Long id) {
        // Tu korzystasz z repozytorium bezpośrednio lub przez serwis (klientRepository.findById(id))
        return klientService.pobierzPoId(id);
    }
    @Get("/z_transakcjami/{id}")
    public Optional<KlientZTransakcjami> pobierzPelnyProfil(Long id) {
        return klientService.pobierzKlientaZTransakcjami(id);
    }
}