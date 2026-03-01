package arti.example;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.List;

@Controller("/osoby")
public class NazwiskoController {

    private final NazwiskoRepository nazwiskoRepository;

    // Wstrzykiwanie przez konstruktor (Zalecane!)
    // Micronaut widzi ten konstruktor i sam dostarcza instancję NazwiskoRepository
    public NazwiskoController(NazwiskoRepository nazwiskoRepository) {
        this.nazwiskoRepository = nazwiskoRepository;
    }

    @Post("/") // API do zapisywania
    public Nazwisko save(@Body Nazwisko osoba) {
        return nazwiskoRepository.save(osoba);
    }

    @Get // Obsłuży zapytanie GET na główny adres kontrolera
    public List<Nazwisko> listAll() {
        return nazwiskoRepository.findAll();
    }
}