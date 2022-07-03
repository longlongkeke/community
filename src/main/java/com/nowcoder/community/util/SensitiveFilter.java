package com.nowcoder.community.util;

import com.nowcoder.community.controller.LoginController;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author longke
 * @description
 * @create 2022/6/30 20:03
 */
@Component//将这个对象放入spring容器里
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String REPLACE="***";

    private TriNode root=new TriNode();

    @PostConstruct//在服务启动时初始化，这个方法被调用（构造前缀树）
    public void init(){
        try(InputStream is=this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            ){
            String keyword;
            while ((keyword=reader.readLine())!=null){
                //添加到前缀树
                this.addKeyWord(keyword);
            }


        } catch(IOException e){
            logger.error("加载敏感词失败："+e.getMessage());
        }


    }

    //将一个敏感词添加到前缀树
    private void addKeyWord(String keyword) {
        TriNode tempNode=root;
        for(int i=0;i<keyword.length();i++){
            char c = keyword.charAt(i);
            TriNode child = tempNode.getChild(c);
            if(child==null){
                //初始化子节点
                child=new TriNode();
                tempNode.addChild(c,child);
            }
            tempNode=child;
            if(i==keyword.length()-1){
                tempNode.setisKeyEnd(true);
            }
        }
    }
    /*//过滤敏感词
    *
    * */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TriNode tempNode=root;
        //指针2
        int begin=0;

        //指针3
        int end=0;

        //结果
        StringBuilder sb=new StringBuilder();
        while (end<text.length()){
            char c = text.charAt(end);
            //跳出符号   ☆开☆票☆
            if(isSymbol(c)){
                if(tempNode==root){
                    sb.append(c);
                    begin++;
                }
                end++;
                continue;
            }
            //检查下级结点
            tempNode = tempNode.getChild(c);
            if(tempNode==null){
                sb.append(text.charAt(begin));
                end=++begin;
                tempNode=root;
            }else if(tempNode.isKeyEnd()){
                //发现敏感词
                sb.append(REPLACE);
                begin=++end;
                tempNode=root;

            }else{
                end++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin,end));
        return sb.toString();

    }
    public boolean isSymbol(Character c){
        /*isAsciiAlphanumeric是判断是否是合法字符*/
        return !CharUtils.isAsciiAlphanumeric(c)&& (c<0x2E80||c>0x9FFF);
    }
    /*前缀树*/
    private class TriNode{
        private boolean isKeyEnd=false;
        private Map<Character,TriNode> children=new HashMap<>();//子节点 key是子节点字符，value是下级结点

        public boolean isKeyEnd() {
            return isKeyEnd;
        }

        public void setisKeyEnd(boolean isKeyEnd) {
            this.isKeyEnd = isKeyEnd;
        }
        public  void addChild(Character ch,TriNode node){
            children.put(ch,node);
        }
        public TriNode getChild(Character ch){
            return children.get(ch);
        }
    }
}
