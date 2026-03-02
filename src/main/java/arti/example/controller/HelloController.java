package arti.example.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

    @Controller("/hello")
    public class HelloController {

        @Get
        public String index() {
            return "Dziala! Body Battery: 28, ale serwer żyje.";
        }
    }
