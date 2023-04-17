package com.example.CafeManagementbackend.service;

import com.example.CafeManagementbackend.model.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateBill(Map<String, Object> requestMap);

    ResponseEntity<List<Bill>> getBill();
}
