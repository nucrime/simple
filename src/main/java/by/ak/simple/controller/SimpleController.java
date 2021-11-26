package by.ak.simple.controller;

import by.ak.simple.telegram.TelegramPollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SimpleController {
    private final TelegramPollingService telegramPollingService;

    @GetMapping("/{token}")
    public String talkToBot(@PathVariable String token) {
        telegramPollingService.poll(token);
        return "[SIMPLE] Successfully started polling...";
        // todo add thymeleaf template and js&jquery
    }
}
