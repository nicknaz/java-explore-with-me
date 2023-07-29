package com.ewm.ewmmainservice.compilation.service;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import com.ewm.ewmmainservice.compilation.dto.mapper.CompilationMapper;
import com.ewm.ewmmainservice.compilation.repository.CompilationRepositoryJPA;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private CompilationRepositoryJPA compilationRepositoryJPA;

    @Autowired
    public PublicCompilationServiceImpl(CompilationRepositoryJPA compilationRepositoryJPA) {
        this.compilationRepositoryJPA = compilationRepositoryJPA;
    }


    @Override
    public CompilationDto getCompilation(Long compId) {
        return CompilationMapper.toCompilationDto(compilationRepositoryJPA.findById(compId)
                .orElseThrow(() -> new NotFoundedException("Подборка не найдена")));
    }

    @Override
    public List<CompilationDto> getCompilationList(Boolean pinned, Pageable page) {
        return compilationRepositoryJPA.findAllByPinned(pinned, page)
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
