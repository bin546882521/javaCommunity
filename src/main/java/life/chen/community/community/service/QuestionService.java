package life.chen.community.community.service;

import life.chen.community.community.dto.PaginationDTO;
import life.chen.community.community.dto.QuestionDTO;

import life.chen.community.community.exception.CustomizeErrorCode;
import life.chen.community.community.exception.CustomizeException;
import life.chen.community.community.mapper.QuestionExtMapper;
import life.chen.community.community.mapper.QuestionMapper;
import life.chen.community.community.mapper.UserMapper;
import life.chen.community.community.model.Question;
import life.chen.community.community.model.QuestionExample;
import life.chen.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset = size * (page -1);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return  paginationDTO;
    }

    public PaginationDTO listById(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage;
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset = size * (page -1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return  paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);

        if(question == null){
            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        System.out.println(question);
        System.out.println(questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtModified());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            System.out.println(question);
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if(update != 1){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
//        System.out.println("id:"+id);
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        System.out.println("viewCount:"+question.getViewCount());
//        updateQuestion.setViewCount(question.getViewCount() + 1);
//        QuestionExample questionExample = new QuestionExample();
//        questionExample.createCriteria().andIdEqualTo(id);
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

       // questionMapper.updateByExampleSelective(updateQuestion,questionExample);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
//                .filter(StringUtils::isNotBlank)
//                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
//                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
