package life.majiang.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@Controller
@Slf4j
public class EmailController {
    public String generateVerifyCode(int number) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    @GetMapping("/mail")
    public String sendAuthCodeEmail(@RequestParam(value = "email", required = false) String email,
                                    HttpServletRequest request, HttpServletResponse response) {
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
            mail.setSubject("注册验证码");
            /*设置邮件的内容*/
            mail.setMsg("尊敬的用户:你好! 注册验证码为:" + code + "(有效期为一分钟)");
            mail.send();//发送


        } catch (Exception e) {
        }
        return "emailcode";
    }


}
