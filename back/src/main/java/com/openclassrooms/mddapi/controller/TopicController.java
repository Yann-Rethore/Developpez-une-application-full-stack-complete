package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public List<TopicDto> getAllTopics() {
        return topicService.getAllTopics();
    }
}
