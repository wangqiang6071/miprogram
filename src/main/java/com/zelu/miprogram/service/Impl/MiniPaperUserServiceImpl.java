package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.dao.MiniPaperSubjectMapper;
import com.zelu.miprogram.dao.MiniPaperUserMapper;
import com.zelu.miprogram.domain.MiniPaperSubject;
import com.zelu.miprogram.domain.MiniPaperUser;
import com.zelu.miprogram.service.MiniPaperUserService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class MiniPaperUserServiceImpl extends ServiceImpl<MiniPaperUserMapper, MiniPaperUser> implements MiniPaperUserService {

}