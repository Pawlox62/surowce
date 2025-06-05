import com.surowce.SurowceApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest(
        classes = SurowceApplication.class,
        properties = "spring.task.scheduling.enabled=false"
)
public class IsolationLevelTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void readCommitted_shouldNotSeeUncommittedData() throws Exception {
        try (Connection c1 = dataSource.getConnection();
             Connection c2 = dataSource.getConnection()) {

            c1.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            c1.setAutoCommit(false);
            c2.setAutoCommit(false);

            // Używamy nazw kolumn tak, jak Hibernate je wygenerował,
            // czyli bez podkreśleń: SIDEA i SIDEB (w zapisie SQL wystarczy "sidea", "sideb").
            try (PreparedStatement ps = c1.prepareStatement(
                    "INSERT INTO KONFLIKT (conflict_id, location, sidea, sideb, rok) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, "TEST-ISO");
                ps.setString(2, "TestLocation");
                ps.setString(3, "A");
                ps.setString(4, "B");
                ps.setInt(5, 2025);
                ps.executeUpdate();
            }

            // W c2 powinno zwrócić 0, bo c1 jeszcze nie commitował
            int countBeforeCommit = queryCount(c2, "TEST-ISO");
            Assertions.assertEquals(0, countBeforeCommit, "Niezatwierdzone dane nie mogą być widoczne");

            // Commit w c1
            c1.commit();

            // Po commicie c2 powinno widzieć już 1 wiersz
            int countAfterCommit = queryCount(c2, "TEST-ISO");
            Assertions.assertEquals(1, countAfterCommit, "Po commit dane powinny być widoczne");
        }
    }

    private int queryCount(Connection conn, String conflictId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM KONFLIKT WHERE conflict_id = ?")) {
            ps.setString(1, conflictId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
