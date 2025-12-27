package org.example.demomanagementsystemcproject.service;

import org.example.demomanagementsystemcproject.dto.*;
import org.example.demomanagementsystemcproject.repo.OrderRepository;
import org.example.demomanagementsystemcproject.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public DashboardService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getSummary() {
        long ordersToday = orderRepository.countOrdersToday();
        BigDecimal salesToday = orderRepository.sumSalesToday();
        if (salesToday == null) salesToday = BigDecimal.ZERO;

        // 修复过时的 BigDecimal.divide 方法
        BigDecimal avg = ordersToday == 0 ? BigDecimal.ZERO :
                salesToday.divide(BigDecimal.valueOf(ordersToday), 2, RoundingMode.HALF_UP);

        return new DashboardSummaryDTO(ordersToday, salesToday, avg);
    }

    @Transactional(readOnly = true)
    public List<DayCountDTO> getSalesTrend(int days) {
        if (days <= 0) days = 7;
        if (days > 365) days = 365;
        List<Object[]> rows = orderRepository.ordersAndSalesByDay(days);

        // map day -> (orders, sales)
        Map<LocalDate, Object[]> map = new HashMap<>();
        for (Object[] r : rows) {
            Object day = r[0];
            if (day == null) {
                continue;
            }

            LocalDate localDate;
            if (day instanceof java.sql.Date d) {
                localDate = d.toLocalDate();
            } else if (day instanceof LocalDate d) {
                localDate = d;
            } else if (day instanceof java.util.Date d) {
                localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                continue;
            }

            map.put(localDate, r);
        }

        List<DayCountDTO> out = new ArrayList<>();
        LocalDate start = LocalDate.now().minusDays(days - 1);
        for (int i = 0; i < days; i++) {
            LocalDate day = start.plusDays(i);
            if (map.containsKey(day)) {
                Object[] r = map.get(day);
                long orders = ((Number) r[1]).longValue();
                BigDecimal sales = (BigDecimal) r[2];
                if (sales == null) sales = BigDecimal.ZERO;
                out.add(new DayCountDTO(day, orders, sales));
            } else {
                out.add(new DayCountDTO(day, 0L, BigDecimal.ZERO));
            }
        }
        return out;
    }

    @Transactional(readOnly = true)
    public List<ProductRankDTO> getTopProducts(int days, int limit) {
        if (days <= 0) days = 30;
        if (limit <= 0) limit = 10;
        List<Object[]> rows = orderRepository.topProducts(days, limit);
        List<ProductRankDTO> out = new ArrayList<>();
        for (Object[] r : rows) {
            Long pid = r[0] == null ? null : ((Number) r[0]).longValue();
            String pname = r[1] == null ? "" : r[1].toString();
            Long qty = r[2] == null ? 0L : ((Number) r[2]).longValue();
            BigDecimal revenue = r[3] == null ? BigDecimal.ZERO : (BigDecimal) r[3];
            out.add(new ProductRankDTO(pid, pname, qty, revenue));
        }
        return out;
    }

    @Transactional(readOnly = true)
    public List<WarningDTO> getInventoryWarnings(Integer threshold) {
        // 如果没有传阈值，使用默认阈值 10
        int thr = (threshold == null) ? 10 : threshold;

        // 修复方法名：使用正确的方法名
        List<Object[]> rows = productRepository.findProductsLowStock(thr);

        List<WarningDTO> out = new ArrayList<>();
        for (Object[] r : rows) {
            Long pid = r[0] == null ? null : ((Number) r[0]).longValue();
            String pname = r[1] == null ? "" : r[1].toString();
            Integer qty = r[2] == null ? 0 : ((Number) r[2]).intValue();
            out.add(new WarningDTO(pid, pname, qty, thr));
        }
        return out;
    }
}