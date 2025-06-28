package com.zosh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salons")
public class SalonController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSalons() {
        // Respuesta temporal con datos mock
        List<Map<String, Object>> salons = new ArrayList<>();

        Map<String, Object> salon1 = new HashMap<>();
        salon1.put("id", 1);
        salon1.put("name", "Salón de Belleza Premium");
        salon1.put("address", "Av. Providencia 123");
        salon1.put("city", "Santiago");
        salon1.put("images", List.of(
                "https://images.pexels.com/photos/4625615/pexels-photo-4625615.jpeg?auto=compress&cs=tinysrgb&w=600"));

        Map<String, Object> salon2 = new HashMap<>();
        salon2.put("id", 2);
        salon2.put("name", "UrbanGlow Spa");
        salon2.put("address", "Las Condes 456");
        salon2.put("city", "Santiago");
        salon2.put("images", List.of(
                "https://images.pexels.com/photos/3998415/pexels-photo-3998415.jpeg?auto=compress&cs=tinysrgb&w=600"));

        salons.add(salon1);
        salons.add(salon2);

        return ResponseEntity.ok(salons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getSalonById(@PathVariable Long id) {
        Map<String, Object> salon = new HashMap<>();
        salon.put("id", id);
        salon.put("name", "Salón de Belleza Premium");
        salon.put("address", "Av. Providencia 123");
        salon.put("city", "Santiago");
        salon.put("openTime", "09:00:00");
        salon.put("closeTime", "18:00:00");
        salon.put("images", List.of(
                "https://images.pexels.com/photos/4625615/pexels-photo-4625615.jpeg?auto=compress&cs=tinysrgb&w=600",
                "https://images.pexels.com/photos/3331488/pexels-photo-3331488.jpeg?auto=compress&cs=tinysrgb&w=600",
                "https://images.pexels.com/photos/5069455/pexels-photo-5069455.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));

        return ResponseEntity.ok(salon);
    }
}