package com.ewm.ewmmainservice.compilation.controller;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import com.ewm.ewmmainservice.compilation.service.PublicCompilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private PublicCompilationService compilationService;

    @Autowired
    public PublicCompilationController(PublicCompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilationList(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getCompilationList(pinned, PageRequest.of(from / size, size));
    }
}
