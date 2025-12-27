package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.OrderDTO;
import org.example.demomanagementsystemcproject.dto.OrderQueryDTO;
import org.example.demomanagementsystemcproject.dto.RefundRequestDTO;
import org.example.demomanagementsystemcproject.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader
    ) {
        if (request.getUserId() == null && userIdHeader != null && !userIdHeader.isBlank()) {
            request.setUserId(Long.valueOf(userIdHeader));
        }
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getOrders(OrderQueryDTO query) {
        return ResponseEntity.ok(orderService.getOrders(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/no/{orderNo}")
    public ResponseEntity<OrderDTO> getOrderByNo(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.getOrderByNo(orderNo));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable Long id) {
        orderService.confirmOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-confirm")
    public ResponseEntity<Void> batchConfirmOrders(@RequestBody List<Long> ids) {
        orderService.batchConfirmOrders(ids);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, @RequestBody Map<String, String> body) {
        orderService.cancelOrder(id, body.get("reason"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> requestRefund(@RequestBody RefundRequestDTO request) {
        orderService.requestRefund(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/refund/approve")
    public ResponseEntity<Void> approveRefund(@PathVariable Long id) {
        orderService.approveRefund(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/refund/reject")
    public ResponseEntity<Void> rejectRefund(@PathVariable Long id, @RequestBody Map<String, String> body) {
        orderService.rejectRefund(id, body.get("reason"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/alerts/new")
    public ResponseEntity<List<OrderDTO>> getNewOrderAlerts() {
        return ResponseEntity.ok(orderService.getNewOrderAlerts());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportOrders(OrderQueryDTO query) {
        byte[] csvData = orderService.exportOrders(query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "orders_export.csv");

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }
}