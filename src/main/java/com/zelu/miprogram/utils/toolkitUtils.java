package com.zelu.miprogram.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.MiniUser;
import com.zelu.miprogram.emus.ManagerUserEmus;
import com.zelu.miprogram.exceptions.StringException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangqiang
 * @Date: 2021/6/1 17:19
 */
@Slf4j
public class toolkitUtils {


    // 1MD5加密+盐（用户名）+散列次数
    public static String addMd5Hash(String sercrty,int random,String salt) {
        return new Md5Hash(sercrty, salt, random).toString();
    }
    //默认格式
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String STANDARD_FORMAT1 = "ss mm HH dd MM ? yyyy";
    //1时间转字符串
    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    //2字符串转时间
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }
    //3字符串转时间
    public static Date cornStrToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT1);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }


    //判断字符串是否由字母和数字组成
    /** 全字母规则 正整数规则*/
    public static final String STR_ENG_PATTERN="^[a-z0-9A-Z]+$";
    public static boolean validateStrEnglish(final String str){
        if(StringUtils.isEmpty(str)){
            return Boolean.FALSE ;
        }
        boolean matches = str.matches(STR_ENG_PATTERN);
        if(str.length() < 6 || str.length() > 20) {
            return  Boolean.FALSE;
        }
        if (matches){
            return  true;
        }else{
            return  false;
        }
    }
    //校验手机号
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //5 uuidfactary
    public static String getStringId(){
        Long random = UUID.randomUUID().getMostSignificantBits();
        long ane=random+new Date().getTime();
        Long id = Math.abs(ane);
        return id.toString();
    }
    //判断是否是合法的url
    private static String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
    public static boolean UrlPattern(String url){
        Pattern patt = Pattern. compile(regex);
        Matcher matcher = patt.matcher(url);
        return matcher.matches();
    }

    /***
     *  功能描述：日期转换cron表达式
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    //计算差多少分钟
    public static Long dateToMinus(Date startdate,Date enddate) {
        if(startdate==null) {
            throw new StringException("定时任务开始时间错误");
        }
        if(enddate==null) {
            throw new RuntimeException("定时任务开始时间错误");
        }
        long nh = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        Long time=enddate.getTime()-startdate.getTime();
        // 计算差多少分钟
        return time / nh;
    }

    //生成token
    public static String CreateToken(String username){
        //生成token
        return JWT.create().withClaim("username",username).sign(Algorithm.HMAC256("F0Jbv4SICxVxFxar-gEnDpVgA"));
    }

    //解析token
    public static String ParaseToken(String token){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256("F0Jbv4SICxVxFxar-gEnDpVgA")).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        return verify.getClaim("username").asString();
    }

    //写回html文件
    public static void rebackHtml(String outPutHtml,String html){
        OutputStream outStream=null;
        try {
            outStream=new BufferedOutputStream(new FileOutputStream(outPutHtml));
            outStream.write(html.toString().getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //解析模版
    public static Document paraHtml(String html) throws IOException {
        File file=new File(html);
        Document document = Jsoup.parse(file,"utf-8");
        return document;
    }

    //linux获取ip
    public static String getLinuxHostIp(){
        String hostIP = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                        hostIP=ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIP;
    }
    //获取当前管理员用户
    public static MiniManagerUser getManageUser(){
        MiniManagerUser managerUser=null;
        try {
            managerUser=(MiniManagerUser)SecurityUtils.getSubject().getPrincipal();
        }catch (ClassCastException e){
            throw new ClassCastException("后台管理系统非法账号");
        }
        if(com.zelu.miprogram.utils.StringUtils.isNull(managerUser)){
            throw new StringException("非法账号");
        }
        return managerUser;
    }
    //检查是否是管理员
    public static boolean isAdmin(Subject subject){
        MiniManagerUser managerUser=null;
        try {
            managerUser=(MiniManagerUser)subject.getPrincipal();
        }catch (ClassCastException e){
            throw new ClassCastException("后台管理系统非法账号");
        }
        if(com.zelu.miprogram.utils.StringUtils.isNull(managerUser)){
            throw new StringException("非法账号");
        }
        if(StringUtils.equals(ManagerUserEmus.getEnumType(managerUser.getType()),ManagerUserEmus.Super_Manager_User.getMsg())){
            return true;
        }
        return false;
    }
    //微信用户是否登陆
    public static MiniUser isWxLogin(){
        MiniUser user=null;
         Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()) {
            try {
                 user=(MiniUser) subject.getPrincipal();
            }catch (ClassCastException e){
                throw new ClassCastException("微信小程序非法账号");
            }
        }else {
            throw new StringException("温馨提示:请使用对应的账号登陆");
        }
        return user;
    }
    //后台管理系统用户是否登陆
    public static MiniManagerUser isManageLogin(){
        MiniManagerUser user=null;
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()) {
            try {
                user=(MiniManagerUser) subject.getPrincipal();
            }catch (ClassCastException e){
                throw new ClassCastException("后台管理系统非法账号");
            }
        }else {
            throw new StringException("温馨提示:请使用对应的账号登陆");
        }
        return user;
    }

    public static MiniSubject RandomSubject(MiniSubject subject){
        String rightList="A B C D E F G H";
        String[] split = subject.getSubjectChoice().split(",");
        List<String> lists = Arrays.asList(split);
        Collections.shuffle(lists);
        int ii=0;
        for(int i=0;i<lists.size();i++){
            if(com.zelu.miprogram.utils.StringUtils.equals(lists.get(i).split(" ")[0],subject.getRightAnswer())){
                ii=i;
            }
        }
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < lists.size(); i++) {
            sb.append(rightList.split(" ")[i]).append(" ").append(lists.get(i).split(" ")[1]).append(",");
        }
        MiniSubject subjects=new MiniSubject();
        BeanUtils.copyProperties(subject,subjects);
        subjects.setRightAnswer(sb.toString().split(",")[ii].split(" ")[0]);
        subjects.setSubjectChoice(sb.toString());
        return subjects;
    }

    public static void main(String[] args) {
        String rightList="A B C D E F G H";
        String right="B";
        String right_answer="A a,B b,C c,D d";
        String[] split = right_answer.split(",");
        List<String> lists = Arrays.asList(split);
        System.out.println("之前==》"+lists);
        Collections.shuffle(lists);
        System.out.println("之后==》"+lists);
        int ii=0;
        for(int i=0;i<lists.size();i++){
            if(com.zelu.miprogram.utils.StringUtils.equals(lists.get(i).split(" ")[0],right)){
                ii=i;
            }
        }
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < lists.size(); i++) {
            sb.append(rightList.split(" ")[i]).append(" ").append(lists.get(i).split(" ")[1]).append(",");
        }
        System.out.println("最后的答案1==》"+sb.toString());
        System.out.println("最后的答案==》"+sb.toString().split(",")[ii].split(" ")[0]);
    }
}
