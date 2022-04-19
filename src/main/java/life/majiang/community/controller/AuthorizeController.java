package life.majiang.community.controller;

import life.majiang.community.cache.TagCache;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.provider.UFileResult;
import life.majiang.community.provider.UFileService;
import life.majiang.community.provider.dto.GithubUser;
import life.majiang.community.service.UserService;
import life.majiang.community.strategy.LoginUserInfo;
import life.majiang.community.strategy.UserStrategy;
import life.majiang.community.strategy.UserStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by codedrinker on 2019/4/24.
 */
@Controller
@Slf4j
public class AuthorizeController {

//    @Autowired
//    private UserStrategyFactory userStrategyFactory;
//
//    @Autowired
//    private GithubProvider githubProvider;
//
//    @Value("${github.client.id}")
//    private String clientId;
//
//    @Value("${github.client.secret}")
//    private String clientSecret;
//
//    @Value("${github.redirect.uri}")
//    private String redirectUri;

    @Autowired
    private UserService userService;



    @Autowired
    private UFileService uFileService;
//
//    @GetMapping("/callback/{type}")
//    public String newCallback(@PathVariable(name = "type") String type,
//                              @RequestParam(name = "code") String code,
//                              @RequestParam(name = "state", required = false) String state,
//                              HttpServletRequest request,
//                              HttpServletResponse response) {
//        UserStrategy userStrategy = userStrategyFactory.getStrategy(type);
//        LoginUserInfo loginUserInfo = userStrategy.getUser(code, state);
//        if (loginUserInfo != null && loginUserInfo.getId() != null) {
//            User user = new User();
//            String token = UUID.randomUUID().toString();
//            user.setToken(token);
//            user.setName(loginUserInfo.getName());
//            user.setAccountId(String.valueOf(loginUserInfo.getId()));
//            user.setType(type);
//            UFileResult fileResult = null;
//            try {
//                fileResult = uFileService.upload(loginUserInfo.getAvatarUrl());
//                user.setAvatarUrl(fileResult.getFileUrl());
//            } catch (Exception e) {
//                user.setAvatarUrl(loginUserInfo.getAvatarUrl());
//            }
//            userService.createOrUpdate(user);
//            Cookie cookie = new Cookie("token", token);
//            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            return "redirect:/";
//        } else {
//            log.error("callback get github error,{}", loginUserInfo);
//            // 登录失败，重新登录
//            return "redirect:/";
//        }
//    }

    @PostMapping("/callback")
    public String callback(@RequestParam(value = "username",required = false) String username,
                           @RequestParam(value = "password",required = false) String password,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {

        model.addAttribute("loginusername", username);
        model.addAttribute("loginpassword", password);
        if (StringUtils.isBlank(username)) {
            model.addAttribute("loginerror", "用户名不能为空");
            return "login";
        }
        if (StringUtils.isBlank(password)) {
            model.addAttribute("loginerror", "密码不能为空");
            return "login";
        }

        if(!userService.checkUserName(username)){
            model.addAttribute("loginerror", "用户名不存在");
            return "login";
        }


        if (userService.checkUser(username,password)) {

            String token = UUID.randomUUID().toString();
            userService.updateToken(username,token);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            model.addAttribute("loginerror", "用户名或密码错误");
            return "login";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "username",required = false) String username,
                           @RequestParam(value = "password",required = false) String password,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {

        model.addAttribute("loginusername", username);
        model.addAttribute("loginpassword", password);
        if (StringUtils.isBlank(username)) {
            model.addAttribute("loginerror", "用户名不能为空");
            return "login";
        }
        if (StringUtils.isBlank(password)) {
            model.addAttribute("loginerror", "密码不能为空");
            return "login";
        }

        if(!userService.checkUserName(username)){
            model.addAttribute("loginerror", "用户名不存在");
            return "login";
        }
        if (userService.checkUser(username,password)) {

            String token = UUID.randomUUID().toString();
            userService.updateToken(username,token);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            model.addAttribute("loginerror", "用户名或密码错误");
            return "login";
        }
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

     public static boolean checkEmail(String email) {
            boolean flag = false;
            try {
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }

    public String generateVerifyCode(int number) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }


    @PostMapping("/reg")
    public String doPublish(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "password2", required = false) String password2,
            @RequestParam(value = "email", required = false) String email,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("password2", password2);
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("tags", TagCache.get());

        if (StringUtils.isBlank(username)) {
            model.addAttribute("error", "用户不能为空");
            return "reg";
        }

        if (StringUtils.length(username) > 16 ||StringUtils.length(username) < 6 ) {
            model.addAttribute("error", "用户名长度不能少于6并且不能超过16");
            return "reg";
        }
        if (isSpecialChar(username)) {
            model.addAttribute("error", "用户名中不可含有特殊字符");
            return "reg";
        }
        if (StringUtils.isBlank(name)) {
            model.addAttribute("error", "昵称不能为空");
            return "reg";
        }
        if (StringUtils.length(name) > 16 ||StringUtils.length(name) < 2 ) {
            model.addAttribute("error", "昵称长度不能少于2并且不能超过16");
            return "reg";
        }
        if (StringUtils.isBlank(password)) {
            model.addAttribute("error", "密码不能为空");
            return "reg";
        }

        if (StringUtils.length(password) > 16 ||StringUtils.length(password) < 6 ) {
            model.addAttribute("error", "密码长度不能少于6并且不能超过16");
            return "reg";
        }
        if (StringUtils.isBlank(password2)) {
            model.addAttribute("error", "确认密码不能为空");
            return "reg";
        }
        if (!password.equals(password2)) {
            model.addAttribute("error", "两次密码输入不一致");
            return "reg";
        }
        if (StringUtils.isBlank(email)) {
            model.addAttribute("error", "邮箱不能为空");
            return "reg";
        }

        if(userService.checkUserName(username)){
            model.addAttribute("error", "该名称已有他人使用");
            return "reg";

        }

        if(!checkEmail(email)){
            model.addAttribute("error", "邮箱格式错误");
            return "reg";
        }
        String token = UUID.randomUUID().toString();
        request.getSession().setAttribute("regtoken",token);
        request.getSession().setAttribute("regusername",username);
        request.getSession().setAttribute("regpassword",password);
        request.getSession().setAttribute("regname",name);
//        user.setToken(token);
//        user.setAccountId(username);
//        user.setPassword(password);
//        user.setEmail(email);
//        user.setName(name);
//        user.setType("1");
//        user.setAvatarUrl("http://gxdproject.cn-bj.ufileos.com/5e626d0c06e145ce85cd8dd520eaddef.png?UCloudPublicKey=TOKEN_f5c421ae-f113-40d9-b98f-80fd78d1217c&Signature=68d3SVSWUf4HvrR9T0zDacXdefI%3D&Expires=1965622706");
//        user.setBio("这个人很懒，什么也没留下");
//        userService.createOrUpdate(user);


        try {
            HtmlEmail mail = new HtmlEmail();
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com*/
            mail.setHostName("smtp.qq.com");
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset("UTF-8");
            /*IMAP/SMTP服务的密码*/
            mail.setAuthentication("965394628@qq.com", "tkdeapokstxnbfaj");
            /*发送邮件的邮箱和发件人*/
            mail.setFrom("965394628@qq.com", "gxd");
            /*使用安全链接*/
            mail.setSSLOnConnect(true);
            /*接收的邮箱*/
            mail.addTo(email);
            /*验证码*/
            String code = this.generateVerifyCode(6);
            /*设置邮件的主题*/

            request.getSession().setAttribute("emailcode",code);
            mail.setSubject("注册验证码");
            /*设置邮件的内容*/
            mail.setMsg("尊敬的用户:你好! 注册验证码为:" + code + "(有效期为30分钟)");
            mail.send();//发送

        } catch (Exception e) {
            model.addAttribute("error", "验证邮箱发送失败，请重试");
            return "reg";
        }
        request.getSession().setAttribute("email",email);
        return "emailcode";
    }



    @PostMapping("/emailcode")
    public String doPublish(
            @RequestParam(value = "password", required = false) String code,
            HttpServletRequest request,
            Model model) {
        String email = (String)request.getSession().getAttribute("email");
        model.addAttribute("email", email);

        String code2 = (String)request.getSession().getAttribute("emailcode");

        if (!code.equals(code2)) {
            model.addAttribute("emailerror", "验证码不正确");
            return "emailcode";
        }
        String username = (String)request.getSession().getAttribute("regusername");
        String password = (String)request.getSession().getAttribute("regpassword");
        String name = (String)request.getSession().getAttribute("regname");
        User user = new User();
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setAccountId(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        user.setType("1");
        user.setAvatarUrl("http://gxdproject.cn-bj.ufileos.com/5e626d0c06e145ce85cd8dd520eaddef.png?UCloudPublicKey=TOKEN_f5c421ae-f113-40d9-b98f-80fd78d1217c&Signature=68d3SVSWUf4HvrR9T0zDacXdefI%3D&Expires=1965622706");
        user.setBio("这个人很懒，什么也没留下");
        userService.createOrUpdate(user);

        return "redirect:/";
        }


    @PostMapping("/myInfo")
    public String moMyInfo(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "info", required = false) String info,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("myusername", username);
        model.addAttribute("myemail", email);
        model.addAttribute("myname", name);
        model.addAttribute("info", info);
        model.addAttribute("tags", TagCache.get());

        if (StringUtils.isBlank(name)) {
            model.addAttribute("infoerror", "昵称不能为空");
            return "myInfo";
        }
        if (StringUtils.length(name) > 16 ||StringUtils.length(name) < 2 ) {
            model.addAttribute("infoerror", "昵称长度不能少于2并且不能超过16");
            return "myInfo";
        }
        if (StringUtils.isBlank(email)) {
            model.addAttribute("infoerror", "邮箱不能为空");
            return "myInfo";
        }
        if (StringUtils.isBlank(info)) {
            model.addAttribute("infoerror", "简介不能为空");
            return "myInfo";
        }

        User user = new User();
        user.setAccountId(username);
        user.setEmail(email);
        user.setName(name);
        user.setBio(info);
        userService.updateInfo(user);

        return "redirect:/";
    }

    @PostMapping("/password")
    public String moPassword(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "password2", required = false) String password2,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("newpassword", password);
        model.addAttribute("newpassword2", password2);
        if (StringUtils.isBlank(password)) {
            model.addAttribute("passworderror", "密码不能为空");
            return "password";
        }

        if (StringUtils.length(password) > 16 ||StringUtils.length(password) < 6 ) {
            model.addAttribute("passworderror", "密码长度不能少于6并且不能超过16");
            return "password";
        }
        if (StringUtils.isBlank(password2)) {
            model.addAttribute("passworderror", "确认密码不能为空");
            return "password";
        }
        if (!password.equals(password2)) {
            model.addAttribute("passworderror", "两次密码输入不一致");
            return "password";
        }

        User user = new User();
        user.setAccountId(username);
        user.setPassword(password);
        userService.updatePassword(user);

        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request,
                         HttpServletResponse response) {
        return "reg";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                           HttpServletResponse response) {
        return "login";
    }

    @GetMapping("/myInfo")
    public String myInfo(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model){

        User user = (User) request.getSession().getAttribute("user");
        User user1 = new User();
        user1=userService.getUsername(user.getAccountId());
        model.addAttribute("myusername", user1.getAccountId());
        model.addAttribute("myemail", user1.getEmail());
        model.addAttribute("myname", user1.getName());
        model.addAttribute("info", user1.getBio());
        model.addAttribute("myavl",user.getAvatarUrl());

        return "myInfo";

    }

    @GetMapping("/password")
    public String Modpassword(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model){
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("myusername2", user.getAccountId());
        model.addAttribute("myavl2", user.getAvatarUrl());
        return "password";

    }

    @GetMapping("/emailcode")
    public String emailcode(
            HttpServletRequest request,
                              HttpServletResponse response,
                              Model model){
        String email = (String)request.getSession().getAttribute("email");
        model.addAttribute("email", email);
        return "emailcode";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
