package com.zelu.miprogram.controller;

import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniPermission;
import com.zelu.miprogram.domain.Params.MiniModelPermissionParam;
import com.zelu.miprogram.service.MiniPermissionService;
import com.zelu.miprogram.utils.toolkitUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("permission")
public class MiniPermissionController {
    @Autowired
    private MiniPermissionService permissionService;
    //权限列表
    @ApiOperation(value = "权限列表的接口")
    @GetMapping(value = "/permission_list")
    public ServerResponse<List<MiniModelPermissionParam>> PermissionList(){
        return permissionService.permissionList();
    }

    //查询当前角色下有什么权限
    @ApiOperation(value = "查询当前角色的权限接口")
    @GetMapping(value = "/role_permission_list")
    public ServerResponse<List<String>> RolePermissionList(String roleNo){
        return permissionService.rolePermissionList(roleNo);
    }
    //当前登陆登陆的用户有哪些权限
    @ApiOperation(value = "查询当前登陆用户有哪些权限接口")
    @GetMapping(value = "/user_permission_list")
    public ServerResponse<List<Map<String,List<MiniPermission>>>> UserPermissionList(){
        final MiniManagerUser manageLogin = toolkitUtils.isManageLogin();
        return permissionService.userPermissionList(manageLogin.getManagerNo());
    }
}