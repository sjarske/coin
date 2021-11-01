package com.coin.coin.controllers;

import com.coin.coin.models.TradeRule;
import com.coin.coin.services.TradeRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TradeRuleController {
    private final TradeRuleService tradeRuleService;

    @GetMapping("/traderules")
    public ResponseEntity<List<TradeRule>> getTradeRules(){
        return ResponseEntity.ok().body(tradeRuleService.getTradeRules());
    }

    @PostMapping("/traderule/save")
    public ResponseEntity<TradeRule> saveTradeRule(@RequestBody TradeRule tradeRule){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/traderule/save").toUriString());
        return ResponseEntity.created(uri).body(tradeRuleService.saveTradeRule(tradeRule));
    }
}
