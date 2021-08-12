package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.dao.MiniUserRoleMapper;
import com.zelu.miprogram.domain.MiniSubjectUser;
import com.zelu.miprogram.domain.MiniUserRole;
import com.zelu.miprogram.service.MiniUserRoleService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class MiniUserRoleServiceImpl extends ServiceImpl<MiniUserRoleMapper, MiniUserRole> implements MiniUserRoleService {

}