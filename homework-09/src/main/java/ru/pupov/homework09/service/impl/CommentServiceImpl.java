package ru.pupov.homework09.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework09.dto.CommentDto;
import ru.pupov.homework09.entity.Comment;
import ru.pupov.homework09.mapper.CommentMapper;
import ru.pupov.homework09.repository.CommentRepository;
import ru.pupov.homework09.service.CommentService;

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
    public CommentDto update(Long commentId, String text) {
        var comment = this.findById(commentId).orElse(null);
        if (comment != null) {
            if (!text.isBlank()) {
                comment.setText(text);
            }
            comment = commentRepository.save(comment);
        }
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
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
        return comments.stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto add(CommentDto commentDto) {
        var comment = commentMapper.toEntity(commentDto);
        var savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }
}
