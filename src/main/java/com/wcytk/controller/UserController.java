package com.wcytk.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.xdevapi.JsonArray;
import com.wcytk.entity.*;
import com.wcytk.service.ArticleService;
import com.wcytk.service.impl.MyWebSocketHandler;
import com.wcytk.service.impl.UserServiceImpl;
import com.wcytk.service.impl.MyWebSocketHandler;
import com.wcytk.util.HtmlUtils;
import com.wcytk.util.Md5;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private ArticleService articleService;

    @Autowired
    @Qualifier("mailSender")
    private JavaMailSenderImpl mailSender;

    @RequestMapping(value = "/user/getAttentionUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject getAttentionUser(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
        PageHelper.startPage(currentPage, pageSize);
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> list = service.userAttention(user_id);
            PageInfo<User> users = new PageInfo<>(list);
            jsonObject.put("data", users);
            jsonObject.put("msg", "获取关注列表成功");
            jsonObject.put("code", 0);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("msg", "获取关注列表失败");
        jsonObject.put("code", 1);
        System.out.println(jsonObject);
        return jsonObject;
    }

    @RequestMapping(value = "/user/deleteAttention", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject deleteAttention(@RequestParam("attentionUsername") String attentionUsername) {
        JSONObject jsonObject = new JSONObject();
        boolean flag = service.deleteAttention(attentionUsername);
        if (flag) {
            jsonObject.put("msg", "恭喜你，取消关注该用户成功");
            jsonObject.put("code", 0);
        } else {
            jsonObject.put("msg", "恭喜你，没有成功地取消关注");
            jsonObject.put("code", 1);
        }
        System.out.println(jsonObject);
        return jsonObject;
    }

    @RequestMapping(value = "/user/getThumb.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject getThumb(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
        PageHelper.startPage(currentPage, pageSize);
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> list = service.selectUserThumbArticle(user_id);
            return getCollectionOrThumbList(jsonObject, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询失败!");
        return jsonObject;

    }

    private JSONObject getCollectionOrThumbList(JSONObject jsonObject, List<User> list) {
        List<Article> temp = new ArrayList<>();
        for (User user : list) {
            for (Article article : user.getGroup()) {
                getArticlePlus(article);
                temp.add(article);
            }
        }
        PageInfo<Article> articles = new PageInfo<>(temp);
        jsonObject.put("data", articles);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "查询成功!");
        return jsonObject;
    }

    static void getArticlePlus(Article article) {
        String content = article.getContent();
        if (content != null) {
            System.out.println(HtmlUtils.html2Str(content));
            if (HtmlUtils.html2Str(content).length() > 30) {
                article.setContent(HtmlUtils.html2Str(content).substring(0, 29));
            } else {
                article.setContent(HtmlUtils.html2Str(content));
            }
        }
    }

    @RequestMapping(value = "/user/addThumb.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject addThumb(HttpServletRequest request) {
        Status status = new Status();
        String articleId = request.getParameter("articleId");
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            UserThumbArticle userThumbArticle = new UserThumbArticle();
            Timestamp create_time = new Timestamp(System.currentTimeMillis());
            userThumbArticle.setUser(user_id);
            userThumbArticle.setArticle(article_id);
            userThumbArticle.setCreate_time(create_time);
            try {
                if (articleService.SelectArticlesById(article_id, user_id) == null) {
                    if (service.selectThumb(user_id, article_id) == null) {
                        boolean s = service.insertThumb(userThumbArticle);
                        boolean s1 = service.addThumbNum(article_id);
                        status.setCode(s && s1);
                        status.setMsg("点赞文章失败!", "点赞文章成功!", s && s1);
                        return JSONObject.fromObject(status);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("msg", "您已点赞此文章!");
                        return jsonObject;
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "不能点赞自己的文章!");
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            status.setCode(false);
            status.setMsg("点赞文章失败!", "点赞文章成功!", false);
            return JSONObject.fromObject(status);
        }
        status.setCode(false);
        status.setMsg("点赞文章失败!", "点赞文章成功!", false);
        return JSONObject.fromObject(status);
    }

    @RequestMapping(value = "/user/checkThumb.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject checkThumb(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        String articleId = request.getParameter("articleId");
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            try {
                if (articleService.SelectArticlesById(article_id, user_id) != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "这是自己的文章!");
                    return jsonObject;
                }
                JSONObject jsonObject = new JSONObject();
                if (service.selectThumb(user_id, article_id) != null) {
                    jsonObject.put("data", true);
                } else {
                    jsonObject.put("data", false);
                }
                jsonObject.put("code", 0);
                jsonObject.put("msg", "查询成功!");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "查询失败!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询失败!");
        return jsonObject;
    }

    @RequestMapping(value = "/user/deleteThumb.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject deleteThumb(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        String articleId = request.getParameter("articleId");
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            if (service.selectThumb(user_id, article_id) != null) {
                Status status = new Status();
                UserThumbArticle userThumbArticle = new UserThumbArticle();
                userThumbArticle.setUser(user_id);
                userThumbArticle.setArticle(article_id);
                boolean s = service.deleteThumb(userThumbArticle);
                boolean s1 = service.subThumbNum(article_id);
                status.setCode(s && s1);
                status.setMsg("取消点赞失败!", "取消点赞成功!", s && s1);
                return JSONObject.fromObject(status);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "未点赞此文章!");
                return jsonObject;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "取消点赞失败!");
        return jsonObject;
    }

    @RequestMapping(value = "/user/getCollection.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject getCollection(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
        PageHelper.startPage(currentPage, pageSize);
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> list = service.selectUserArticle(user_id);
            return getCollectionOrThumbList(jsonObject, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询失败!");
        return jsonObject;

    }

    @RequestMapping(value = "/user/addCollection.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject addCollection(HttpServletRequest request) {
        Status status = new Status();
        String articleId = request.getParameter("articleId");
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            UserCollectArticle userCollectArticle = new UserCollectArticle();
            Timestamp create_time = new Timestamp(System.currentTimeMillis());
            userCollectArticle.setUser(user_id);
            userCollectArticle.setArticle(article_id);
            userCollectArticle.setCreate_time(create_time);
            try {
                if (articleService.SelectArticlesById(article_id, user_id) == null) {
                    if (service.selectCollection(user_id, article_id) == null) {
                        boolean s = service.insertCollection(userCollectArticle);
                        boolean s1 = service.addCollectionNum(article_id);
                        status.setCode(s && s1);
                        status.setMsg("收藏文章失败!", "收藏文章成功!", s && s1);
                        return JSONObject.fromObject(status);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("msg", "您已收藏此文章!");
                        return jsonObject;
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "不能收藏自己的文章!");
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            status.setCode(false);
            status.setMsg("收藏文章失败!", "收藏文章成功!", false);
            return JSONObject.fromObject(status);
        }
        status.setCode(false);
        status.setMsg("收藏文章失败!", "收藏文章成功!", false);
        return JSONObject.fromObject(status);
    }

    @RequestMapping(value = "/user/checkCollection.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject checkCollection(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        String articleId = request.getParameter("articleId");
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            try {
                if (articleService.SelectArticlesById(article_id, user_id) != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "这是自己的文章!");
                    return jsonObject;
                }
                JSONObject jsonObject = new JSONObject();
                if (service.selectCollection(user_id, article_id) != null) {
                    jsonObject.put("data", true);
                } else {
                    jsonObject.put("data", false);
                }
                jsonObject.put("code", 0);
                jsonObject.put("msg", "查询成功!");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "查询失败!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询失败!");
        return jsonObject;
    }

    @RequestMapping(value = "/user/deleteCollection.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject deleteCollection(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        String articleId = request.getParameter("articleId");
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            if (service.selectCollection(user_id, article_id) != null) {
                Status status = new Status();
                UserCollectArticle userCollectArticle = new UserCollectArticle();
                userCollectArticle.setUser(user_id);
                userCollectArticle.setArticle(article_id);
                boolean s = service.deleteCollection(userCollectArticle);
                boolean s1 = service.subCollectionNum(article_id);
                status.setCode(s && s1);
                status.setMsg("取消收藏失败!", "取消收藏成功!", s && s1);
                return JSONObject.fromObject(status);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "未收藏此文章!");
                return jsonObject;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "取消收藏失败!");
        return jsonObject;
    }

    @RequestMapping(value = "/user/login.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(User user, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        user.setEmail(user.getUsername());
        String passwordByMD5 = Md5.MD5(user.getPassword());
        user.setPassword(passwordByMD5);
        User user1 = service.login(user);
        if (user1 != null) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "登录成功！");
            jsonObject1.put("id", user1.getId());
            jsonObject1.put("username", user1.getUsername());
            jsonObject1.put("email", user1.getEmail());
            jsonObject.put("data", jsonObject1);
            request.getSession().setAttribute("user", user1);
        } else {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "用户未登录，无法获取当前用户信息");
        }
        System.out.println(jsonObject);
//        return  jsonObject.toString();
        return jsonObject;

    }


    @RequestMapping("/user/logout.do")
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
//        session.invalidate();    // 获取session信息，使session信息失效，直接返回登录界面，并连接跳转。
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
            service.logout(user);
            JSONObject jsonObject = new JSONObject();
            if (user.getIsLogin() == 0) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "退出成功");
            } else {
                jsonObject.put("code", 1);
                jsonObject.put("msg", "网络或服务器异常");
            }
            return jsonObject;
        }
        request.getSession().invalidate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 10);
        jsonObject.put("msg", "用户未登录!");
        return jsonObject;
    }


    @RequestMapping(value = "/ListAll1")
    @ResponseBody
    public String ListAll1(Model model) {
        List<User> list = service.ListAll();
        System.out.println(JSONArray.fromObject(list).toString());
        return JSONArray.fromObject(list).toString();
    }

    @RequestMapping(value = "/getUser.do")
    @ResponseBody
    public JSONObject getUser(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", user);
            jsonObject.put("code", 0);
            jsonObject.put("msg", "成功获取用户!");
            return JSONObject.fromObject(jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "未登录!");
            return jsonObject;
        }
    }


    @RequestMapping(value = "/findById1")
    @ResponseBody
    public String findById1(int id) {
        User user = service.findById(id);
        System.out.println(JSONArray.fromObject(user).toString());
        System.out.println(JSONObject.fromObject(user).toString());
        return JSONObject.fromObject(user).toString();
    }


    //后端生成验证码
    @RequestMapping("/VerifyCode")
    public void VerifyCode(HttpServletRequest request, HttpServletResponse response) {
        // 创建一个宽100,高50,且不带透明色的image对象 100 40
        BufferedImage bi = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        //获得一个画笔
        Graphics g = bi.getGraphics();
        //RGB色彩
        Color c = new Color(255, 255, 255);
        // 框中的背景色
        g.setColor(c);
        // 颜色填充像素
        g.fillRect(0, 0, 100, 40);
        // 定义验证码字符数组
        // 将字符串对象转换为一个字符数组
        char[] ch = "ABCDEFGHIJKLMNPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm0123456798".toCharArray();
        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuffer sb = new StringBuffer();
        // 取出四个数字
        for (int i = 0; i < 4; i++) {
            // 循环四次随机取长度定义为索引
            index = r.nextInt(len);
            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
            Font font = new Font("Times New Roman", Font.ITALIC, 17);
            g.setFont(font);
            g.drawString(ch[index] + "", (i * 18) + 10, 30);
            sb.append(ch[index]);
        }
        //放入Session中
        request.getSession().setAttribute("piccode", sb.toString());
        try {
            ImageIO.write(bi, "JPG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        if (str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @ResponseBody
    @RequestMapping(value = "/user/findById.do")
    public JSONObject findById(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (isInteger(request.getParameter("userid"))) {
            int user_id = Integer.parseInt(request.getParameter("userid"));
            try {
                User user = service.findById(user_id);
                jsonObject.put("data", user);
                jsonObject.put("code", 0);
                jsonObject.put("msg", "查询用户成功!");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询用户失败!");
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/email.do", method = RequestMethod.POST)
    public JSONObject sendSimpleEmail(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        System.out.print(email);
        // 创建一个宽100,高50,且不带透明色的image对象 100 40
        BufferedImage bi = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        //获得一个画笔
        Graphics g = bi.getGraphics();
        // 定义验证码字符数组
        // 将字符串对象转换为一个字符数组
        char[] ch = "ABCDEFGHIJKLMNPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm0123456798".toCharArray();
        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuffer sb = new StringBuffer();
        // 取出四个数字
        for (int i = 0; i < 4; i++) {
            // 循环四次随机取长度定义为索引
            index = r.nextInt(len);
            g.drawString(ch[index] + "", (i * 18) + 10, 30);
            sb.append(ch[index]);
        }
        //放入Session中
        request.getSession().setAttribute("piccode", sb.toString());
        try {
            ImageIO.write(bi, "JPG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleMailMessage message = new SimpleMailMessage();//消息构造器
        message.setFrom("674908679@qq.com");//发件人
        message.setTo(email);//收件人
        message.setSubject("俊杰的验证码");//主题
        message.setText(sb.toString());//正文
        mailSender.send(message);
        System.out.println("邮件发送完毕");
        int ok = 1;
        return JSONObject.fromObject(ok);
    }

    @RequestMapping(value = "/user/check_username.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkUsername(String username) {
        System.out.println("开始验证用户名查重");
        JSONObject jsonObject = new JSONObject();
        //数据库查重
        if (service.findByName(username) != null) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "这个用户名已经存在");
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "这个用户名有效");
        }
        return jsonObject;
    }

    @RequestMapping(value = "/user/register.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertUser(User user, HttpServletRequest request) {
        //Session中存好的验证码数值
        String piccode = (String) request.getSession().getAttribute("piccode");
        //前端用户输入的验证码值
        String identifyingCode = request.getParameter("identifyingCode");
        JSONObject jsonObject = new JSONObject();
        String username = user.getUsername();
        String password = user.getPassword();
        user.setUsername(username);
        user.setPassword(password);
        String passwordByMD5 = Md5.MD5(password);
        user.setPassword(passwordByMD5);
        user.setIsLogin(0);
        if (piccode.equals(identifyingCode)) {
            if (service.findByName(username) == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "注册成功");
            } else {
                jsonObject.put("code", 1);
                jsonObject.put("msg", "用户已存在");
            }
            if (service.findByName(username) == null) {
                service.insertUser(user);
                int user_id = service.findByName(username).getId();
                service.insertUserDetail(user_id);
                System.out.println(user);
                System.out.println(jsonObject);
            }
        } else {//如果用户输入的验证码跟邮箱发送的验证码不相同，返回前端信息
            jsonObject.put("code", 1);
            jsonObject.put("msg", "验证码输入错误，请重新输入");
            System.out.println(jsonObject);
        }
        return jsonObject;
    }

    @RequestMapping(value = "/user/showDetail.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject showDetail(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        DetailUser user1 = service.showDetailInfo(user_id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "获取成功!");
        jsonObject.put("data", user1);
        return jsonObject;
    }

    //    下面是修改个人信息页面
    @RequestMapping("/user/user_info_update.do")
    @ResponseBody
    public JSONObject updateUserDetail(HttpServletRequest request, DetailUser detailUser) {
        int detailId;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            detailId = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        System.out.println(detailId);
        detailUser.setDetailId(detailId);
        JSONObject jsonObject = new JSONObject();
//        int id1 = Integer.parseInt(detailId);
        service.updateUserDetail(detailUser);
//        jsonObject.put("user",);
        jsonObject.put("msg", "修改成功");
        jsonObject.put("code", 0);
        System.out.println(jsonObject);
        return jsonObject;
    }

    @RequestMapping(value = "/delete")
    public String deleteById(int id) {
        service.deleteById(id);
        System.out.println(id);
        return "redirect:ListAll";
    }

    @ResponseBody
    @RequestMapping(value = "/user/checkAttention.do", method = RequestMethod.POST)
    public JSONObject checkAttention(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "未登录!");
            return jsonObject;
        }
        String id = request.getParameter("userId");
        JSONObject jsonObject = new JSONObject();
        if (isInteger(id)) {
            int attentionId = Integer.parseInt(id);
            if (user_id != attentionId) {
                try {
                    UserAttention userAttention = service.selectAttention(user_id, attentionId);
                    if (userAttention != null) {
                        jsonObject.put("data", true);
                    } else {
                        jsonObject.put("data", false);
                    }
                    jsonObject.put("msg", "查询成功!");
                    jsonObject.put("code", 0);
                    return jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("msg", "不能关注自己!");
                jsonObject.put("code", 1);
            }
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询失败!");
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/user/checkFan.do", method = RequestMethod.POST)
    public JSONObject checkFan(HttpServletRequest request) {
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "未登录!");
            return jsonObject;
        }
        String id = request.getParameter("userId");
        JSONObject jsonObject = new JSONObject();
        if (isInteger(id)) {
            int attentionId = Integer.parseInt(id);
            if (user_id != attentionId) {
                try {
                    UserAttention userAttention = service.selectAttention(attentionId, user_id);
                    if (userAttention != null) {
                        jsonObject.put("data", true);
                    } else {
                        jsonObject.put("data", false);
                    }
                    jsonObject.put("msg", "查询成功!");
                    jsonObject.put("code", 0);
                    return jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("msg", "不能关注自己!");
                jsonObject.put("code", 1);
            }
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "查询失败!");
        return jsonObject;
    }

    @RequestMapping(value = "/user/setAttention.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject setAttention(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        int user_id;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            user_id = user.getId();
        } else {
            jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        String id2 = request.getParameter("userId");
        if (isInteger(id2)) {
            int userId = Integer.parseInt(id2);
            if (user_id != userId) {
                jsonObject.put("msg", "恭喜关注了这个用户!");
                service.setAttention(user_id, userId);
                jsonObject.put("code", 0);
                jsonObject.put("userId2", userId);
            } else {
                jsonObject.put("code", 1);
                jsonObject.put("msg", "不能关注自己!");
            }
        } else {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "关注用户失败!");
        }
        return jsonObject;
    }

    @RequestMapping(value = "/admin/admin_listAll")
    public String admin_listAll(@RequestParam(required = true, defaultValue = "1")Integer page ,HttpServletRequest request,Model model) {
        PageHelper.startPage(page, 4);
        List<User> list = service.admin_listAll();
        request.getSession().setAttribute("userlist", list);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        request.getSession().setAttribute("page", pageInfo);
        return "redirect:/admin/listAll.jsp";
    }

    @RequestMapping(value = "/admin/updateUser")
    private String adminUpdateUser(User user, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        user.setId(id);
        user.setPassword(Md5.MD5(user.getPassword()));
        service.admin_updateUser(user);
        return "redirect:/admin/admin_listAll";
    }

    @RequestMapping(value = "/admin/admin_delete")
    private String admin_delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        service.admin_delete(id);
        return "redirect:/admin/admin_listAll";
    }

    @RequestMapping(value = "/admin/admin_addUser")
    private String admin_addUser(User user) {
        user.setIsLogin(0);
        user.setId(new Random().nextInt(1000000));
        user.setPassword(Md5.MD5(user.getPassword()));
        service.admin_addUser(user);
        return "redirect:/admin/admin_listAll";
    }

    @RequestMapping(value = "/admin/admin_search")
    private String admin_search(HttpServletRequest request) {
        String username = request.getParameter("admin_search");
        List<User> list = service.admin_search(username);
        request.getSession().setAttribute("userlist", list);
        return "redirect:/admin/admin_search.jsp";
    }

    @RequestMapping(value = "/user/upload")
    @ResponseBody
    public JSONObject changeHeadImage(HttpServletRequest request, MultipartFile file) throws Exception {
        int detailId;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            detailId = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
        String localPath = request.getSession().getServletContext().getRealPath("") + "headImage/";
        //定义 文件名
        String filename = null;
        if (!file.isEmpty()) {
            filename = file.getOriginalFilename();
            file.transferTo(new File(localPath + filename));
        }
        //把图片的相对路径保存至数据库
        sqlPath = "/headImage/" + filename;
        System.out.println(sqlPath);
        JSONObject jsonObject = new JSONObject();
        if (sqlPath != null) {
            service.changeHeadImage(sqlPath, detailId);
            jsonObject.put("msg", "恭喜上传头像成功");
            jsonObject.put("code", 0);

        } else {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "头像上传不成功");
        }
        return jsonObject;
    }

    //传入当前正在登录用户的id  得到这个用户对应的粉丝列表，跟获得用户的关注列表基本一样。
    @RequestMapping(value = "/user/getFans.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject userFans(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
        PageHelper.startPage(currentPage, pageSize);
        int userId2;
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            userId2 = user.getId();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 10);
            jsonObject.put("msg", "请先登录!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> list = service.getFans(userId2);
            PageInfo<User> users = new PageInfo<>(list);
            jsonObject.put("data", users);
            jsonObject.put("msg", "获取粉丝列表成功!");
            jsonObject.put("code", 0);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("msg", "获取粉丝列表失败!");
        jsonObject.put("code", 1);
        System.out.println(jsonObject);
        return jsonObject;
    }
}
