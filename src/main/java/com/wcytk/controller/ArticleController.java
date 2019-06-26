package com.wcytk.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wcytk.entity.Article;
import com.wcytk.entity.ArticlePlus;
import com.wcytk.entity.Status;
import com.wcytk.entity.User;
import com.wcytk.service.ArticleService;
import com.wcytk.util.HtmlUtils;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @ResponseBody
    @RequestMapping(value = "/center/addArticle.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject addArticle(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
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
        int thumbNum = 0, collectNum = 0;
        String publishTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Timestamp(System.currentTimeMillis()));
        String updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Timestamp(System.currentTimeMillis()));
        boolean s = articleService.AddArticle(user_id, title, content, thumbNum, collectNum, publishTime, updateTime);
        Status status = new Status();
        status.setCode(s);
        status.setMsg("添加文章失败!", "文章添加成功!", s);
        return JSONObject.fromObject(status);
    }

    @ResponseBody
    @RequestMapping(value = "/center/updateArticle.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject updateArticle(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        boolean s = false;
        System.out.println(request.getParameter("id"));
        if (isInteger(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
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
            Article article = articleService.SelectArticlesById(id, user_id);
            if (article == null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "修改失败，此文章不存在!");
                return jsonObject;
            }
            int thumbNum = 0, collectNum = 0;
            String updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Timestamp(System.currentTimeMillis()));
            s = articleService.UpdateArticleById(id, user_id, title, content, thumbNum, collectNum, updateTime);
        }
        Status status = new Status();
        status.setCode(s);
        status.setMsg("修改文章失败!", "文章修改成功!", s);
        return JSONObject.fromObject(status);
    }

    @ResponseBody
    @RequestMapping(value = "/view/searchArticles.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JSONObject viewSearchArticles(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
        String search = request.getParameter("search");
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Article> temp = articleService.searchArticles(search);
            JSONObject jsonObject = getArticleList(temp);
            if (jsonObject != null) return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Status status = new Status();
            status.setCode(false);
            status.setMsg("查询出错!", "查询成功!", false);
            return JSONObject.fromObject(status);
        }
        Status status = new Status();
        status.setCode(true);
        status.setMsg("未查询到内容!", "查询成功!", false);
        return JSONObject.fromObject(status);
    }

    @ResponseBody
    @RequestMapping(value = "/center/searchArticles.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JSONObject centerSearchArticles(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
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
        String search = request.getParameter("search");
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Article> temp = articleService.searchMyArticles(search, user_id);
            JSONObject jsonObject = getArticleList(temp);
            if (jsonObject != null) return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Status status = new Status();
            status.setCode(false);
            status.setMsg("查询出错!", "查询成功!", false);
            return JSONObject.fromObject(status);
        }
        Status status = new Status();
        status.setCode(true);
        status.setMsg("未查询到内容!", "查询成功!", false);
        return JSONObject.fromObject(status);
    }

    private JSONObject getArticleList(List<Article> temp) {
        if (!temp.isEmpty()) {
            for (Article article : temp) {
                UserController.getArticlePlus(article);
            }
            PageInfo<Article> pageInfo = new PageInfo<>(temp);
            JSONObject data = JSONObject.fromObject(pageInfo);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询成功!");
            return jsonObject;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/center/articles.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject articles(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
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
        try {
            List<Article> temp = articleService.SelectAllArticles(user_id);
            JSONObject jsonObject = getArticleList(temp);
            if (jsonObject != null) return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Status status = new Status();
            status.setCode(false);
            status.setMsg("查询出错!", "查询成功!", false);
            return JSONObject.fromObject(status);
        }
        Status status = new Status();
        status.setCode(true);
        status.setMsg("未查询到内容!", "查询成功!", false);
        return JSONObject.fromObject(status);
    }

    @ResponseBody
    @RequestMapping(value = "/view/articles.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject viewArticles(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        int pageSize = 5;
        PageHelper.startPage(currentPage, pageSize);
        try {
            List<Article> temp = articleService.selectAllUserArticles();
            JSONObject jsonObject = getArticleList(temp);
            if (jsonObject != null) return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Status status = new Status();
            status.setCode(false);
            status.setMsg("查询出错!", "查询成功!", false);
            return JSONObject.fromObject(status);
        }
        Status status = new Status();
        status.setCode(false);
        status.setMsg("未查询到内容!", "查询成功!", false);
        return JSONObject.fromObject(status);
    }

    @ResponseBody
    @RequestMapping(value = "/center/deleteArticle.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject deleteArticle(HttpServletRequest request) {
        boolean s = false;
        if (isInteger(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
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
            Article article = articleService.SelectArticlesById(id, user_id);
            if (article != null) {
                s = articleService.DeleteArticleById(id, user_id);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "删除失败，此文章不存在!");
                return jsonObject;
            }
        }
        Status status = new Status();
        status.setCode(s);
        status.setMsg("删除文章失败!", "删除文章成功!", s);
        return JSONObject.fromObject(status);
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
    @RequestMapping(value = "/center/article.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject article(HttpServletRequest request) {
        if (isInteger(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
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
            try {
                Article article = articleService.SelectArticlesById(id, user_id);
                if (article != null) {
                    JSONObject data = JSONObject.fromObject(article);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("data", data);
                    jsonObject.put("code", 0);
                    jsonObject.put("msg", "查询成功!");
                    return JSONObject.fromObject(jsonObject);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "此文章不存在!");
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Status status = new Status();
                status.setCode(false);
                status.setMsg("查询出错!", "查询成功!", false);
                return JSONObject.fromObject(status);
            }
        }
        Status status = new Status();
        status.setCode(false);
        status.setMsg("未查询到内容!", "查询成功!", false);
        return JSONObject.fromObject(status);
    }

    @ResponseBody
    @RequestMapping(value = "/view/article.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject viewArticle(HttpServletRequest request) {
        if (isInteger(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                Article article = articleService.selectUserArticles(id);
                if (article != null) {
                    JSONObject data = JSONObject.fromObject(article);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("data", data);
                    jsonObject.put("code", 0);
                    jsonObject.put("msg", "查询成功!");
                    return JSONObject.fromObject(jsonObject);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("msg", "此文章不存在!");
                    return jsonObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Status status = new Status();
                status.setCode(false);
                status.setMsg("查询出错!", "查询成功!", false);
                return JSONObject.fromObject(status);
            }
        }
        Status status = new Status();
        status.setCode(false);
        status.setMsg("未查询到内容!", "查询成功!", false);
        return JSONObject.fromObject(status);
    }

    @RequestMapping(value = "/admin/articles", method = {RequestMethod.GET, RequestMethod.POST})
    public String admin_articles(@RequestParam(required = true, defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        List<Article> articles = articleService.adminArticles();
        request.getSession().setAttribute("articleList", articles);
        return "redirect:/admin/listAllArticles.jsp";
    }

    @RequestMapping(value = "/admin/deleteArticle", method = {RequestMethod.GET, RequestMethod.POST})
    public String adminDeleteArticle(HttpServletRequest request) {
        articleService.adminDeleteArticle(Integer.parseInt(request.getParameter("id")));
        return "redirect:/admin/articles";
    }

    @RequestMapping(value = "/admin/addArticle", method = {RequestMethod.GET, RequestMethod.POST})
    public String adminAddArticle(HttpServletRequest request) {
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int thumbNum = 0, collectNum = 0;
        Timestamp publishTime = new Timestamp(System.currentTimeMillis());
        Timestamp updateTime = new Timestamp(System.currentTimeMillis());
        boolean s = articleService.adminAddArticle(user_id, title, content, thumbNum, collectNum, publishTime, updateTime);
        return "redirect:/admin/articles";
    }

    @RequestMapping(value = "/admin/updateArticle", method = {RequestMethod.GET, RequestMethod.POST})
    public String adminUpdateArticle(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int thumbNum = Integer.parseInt(request.getParameter("thumbNum"));
        int collectNum = Integer.parseInt(request.getParameter("collectNum"));
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        int id = Integer.parseInt(request.getParameter("id"));
        String updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Timestamp(System.currentTimeMillis()));
        boolean s = articleService.UpdateArticleById(id, user_id, title, content, thumbNum, collectNum, updateTime);
        return "redirect:/admin/articles";
    }
}
