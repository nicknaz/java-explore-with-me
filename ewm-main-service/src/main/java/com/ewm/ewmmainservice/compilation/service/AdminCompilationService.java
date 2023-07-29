package com.ewm.ewmmainservice.compilation.service;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestUpdateDto;
import org.springframework.stereotype.Service;

@Service
public interface AdminCompilationService {
    CompilationDto create(CompilationRequestDto compilationRequestDto);

    CompilationDto patch(Long compId, CompilationRequestUpdateDto updateCompilationRequestDto);

    void delete(Long compId);
}
