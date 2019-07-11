package life.el.community.controller;

import life.el.community.dto.AccessTokenDTO;
import life.el.community.dto.GithubUser;
import life.el.community.mapper.UserMapper;
import life.el.community.model.User;
import life.el.community.provider.GithubProvider;
import life.el.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    GithubProvider githubProvider;

    //去配置文件中读取信息
    @Value("${github.client.id}")
    private String clientid;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientid);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        //判断user
        if(githubUser!=null) {
            //user不为空，登录成功，记录cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setName(githubUser.getLogin());
            user.setToken(token);
            user.setAccountId(String.valueOf(githubUser.getId()));
            //获取用户头像链接
            user.setAvatarUrl(githubUser.getAvatar_url());
            //更新还是创建新用户
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));

            //重定向回主页
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //移除session
        request.getSession().removeAttribute("user");
        //移除cookie,新建一个同名cookie
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);//立即删除
        response.addCookie(cookie);
        return "redirect:/";
    }

}

