package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.*;
import com.zelu.miprogram.domain.*;
import com.zelu.miprogram.domain.Params.MiniManageUserInforParam;
import com.zelu.miprogram.domain.Params.MiniManageUserParam;
import com.zelu.miprogram.domain.Params.MiniRolePermissionParam;
import com.zelu.miprogram.emus.ManagerUserEmus;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.service.MiniRoleService;
import com.zelu.miprogram.utils.StringUtils;
import com.zelu.miprogram.utils.toolkitUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Service
public class MiniRoleServiceImpl extends ServiceImpl<MiniRoleMapper, MiniRole> implements MiniRoleService {

    @Value("${shiro.password.salt}")
    private String salt;
    @Value("${shiro.password.random}")
    private int random;

    @Autowired
    private MiniPermissionMapper miniPermissionMapper;
    @Autowired
    private MiniRolePermissionMapper rolePermissionMapper;
    @Autowired
    private MiniManagerUserMapper userMapper;
    @Autowired
    private MiniUserRoleMapper userRoleMapper;
    @Autowired
    private MiniBussiesNameMapper bussiesNameMapper;
    @Autowired
    private MiniRoleMapper roleMapper;
    @Override
    @Transactional
    public ServerResponse<String> addRole(MiniRolePermissionParam param) {
        if(StringUtils.isBlank(param.getRoleName())){
            throw new StringException("????????????????????????");
        }
        QueryWrapper<MiniRole>roleQuery=new QueryWrapper<>();
        roleQuery.eq("role_name",param.getRoleName());
        MiniRole miniRole = this.baseMapper.selectOne(roleQuery);
        if(!StringUtils.isNull(miniRole)){
            throw new StringException("?????????????????????");
        }
        //????????????
        String roleuuid = toolkitUtils.getStringId();
        MiniRole role=new MiniRole();
        role.setRoleName(param.getRoleName());
        role.setRoleNo(roleuuid);
        int insert = this.baseMapper.insert(role);
        if(insert==0){
            throw new StringException("??????????????????");
        }
        for(String permissionNo:param.getPermissions()){
            QueryWrapper<MiniPermission>permissionQuery=new QueryWrapper<>();
            permissionQuery.eq("permission_no",permissionNo);
            MiniPermission miniPermission = miniPermissionMapper.selectOne(permissionQuery);
            if(StringUtils.isNull(miniPermission)){
                throw new StringException("??????????????????");
            }
            //??????????????????????????????
            MiniRolePermission rolePermission=new MiniRolePermission();
            rolePermission.setRoleNo(roleuuid);
            rolePermission.setPermissionNo(permissionNo);
            int count=rolePermissionMapper.insert(rolePermission);
            if(count==0){
                throw new StringException("???????????????????????????");
            }
        }
        return ServerResponse.createBySuccess("???????????????????????????");
    }

    @Override
    public ServerResponse<String> deleteRole(String roleNo) {
        if(StringUtils.isBlank(roleNo)){
            throw new StringException("????????????????????????");
        }
        QueryWrapper<MiniRole>roleQuery=new QueryWrapper<>();
        roleQuery.eq("role_no",roleNo);
        MiniRole miniRole = this.baseMapper.selectOne(roleQuery);
        if(StringUtils.isNull(miniRole)){
            throw new StringException("?????????????????????");
        }
        //??????????????????????????????????????????
        this.baseMapper.delete(roleQuery);
        QueryWrapper<MiniRolePermission>rolePermissionQuery=new QueryWrapper<>();
        rolePermissionQuery.eq("role_no",roleNo);
        rolePermissionMapper.delete(rolePermissionQuery);
        return ServerResponse.createBySuccess("??????????????????");
    }

    @Override
    public ServerResponse<String> updateRole(MiniRolePermissionParam param) {
        //????????????????????????????????????????????????
        this.deleteRole(param.getRoleNo());
        //??????????????????
        this.addRole(param);
        return ServerResponse.createBySuccess("???????????????????????????");
    }

    @Override
    public ServerResponse<List<MiniRole>> roleList() {
        Map<String,Object> map=new HashMap<>();
        List<MiniRole> miniRoles = this.baseMapper.selectByMap(map);
        return ServerResponse.createBySuccess(miniRoles);
    }

    @Override
    public ServerResponse<List<MiniManageUserInforParam>> selectUserByRole(String roleNo) {
        if(StringUtils.isBlank(roleNo)){
            throw new StringException("????????????????????????");
        }
        QueryWrapper<MiniRole>roleQuery=new QueryWrapper<>();
        roleQuery.eq("role_no",roleNo);
        MiniRole miniRole = this.baseMapper.selectOne(roleQuery);
        if(StringUtils.isNull(miniRole)){
            throw new StringException("?????????????????????");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("role_no",roleNo);
        List<MiniUserRole> userRoles = userRoleMapper.selectByMap(map);
        List<MiniManageUserInforParam>users=new ArrayList<>();

        for(MiniUserRole userRole:userRoles){
            QueryWrapper<MiniManagerUser>userQuery=new QueryWrapper<>();
            userQuery.eq("manager_no",userRole.getManagerNo());
            MiniManagerUser user = userMapper.selectOne(userQuery);
            MiniManageUserInforParam param=new MiniManageUserInforParam();
            BeanUtils.copyProperties(user,param);
            //????????????????????????
            QueryWrapper<MiniBussiesName>bussiesQuery=new QueryWrapper<>();
            bussiesQuery.eq("bussines_no",user.getBussinesNo());
            MiniBussiesName bussiesName = bussiesNameMapper.selectOne(bussiesQuery);
            param.setBusinessName(bussiesName.getBussiesName());
            users.add(param);
        }
        return ServerResponse.createBySuccess(users);
    }

    @Override
    public ServerResponse<String> addAccountRole(MiniManageUserParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("?????????????????????");
        }
        if(StringUtils.isBlank(param.getUserName())){
            return ServerResponse.createByError("?????????????????????????????????");
        }
        if(StringUtils.isBlank(param.getAccount())){
            return ServerResponse.createByError("???????????????????????????");
        }
        if(StringUtils.isBlank(param.getPassword())){
            return ServerResponse.createByError("???????????????????????????");
        }
        if(StringUtils.isBlank(param.getBussinesNo())){
            return ServerResponse.createByError("?????????????????????????????????");
        }
        if(StringUtils.isBlank(param.getRoleNo())){
            return ServerResponse.createByError("?????????????????????????????????");
        }
        //????????????
        if(StringUtils.isBlank(ManagerUserEmus.getEnumType(param.getType()))){
            return ServerResponse.createByError("??????????????????????????????");
        }
        //??????????????????
        QueryWrapper<MiniManagerUser>queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("account",param.getAccount());
        MiniManagerUser managerUser1 = userMapper.selectOne(queryWrapper1);
        if(managerUser1!=null){
            return ServerResponse.createByError("???????????????");
        }
        //??????????????????
        QueryWrapper<MiniBussiesName>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bussines_no",param.getBussinesNo());
        MiniBussiesName bussiesName = bussiesNameMapper.selectOne(queryWrapper);
        if(bussiesName==null){
            return ServerResponse.createByError("?????????????????????");
        }
        //??????????????????????????????????????????????????????
        if(!toolkitUtils.validateStrEnglish(param.getAccount())){
            return ServerResponse.createByError("???????????????");
        }
        if(!toolkitUtils.validateStrEnglish(param.getPassword())){
            return ServerResponse.createByError("???????????????");
        }
        //????????????????????????
        QueryWrapper<MiniRole>queryWrapper2=new QueryWrapper<>();
        queryWrapper2.eq("role_no",param.getRoleNo());
        MiniRole role = roleMapper.selectOne(queryWrapper2);
        if(StringUtils.isNull(role)){
            return ServerResponse.createByError("?????????????????????");
        }
        //?????????????????????
        MiniManagerUser managerUser=new MiniManagerUser();
        String s = toolkitUtils.addMd5Hash(param.getPassword(), random, salt);
        BeanUtils.copyProperties(param,managerUser);
        managerUser.setPassword(s);
        String uuid =toolkitUtils.getStringId();
        managerUser.setManagerNo(uuid);
        int count1=userMapper.insert(managerUser);
        if(count1==0){
           throw new StringException("?????????????????????:??????????????????");
        }
        MiniUserRole userRole=new MiniUserRole();
        userRole.setManagerNo(uuid);
        userRole.setRoleNo(param.getRoleNo());
        //??????????????????????????????
        int count2=userRoleMapper.insert(userRole);
        if(count2==0){
            throw new StringException("?????????????????????:?????????????????????????????????");
        }
        return ServerResponse.createBySuccess("???????????????????????????");
    }

    @Override
    public ServerResponse<List<MiniManageUserInforParam>> SelectUserByBussiness(String bussinesNo) {
        if(StringUtils.isBlank(bussinesNo)){
            return ServerResponse.createByError("????????????????????????");
        }
        //??????????????????????????????
        QueryWrapper<MiniBussiesName>bussiesNameQuery=new QueryWrapper<>();
        bussiesNameQuery.eq("bussines_no",bussinesNo);
        MiniBussiesName bussiesName = bussiesNameMapper.selectOne(bussiesNameQuery);
        if(StringUtils.isNull(bussiesName)){
            return ServerResponse.createByError("?????????????????????");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("bussines_no",bussinesNo);
        List<MiniManagerUser> miniManagerUsers = userMapper.selectByMap(map);
        List<MiniManageUserInforParam>params=new ArrayList<>();
        for(MiniManagerUser miniManagerUser:miniManagerUsers){
            MiniManageUserInforParam param=new MiniManageUserInforParam();
            BeanUtils.copyProperties(miniManagerUser,param);
            params.add(param);
        }
        return ServerResponse.createBySuccess(params);
    }
}