import com.surowce.entity.Surowiec;
import com.surowce.service.SurowiecService;
import com.surowce.SurowceApplication;   // <<< import klasy głównej
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SurowceApplication.class)
public class SurowiecServiceTest {

    @Autowired
    private SurowiecService surowiecService;

    @Test
    void powinnoPobracWszystkieSurowce() {
        // Wywołujemy metodę 'all()' (alias do findAll())
        List<Surowiec> lista = surowiecService.all();
        assertThat(lista).isNotEmpty();
    }
}
