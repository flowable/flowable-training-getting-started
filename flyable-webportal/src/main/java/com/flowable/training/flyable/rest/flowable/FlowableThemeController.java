package com.flowable.training.flyable.rest.flowable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.training.flyable.service.FlowableWorkThemeService;

/**
 * Controller to fetch the theme from Flowable Work.
 * Requires a technical user to fetch.
 */
@RestController
@RequestMapping("/flyable-api")
public class FlowableThemeController {

    private final FlowableWorkThemeService flowableWorkThemeService;

    public FlowableThemeController(FlowableWorkThemeService flowableWorkThemeService) {
        this.flowableWorkThemeService = flowableWorkThemeService;
    }

    @GetMapping("/theme")
    public ResponseEntity<String> getTheme() {
        String flyableTheme = flowableWorkThemeService.getTheme();
        return ResponseEntity.ok(flyableTheme);
    }

}
