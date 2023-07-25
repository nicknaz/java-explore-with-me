package com.ewm.ewmmainservice.compilation.service;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicCompilationService {
    CompilationDto getCompilation(Long compId);
    List<CompilationDto> getCompilationList(Boolean pinned, Pageable page);
}
