package com.surowce;

import com.surowce.entity.Surowiec;
import com.surowce.service.SurowiecService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SurowiecServiceTest {

    @Autowired
    private SurowiecService surowiecService;

    @Test
    void powinnoPobracWszystkieSurowce() {
        List<Surowiec> lista = surowiecService.pobierzWszystkie();
        assertThat(lista).isNotEmpty();
    }
}
