package arti.example.controller;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaService;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;

@Controller("/transakcje")
public class TransakcjaController {

    private final TransakcjaService transakcjaService;

    public TransakcjaController(TransakcjaService transakcjaService) {
        this.transakcjaService = transakcjaService;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Transakcja dodaj(@Body Transakcja transakcja) {
        return transakcjaService.zapiszTransakcje(transakcja);
    }

    @Get
    public Iterable<Transakcja> listuj() {
        return transakcjaService.pobierzWszystkie();
    }
}