package ru.pupov.homework07.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework07.dto.CommentDto;
import ru.pupov.homework07.entity.Comment;
import ru.pupov.homework07.mapper.CommentMapper;
import ru.pupov.homework07.repository.CommentRepository;
import ru.pupov.homework07.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto save(Comment comment) {
        var savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto update(Comment comment, String text) {
        if (!text.isBlank()) {
            comment.setText(text);
        }
        var savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> getById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllByBookId(Long bookId) {
        var comments = commentRepository.findByBookId(bookId);
        return commentMapper.toDtoList(comments);
    }
}
