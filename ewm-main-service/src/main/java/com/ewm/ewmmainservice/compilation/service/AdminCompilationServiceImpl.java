package com.ewm.ewmmainservice.compilation.service;

import com.ewm.ewmmainservice.compilation.dto.CompilationDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestDto;
import com.ewm.ewmmainservice.compilation.dto.CompilationRequestUpdateDto;
import com.ewm.ewmmainservice.compilation.dto.mapper.CompilationMapper;
import com.ewm.ewmmainservice.compilation.model.Compilation;
import com.ewm.ewmmainservice.compilation.repository.CompilationRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCompilationServiceImpl implements AdminCompilationService{
    private CompilationRepositoryJPA compilationRepositoryJPA;
    private EventRepositoryJPA eventRepositoryJPA;

    @Autowired
    public AdminCompilationServiceImpl(CompilationRepositoryJPA compilationRepositoryJPA, EventRepositoryJPA eventRepositoryJPA) {
        this.compilationRepositoryJPA = compilationRepositoryJPA;
        this.eventRepositoryJPA = eventRepositoryJPA;
    }


    @Override
    public CompilationDto create(CompilationRequestDto compilationRequestDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationRequestDto,
                compilationRequestDto.getEvents() != null ? eventRepositoryJPA.findAllById(compilationRequestDto.getEvents()) : null);
        return CompilationMapper.toCompilationDto(compilationRepositoryJPA.save(compilation));
    }

    @Override
    public CompilationDto patch(Long compId, CompilationRequestUpdateDto updateCompilationRequestDto) {
        Compilation compilation = compilationRepositoryJPA.findById(compId)
                .orElseThrow(() -> new NotFoundedException("Подборка не найдена"));

        if (updateCompilationRequestDto.getEvents() != null) {
            compilation.setEvents(eventRepositoryJPA.findAllById(updateCompilationRequestDto.getEvents()));
        }

        if (updateCompilationRequestDto.getPinned() != null
                || updateCompilationRequestDto.getPinned() != compilation.getPinned()) {
            compilation.setPinned(updateCompilationRequestDto.getPinned());
        }

        if (updateCompilationRequestDto.getTitle() != null) {
            compilation.setTitle(updateCompilationRequestDto.getTitle());
        }

        return CompilationMapper.toCompilationDto(compilationRepositoryJPA.save(compilation));
    }

    @Override
    public void delete(Long compId) {
        compilationRepositoryJPA.deleteById(compId);
    }
}
