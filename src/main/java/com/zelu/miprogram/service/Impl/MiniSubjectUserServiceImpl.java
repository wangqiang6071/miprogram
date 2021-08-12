package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.dao.MiniSubjectUserMapper;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.MiniSubjectUser;
import com.zelu.miprogram.service.MiniSubjectUserService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class MiniSubjectUserServiceImpl extends ServiceImpl<MiniSubjectUserMapper, MiniSubjectUser> implements MiniSubjectUserService {

}