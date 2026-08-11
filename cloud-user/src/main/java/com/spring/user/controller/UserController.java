package com.spring.user.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.spring.common.core.constant.ResultCode;
import com.spring.common.core.exception.BaseException;
import com.spring.common.core.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class UserController {

    //    获取登录用户信息例子
    @GetMapping("/user/hello")
    public R<String> hello(@RequestHeader("user-info") String userInfoVO) {
        JSONObject jsonObject = JSONUtil.parseObj(userInfoVO);
        return R.ok("欢迎用户"+jsonObject.get("username") + "登录");
    }

    @GetMapping("/internal/hello")
    public String inView(){
        return "✅ 内部服务，不对外开放 SpringBoot4.0.5 正常运行";
    }

    /**
     * 全局异常捕捉验证
     * @return
     */
    @GetMapping("/exception")
    public Map<String, Object> exception(){
        throw new BaseException(ResultCode.BAD_REQUEST);
//        throw new RuntimeException("主动抛出异常.");
    }
}
