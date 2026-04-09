package bo.com.bg.app.controller;

import bo.com.bg.app.response.PingResponse;
import bo.com.bg.domain.service.SaguapacService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StatusController extends AbstractController {

    private final SaguapacService saguapacService;

    public StatusController(SaguapacService saguapacService) {
        this.saguapacService = saguapacService;
    }

    @GetMapping("/health")
    public ResponseEntity<PingResponse> health() {
        logger().debug("health check");
        return ResponseEntity.ok(saguapacService.ping());
    }
}
