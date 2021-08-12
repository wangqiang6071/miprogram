package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.MiniPaperSubjectMapper;
import com.zelu.miprogram.dao.MiniSubjectMapper;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniPaperSubject;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniSubjectPageParam;
import com.zelu.miprogram.emus.SubjectEmus;
import com.zelu.miprogram.excelmethod.ExcelUtil;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.service.MiniSubjectService;
import com.zelu.miprogram.utils.StringUtils;
import com.zelu.miprogram.utils.toolkitUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MiniSubjectServiceImpl extends ServiceImpl<MiniSubjectMapper, MiniSubject> implements MiniSubjectService {

    //=======后台接口===========
    @Autowired
    private MiniPaperSubjectMapper paperSubjectMapper;
    //导入
    @Transactional
    public ServerResponse<String>ImportSubject(MultipartFile file) throws Exception {
        if(StringUtils.isNull(file)){
            return ServerResponse.createByError("请上传excel文件");
        }
        ExcelUtil<MiniSubject> util=new ExcelUtil<>(MiniSubject.class);
        List<MiniSubject> subjectList=util.importExcel(file.getInputStream());
        final MiniManagerUser manageUser = toolkitUtils.getManageUser();
        //检查一下题目的名字是否存在
        for(MiniSubject subject:subjectList){
            if(StringUtils.isBlank(subject.getSubjectName())){
                throw new StringException("批量导入题目名字存在空数据");
            }
            Map<String,Object> map=new HashMap<>();
            map.put("subject_name",subject.getSubjectName());
            List<MiniSubject> subjects = this.baseMapper.selectByMap(map);
            if(subjects.size()>0){
                throw new StringException("批量导入的题目名字'"+subject.getSubjectName()+"'已存在");
            }
            //题目题型是否正确
            if(StringUtils.isBlank(SubjectEmus.getEnumType(subject.getSubjectType()))){
                throw new StringException("批量导入的题目类型不正确,对应的题目:"+subject.getSubjectName());
            }
            //检查传入的答案是否正确
            if(!StringUtils.equals(SubjectEmus.getEnumType(subject.getSubjectType()),SubjectEmus.Subject_Fill_In_Blanks.getMsg())){
                if(!StringUtils.equals(subject.getSubjectChoice().split(",")[0].split(" ")[0],"A")){
                    throw new StringException("传入题目备选答案格式错误");
                }
                //备选的答案必须大于2
                int size=subject.getSubjectChoice().split(",").length;
                if(size < 2||size>8){
                    throw new StringException("传入题目备选答案数量不能少于2个并且不能多于8个");
                }
                //正确答案必须为大写字母
                char aa[] = subject.getRightAnswer().toCharArray();
                for (int  q= 0; q < aa.length; q++) {
                    if (aa[q] < 'A' && aa[q] > 'Z') {
                        throw new StringException("传入的正确答案不是大写字母");
                    }
                }
                List<String>answers=new ArrayList<>();
                //选择的必须为大写字母
                for (int i = 0; i < size; i++) {
                    String str= subject.getSubjectChoice().split(",")[i].split(" ")[0];
                    answers.add(str);
                    char ss[] = str.toCharArray();
                    for (int j = 0; j < ss.length; j++){
                        if(ss[j]<'A' && ss[j]>'Z'){
                            throw new StringException("传入的备选答案中'备选字母'错误");
                        }
                    }
                }
                //答案必须在备选答案中存在
                if(!answers.contains(subject.getRightAnswer())){
                    throw new StringException("传入的正确答案没有在备选的答案中");
                }
            }
            //题目编号
            subject.setSubjectNo(toolkitUtils.getStringId());
            subject.setBussinesNo(manageUser.getBussinesNo());
        }
        if(!this.saveBatch(subjectList)){
            throw new StringException("题目导入失败");
        }
        return ServerResponse.createBySuccess("题目导入成功");
    }

    //导出
    public ServerResponse<IPage<MiniSubject>>ExportSubject(@RequestBody MiniSubjectPageParam param){
        Page<MiniSubject> itempage= new Page<>(param.getPageIndex(),param.getPageSize());
        QueryWrapper<MiniSubject> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getSubjectName())){
            queryWrapper.like("subject_name",param.getSubjectName());
        }
        if(StringUtils.isNotBlank(SubjectEmus.getEnumType(param.getSubjectType()))){
            queryWrapper.eq("subject_type",param.getSubjectType());
        }
        final Subject subject = SecurityUtils.getSubject();
        if(!toolkitUtils.isAdmin(subject)){
            MiniManagerUser managerUser=(MiniManagerUser)subject.getPrincipal();
            queryWrapper.eq("bussines_no",managerUser.getBussinesNo());
        }
        IPage<MiniSubject> recordIPage = this.baseMapper.selectPage(itempage, queryWrapper);
        //如果是2为导出excel数据
        if(param.getPageType()==2){
            ExcelUtil<MiniSubject> util=new ExcelUtil<>(MiniSubject.class);
            util.exportExcel(recordIPage.getRecords(),"题目列表");
        }
        return ServerResponse.createBySuccess(recordIPage);
    }

    @Override
    public ServerResponse<String> Add_Subject(MiniSubjectPageParam param) {
        //判断是否为空
        if(StringUtils.isNull(param)){
            throw new StringException("上传的参数为空");
        }
        //检查一下题目的名字是否重复
        if(StringUtils.isBlank(param.getSubjectName())){
            throw new StringException("传入的题目名字不能为空");
        }
        //检查一下传入题目的名字是否重复
        Map<String,Object>map=new HashMap<>();
        map.put("subject_name",param.getSubjectName());
        if(this.baseMapper.selectByMap(map).size()>0){
            throw new StringException("传入的题目名字已经存在");
        }
        //题目题型是否正确
        if(StringUtils.isBlank(SubjectEmus.getEnumType(param.getSubjectType()))){
            throw new StringException("题目类型不正确");
        }
        //检查传入的答案是否正确
        if(!StringUtils.equals(SubjectEmus.getEnumType(param.getSubjectType()),SubjectEmus.Subject_Fill_In_Blanks.getMsg())){
            if(!StringUtils.equals(param.getSubjectChoice().split(",")[0].split(" ")[0],"A")){
                throw new StringException("传入题目备选答案格式错误");
            }
            //备选的答案必须大于2
            int size=param.getSubjectChoice().split(",").length;
            if(size < 2||size>8){
                throw new StringException("传入题目备选答案数量不能少于2个并且不能多于8个");
            }
            //正确答案必须为大写字母
            char aa[] = param.getRightAnswer().toCharArray();
            for (int  q= 0; q < aa.length; q++) {
                if (aa[q] < 'A' && aa[q] > 'Z') {
                    throw new StringException("传入的正确答案不是大写字母");
                }
            }
            List<String>answers=new ArrayList<>();
            //选择的必须为大写字母
            for (int i = 0; i < size; i++) {
               String str= param.getSubjectChoice().split(",")[i].split(" ")[0];
                answers.add(str);
                char ss[] = str.toCharArray();
                for (int j = 0; j < ss.length; j++){
                    if(ss[j]<'A' && ss[j]>'Z'){
                        throw new StringException("传入的备选答案中'备选字母'错误");
                    }
                }
            }
            //答案必须在备选答案中存在
            if(!answers.contains(param.getRightAnswer())){
                throw new StringException("传入的正确答案没有在备选的答案中");
            }
        }
        //添加题目
        MiniSubject subject=new MiniSubject();
        BeanUtils.copyProperties(param,subject);
        final MiniManagerUser manageUser = toolkitUtils.getManageUser();
        subject.setBussinesNo(manageUser.getBussinesNo());
        //题目编号
        subject.setSubjectNo(toolkitUtils.getStringId());
        int insert = this.baseMapper.insert(subject);
        if(insert==0){
            return ServerResponse.createByError("添加题目失败");
        }
        return ServerResponse.createBySuccess("添加题目成功");
    }

    @Override
    public ServerResponse<String> Update_Subject(MiniSubjectPageParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("传入的参数不能为空");
        }
        //查询题目是否存在
        Map<String,Object>map=new HashMap<>();
        map.put("subject_no",param.getSubjectNo());
        List<MiniSubject> subjects = this.baseMapper.selectByMap(map);
        if(subjects==null){
            return ServerResponse.createByError("题目编号错误");
        }
        if(subjects.size()==0){
            return ServerResponse.createByError("题目编号不存在");
        }
        if(!StringUtils.equals(subjects.get(0).getSubjectName(),param.getSubjectName())){
            Map<String,Object>map2=new HashMap<>();
            map2.put("subject_name",param.getSubjectName());
            List<MiniSubject> subjects2 = this.baseMapper.selectByMap(map2);
            if(subjects2.size()>0){
                return ServerResponse.createByError("题目名字已存在");
            }
        }
        //题目题型是否正确
        if(StringUtils.isBlank(SubjectEmus.getEnumType(param.getSubjectType()))){
            return ServerResponse.createByError("题目类型不正确");
        }
        //检查传入的答案是否正确
        if(!StringUtils.equals(SubjectEmus.getEnumType(param.getSubjectType()),SubjectEmus.Subject_Fill_In_Blanks.getMsg())) {
            if (!StringUtils.equals(param.getSubjectChoice().split(",")[0].split(" ")[0], "A")) {
                return ServerResponse.createByError("传入题目备选答案格式错误");
            }
            //备选的答案必须大于2
            int size = param.getSubjectChoice().split(",").length;
            if (size < 2) {
                return ServerResponse.createByError("传入题目备选答案数量不能少于2个");
            }
            //正确答案必须为大写字母
            char aa[] = param.getRightAnswer().toCharArray();
            for (int q = 0; q < aa.length; q++) {
                if (aa[q] < 'A' && aa[q] > 'Z') {
                    throw new StringException("传入的正确答案不是大写字母");
                }
            }
            List<String> answers = new ArrayList<>();
            //选择的必须为大写字母
            for (int i = 0; i < size; i++) {
                String str = param.getSubjectChoice().split(",")[i].split(" ")[0];
                answers.add(str);
                char ss[] = str.toCharArray();
                for (int j = 0; j < ss.length; j++) {
                    if (ss[j] < 'A' && ss[j] > 'Z') {
                        throw new StringException("传入的备选答案中'备选字母'错误");
                    }
                }
            }
            //答案必须在备选答案中存在
            if (!answers.contains(param.getRightAnswer())) {
                return ServerResponse.createByError("传入的正确答案没有在备选的答案中");
            }
        }
        BeanUtils.copyProperties(param,subjects.get(0));
        int count=this.baseMapper.updateById(subjects.get(0));
        if(count==0){
            return ServerResponse.createByError("题目更新失败");
        }
        return ServerResponse.createBySuccess("题目更新成功");
    }

    @Override
    public ServerResponse<String> Delete_Subject(String subjectNo) {
        if(StringUtils.isBlank(subjectNo)){
            return ServerResponse.createByError("传入的题目编号为空");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("subject_no",subjectNo);
        List<MiniSubject> subjects = this.baseMapper.selectByMap(map);
        if(subjects.size()==0){
            return ServerResponse.createByError("传入的题目编号不存在");
        }
        List<MiniPaperSubject> paperSubjects = paperSubjectMapper.selectByMap(map);
        if(paperSubjects.size()>0){
           return ServerResponse.createByError("当前题目已被试卷关联");
        }
        int count=this.baseMapper.deleteByMap(map);
        if(count==0){
            return ServerResponse.createByError("题目删除失败");
        }
        return ServerResponse.createBySuccess("题目删除成功");
    }
}