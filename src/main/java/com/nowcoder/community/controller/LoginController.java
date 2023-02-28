package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import static com.nowcoder.community.utils.CommunityConstant.*;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path")
    private String contextPath;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String getForgetPage() {
        return "/site/forget";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> msg = userService.register(user);
        if (msg == null || msg.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已向您的邮箱中发送了一封激活邮件，请尽快激活，谢谢！");
            model.addAttribute("url", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", msg.get("usernameMsg"));
            model.addAttribute("passwordMsg", msg.get("passwordMsg"));
            model.addAttribute("emailMsg", msg.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{username}/{activationCode}", method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable("username") String username,
                             @PathVariable("activationCode") String activationCode) {
        int activationResult = userService.activation(username, activationCode);
        if (activationResult == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功！您的账户已经可以使用了！");
            model.addAttribute("url", "/login");
        } else if (activationResult == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "该账号已完成激活，请勿重复操作！");
            model.addAttribute("url", "/index");
        } else if (activationResult == ACTIVATION_FAILURE) {
            model.addAttribute("msg", "激活失败！您提供的激活码有误！");
            model.addAttribute("url", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        try {
            // Kaptcha
            String text = kaptchaProducer.createText();
            BufferedImage image = kaptchaProducer.createImage(text);
            // 图片响应到浏览器
            response.setContentType("image/png");
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
            // 保存验证码至session（用户点击登录时提交了所有信息，发了新的请求，但此时还要记住发送请求前验证码的真实值：用session）
            session.setAttribute("kaptcha", text);
        } catch (IOException e) {
            logger.error("响应验证码失败: " + e.getMessage());
        }
    }

    @RequestMapping(path = "/forget/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendForgetCode(HttpSession session, String email) {
        Map<String, Object> map = userService.sendForgetCode(email);
        if (map.containsKey("emailMsg")) {
            return CommunityUtil.getJsonString("emailMsg", map.get("emailMsg"));
        }
        if (map.containsKey("codeInSession") && map.containsKey("sent")) {
            session.setAttribute("codeInSession", map.get("codeInSession"));
            return CommunityUtil.getJsonString("sent", map.get("sent"));
        }
        return CommunityUtil.getJsonString("sent", "验证码发送失败！");
    }

    @RequestMapping(path = "/forget", method = RequestMethod.POST)
    public String forget(Model model, HttpSession session, String email, String code, String password) {
        if (StringUtils.isBlank(email)) {
            model.addAttribute("emailMsg", "邮箱不能为空！");
        }
        String codeInSession = (String) session.getAttribute("codeInSession");
        code = CommunityUtil.md5(email + code);
        if (StringUtils.isBlank(codeInSession) || StringUtils.isBlank(code) || !codeInSession.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确！");
            return "/site/forget";
        }
        userService.updatePassword(email, password);
        model.addAttribute("msg", "重置密码已成功！");
        model.addAttribute("url", "/login");
        return "/site/operate-result";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, HttpSession session, HttpServletResponse response,
                        String username, String password, String code, boolean rememberMe) {
        // 判断验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确！");
            return "/site/login";
        }

        // 检查账号和密码
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
