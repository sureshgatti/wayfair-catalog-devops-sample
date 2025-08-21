package com.wayfair.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {package com.wayfair.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

  @GetMapping("/health")
  public String health() {
    return "OK";
  }

  @GetMapping("/products")
  public List<Map<String, Object>> products() {
    return List.of(
      Map.of("id", 1, "name", "Sofa", "price", 349.99),
      Map.of("id", 2, "name", "Dining Table", "price", 259.99)
    );
  }
}


  @GetMapping("/health")
  public String health() {
    return "OK";
  }

  @GetMapping("/products")
  public List<Map<String, Object>> products() {
    return List.of(
      Map.of("id", 1, "name", "Sofa", "price", 349.99),
      Map.of("id", 2, "name", "Dining Table", "price", 259.99)
    );
  }
}
