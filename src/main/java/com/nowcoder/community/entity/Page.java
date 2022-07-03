package com.nowcoder.community.entity;

/**
 * @author longke
 * @description
 * @create 2022/6/29 17:33
 */
/**
 * 封装分页相关的信息
 */

public class Page {
    private int current=1;//当前页码
    private int total;//总记录数

    private int limit=10;
    private String path;//用于复用分页链接

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1){
            this.current = current;
        }

    }

    public int getTotal() {

        return total;
    }

    public void setTotal(int total) {
        if(total>=0){
            this.total = total;
        }

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit<100){
            this.limit = limit;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    /***
     * 获取当前页的起始行
     * ***/
    public int getoffset(){
        return (current-1)*limit;
    }

    /***
     * 获取总页数
     * ***/
    public int getPages(){
        return total%limit==0?total/limit:total/limit+1;
    }
    public int getFrom(){
        int from=current-2;
        return from<1?1:from;
    }
    public int getTo(){
        int to=current+2;
        int allpages=getPages();
        return to>allpages?allpages:to;
    }



}
