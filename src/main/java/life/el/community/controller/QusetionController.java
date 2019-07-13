package life.el.community.controller;

import life.el.community.dto.QuestionDTO;
import life.el.community.mapper.QuestionMapper;
import life.el.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QusetionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id, Model model){

        QuestionDTO questionDTO = questionService.getById(id);

        //累加阅读数量
        questionService.incView(questionDTO);
        questionDTO = questionService.getById(id);

        model.addAttribute("question",questionDTO);
        return "question";
    }
}
