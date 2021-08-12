package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.MiniPaperMapper;
import com.zelu.miprogram.dao.MiniPaperSubjectMapper;
import com.zelu.miprogram.dao.MiniSubjectMapper;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.MiniPaperSubject;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniPaperSubjectParam;
import com.zelu.miprogram.emus.PaperEmus;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.service.MiniPaperSubjectService;
import com.zelu.miprogram.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MiniPaperSubjectServiceImpl extends ServiceImpl<MiniPaperSubjectMapper, MiniPaperSubject> implements MiniPaperSubjectService {

    @Autowired
    private MiniPaperMapper paperMapper;
    @Autowired
    private MiniSubjectMapper subjectMapper;

    @Override
    @Transactional
    public ServerResponse<String> Create_Update_PageSubject(MiniPaperSubjectParam param) {
        //判断一下参数是否为空
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("传入的试卷参数不能为空");
        }
        if(StringUtils.isBlank(param.getPageNo())){
            return ServerResponse.createByError("传入的试卷编号不能为空");
        }
        //当前试卷编号是否存在
        Map<String,Object> map=new HashMap<>();
        map.put("paper_no",param.getPageNo());
        List<MiniPaper> papers = paperMapper.selectByMap(map);
        if(papers.size()==0){
            return ServerResponse.createByError("传入的试卷编号不存在");
        }
        //取余判断是否整除
        Integer count=papers.get(0).getTotalSorce()%param.getSubjectNo().size()==0 ? papers.get(0).getTotalSorce()/param.getSubjectNo().size() : -1;
        if(count==-1){
            throw new StringException("添加的试题数量与总分不能除尽");
        }
        //检查一下当前试卷下是否存在试题
        if(this.baseMapper.selectByMap(map).size()==0){
            //批量添加
            for(String subjectNo:param.getSubjectNo()){
                Map<String,Object>map1=new HashMap<>();
                map1.put("subject_no",subjectNo);
                List<MiniSubject> subjects = subjectMapper.selectByMap(map1);
                if(subjects.size()==0){
                    throw new StringException("传入的题目编号有错误");
                }
                MiniPaperSubject papersubject=new MiniPaperSubject();
                papersubject.setPaperNo(param.getPageNo());
                papersubject.setSubjectNo(subjectNo);
                this.baseMapper.insert(papersubject);
            }
            //更新试卷中的题目数量和单个题目的数值
            papers.get(0).setTotalSubjects(param.getSubjectNo().size());
            papers.get(0).setPeerSorce(count);
            paperMapper.updateById(papers.get(0));
            return ServerResponse.createBySuccess("试卷中的试题添加成功");
        }else{
            //如果当前试卷编号存在==判断当前试卷是否上架/下架
           if(StringUtils.equals(PaperEmus.Pager_Up.getMsg(),PaperEmus.getEnumType(papers.get(0).getStatus()))){
               //已上架
               return ServerResponse.createByError("试卷已上架不能进行更改");
           }else{
               //已下架
               //1删除之前的数据
               this.baseMapper.deleteByMap(map);
               //2添加最新的数据
               for(String subjectno:param.getSubjectNo()){
                   Map<String,Object>map2=new HashMap<>();
                   map2.put("subject_no",subjectno);
                   List<MiniSubject> subjects = subjectMapper.selectByMap(map2);
                   if(subjects.size()==0){
                       throw new StringException("传入的题目编号有错误");
                   }
                   MiniPaperSubject papersubject=new MiniPaperSubject();
                   papersubject.setPaperNo(param.getPageNo());
                   papersubject.setSubjectNo(subjectno);
                   this.baseMapper.insert(papersubject);
               }
               //更新试卷中的题目数量和单个题目的数值
               papers.get(0).setTotalSubjects(param.getSubjectNo().size());
               papers.get(0).setPeerSorce(count);
               paperMapper.updateById(papers.get(0));
               return ServerResponse.createBySuccess("试卷中的试题更新成功");
           }
        }
    }

    @Override
    public ServerResponse<List<MiniSubject>> List_Pager_Subject(String paperNo) {
        if(StringUtils.isBlank(paperNo)){
            return ServerResponse.createByError("传入的试卷的编号不能为空");
        }
        //查询试题是否存在
        Map<String,Object>map=new HashMap<>();
        map.put("paper_no",paperNo);
        if(paperMapper.selectByMap(map).size()==0){
            return ServerResponse.createByError("传入的试卷的编号不存在");
        }
        List<MiniPaperSubject> papersubjects = this.baseMapper.selectByMap(map);
        List<MiniSubject>subjects=new ArrayList<>();
        for(MiniPaperSubject papersubject:papersubjects){
            Map<String,Object>map2=new HashMap<>();
            map2.put("subject_no",papersubject.getSubjectNo());
            List<MiniSubject> miniSubjects = this.subjectMapper.selectByMap(map2);
            subjects.addAll(miniSubjects);
        }
        return ServerResponse.createBySuccess(subjects);
    }

}