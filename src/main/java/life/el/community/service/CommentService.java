package life.el.community.service;

import life.el.community.dto.CommentDTO;
import life.el.community.enums.CommentTypeEnum;
import life.el.community.exception.CustomizeErrorCode;
import life.el.community.exception.CustomizeException;
import life.el.community.mapper.CommentMapper;
import life.el.community.mapper.QuestionMapper;
import life.el.community.mapper.UserMapper;
import life.el.community.model.Comment;
import life.el.community.model.Question;
import life.el.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    //开启事务回滚
    @Transactional
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

    public List<CommentDTO> listByParnetId(Integer id) {
        List<Comment> comments = commentMapper.selectByParentId(id);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        //遍历comments得到每个comment中的commentator,在转换为set集合
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //把集合转换为Stringg才可以查询数据库
        String suserIds = userIds.toString();
        suserIds = suserIds.substring(1,suserIds.length()-1);
        List<User> users = userMapper.findByIds(suserIds);
        //获取评论人并转换为Map
        //把userList转换为userMap
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        //遍历comments,把comment转换为commentDto(setuser值，根据userMap中，userid和comment中的commentator)
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
