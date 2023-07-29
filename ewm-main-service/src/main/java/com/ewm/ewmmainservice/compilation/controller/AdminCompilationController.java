package com.ewm.ewmmainservice.compilation.controller;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestUpdateDto;
import com.ewm.ewmmainservice.compilation.service.AdminCompilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private AdminCompilationService compilationService;

    @Autowired
    public AdminCompilationController(AdminCompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody CompilationRequestDto compilationRequestDto) {
        CompilationDto compilationDto = compilationService.create(compilationRequestDto);
        log.info("Create compilation with id = {}", compilationDto.getId());
        return compilationDto;
    }

    @PatchMapping("/{compId}")
    public CompilationDto patch(@PathVariable Long compId,
                                @Valid @RequestBody CompilationRequestUpdateDto updateCompilationRequestDto) {
        CompilationDto compilationDto = compilationService.patch(compId, updateCompilationRequestDto);
        log.info("Update compilation with id = {}", compId);
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationService.delete(compId);
        log.info("Delete compilation with id = {}", compId);
    }
}
