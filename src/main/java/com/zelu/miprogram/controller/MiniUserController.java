package com.zelu.miprogram.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.MiniUser;
import com.zelu.miprogram.domain.Params.MiniBuessinessRankParam;
import com.zelu.miprogram.domain.Params.MiniSubjectListParam;
import com.zelu.miprogram.domain.Params.MiniUserParam;
import com.zelu.miprogram.service.MiniUserService;
import com.zelu.miprogram.utils.toolkitUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class MiniUserController {
    @Autowired
    private MiniUserService userService;
    //===================小程序接口==============
    //用户登陆
    @ResponseBody
    @PostMapping("login")
    public ServerResponse<Map<String, Object>>UserLogin(@RequestParam("account") String account){
        return userService.user_login(account);
    }
    //用户退出
    @ResponseBody
    @PostMapping("loginout")
    public ServerResponse<String>UserLoginOut(){
        return userService.user_login_out();
    }

    //0获取用户信息== TODO

    //1判断用户的类别:游客还是企业用户2获取试卷的列表3试卷的可见范围
    @ResponseBody
    @PostMapping("wx_auth")
    public ServerResponse<List<MiniPaper>>WechatAuth(){
        return userService.Wechat_Auth();
    }
    //2返回试卷的题目列表:试卷的编号paper_no
    @ResponseBody
    @PostMapping("wx_subject")
    public ServerResponse<List<MiniSubject>>WechatSubject(@RequestParam("paper_no") String paper_no){
        return userService.Wechat_Subject(paper_no);
    }
    //3List<subject> 每道题所答题的正确答案<==>用户所选答案 每道题的分数peer_sorce 试卷的编号paper_no 登陆的当前用户编号
    @ResponseBody
    @PostMapping("wx_submit")
    public ServerResponse<Integer>WechatSubmit(@RequestBody MiniSubjectListParam subjects){
        MiniUser wxLogin = toolkitUtils.isWxLogin();
        subjects.setUser(wxLogin);
        return userService.Wechat_Submit(subjects);
    }
    //4 企业内排名
    @ResponseBody
    @PostMapping("wx_rank")
    public ServerResponse<List<MiniBuessinessRankParam>>WechatRank(@RequestParam("index") int index, @RequestParam("size") int size){
        return userService.Wechat_Rank(index,size);
    }
    //===================后台接口================

    //添加用户
    @ResponseBody
    @PostMapping("create")
    public ServerResponse<String>CreateUser(@RequestBody MiniUserParam param){
        return userService.create_user(param);
    }

    //批量导入用户
    @ResponseBody
    @PostMapping("import")
    public ServerResponse<String>ImportUser(MultipartFile file) throws Exception {
        return userService.import_user(file);
    }

    //修改用户
    @ResponseBody
    @PostMapping("update")
    public ServerResponse<String>UpdateUser(@RequestBody MiniUserParam param){
        return userService.update_user(param);
    }

    //删除用户
    @ResponseBody
    @PostMapping("delete")
    public ServerResponse<String>DeleteUser(String userNo){
        return userService.delete_user(userNo);
    }

    //查询列表(可以导出)
    @ResponseBody
    @PostMapping("list")
    public ServerResponse<IPage<MiniUser>>ListUser(@RequestBody MiniUserParam param){
        return userService.list_user(param);
    }

    //提示用户需要的登陆的接口
    @GetMapping(value = "/needlogin")
    public ServerResponse<String> needlogin(){
        return ServerResponse.createByError("请登陆账号");
    }
}