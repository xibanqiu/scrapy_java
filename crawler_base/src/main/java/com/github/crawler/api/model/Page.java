package com.github.crawler.api.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.crawler.api.CrawlCandidate;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page是爬取过程中，内存中保存网页爬取信息的一个容器，Page只在内存中存 放，用于保存一些网页信息，方便用户进行自定义网页解析之类的操作。
 *
 */
public class Page {

    public static final Logger LOG = LoggerFactory.getLogger(Page.class);

    private CrawlCandidate crawlCandidate;

    private String contentType;

    private String html = null;
    private boolean contentHasNotChanged;
    private Document doc = null;

    private String charset="utf-8";
    private byte[] content;
    private JSONObject extInfo;
    private Integer pageNum;
    private Integer totalPage;
    private List<String> nextPages = new ArrayList<>();


    public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public JSONObject getExtInfo() {
        if(null == extInfo){
            extInfo = crawlCandidate.getExtInfo();
        }
        return extInfo;
    }

    public JSONObject jsonObject(){
        return JSONObject.parseObject(html());
    }

    public JSONArray jsonArray(){
        return JSONObject.parseArray(html());
    }

    public JSONObject regexJSONObject(String regex){
        return JSONObject.parseObject(regex(regex));
    }

    public JSONObject regexJSONObject(String regex, int group){
        return JSONObject.parseObject(regex(regex, group));
    }

    public JSONArray regexJSONArray(String regex){
        return JSONObject.parseArray(regex(regex));
    }

    public JSONArray regexJSONArray(String regex, int group){
        return JSONObject.parseArray(regex(regex, group));
    }


    /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过attrs("img[src]","abs:src")可获取网页中所有图片的链接
     *
     * @param cssSelector
     * @param attrName
     * @return
     */
    public ArrayList<String> attrs(String cssSelector, String attrName) {
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(cssSelector);
        for (Element ele : eles) {
            if (ele.hasAttr(attrName)) {
                result.add(ele.attr(attrName));
            }
        }
        return result;
    }

    /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过attr("img[src]","abs:src")可获取网页中第一个图片的链接
     *
     * @param cssSelector
     * @param attrName
     * @return
     */
    public String attr(String cssSelector, String attrName) {
        return select(cssSelector).attr(attrName);
    }


    public ArrayList<String> selectTextList(String cssSelector){
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(cssSelector);
        for(Element ele:eles){
            result.add(ele.text());
        }
        return result;
    }

    public String selectText(String cssSelector) {
        return select(cssSelector).first().text();
    }

    public Elements select(String cssSelector) {
        return this.doc().select(cssSelector);
    }

    public Element select(String cssSelector, int index) {
        Elements eles = select(cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = eles.size() + index;
        }
        return eles.get(realIndex);
    }

    public String regex(String regex, int group, String defaultResult) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html());
        if (matcher.find()) {
            return matcher.group(group);
        } else {
            return defaultResult;
        }
    }

    public String regex(String regex, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html());
        matcher.find();
        return matcher.group(group);
    }

    public String regexAndFormat(String regex, String format){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html());
        matcher.find();
        String[] strs = new String[matcher.groupCount()];
        for(int i=0;i<matcher.groupCount();i++){
            strs[i] = matcher.group(i+1);
        }
        return String.format(format, strs);
    }

    public String regex(String regex, String defaultResult) {
        return regex(regex, 0, defaultResult);
    }

    public String regex(String regex) {
        return regex(regex, 0);
    }

    public Page(CrawlCandidate crawlCandidate,
                ContentType contentType,
                byte[] content){

        this.crawlCandidate = crawlCandidate;
        this.contentType = contentType.getMimeType();
        Charset getCharset = contentType.getCharset();
        if( null != getCharset) {
        	this.charset = getCharset.name();
        }
        this.content = content;
    }



    /**
     * 返回网页/文件的内容
     *
     * @return 网页/文件的内容
     */
    public byte[] content() {
        return content;
    }

    public void content(byte[] content){
        this.content = content;
    }


    /**
     * 返回网页的url
     *
     * @return 网页的url
     */
    public String url() {
        return crawlCandidate.getRequestUrl().toString();
    }


    /**
     * 返回网页的源码字符串
     *
     * @return 网页的源码字符串
     */
    public String html() {
        if (html != null) {
            return html;
        }

        if (content == null) {
            return null;
        }
        try {
            html = new String(content, charset);
        } catch (UnsupportedEncodingException e) {
            LOG.info("Exception when decoding "+ charset,e);
            return null;
        }
        return html;
    }

    /**
     * 设置网页的源码字符串
     *
     * @param html 网页的源码字符串
     */
    public void html(String html) {
        this.html = html;
    }



    public String contentType() {
        return contentType;
    }

    /**
     * 返回网页解析后的DOM树(Jsoup的Document对象) 已废弃，使用doc()方法代替
     *
     * @return 网页解析后的DOM树
     */
    public Document doc() {
        if (doc != null) {
            return doc;
        }
        this.doc = Jsoup.parse(html(), url());
        return doc;
//        try {
//            this.doc = Jsoup.parse(html(), url());
//            return doc;
//        } catch (Exception ex) {
//            LOG.info("Exception", ex);
//            return null;
//        }
    }

    /**
     * 设置网页解析后的DOM树(Jsoup的Document对象)
     *
     * @param doc 网页解析后的DOM树
     */
    public void doc(Document doc){
        this.doc = doc;
    }


    public CrawlCandidate getCrawlCandidate() {
        return crawlCandidate;
    }

    public void setCrawlCandidate(CrawlCandidate crawlCandidate) {
        this.crawlCandidate = crawlCandidate;
    }

    public int getPageNum() {
        if(getExtInfo().containsKey("pageNum")){
            pageNum = (Integer) getExtInfo().get("pageNum");
        }
        return pageNum;
    }

    public int incrementAndGetPageNum(){
        setPageNum(getPageNum() + 1);
        return getPageNum();
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        getExtInfo().put("pageNum", pageNum);
    }

    public Integer getTotalPage() {
        if(getExtInfo().containsKey("totalPage")){
            totalPage = (Integer) getExtInfo().get("totalPage");
        }
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
        getExtInfo().put("totalPage", totalPage);
    }

    public List<String> getNextPages() {
        return nextPages;
    }

    public void setNextPage(String nextPage) {
        this.nextPages.add(Objects.requireNonNull(nextPage));
    }

    public void setNextPages(List<String> nextPages) {
        this.nextPages.addAll(Objects.requireNonNull(nextPages));
    }

    public boolean isContentHasNotChanged() {
        return contentHasNotChanged;
    }

    public void setContentHasNotChanged(boolean contentHasNotChanged) {
        this.contentHasNotChanged = contentHasNotChanged;
    }
}