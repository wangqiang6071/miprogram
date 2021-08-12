package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.dao.MiniRolePermissionMapper;
import com.zelu.miprogram.domain.MiniPermission;
import com.zelu.miprogram.domain.MiniRolePermission;
import com.zelu.miprogram.service.MiniRolePermissionService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class MiniRolePermissionServiceImpl extends ServiceImpl<MiniRolePermissionMapper, MiniRolePermission> implements MiniRolePermissionService {

}