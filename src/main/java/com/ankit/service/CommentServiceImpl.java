package com.ankit.service;

import com.ankit.modal.Comment;
import com.ankit.modal.Issue;
import com.ankit.modal.User;
import com.ankit.repository.CommentRepository;
import com.ankit.repository.IssueRepository;
import com.ankit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long issueId, Long userId, String content) throws Exception {
        Optional<Issue> issueOp = issueRepository.findById(issueId);
        Optional<User> userOp = userRepository.findById(userId);

        if(issueOp.isEmpty()){
            throw new Exception("issue not found with id " + issueId);
        }
        if(userOp.isEmpty()){
            throw new Exception("User not found with id " + userId);
        }
        Issue issue = issueOp.get();
        User user = userOp.get();

        Comment comment = new Comment();
        comment.setIssue(issue);
        comment.setUser(user);
        comment.setCreatedDateTime(LocalDateTime.now());
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);
        issue.getComments().add(savedComment);
        return savedComment;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) throws Exception {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (commentOptional.isEmpty()){
            throw new Exception("Comment not found with id" + commentId);
        }
        if (userOptional.isEmpty()){
            throw new Exception("User not found with id" + userId);
        }

        Comment comment = commentOptional.get();
        User user = userOptional.get();

        if (comment.getUser().equals(user)){
            commentRepository.delete(comment);
        } else{
            throw new Exception("Comment does not belong to you.");
        }

    }

    @Override
    public List<Comment> findCommentByIssueId(Long issueId) {
        return commentRepository.findCommentByIssueId(issueId);
    }
}
