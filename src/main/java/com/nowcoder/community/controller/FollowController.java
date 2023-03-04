package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityConstant;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.HostHolder;
import com.nowcoder.community.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        return CommunityUtil.getJsonString(0, "已关注！");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJsonString(0, "已取关！");
    }

    @RequestMapping(path = "/followee/{userId}", method = RequestMethod.GET)
    public String followee(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) throw new RuntimeException("用户不存在！");
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followee/" + userId);
        page.setRows((int)followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> followee = followService.findFollowee(userId, page.getOffset(), page.getLimit());
        if (followee != null) {
            for (Map<String, Object> map : followee) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("followee", followee);

        return "/site/followee";
    }

    @RequestMapping(path = "/follower/{userId}", method = RequestMethod.GET)
    public String follower(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) throw new RuntimeException("用户不存在！");
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/follower/" + userId);
        page.setRows((int)followService.findFollowerCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> follower = followService.findFollower(userId, page.getOffset(), page.getLimit());
        if (follower != null) {
            for (Map<String, Object> map : follower) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("follower", follower);

        return "/site/follower";
    }

    private boolean hasFollowed(int userId) {
        User user = hostHolder.getUser();
        if (user == null) return false;
        return followService.hasFollowed(user.getId(), ENTITY_TYPE_USER, userId);
    }
}
