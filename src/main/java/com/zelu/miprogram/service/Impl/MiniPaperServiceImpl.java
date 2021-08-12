package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.MiniBussiesNameMapper;
import com.zelu.miprogram.dao.MiniPaperMapper;
import com.zelu.miprogram.dao.MiniPaperSubjectMapper;
import com.zelu.miprogram.domain.MiniBussiesName;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniPaperPageParam;
import com.zelu.miprogram.domain.Params.MiniPaperParam;
import com.zelu.miprogram.domain.Params.MiniPaperUpDownParam;
import com.zelu.miprogram.emus.PaperEmus;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.service.MiniPaperService;
import com.zelu.miprogram.utils.StringUtils;
import com.zelu.miprogram.utils.toolkitUtils;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MiniPaperServiceImpl extends ServiceImpl<MiniPaperMapper,MiniPaper> implements MiniPaperService {

    //======================后台接口======================
    @Autowired
    private MiniPaperSubjectMapper paperSubjectMapper;
    @Autowired
    private MiniBussiesNameMapper bussiesNameMapper;
    //创建试卷
    public ServerResponse<String> Create_Page(@RequestBody MiniPaperParam param){
        //查询以下题目是否重复
        if(StringUtils.isBlank(param.getPaperName())){
            return ServerResponse.createByError("试卷的名字不能为空");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("paper_name",param.getPaperName());
        if(this.baseMapper.selectByMap(map).size()>0){
            return ServerResponse.createByError("试卷的名字已经存在");
        }
        //校验试卷类型是否正确
        if(StringUtils.isBlank(PaperEmus.getEnumType(param.getPaperLevel()))){
            return ServerResponse.createByError("试卷类型传入错误");
        }
        if(StringUtils.isBlank(PaperEmus.getEnumType(param.getPaperType()))){
            return ServerResponse.createByError("试卷类型传入错误");
        }
        //开放时间不能晚于当前时间
        if(param.getStartTime().before(new Date())){
            return ServerResponse.createByError("试卷开始时间不能晚于当前时间");
        }
        if(param.getEndTime().before(param.getStartTime())){
            return ServerResponse.createByError("试卷结束时间不能早于时间开始时间");
        }
        //考试的时间不能小于0
        if(param.getTotalTimes()<=0){
            return ServerResponse.createByError("试卷考试时间不能小于等于0");
        }
        if(param.getTotalSorce()<=0){
            return ServerResponse.createByError("试卷考试总分不能小于等于0");
        }
        //试卷的范围
        MiniPaper paper=new MiniPaper();
        BeanUtils.copyProperties(param,paper);
        if(StringUtils.isBlank(PaperEmus.getEnumType(param.getPaperRange()))){
            return ServerResponse.createByError("传入的试卷范围有误");
        }
        //如果是企业用户
        if (StringUtils.equals(PaperEmus.getEnumType(param.getPaperRange()),PaperEmus.Pager_Range_Business.getMsg())){
            final MiniManagerUser manageUser = toolkitUtils.getManageUser();
            paper.setPaperBussno(manageUser.getBussinesNo());
        }
        //试卷编号
        paper.setPaperNo(toolkitUtils.getStringId());
        int count=this.baseMapper.insert(paper);
        if(count==0){
            return ServerResponse.createByError("试卷创建失败");
        }
        return ServerResponse.createBySuccess("试卷创建成功");
    }

    @Override
    public ServerResponse<String> Delete_Page(String pageNo) {
        //传入的编号是否存在
        if(StringUtils.isBlank(pageNo)){
            return ServerResponse.createByError("删除的试卷编号不能为空");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("paper_no",pageNo);
        if(this.baseMapper.selectByMap(map).size()==0){
            return ServerResponse.createByError("删除的试卷编号不存在");
        }
        //查询试卷下是否存在试题
        if(paperSubjectMapper.selectByMap(map).size()>0){
            return ServerResponse.createByError("删除的试卷存在绑定的试题不能删除");
        }
        int count=this.baseMapper.deleteByMap(map);
        if(count==0){
            return ServerResponse.createByError("删除试卷失败");
        }
        return ServerResponse.createBySuccess("删除试卷成功");
    }
    //批量上架/下架
    @Override
    public ServerResponse<String> Betch_Up_Down_Page(@RequestBody MiniPaperUpDownParam param) {
        //传入的编号是否存在
        if(StringUtils.isNull(param.getPageNo())){
            return ServerResponse.createByError("试卷编号不能为空");
        }
        if(param.getPageNo().size()==0){
            return ServerResponse.createByError("试卷编号不能为空");
        }
        Map<String,Object>map=new HashMap<>();
        for(String no:param.getPageNo()){
            map.put("paper_no",no);
            List<MiniPaper> papers=this.baseMapper.selectByMap(map);
            if(papers.size()==0){
                return ServerResponse.createByError("传入的试卷编号不存在");
            }
            papers.get(0).setStatus(param.getStatus());
            this.baseMapper.updateById(papers.get(0));
        }
        return ServerResponse.createBySuccess("试卷"+PaperEmus.getEnumType(param.getStatus())+"成功");
    }

    @Override
    public ServerResponse<String> Update_Page(MiniPaperParam param) {
        if(param!=null){
            return ServerResponse.createByError("修改试卷的参数不能为空");
        }
        //判断试卷是否已经上架
        Map<String,Object>page=new HashMap<>();
        page.put("paper_no",param.getPageNo());
        List<MiniPaper> papers = this.baseMapper.selectByMap(page);
        if(papers.get(0)==null){
            return ServerResponse.createByError("修改的试卷编号不存在");
        }
        //查看修改的名字是否重复
        if(!StringUtils.equals(param.getPaperName(),papers.get(0).getPaperName())){
           //判断一下修改的名字是否已经存在
            Map<String,Object> map=new HashMap<>();
            map.put("paper_name",param.getPaperName());
            if(this.baseMapper.selectByMap(map).size()>0){
                return ServerResponse.createByError("试卷的名字已经存在");
            }
        }
        //校验试卷类型是否正确
        if(StringUtils.isBlank(PaperEmus.getEnumType(param.getPaperLevel()))){
            return ServerResponse.createByError("试卷类型传入错误");
        }
        if(StringUtils.isBlank(PaperEmus.getEnumType(param.getPaperType()))){
            return ServerResponse.createByError("试卷类型传入错误");
        }
        //开放时间不能晚于当前时间
        if(param.getStartTime()!=null){
            if(param.getStartTime().before(new Date())){
                return ServerResponse.createByError("试卷开始时间不能晚于当前时间");
            }
        }
        if(param.getEndTime()!=null){
            if(param.getEndTime().before(param.getStartTime())){
                return ServerResponse.createByError("试卷结束时间不能早于时间开始时间");
            }
        }
        //考试的时间不能小于0
        if(param.getTotalTimes()<=0){
            return ServerResponse.createByError("试卷考试时间不能小于等于0");
        }
        if(param.getTotalSorce()<=0){
            return ServerResponse.createByError("试卷考试总分不能小于等于0");
        }
        if(StringUtils.equals(PaperEmus.getEnumType(papers.get(0).getStatus()),PaperEmus.Pager_Up.getMsg())){
            return ServerResponse.createByError("试卷上架状态不能进行修改");
        }
        //修改试卷信息
        MiniPaper paper=new MiniPaper();
        BeanUtils.copyProperties(param,paper);
        //试卷的范围
        //如果是企业用户
        if (StringUtils.equals(PaperEmus.getEnumType(param.getPaperRange()),PaperEmus.Pager_Range_Business.getMsg())){
            final MiniManagerUser manageUser = toolkitUtils.getManageUser();
            paper.setPaperBussno(manageUser.getBussinesNo());
        }
        QueryWrapper<MiniPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("paper_no",param.getPageNo());
        int update = this.baseMapper.update(paper, queryWrapper);
        if(update==0){
            return ServerResponse.createByError("试卷更新失败");
        }
        return ServerResponse.createBySuccess("试卷更新成功");
    }

    @Override
    public ServerResponse<IPage<MiniPaper>> List_Page(MiniPaperPageParam param) {
        Page<MiniPaper> itempage= new Page<>(param.getPageIndex(),param.getPageSize());
        QueryWrapper<MiniPaper> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getPaperName())){
            queryWrapper.like("paper_name",param.getPaperName());
        }
        if(StringUtils.isNotBlank(PaperEmus.getEnumType(param.getPaperLevel()))){
            queryWrapper.eq("paper_level",param.getPaperLevel());
        }
        final Subject subject = SecurityUtils.getSubject();
        if(!toolkitUtils.isAdmin(subject)){
            MiniManagerUser managerUser=(MiniManagerUser)subject.getPrincipal();
            queryWrapper.eq("paper_bussno",managerUser.getBussinesNo());
            queryWrapper.eq("paper_range",7);
        }
        IPage<MiniPaper> recordIPage = this.baseMapper.selectPage(itempage, queryWrapper);
        return ServerResponse.createBySuccess(recordIPage);
    }

}