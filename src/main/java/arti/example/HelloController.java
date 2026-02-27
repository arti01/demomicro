package arti.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

    @Controller("/hello")
    public class HelloController {

        @Get
        public String index() {
            return "Bank działa! Body Battery: 28, ale serwer żyje.";
        }
    }
