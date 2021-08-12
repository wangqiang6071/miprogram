package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.*;
import com.zelu.miprogram.domain.*;
import com.zelu.miprogram.domain.Params.MiniModelPermissionParam;
import com.zelu.miprogram.service.MiniPermissionService;
import com.zelu.miprogram.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MiniPermissionServiceImpl extends ServiceImpl<MiniPermissionMapper, MiniPermission> implements MiniPermissionService {

    @Autowired
    private MiniModelMapper modelService;
    @Autowired
    private MiniRoleMapper roleMapper;
    @Autowired
    private MiniPermissionMapper permissionMapper;
    @Autowired
    private MiniRolePermissionMapper rolePermissionMapper;
    @Autowired
    private MiniUserRoleMapper userRoleMapper;
    @Override
    public ServerResponse<List<MiniModelPermissionParam>> permissionList() {
        Map<String,Object>map=new HashMap<>();
        List<MiniPermission> miniPermissions = this.baseMapper.selectByMap(map);
        List<MiniModelPermissionParam>params=new ArrayList<>();
        for(MiniPermission miniPermission:miniPermissions){
            MiniModelPermissionParam param=new MiniModelPermissionParam();
            BeanUtils.copyProperties(miniPermission,param);
            params.add(param);
        }
        return ServerResponse.createBySuccess(params);
    }

    @Override
    public ServerResponse<List<String>> rolePermissionList(String roleNo) {
        if(StringUtils.isBlank(roleNo)){
           return ServerResponse.createByError("角色编号不能为空");
        }
        QueryWrapper<MiniRole>roleQuery=new QueryWrapper<>();
        roleQuery.eq("role_no",roleNo);
        MiniRole role = roleMapper.selectOne(roleQuery);
        if(StringUtils.isNull(role)){
            return ServerResponse.createByError("角色编号不存在");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("role_no",roleNo);
        List<MiniRolePermission> rolePermissions = rolePermissionMapper.selectByMap(map);
        List<String> collect = rolePermissions.stream().map(t -> t.getPermissionNo()).collect(Collectors.toList());
        return ServerResponse.createBySuccess(collect);
    }

    @Override
    public ServerResponse<List<Map<String, List<MiniPermission>>>> userPermissionList(String managerNo) {
        Map<String,Object>map=new HashMap<>();
        map.put("manager_no",managerNo);
        List<MiniUserRole> userRoles=userRoleMapper.selectByMap(map);
        List<MiniRole> roles= new ArrayList<>();
        for(MiniUserRole userRole:userRoles){
            QueryWrapper<MiniRole>queryWrapper=new QueryWrapper();
            queryWrapper.eq("role_no",userRole.getRoleNo());
            roles.add(roleMapper.selectOne(queryWrapper));
        }
        List<Map<String,List<MiniPermission>>> listMap=null;
        for(MiniRole role:roles){
            List<MiniPermission> permission = new ArrayList<>();
            Map<String,Object>maps=new HashMap<>();
            maps.put("role_no",role.getRoleNo());
            List<MiniRolePermission> rolePermissions=rolePermissionMapper.selectByMap(maps);
            for(MiniRolePermission rolePermission:rolePermissions){
                QueryWrapper<MiniPermission>queryWrapper=new QueryWrapper();
                queryWrapper.eq("permission_no",rolePermission.getPermissionNo());
                permission.add(permissionMapper.selectOne(queryWrapper));
            }
            Map<String,Object>mapss=new HashMap<>();
            List<MiniModel> MiniModels = modelService.selectByMap(mapss);
            listMap=new ArrayList<>();
            for(MiniModel MiniModel:MiniModels){
                List<MiniPermission>permissions=new ArrayList<>();
                Map<String,List<MiniPermission>>mapsss=new HashMap<>();
                for(MiniPermission perm:permission){
                    if(StringUtils.equals(perm.getPermissionNo(),MiniModel.getModelNo())){
                        permissions.add(perm);
                    }
                }
                mapsss.put(MiniModel.getModelNo(),permissions);
                listMap.add(mapsss);
            }
        }
        return null;
    }
}