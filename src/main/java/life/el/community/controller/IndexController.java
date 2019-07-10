package life.el.community.controller;

import life.el.community.dto.QuestionDTO;
import life.el.community.mapper.QuestionMapper;
import life.el.community.mapper.UserMapper;
import life.el.community.model.Question;
import life.el.community.model.User;
import life.el.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String hello(HttpServletRequest request, Model model){
        //保存用户信息到cookie中，并在前台展示
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        //获取文章信息
        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questionList",questionList);
        return "index";
    }
}
