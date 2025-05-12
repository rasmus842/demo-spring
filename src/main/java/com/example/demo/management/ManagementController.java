package com.example.demo.management;

import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {
    private final Clock clock;
    private final Instant startTime;

    public ManagementController(Clock clock) {
        this.clock = clock;
        this.startTime = clock.instant();
    }

    @GetMapping("/aliveCheck")
    public ResponseEntity<Map<String, String>> healthCheck() {
        val params = new HashMap<String, String>();
        params.put("isAlive", Boolean.TRUE.toString());
        params.put("startTime", startTime.atZone(ZoneId.systemDefault()).toString());

        Instant now = clock.instant();
        long uptimeSeconds = now.getEpochSecond() - startTime.getEpochSecond();
        params.put("timeStamp", now.atZone(ZoneId.systemDefault()).toString());
        params.put("upTime", formatUptime(uptimeSeconds));

        return ResponseEntity.ok(params);
    }

    private String formatUptime(long seconds) {
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        return String.format("%d days, %d hours, %d minutes, %d seconds",
                days, hours, minutes, seconds);
    }

}
