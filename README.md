dodanie klienta

curl -X POST http://localhost:8080/klienci \
-H "Content-Type: application/json" \
-d '{
"nazwa": "Samuel Vimes",
"email": "vimes@ankhmorpork.gov"
}'

curl -X POST https://demomicro.onrender.com/klienci \
-H "Content-Type: application/json" \
-d '{
"nazwa": "Samuel Vimes",
"email": "vimes@ankhmorpork.gov"
}'


pobranie klienta
curl -X GET http://localhost:8080/klienci/2

wszyscy klienci
curl -X GET http://localhost:8080/klienci

klient z transakcjami
curl -X GET http://localhost:8080/klienci/z_transakcjami/2

dodanie transakcji
curl -X POST http://localhost:8080/transakcje \
-H "Content-Type: application/json" \
-d '{
"kwota": 120.50,
"waluta": "PLN",
"klient": {
"id": 2
}
}'

curl -X POST http://localhost:8080/transakcje/zapis-do-kolejki \
-H "Content-Type: application/json" \
-d '{
"kwota": 1500.50,
"waluta": "PLN",
"klient": {
"id": 2
}
}'

curl -X POST https://demomicro.onrender.com/transakcje/zapis-do-kolejki \
-H "Content-Type: application/json" \
-d '{
"kwota": 1500.50,
"waluta": "PLN",
"klient": {
"id": 2
}
}'



## Micronaut 4.10.9 Documentation

- [User Guide](https://docs.micronaut.io/4.10.9/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.10.9/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.10.9/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://gradleup.com/shadow/)

## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)

## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)

## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)

## Feature http-client-jdk documentation

- [Micronaut HTTP Client Jdk documentation](https://docs.micronaut.io/latest/guide/index.html#jdkHttpClient)

- [https://openjdk.org/groups/net/httpclient/intro.html](https://openjdk.org/groups/net/httpclient/intro.html)

## Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)

## Feature data-jdbc documentation

- [Micronaut Data JDBC documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc)


