package arti.example;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

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
}