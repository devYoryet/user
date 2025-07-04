package com.zosh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-connection")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Test básico de conexión
            try (Connection conn = dataSource.getConnection()) {
                result.put("connectionValid", conn.isValid(5));
                result.put("databaseProductName", conn.getMetaData().getDatabaseProductName());
                result.put("databaseProductVersion", conn.getMetaData().getDatabaseProductVersion());
                result.put("url", conn.getMetaData().getURL());

                // Test query simple
                try (PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM DUAL");
                        ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        result.put("queryTest", "SUCCESS - SELECT 1 FROM DUAL");
                        result.put("queryResult", rs.getInt(1));
                    }
                }

                // Test tabla usuarios
                try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
                        ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        result.put("usersCount", rs.getInt(1));
                        result.put("usersTableTest", "SUCCESS");
                    }
                } catch (Exception e) {
                    result.put("usersTableTest", "FAILED: " + e.getMessage());
                }

                result.put("status", "SUCCESS");

            }
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
            result.put("errorClass", e.getClass().getSimpleName());
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/users-simple")
    public ResponseEntity<Map<String, Object>> testUsersTable() {
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn
                        .prepareStatement("SELECT id, email, full_name, role FROM users WHERE ROWNUM <= 5");
                ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getLong("id"));
                user.put("email", rs.getString("email"));
                user.put("fullName", rs.getString("full_name"));
                user.put("role", rs.getString("role"));
                result.put("user" + (++count), user);
            }

            result.put("totalUsers", count);
            result.put("status", "SUCCESS");

        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}