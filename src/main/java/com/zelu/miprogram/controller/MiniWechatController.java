package com.zelu.miprogram.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.config.WxMaConfiguration;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangqiang
 * @Date: 2021/8/10 10:17
 */
@RestController
@RequestMapping("wechat")
public class MiniWechatController {
    @Value("${wx.miniapp.appid}")
    private String openId;
    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public ServerResponse<String> login(@RequestParam("code") String code) {
        if (StringUtils.isBlank(code)) {
            return ServerResponse.createByError("微信code不能为空");
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(openId);
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            return ServerResponse.createBySuccess(JsonUtils.toJson(session));
        } catch (WxErrorException e) {
            throw new StringException("获取微信code发生错误:"+e.getMessage());
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public ServerResponse<String> info(String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(openId);
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return ServerResponse.createByError("微信验证用户错误");
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        return ServerResponse.createBySuccess(JsonUtils.toJson(userInfo));
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public ServerResponse<String> phone(String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(openId);
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return ServerResponse.createByError("微信验证用户错误");
        }
        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        return ServerResponse.createBySuccess(JsonUtils.toJson(phoneNoInfo));
    }

}
