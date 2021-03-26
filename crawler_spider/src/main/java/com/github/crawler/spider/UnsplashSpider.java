package com.github.crawler.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.crawler.api.CrawlRequest;
import com.github.crawler.api.CrawlerConfiguration;
import com.github.crawler.api.event.*;
import com.github.crawler.api.model.Page;
import com.github.crawler.entity.UnsplashBoardImg;
import com.github.crawler.http.HttpEntity;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnsplashSpider extends BaseCrawler {
	
	private final static String IMG_API = "https://unsplash.com/napi/photos/";
	Pattern pagePat = Pattern.compile("page=([0-9]+)");
	public UnsplashSpider(CrawlerConfiguration config) {
		super(config);
	}

	@Override
	public void visit(Page page) throws Exception {
		JSONArray jsonArray = page.jsonArray();
		
		if(jsonArray.size() == 0) {
			LOG.info("没有数据 url={}", page.url());
			return;
		}
		JSONObject jsonObj = jsonArray.getJSONObject(0);
		if (jsonObj.toString().length() < 5) {
			LOG.info("没有数据 url={}", page.url());
			return;
		}

		Matcher matcher = pagePat.matcher(page.url());
		if (matcher.find()) {
			int nextPage = Integer.parseInt(matcher.group(1)) + 1;
			String nextUrl = page.url().replaceFirst("page=([0-9]+)", "page=" + nextPage);
			this.crawl(CrawlRequest.createDefaultWithAttr(nextUrl, page.getCrawlCandidate()));
		}
		parse(page);
	}

    
    private void parse(Page page){
		JSONArray jsonArray = page.jsonArray();
		JSONObject extInfo = page.getExtInfo();

		for (int i = 0; i < jsonArray.size(); i++) {

			UnsplashBoardImg img = new UnsplashBoardImg();
			try {
				JSONObject item = jsonArray.getJSONObject(i);
				String id = item.getString("id");


				img.setUnsplash_image_id(id);

				img.setBoard_id(extInfo.getLong("dzb_id"));
				img.setUser_id(extInfo.getLong("dz_id"));

				if(duplicateItem2(img))continue;

				img.setUnsplash_image_id(id);

				HttpEntity htmlData = getCustomHttpClient().doGetByProxy(null,IMG_API+id);
				String content = htmlData.getContentAsString();


				JSONObject parseObject = JSON.parseObject(content);
				JSONArray tags = parseObject.getJSONArray("tags");
				img.setViews(parseObject.getInteger("views") == null ? 0 : parseObject.getInteger("views"));
				img.setDownloads(parseObject.getInteger("downloads") == null ? 0 : parseObject.getInteger("downloads"));
				String title = Stream.of(tags.toArray()).map(it -> (JSONObject) it).map(it -> it.getString("title")).collect(Collectors.joining(","));

				if (StringUtils.isNotBlank(title) && title.length() > 1000) {
					title = title.substring(0, 1000);
					title = title.substring(0, title.lastIndexOf(","));
				}
				img.setTag(title);

				JSONObject urls = (JSONObject) item.computeIfAbsent("urls", s -> new JSONObject());
				Optional<String> img_max_url = getMaxImgUrl(urls);
				if(!img_max_url.isPresent()) return;
				String img_url = getSteadyImgUrl(urls);

				if (img_url == null) {
					return;
				}
				JSONObject user = (JSONObject) item.computeIfAbsent("user", s -> new JSONObject());
				JSONObject user_links = (JSONObject) user.computeIfAbsent("links", s -> new JSONObject());
				JSONObject img_links = (JSONObject) item.computeIfAbsent("links", s -> new JSONObject());

				try {
					String img_create_at = item.getString("created_at");
					String img_update_at = item.getString("updated_at");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
					img.setImg_create_at(sdf.parse(img_create_at.substring(0,23).replace("T", " ")));
					img.setImg_update_at(sdf.parse(img_update_at.substring(0,23).replace("T", " ")));
				} catch (ParseException e) {
					LOG.info("时间错误 Unsplash_image_id()={}",img.getUnsplash_image_id(),e);
				}

				img.setUnsplash_image_id(item.getString("id"));
				img.setImg_width(item.getInteger("width"));
				img.setImg_height(item.getInteger("height"));
				img.setTitle(item.getString("alt_description"));
				img.setDescription(item.getString("description"));
				img.setLikes(item.getInteger("likes"));
				img.setImg_max_url(img_max_url.get());
				img.setImg_url(img_url);

				// 大图页链接
				img.setOriginal_link(img_links.getString("html"));
				// 图片拥有者名字
				img.setCr_author(user.getString("name"));
				img.setUnsplash_user_id(user.getString("id"));
				img.setInstagram_username(user.getString("instagram_username"));

				// 用户的网站主页链接
				img.setCr_author_url(user_links.getString("html"));

				img.setBoard_id((Long)extInfo.get("dzb_id"));
				img.setUser_id((Long)extInfo.get("dz_id"));
				consumer.accept(img);
			} catch (IOException e1) {
				LOG.info("获取标签错误 Unsplash_image_id()={}",img.getUnsplash_image_id(),e1);
			}
    	}
    	
    }
    
	private String getSteadyImgUrl(JSONObject urls) {

		String[] params = {"small","thumb","regular"}; 
		for (String param : params) {
			if(urls.containsKey(param)) {
				return urls.getString(param).replaceAll("&w=(.+)&fit", "&w=680&fit");
			}
		}
				
		return null;
	}
    
	private Optional<String> getMaxImgUrl(JSONObject urls) {
		Optional<String> optional = Optional.empty();
		if (urls == null) {
			return optional;
		}
		if (urls.containsKey("full")) {
			optional = Optional.of(urls.getString("full"));
		}
		if (urls.containsKey("raw")) {
			optional = Optional.of(urls.getString("raw"));
		}
		return optional;
	}
    
	@Override
	protected void onParseError(ParseErrorEvent event) {
		super.onParseError(event);
		LOG.error("parse error: request={}",event.getCrawlCandidate(),  event.getException());
		if(null != errorEventConsumer) errorEventConsumer.accept(event);
	}
	
    @Override
    protected void onNetworkError(NetworkErrorEvent event) {
        super.onNetworkError(event);
    }

    @Override
    protected void onResponseError(ResponseErrorEvent event) {
        super.onResponseError(event);
    }

    @Override
    protected void onRequestRedirect(RequestRedirectEvent event) {
        super.onRequestRedirect(event);
    }

    @Override
    protected void onPageLoadTimeout(PageLoadTimeoutEvent event) {
        super.onPageLoadTimeout(event);
    }

	
}
