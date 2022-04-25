package com.flowable.training.handson.work.bot;

import java.util.List;
import java.util.Map;

import org.flowable.cmmn.api.CmmnRuntimeService;
import org.springframework.stereotype.Component;

import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;

@Component
public class AssignChargerBotService implements BotService {

    private final CmmnRuntimeService cmmnRuntimeService;

    public AssignChargerBotService(CmmnRuntimeService cmmnRuntimeService) {
        this.cmmnRuntimeService = cmmnRuntimeService;
    }

    @Override
    public String getKey() {
        return "exercise7-assign-charger";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public BotActionResult invokeBot(HistoricActionInstance actionInstance, ActionDefinition actionDefinition, Map<String, Object> payload) {
        List<String> scooters = (List<String>) payload.get("scooterList");
        String charger = String.valueOf(payload.get("charger"));

        for (String scooter : scooters) {
            this.cmmnRuntimeService.setVariable(scooter, "charger", charger);
        }
        return new BaseBotActionResult();
    }
}
