package life.el.community.controller;

import life.el.community.dto.CommentCreateDTO;
import life.el.community.dto.CommentDTO;
import life.el.community.dto.QuestionDTO;
import life.el.community.service.CommentService;
import life.el.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QusetionController {

    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id, Model model) {

        QuestionDTO questionDTO = questionService.getById(id);

        List<CommentDTO> commentDTOS = commentService.listByParnetId(id);

        //累加阅读数量
        questionService.incView(questionDTO);
        questionDTO = questionService.getById(id);

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments",commentDTOS);
        return "question";
    }
}
