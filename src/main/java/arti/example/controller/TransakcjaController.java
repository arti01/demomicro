package arti.example.controller;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/transakcje")
public class TransakcjaController {

    private final TransakcjaService transakcjaService;

    public TransakcjaController(TransakcjaService transakcjaService) {
        this.transakcjaService = transakcjaService;
    }

    @Post//zapisuje pojedyncza do bazy
    @Status(HttpStatus.CREATED)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public Transakcja dodaj(@Body Transakcja transakcja) {
        return transakcjaService.zapiszTransakcje(transakcja);
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/zapis-do-kolejki")
    public HttpResponse<String> testowyZapis(@Body Transakcja transakcja) {
        transakcjaService.zlecZapisPrzezRabbit(transakcja);
        return HttpResponse.ok("Zlecenie przyjęte do procesowania.");
    }

    @Get
    public Iterable<Transakcja> listuj() {
        return transakcjaService.pobierzWszystkie();
    }
    
}