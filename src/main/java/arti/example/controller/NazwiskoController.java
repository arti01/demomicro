package arti.example.controller;

import arti.example.model.Nazwisko;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import arti.example.service.NazwiskoService;

import java.util.List;

@Controller("/nazwisko")
public class NazwiskoController {

    private final NazwiskoService nazwiskoService;

    public NazwiskoController(NazwiskoService nazwiskoService) {
        this.nazwiskoService = nazwiskoService;
    }

    @Post
    public void add(@Body Nazwisko osoba) {
        nazwiskoService.zapisz(osoba);
    }

    @Get
    public List<Nazwisko> list() {
        return nazwiskoService.pobierzWszystkich();
    }
}