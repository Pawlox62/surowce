package com.surowce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootTest
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

            // Wstawiamy rekord w transakcji c1, ale jeszcze nie commitujemy
            try (PreparedStatement ps = c1.prepareStatement(
                    "INSERT INTO KONFLIKT (conflict_id, location, side_a, side_b, rok) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, "TEST-ISO");
                ps.setString(2, "TestLocation");
                ps.setString(3, "A");
                ps.setString(4, "B");
                ps.setInt(5, 2025);
                ps.executeUpdate();
            }

            // Zapytanie w c2 — powinno zwrócić 0, bo c1 się jeszcze nie commitował
            int countBeforeCommit = queryCount(c2, "TEST-ISO");
            Assertions.assertEquals(0, countBeforeCommit, "Niezatwierdzone dane nie mogą być widoczne");

            // Commit w c1
            c1.commit();

            // W c2 ponowne zapytanie — teraz musi zwrócić 1
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
