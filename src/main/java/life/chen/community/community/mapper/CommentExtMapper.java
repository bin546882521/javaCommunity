package life.chen.community.community.mapper;

import life.chen.community.community.dto.QuestionDTO;
import life.chen.community.community.model.Comment;
import life.chen.community.community.model.Question;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}