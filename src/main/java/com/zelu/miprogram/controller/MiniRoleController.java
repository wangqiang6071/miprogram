package com.zelu.miprogram.controller;

import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniRole;
import com.zelu.miprogram.domain.Params.MiniManageUserInforParam;
import com.zelu.miprogram.domain.Params.MiniManageUserParam;
import com.zelu.miprogram.domain.Params.MiniRolePermissionParam;
import com.zelu.miprogram.service.MiniRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class MiniRoleController {

    @Autowired
    private MiniRoleService roleService;
    //添加角色
    @ApiOperation(value = "添加角色的接口")
    @PostMapping(value = "/add_role",produces = "application/json")
    public ServerResponse<String> AddRole(@RequestBody MiniRolePermissionParam param){
        return roleService.addRole(param);
    }
    //删除角色=检查角色下用户以及权限是否删除
    @ApiOperation(value = "删除角色的接口")
    @GetMapping(value = "/delete_role")
    public ServerResponse<String> DeleteRole(String roleNo){
        return roleService.deleteRole(roleNo);
    }
    //编辑角色
    @ApiOperation(value = "编辑角色的接口")
    @PostMapping(value = "/update_role")
    public ServerResponse<String> UpdateRole(@RequestBody MiniRolePermissionParam param){
        return roleService.updateRole(param);
    }
    //角色列表
    @ApiOperation(value = "角色列表的接口")
    @GetMapping(value = "/role_list")
    public ServerResponse<List<MiniRole>> RoleList(){
        return roleService.roleList();
    }

    //根据角色查询用户
    @ApiOperation(value = "根据角色查询用户的接口")
    @GetMapping(value = "/select_user_by_role")
    public ServerResponse<List<MiniManageUserInforParam>> SelectUserByRole(@RequestParam("roleNo") String roleNo){
        return roleService.selectUserByRole(roleNo);
    }

    //添加账号与角色的绑定关系
    @ApiOperation(value = "添加账号与角色的绑定关系接口")
    @PostMapping(value = "/add_account_role",produces = "application/json")
    public ServerResponse<String> AddAccountRole(@RequestBody MiniManageUserParam param){
        return roleService.addAccountRole(param);
    }

    //根据企业id查询对应的用户
    @ApiOperation(value = "查询对应企业下有哪些账号接口")
    @PostMapping(value = "select_user_by_bussiness")
    public ServerResponse<List<MiniManageUserInforParam>> SelectUserByBussiness(String bussinessNo){
        return roleService.SelectUserByBussiness(bussinessNo);
    }

}