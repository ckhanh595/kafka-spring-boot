package com.learnkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.WikiService;
import com.learnkafka.model.WikiMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wiki")
public class WikiController {
    private final WikiService wikiService;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody WikiMedia wikiMedia) throws ExecutionException, JsonProcessingException, InterruptedException {
        wikiService.sendWikiEvent(wikiMedia);
        return ResponseEntity.ok().build();
    }

}
