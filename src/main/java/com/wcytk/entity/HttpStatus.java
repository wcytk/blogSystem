package com.wcytk.entity;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HttpStatus {
    private Map<String, Integer> HttpStatus = new HashMap<String, Integer>(){
    {
        //Informational 1xx  信息
        put("Continue", 100);   //继续
        put("Switching Protocols", 101);  //交换协议

        //Successful 2xx  成功
        put("OK", 200); //OK
        put("Created", 201);  //创建
        put("Accepted", 202);  //已接受
        put("Non-Authoritative Information", 203);   //非权威信息
        put("No Content", 204);  //没有内容
        put("Reset Content", 205);  //重置内容
        put("Partial Content", 206);  //部分内容

        //Redirection 3xx  重定向
        put("Multiple Choices", 300);  //多种选择
        put("Moved Permanently", 301);  //永久移动
        put("Found", 302);  //找到
        put("See Other", 303);  //参见其他
        put("Not Modified", 304);  //未修改
        put("Use Proxy", 305);  //使用代理
        put("Unused", 306);  //未使用
        put("Temporary Redirect", 307);  //暂时重定向

        //Client Error 4xx  客户端错误
        put("Bad Request", 400);  //错误的请求
        put("Unauthorized", 401);  //未经授权
        put("Payment Required", 402);  //付费请求
        put("Forbidden", 403);  //禁止
        put("Not Found", 404);  //没有找到
        put("Method Not Allowed", 405);  //方法不允许
        put("Not Acceptable", 406);  //不可接受
        put("Proxy Authentication Required", 407);  //需要代理身份验证
        put("Request Timeout", 408);  //请求超时
        put("Conflict", 409);  //指令冲突
        put("Gone", 410);  //文档永久地离开了指定的位置
        put("Length Required", 411);  //需要Content-Length头请求
        put("Precondition Failed", 412);  //前提条件失败
        put("Request Entity Too Large", 413);  //请求实体太大
        put("Request-URI Too Long", 414);  //请求URI太长
        put("Unsupported Media Type", 415);  //不支持的媒体类型
        put("Requested Range Not Satisfiable", 416);  //请求的范围不可满足
        put("Expectation Failed", 417);  //期望失败

        //Server Error 5xx  服务器错误
        put("Internal Server Error", 500);  //内部服务器错误
        put("Not Implemented", 501);  //未实现
        put("Bad Gateway", 502);  //错误的网关
        put("Service Unavailable", 503);  //服务不可用
        put("Gateway Timeout", 504);  //网关超时
        put("HTTP Version Not Supported", 505);  //HTTP版本不支持
    }};

    public int getHttpStatus(String status) {
        return this.HttpStatus.get(status);
    }
}
