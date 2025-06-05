# README: URUCHOMIANIE APLIKACJI “Surowce i Konflikty”

## Poniższy plik opisuje trzy różne sposoby uruchomienia aplikacji:
  1. Uruchomienie w IntelliJ IDEA
  2. Uruchomienie jako samodzielny plik JAR
  3. Uruchomienie w kontenerze Docker (w WSL na Windows lub natywnie na Linuxie)

Na końcu znajdziesz komendy curl oraz sposób korzystania z GUI w przeglądarce do testowania najważniejszych endpointów.

----------------------------------------
## 1. URUCHOMIENIE W INTELLIJ IDEA
----------------------------------------

1.1. Wymagania wstępne:
     • Zainstalowane IntelliJ IDEA (Community lub Ultimate)
     • Java 21 (JDK 21) zainstalowane w systemie
     • Maven 3.x (albo wbudowany w IntelliJ)

1.2. Import projektu:
     1. Otwórz IntelliJ IDEA.
     2. Wybierz “Open” (Otwórz) i wskaż katalog główny projektu (tam, gdzie jest plik pom.xml).
     3. Czekaj, aż IntelliJ zaimportuje wszystkie zależności i zbuduje projekt.

1.3. Konfiguracja JDK:
     1. W prawym dolnym rogu IntelliJ sprawdź, czy w panelu “Project SDK” jest ustawione JDK 21.
     2. Jeśli nie – wejdź w File → Project Structure → Project SDK → wybierz lub dodaj ścieżkę do JDK 21.

1.4. Uruchomienie:
     1. W oknie Project (widok struktury projektu) rozwiń ścieżkę 
        `src/main/java/com/surowce/SurowceApplication.java`.
     2. Kliknij prawym przyciskiem na plik `SurowceApplication.java` → “Run 'SurowceApplication.main()'”.
     3. W konsoli IntelliJ powinno pojawić się coś w stylu:
        ```
        2025-06-05 13:00:00.000  INFO 12345 --- [           main] .SurowceApplication       : Started SurowceApplication in X.XXX seconds
        ```
     4. Aplikacja nasłuchuje domyślnie na porcie 8080.

----------------------------------------
## 2. URUCHOMIENIE JAKO PLIK JAR
----------------------------------------

2.1. Wymagania wstępne:
     • Java 21 (JDK 21) zainstalowana w systemie
     • Plik `surowce-0.0.1-SNAPSHOT.jar` dostępny w katalogu `target/`

2.2. Uruchomienie JAR-a jako plik wykonywalny:
     1. W menedżerze plików (Explorer/Finder) przejdź do katalogu `target`.
     2. Dwukrotnie kliknij na plik `surowce-0.0.1-SNAPSHOT.jar`.
     3. Aplikacja uruchomi się automatycznie i będzie nasłuchiwać na porcie 8080.
     4. Aby zatrzymać aplikację, zamknij okno konsoli, które otworzyło się przy starcie JAR-a, lub użyj Menedżera zadań.

2.3. Troubleshooting:
     • Jeśli plik JAR nie uruchamia się po dwukliku, upewnij się, że:
        – System kojarzy rozszerzenie `.jar` z Java 21.  
        – Możesz też uruchomić go ręcznie w terminalu:
        ```
        java -jar target/surowce-0.0.1-SNAPSHOT.jar
        ```
     • Jeśli pojawią się błędy typu „Unsupported major.minor version” lub „command not found”, zweryfikuj wersję Javy:
       ```
       java -version
       ```
       i upewnij się, że to JDK 21. Jeśli nie – zainstaluj OpenJDK 21:
       ```
       # na Ubuntu/Debian:
       sudo apt update
       sudo apt install openjdk-21-jdk
       ```
       W Windows + WSL analogicznie:
       ```
       sudo apt update
       sudo apt install openjdk-21-jdk maven
       ```

----------------------------------------
## 3. URUCHOMIENIE W DOCKERZE (WSL NA WINDOWS LUB NATYWNIE NA LINUXIE)
----------------------------------------

3.1. Wymagania wstępne:
     • Docker Desktop z włączonym WSL 2 (Windows) lub Docker w Linuxie
     • Maven 3.x i JDK 21 
     • Git 

3.2. Klonowanie repozytorium (opcjonalnie):
     Jeśli jeszcze nie masz kodu lokalnie, w WSL lub na Linuksie wykonaj:
     ```
     git clone https://github.com/Pawlox62/surowce
     cd surowce
     ```

3.3. Budowa pliku JAR:
     1. W katalogu projektu (tam, gdzie pom.xml) wpisz:
        ```
        mvn clean package
        ```
     2. Upewnij się, że powstał plik:
        ```
        target/surowce-0.0.1-SNAPSHOT.jar
        ```
     3. Jeśli nie, sprawdź wersje Java i Maven:
        ```
        java -version
        mvn -version
        ```
        – zainstaluj OpenJDK 21 i Maven, jeśli są nieaktualne:
        ```
        sudo apt update
        sudo apt install openjdk-21-jdk maven
        ```

3.4. Budowa obrazu Dockerowego:
     1. W katalogu, w którym znajduje się `Dockerfile`, wpisz:
        ```
        docker build -t surowce-app .
        ```
     2. Docker pobierze obraz bazowy `openjdk:21-jdk-slim`, a następnie skopiuje JAR do kontenera.
     3. Po zakończeniu budowy w lokalnym repozytorium Dockera pojawi się obraz `surowce-app:latest`.

3.5. Uruchomienie kontenera:
     1. Wpisz:
        ```
        docker run -d -p 8080:8080 --name surowce-container surowce-app
        ```
        • `-d` uruchamia kontener w tle  
        • `-p 8080:8080` mapuje port 8080 hosta na port 8080 w kontenerze  
        • `--name surowce-container` nadaje kontenerowi czytelną nazwę  
        • `surowce-app` to nazwa obrazu
     2. Sprawdź listę działających kontenerów:
        ```
        docker ps
        ```
        Powinieneś zobaczyć w tabeli `surowce-container` z portem `0.0.0.0:8080->8080/tcp`.
     3. Aplikacja działa wewnątrz kontenera – dostępna jest na hoście pod:
        ```
        http://localhost:8080
        ```

3.6. Troubleshooting (WSL):
     • Jeśli `docker build` wykazuje “permission denied” lub “cannot connect to Docker daemon”:
       - Upewnij się, że Docker Desktop jest uruchomiony w Windowsie i że WSL jest włączony w ustawieniach 
         `Docker Desktop → Settings → Resources → WSL Integration`.  
     • Jeśli `mvn clean package` wyrzuca błąd w trakcie budowy JAR-a:  
       - Upewnij się, że wewnątrz WSL masz zainstalowane JDK 21 i Maven:
         ```
         sudo apt update
         sudo apt install openjdk-21-jdk maven
         ```
     • Po każdej zmianie w kodzie:  
       1. Zbuduj ponownie JAR: `mvn clean package`  
       2. Zbuduj ponownie obraz: `docker build -t surowce-app .`  
       3. Zatrzymaj i usuń stary kontener (jeśli działa):  
          ```
          docker stop surowce-container
          docker rm surowce-container
          ```  
       4. Uruchom nowy:  
          ```
          docker run -d -p 8080:8080 --name surowce-container surowce-app
          ```

3.7. Zatrzymanie i usunięcie kontenera:
     1. Aby zatrzymać działający kontener:
        ```
        docker stop surowce-container
        ```
     2. Aby usunąć kontener (po zatrzymaniu):
        ```
        docker rm surowce-container
        ```

----------------------------------------
## 4. TESTOWANIE ENDPOINTÓW CURL
----------------------------------------

4.1. Pobranie wszystkich surowców (JSON):
     ```
     curl -i -u user:haslo \
          -H "Accept: application/json" \
          http://localhost:8080/api/surowce
     ```
     • Otrzymasz status `200 OK` i ciało w formacie JSON:
       ```json
       [
         { "id": 64, "nazwa": "All Commodity Price Index", "cena": 52.89 },
         { "id": 65, "nazwa": "Non-Fuel Price Index", "cena": 91.87 },
         …
       ]
       ```

4.2. Pobranie jednego surowca (np. id=64):
     ```
     curl -i -u user:haslo \
          -H "Accept: application/json" \
          http://localhost:8080/api/surowce/64
     ```
     • Powinieneś zobaczyć obiekt JSON:
       ```json
       { "id": 64, "nazwa": "All Commodity Price Index", "cena": 52.89 }
       ```

4.3. Pobranie serii cen dla surowca (id=64) w przedziale dat:
     ```
     curl -i -u user:haslo \
          -H "Accept: application/json" \
          "http://localhost:8080/api/surowce/64/prices?from=2020-01-01&to=2020-12-31"
     ```
     • Otrzymasz JSON-ową tablicę `PricePointDto`, np.:
       ```json
       [
         { "date": "2020-01-02", "value": 50.12 },
         { "date": "2020-02-01", "value": 52.34 },
         …
       ]
       ```

4.4. Pobranie wszystkich konfliktów:
     ```
     curl -i -u user:haslo \
          -H "Accept: application/json" \
          http://localhost:8080/api/konflikty
     ```
     • JSON:
       ```json
       [
         {
           "id": 13700,
           "conflictId": "11342",
           "location": "India",
           "sideA": "Government of India",
           "sideB": "GNLA",
           "rok": 2012,
           "startDate": "1997-05-29",
           "epEndDate": "2012-12-21"
         },
         …
       ]
       ```

4.5. Pobranie konfliktu o id=13700:
     ```
     curl -i -u user:haslo \
          -H "Accept: application/json" \
          http://localhost:8080/api/konflikty/13700
     ```

4.6. Pobranie konfliktów w zakresie lat (np. 1990–2000):
     ```
     curl -i -u user:haslo \
          -H "Accept: application/json" \
          "http://localhost:8080/api/konflikty/range?from=1990&to=2000"
     ```

4.7. Eksport wszystkich surowców w formacie XML:
     ```
     curl -i -u user:haslo \
          -H "Accept: application/xml" \
          http://localhost:8080/api/surowce/export
     ```
     • Otrzymasz:
       ```xml
       <List>
         <Surowiec>
           <id>64</id>
           <nazwa>All Commodity Price Index</nazwa>
           <cena>52.89</cena>
         </Surowiec>
         …
       </List>
       ```

4.8. Import surowców (plikiem `surowce-import.json` w bieżącym katalogu):
     Plik `surowce-import.json` przykładowo:
     ```json
     [
       { "nazwa": "TestowySurowiec1", "cena": 123.45 },
       { "nazwa": "TestowySurowiec2", "cena": 67.89 }
     ]
     ```
     Komenda:
     ```
     curl -i -u user:haslo \
          -X POST \
          -H "Content-Type: application/json" \
          --data-binary "@surowce-import.json" \
          http://localhost:8080/api/surowce/import
     ```
     • Otrzymasz status:
       ```
       HTTP/1.1 202 Accepted
       ```

----------------------------------------
## 5. TESTOWANIE W GUI W PRZEGLĄDARCE
----------------------------------------

5.1. Wymagania:
     • Rozszerzenie JSON Formatter (np. “JSON Viewer” w Chrome/Firefox) – opcjonalnie, by zobaczyć kolorowany JSON/XHTML.  
     • Przeglądarka (Chrome, Firefox, Edge).

5.2. Weryfikacja REST w przeglądarce:
     1. Otwórz nową kartę.
     2. Wpisz adres:
        ```
        http://localhost:8080/api/surowce
        ```
        • Jeśli rozszerzenie JSON Formatter jest włączone, zobaczysz ładnie sformatowany JSON.  
        • Jeśli nie – zobaczysz surowy tekst. Aby wymusić JSON, w DevTools → Network → kliknij request → w “Request Headers” zmień “Accept” na `application/json`, a następnie odśwież.

     3. Wpisz:
        ```
        http://localhost:8080/api/surowce/export
        ```
        • Przeglądarka może domyślnie wyświetlić XML lub zaoferować ściągnięcie pliku. W DevTools możesz ręcznie zmienić “Accept” na `application/xml`, a następnie odświeżyć, by zobaczyć XML w Tree View.

     4. Wpisz:
        ```
        http://localhost:8080/api/konflikty
        ```
        • Zobaczysz listę konfliktów w JSON.

     5. Wpisz:
        ```
        http://localhost:8080/api/konflikty/range?from=2000&to=2020
        ```
        • Zobaczysz konflikty z lat 2000–2020.

----------------------------------------
## 6. TESTOWANIE FRONTEND W PRZEGLĄDARCE (Chart.js)
----------------------------------------

6.1. Otwórz (po starcie aplikacji):
     ```
     http://localhost:8080/
     ```
     – powinien załadować się plik `impact.html`.

6.2. Elementy strony:
     • **Dropdown “Surowiec”** – wypełniony listą z bazy (dzięki ViewController i Thymeleaf).  
     • **Pola dat “Od” i “Do”** – domyślnie 1900-01-01 / 2025-12-31.  
     • **Przycisk “Pokaż”**.  
     • **Canvas** – miejsce, gdzie Chart.js narysuje wykres.

6.3. Rysowanie wykresu:
     1. Wybierz surowiec z dropdown.  
     2. Ustaw daty, np. Od = “2000-01-01”, Do = “2020-12-31”.  
     3. Kliknij “Pokaż”.  
     4. Strona wywoła wewnętrznie API:  
        • `GET /api/surowce/{id}/prices?from=…&to=…`  
        • `GET /api/konflikty/range?from=2000&to=2020`  
     5. Chart.js narysuje:  
        • Linię cen (dataset “Cena”) – wykorzystuje oś X typu `time`.  
        • Scatter punktów konfliktów (dataset “Konflikty”).  
     6. Najedź na czerwony punkt konfliktu → pojawi się tooltip z:  
        – Konflikt ID, Lokalizacja, Strona A, Strona B, Data (YYYY-01-01), Cena (najbliższa wartość).  
     7. Kliknięcie w scatter konfliktu przekieruje na stronę:  
        ```
        http://localhost:8080/konflikty/{conflictId}
        ```
        (widok z danymi szczegółowymi konfliktu lub ewentualnie 404, jeśli nie utworzono osobnego frontendu dla tego).

6.4. WebSocket + automatyczne odświeżanie:
     1. Aby przetestować automatyczne odświeżanie, w `application.properties` ustaw:
        ```
        app.reload-ms=10000
        ```
        (co 10 sekund PriceScheduler uruchamia `reload()`, a STOMP wyśle komunikat `/topic/price-update`).  
     2. Po wejściu na front-end (http://localhost:8080/) odczekaj ~10 sekund.  
     3. Strona odświeży się automatycznie (mechanizm `location.reload()` w kliencie STOMP).  
     4. Wykres zostanie narysowany ponownie z ewentualnie zaktualizowanymi danymi.

----------------------------------------
## 7. SPRAWDZANIE H2-CONSOLE (opcjonalnie)
----------------------------------------

7.1. Wymagania:
     • H2 w pamięci (domyślna, skonfigurowana w `application.properties`).

7.2. Wejdź na:
     ```
     http://localhost:8080/h2-console
     ```

7.3. Dane do logowania:
     • JDBC URL: `jdbc:h2:mem:surowce_db;DB_CLOSE_DELAY=-1`  
     • User: `sa`  
     • Password: (puste)  

7.4. Po zalogowaniu można w zakładce “SQL” wykonywać zapytania:
     ```
     SELECT * FROM SUROWIEC;
     SELECT * FROM KONFLIKT;
     SELECT * FROM PRICE_ENTITY;
     ```

----------------------------------------
## 8. PODSUMOWANIE
----------------------------------------

Po wykonaniu powyższych kroków Twoja aplikacja powinna działać poprawnie, a wszystkie najważniejsze funkcjonalności (REST, eksport/import, front-end z wykresem, WebSocket, Docker, H2‐Console) będą dostępne i przetestowane.  
