package com.surowce.scheduler;

import com.surowce.service.SurowiecService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceScheduler {

    private final SurowiecService service;
    private final SimpMessagingTemplate broker;

    public PriceScheduler(SurowiecService service, SimpMessagingTemplate broker) {
        this.service = service;
        this.broker = broker;
    }

    /**
     * Odświeżanie cen co X milisekund (domyślnie 300000 ms = 5 minut).
     * Wysyła wiadomość na kanał /topic/price-update, żeby front mógł się przeładować.
     */
    @Scheduled(fixedRateString = "${app.reload-ms:300000}")
    public void reloadPrices() {
        service.reload();
        broker.convertAndSend("/topic/price-update", "reload");
    }
}
