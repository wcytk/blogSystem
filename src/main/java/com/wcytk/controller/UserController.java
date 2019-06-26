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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> list = service.userAttention(user_id);
            PageInfo<User> users = new PageInfo<>(list);
            jsonObject.put("data", users);
            jsonObject.put("msg", "��ȡ��ע�б�ɹ�");
            jsonObject.put("code", 0);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("msg", "��ȡ��ע�б�ʧ��");
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
            jsonObject.put("msg", "��ϲ�㣬ȡ����ע���û��ɹ�");
            jsonObject.put("code", 0);
        } else {
            jsonObject.put("msg", "��ϲ�㣬û�гɹ���ȡ����ע");
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
            jsonObject.put("msg", "���ȵ�¼!");
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
        jsonObject.put("msg", "��ѯʧ��!");
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
        jsonObject.put("msg", "��ѯ�ɹ�!");
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
            jsonObject.put("msg", "���ȵ�¼!");
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
                        status.setMsg("��������ʧ��!", "�������³ɹ�!", s && s1);
                        return JSONObject.fromObject(status);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("msg", "���ѵ��޴�����!");
                        return jsonObject;
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "���ܵ����Լ�������!");
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            status.setCode(false);
            status.setMsg("��������ʧ��!", "�������³ɹ�!", false);
            return JSONObject.fromObject(status);
        }
        status.setCode(false);
        status.setMsg("��������ʧ��!", "�������³ɹ�!", false);
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        String articleId = request.getParameter("articleId");
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            try {
                if (articleService.SelectArticlesById(article_id, user_id) != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "�����Լ�������!");
                    return jsonObject;
                }
                JSONObject jsonObject = new JSONObject();
                if (service.selectThumb(user_id, article_id) != null) {
                    jsonObject.put("data", true);
                } else {
                    jsonObject.put("data", false);
                }
                jsonObject.put("code", 0);
                jsonObject.put("msg", "��ѯ�ɹ�!");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "��ѯʧ��!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "��ѯʧ��!");
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
            jsonObject.put("msg", "���ȵ�¼!");
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
                status.setMsg("ȡ������ʧ��!", "ȡ�����޳ɹ�!", s && s1);
                return JSONObject.fromObject(status);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "δ���޴�����!");
                return jsonObject;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ȡ������ʧ��!");
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
            jsonObject.put("msg", "���ȵ�¼!");
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
        jsonObject.put("msg", "��ѯʧ��!");
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
            jsonObject.put("msg", "���ȵ�¼!");
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
                        status.setMsg("�ղ�����ʧ��!", "�ղ����³ɹ�!", s && s1);
                        return JSONObject.fromObject(status);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("msg", "�����ղش�����!");
                        return jsonObject;
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "�����ղ��Լ�������!");
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            status.setCode(false);
            status.setMsg("�ղ�����ʧ��!", "�ղ����³ɹ�!", false);
            return JSONObject.fromObject(status);
        }
        status.setCode(false);
        status.setMsg("�ղ�����ʧ��!", "�ղ����³ɹ�!", false);
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        String articleId = request.getParameter("articleId");
        if (isInteger(articleId)) {
            int article_id = Integer.parseInt(articleId);
            try {
                if (articleService.SelectArticlesById(article_id, user_id) != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "�����Լ�������!");
                    return jsonObject;
                }
                JSONObject jsonObject = new JSONObject();
                if (service.selectCollection(user_id, article_id) != null) {
                    jsonObject.put("data", true);
                } else {
                    jsonObject.put("data", false);
                }
                jsonObject.put("code", 0);
                jsonObject.put("msg", "��ѯ�ɹ�!");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "��ѯʧ��!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "��ѯʧ��!");
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
            jsonObject.put("msg", "���ȵ�¼!");
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
                status.setMsg("ȡ���ղ�ʧ��!", "ȡ���ղسɹ�!", s && s1);
                return JSONObject.fromObject(status);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "δ�ղش�����!");
                return jsonObject;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ȡ���ղ�ʧ��!");
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
            jsonObject.put("msg", "��¼�ɹ���");
            jsonObject1.put("id", user1.getId());
            jsonObject1.put("username", user1.getUsername());
            jsonObject1.put("email", user1.getEmail());
            jsonObject.put("data", jsonObject1);
            request.getSession().setAttribute("user", user1);
        } else {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "�û�δ��¼���޷���ȡ��ǰ�û���Ϣ");
        }
        System.out.println(jsonObject);
//        return  jsonObject.toString();
        return jsonObject;

    }


    @RequestMapping("/user/logout.do")
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
//        session.invalidate();    // ��ȡsession��Ϣ��ʹsession��ϢʧЧ��ֱ�ӷ��ص�¼���棬��������ת��
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
            service.logout(user);
            JSONObject jsonObject = new JSONObject();
            if (user.getIsLogin() == 0) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "�˳��ɹ�");
            } else {
                jsonObject.put("code", 1);
                jsonObject.put("msg", "�����������쳣");
            }
            return jsonObject;
        }
        request.getSession().invalidate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 10);
        jsonObject.put("msg", "�û�δ��¼!");
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
            jsonObject.put("msg", "�ɹ���ȡ�û�!");
            return JSONObject.fromObject(jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "δ��¼!");
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


    //���������֤��
    @RequestMapping("/VerifyCode")
    public void VerifyCode(HttpServletRequest request, HttpServletResponse response) {
        // ����һ����100,��50,�Ҳ���͸��ɫ��image���� 100 40
        BufferedImage bi = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        //���һ������
        Graphics g = bi.getGraphics();
        //RGBɫ��
        Color c = new Color(255, 255, 255);
        // ���еı���ɫ
        g.setColor(c);
        // ��ɫ�������
        g.fillRect(0, 0, 100, 40);
        // ������֤���ַ�����
        // ���ַ�������ת��Ϊһ���ַ�����
        char[] ch = "ABCDEFGHIJKLMNPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm0123456798".toCharArray();
        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuffer sb = new StringBuffer();
        // ȡ���ĸ�����
        for (int i = 0; i < 4; i++) {
            // ѭ���Ĵ����ȡ���ȶ���Ϊ����
            index = r.nextInt(len);
            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
            Font font = new Font("Times New Roman", Font.ITALIC, 17);
            g.setFont(font);
            g.drawString(ch[index] + "", (i * 18) + 10, 30);
            sb.append(ch[index]);
        }
        //����Session��
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
                jsonObject.put("msg", "��ѯ�û��ɹ�!");
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "��ѯ�û�ʧ��!");
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/email.do", method = RequestMethod.POST)
    public JSONObject sendSimpleEmail(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        System.out.print(email);
        // ����һ����100,��50,�Ҳ���͸��ɫ��image���� 100 40
        BufferedImage bi = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        //���һ������
        Graphics g = bi.getGraphics();
        // ������֤���ַ�����
        // ���ַ�������ת��Ϊһ���ַ�����
        char[] ch = "ABCDEFGHIJKLMNPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm0123456798".toCharArray();
        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuffer sb = new StringBuffer();
        // ȡ���ĸ�����
        for (int i = 0; i < 4; i++) {
            // ѭ���Ĵ����ȡ���ȶ���Ϊ����
            index = r.nextInt(len);
            g.drawString(ch[index] + "", (i * 18) + 10, 30);
            sb.append(ch[index]);
        }
        //����Session��
        request.getSession().setAttribute("piccode", sb.toString());
        try {
            ImageIO.write(bi, "JPG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleMailMessage message = new SimpleMailMessage();//��Ϣ������
        message.setFrom("674908679@qq.com");//������
        message.setTo(email);//�ռ���
        message.setSubject("���ܵ���֤��");//����
        message.setText(sb.toString());//����
        mailSender.send(message);
        System.out.println("�ʼ��������");
        int ok = 1;
        return JSONObject.fromObject(ok);
    }

    @RequestMapping(value = "/user/check_username.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkUsername(String username) {
        System.out.println("��ʼ��֤�û�������");
        JSONObject jsonObject = new JSONObject();
        //���ݿ����
        if (service.findByName(username) != null) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "����û����Ѿ�����");
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "����û�����Ч");
        }
        return jsonObject;
    }

    @RequestMapping(value = "/user/register.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertUser(User user, HttpServletRequest request) {
        //Session�д�õ���֤����ֵ
        String piccode = (String) request.getSession().getAttribute("piccode");
        //ǰ���û��������֤��ֵ
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
                jsonObject.put("msg", "ע��ɹ�");
            } else {
                jsonObject.put("code", 1);
                jsonObject.put("msg", "�û��Ѵ���");
            }
            if (service.findByName(username) == null) {
                service.insertUser(user);
                int user_id = service.findByName(username).getId();
                service.insertUserDetail(user_id);
                System.out.println(user);
                System.out.println(jsonObject);
            }
        } else {//����û��������֤������䷢�͵���֤�벻��ͬ������ǰ����Ϣ
            jsonObject.put("code", 1);
            jsonObject.put("msg", "��֤�������������������");
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        DetailUser user1 = service.showDetailInfo(user_id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "��ȡ�ɹ�!");
        jsonObject.put("data", user1);
        return jsonObject;
    }

    //    �������޸ĸ�����Ϣҳ��
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        System.out.println(detailId);
        detailUser.setDetailId(detailId);
        JSONObject jsonObject = new JSONObject();
//        int id1 = Integer.parseInt(detailId);
        service.updateUserDetail(detailUser);
//        jsonObject.put("user",);
        jsonObject.put("msg", "�޸ĳɹ�");
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
            jsonObject.put("msg", "δ��¼!");
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
                    jsonObject.put("msg", "��ѯ�ɹ�!");
                    jsonObject.put("code", 0);
                    return jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("msg", "���ܹ�ע�Լ�!");
                jsonObject.put("code", 1);
            }
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "��ѯʧ��!");
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
            jsonObject.put("msg", "δ��¼!");
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
                    jsonObject.put("msg", "��ѯ�ɹ�!");
                    jsonObject.put("code", 0);
                    return jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("msg", "���ܹ�ע�Լ�!");
                jsonObject.put("code", 1);
            }
        }
        jsonObject.put("code", 1);
        jsonObject.put("msg", "��ѯʧ��!");
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        String id2 = request.getParameter("userId");
        if (isInteger(id2)) {
            int userId = Integer.parseInt(id2);
            if (user_id != userId) {
                jsonObject.put("msg", "��ϲ��ע������û�!");
                service.setAttention(user_id, userId);
                jsonObject.put("code", 0);
                jsonObject.put("userId2", userId);
            } else {
                jsonObject.put("code", 1);
                jsonObject.put("msg", "���ܹ�ע�Լ�!");
            }
        } else {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "��ע�û�ʧ��!");
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        //�������ݿ��·��
        String sqlPath = null;
        //�����ļ�����ı���·��
        String localPath = request.getSession().getServletContext().getRealPath("") + "headImage/";
        //���� �ļ���
        String filename = null;
        if (!file.isEmpty()) {
            filename = file.getOriginalFilename();
            file.transferTo(new File(localPath + filename));
        }
        //��ͼƬ�����·�����������ݿ�
        sqlPath = "/headImage/" + filename;
        System.out.println(sqlPath);
        JSONObject jsonObject = new JSONObject();
        if (sqlPath != null) {
            service.changeHeadImage(sqlPath, detailId);
            jsonObject.put("msg", "��ϲ�ϴ�ͷ��ɹ�");
            jsonObject.put("code", 0);

        } else {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "ͷ���ϴ����ɹ�");
        }
        return jsonObject;
    }

    //���뵱ǰ���ڵ�¼�û���id  �õ�����û���Ӧ�ķ�˿�б�������û��Ĺ�ע�б����һ����
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
            jsonObject.put("msg", "���ȵ�¼!");
            return jsonObject;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> list = service.getFans(userId2);
            PageInfo<User> users = new PageInfo<>(list);
            jsonObject.put("data", users);
            jsonObject.put("msg", "��ȡ��˿�б�ɹ�!");
            jsonObject.put("code", 0);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("msg", "��ȡ��˿�б�ʧ��!");
        jsonObject.put("code", 1);
        System.out.println(jsonObject);
        return jsonObject;
    }
}
