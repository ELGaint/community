package life.el.community.service;

import life.el.community.enums.CommentTypeEnum;
import life.el.community.exception.CustomizeErrorCode;
import life.el.community.exception.CustomizeException;
import life.el.community.mapper.CommentMapper;
import life.el.community.mapper.QuestionMapper;
import life.el.community.model.Comment;
import life.el.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;

    public void insert(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOTFOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.findCommentById(comment.getId());
            if(dbcomment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
            //回复问题
            Question question = questionMapper.getById(Integer.valueOf(comment.getParentId().toString()));
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOTFOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionMapper.updateByComment(question);
        }
    }
}
