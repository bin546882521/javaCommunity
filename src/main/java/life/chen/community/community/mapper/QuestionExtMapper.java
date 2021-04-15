package life.chen.community.community.mapper;

import life.chen.community.community.dto.QuestionDTO;
import life.chen.community.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionDTO questionDTO);

    List<Question> selectBySearch(QuestionDTO questionDTO);
}