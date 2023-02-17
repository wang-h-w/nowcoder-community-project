package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

import static com.nowcoder.community.utils.CommunityConstant.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
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
}
