package stun.league.com.StunLeague.api.controllers.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stun.league.com.StunLeague.domain.interfaces.services.QueueService;

import java.util.UUID;

@Controller
@RequestMapping("/v1/queues")
@CrossOrigin("*")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping
    public ResponseEntity<UUID> getQueue() {
        return ResponseEntity.ok(this.queueService.getQueue().getId());
    }
}
