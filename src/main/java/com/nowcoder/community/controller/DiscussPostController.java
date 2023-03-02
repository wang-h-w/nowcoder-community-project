package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
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

import java.util.*;

@Controller
@RequestMapping(path = "/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) return CommunityUtil.getJsonString(403, "你还没有登录哟！");

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        return CommunityUtil.getJsonString(0, "发布成功！");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPost);
        // 作者
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);
        // 评论分页设置
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        // 评论：给帖子的评论
        // 回复：给评论的评论
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
        // 这里是为了将评论中的用户ID替换为User对象，方便视图层展示用户信息
        List<Map<String, Object>> commentViewObjectList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 添加评论
                Map<String, Object> commentViewObject = new HashMap<>();
                commentViewObject.put("comment", comment);
                commentViewObject.put("user", userService.findUserById(comment.getUserId()));
                // 添加回复
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyViewObjectList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyViewObject = new HashMap<>();
                        replyViewObject.put("reply", reply);
                        replyViewObject.put("user", userService.findUserById(reply.getUserId()));
                        // target是指这条评论是否回复给某个特定的用户
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyViewObject.put("target", target);
                        replyViewObjectList.add(replyViewObject);
                    }
                }
                commentViewObject.put("replies", replyViewObjectList);
                commentViewObject.put("replyCount", commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId()));
                commentViewObjectList.add(commentViewObject);
            }
        }
        model.addAttribute("comments", commentViewObjectList);

        return "/site/discuss-detail";
    }
}