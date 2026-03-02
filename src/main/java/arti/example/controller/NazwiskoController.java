package arti.example.controller;

import arti.example.model.Nazwisko;
import io.micronaut.http.annotation.*;
import arti.example.service.NazwiskoService;

import java.util.List;

@Controller("/nazwisko")
public class NazwiskoController {

    private final NazwiskoService nazwiskoService;

    public NazwiskoController(NazwiskoService nazwiskoService) {
        this.nazwiskoService = nazwiskoService;
    }

    @Post
    public void add(@Body Nazwisko osoba, @QueryValue(defaultValue = "false") boolean czyDuzymi) {
        nazwiskoService.zapisz(osoba, czyDuzymi);
    }

    @Get
    public List<Nazwisko> list() {
        return nazwiskoService.pobierzWszystkich();
    }
}