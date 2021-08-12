package com.zelu.miprogram.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.Params.MiniManageUserParam;
import com.zelu.miprogram.service.MiniManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author wangqiang
 * @Date: 2021/8/9 10:19
 */

//管理员账号
@RestController
@RequestMapping("manager_user")
public class MiniManagerUserController {
    @Autowired
    private MiniManagerUserService managerUserService;

    //用户登陆
    @ResponseBody
    @PostMapping("login")
    public ServerResponse<Map<String, Object>>UserLogin(@RequestParam("account") String account, @RequestParam("password") String password){
        return managerUserService.user_login(account,password);
    }
    //单个添加
    @ResponseBody
    @PostMapping("add")
    public ServerResponse<String> AddManagerUser(@RequestBody MiniManageUserParam param){
        return managerUserService.Add_Manager_User(param);
    }
    //单个修改
    @ResponseBody
    @PostMapping("update")
    public ServerResponse<String> UpdateManagerUser(@RequestBody MiniManageUserParam param){
        return managerUserService.Update_Manager_User(param);
    }
    //单个删除
    @ResponseBody
    @PostMapping("delete")
    public ServerResponse<String> DeleteManagerUser(String manager_no){
        return managerUserService.Delete_Manager_User(manager_no);
    }
    //单个查询
    @ResponseBody
    @PostMapping("selectone")
    public ServerResponse<MiniManagerUser> SelectOne(String manager_no){
        return managerUserService.Select_One(manager_no);
    }
    //查询
    @ResponseBody
    @PostMapping("select")
    public ServerResponse<IPage<MiniManagerUser>> SelectManagerUser(@RequestBody MiniManageUserParam param){
        return managerUserService.Select_Manager_User(param);
    }
    //批量导入
    @ResponseBody
    @PostMapping("batch")
    public ServerResponse<String> BatchManagerUser(MultipartFile file) throws Exception{
        return managerUserService.Batch_Manager_User(file);
    }
}
