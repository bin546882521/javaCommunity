package life.chen.community.community.controller;

import life.chen.community.community.dto.GitHubUser;
import life.chen.community.community.dto.TokenDto;
import life.chen.community.community.model.User;
import life.chen.community.community.provider.GitHubProvider;
import life.chen.community.community.service.UserService;
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
    private GitHubProvider gitHubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code, @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        TokenDto tokenDto = new TokenDto();
        tokenDto.setClient_id(clientId);
        tokenDto.setClient_secret(clientSecret);
        tokenDto.setCode(code);
        tokenDto.setRedirect_uri(redirectUri);
        tokenDto.setState(state);
        String  accessToken = gitHubProvider.getToken(tokenDto);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
        if (gitHubUser != null && gitHubUser.getId() != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            System.out.println("token:"+token);
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            System.out.println("avatarUrl:"+gitHubUser.getAvatarUrl());
            user.setAvatarUrl(gitHubUser.getAvatarUrl());
            userService.createOrUpdate(user);
//            userMapper.insert(user);
            System.out.println("user:"+user);
            response.addCookie(new Cookie("token",token));
            //登录成功,写cookie和session
//            request.getSession().setAttribute("user",gitHubUser);
            return "redirect:/";
        }else{
            //登录失败,重新登录
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
