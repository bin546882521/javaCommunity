package life.chen.community.community.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import life.chen.community.community.dto.CommentCreateDTO;
import life.chen.community.community.dto.CommentDTO;
import life.chen.community.community.dto.ResultDTO;
import life.chen.community.community.enums.CommentTypeEnum;
import life.chen.community.community.exception.CustomizeErrorCode;
import life.chen.community.community.model.Comment;
import life.chen.community.community.model.User;
import life.chen.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Api(tags = "问题提问")
@Controller
public class CommentController {
    @Autowired
    private
    CommentService commentService;

    @ApiOperation("查询详细")
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(request.getSession().getAttribute("user"));
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        //StringUtils.isBlank 判断是否为空
        if(commentDTO == null || StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("message","成功");
        return ResultDTO.okOf();
    }

    @ApiOperation("获取问题")
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
