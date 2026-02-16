package com.example.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/analytics")
public class AnalyticsController {

    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        String totalProducts = redisTemplate.opsForValue().get("stats:total:products");
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String todayProducts = redisTemplate.opsForValue().get("stats:total:products" + today);

        stats.put("totalProducts", totalProducts != null
                ? Long.parseLong(totalProducts)
                : 0);
        stats.put("productsCreatedToday", todayProducts != null
                ? Long.parseLong(todayProducts)
                : 0);
        stats.put("totalUpdates", redisTemplate.opsForValue().get("stats:total:updates"));
        return stats;
    }

    @GetMapping("/top/products")
    public Set<ZSetOperations.TypedTuple<String>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit) {
        return redisTemplate.opsForZSet().reverseRangeWithScores("analytics:top:viewed", 0, limit - 1);
    }
}
