/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.noday.chris.web.admin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import net.noday.chris.model.Article;
import net.noday.chris.model.ForkObj;
import net.noday.chris.service.ArticleService;
import net.noday.core.web.GeneralController;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * chris ArticleController
 *
 * @author <a href="http://www.noday.net">Noday</a>
 * @version , 2012-11-24
 * @since 
 */
@Controller @RequestMapping("/admin/articles")
public class ArticleManager extends GeneralController<Article, Long> {

//	private static final Logger log = Logger.getLogger(ArticleManager.class);
	
	@Autowired private ArticleService service;
	
	@Override
	public String create() {
		return "admin/article/add";
	}
	
	@Override
	public String save(@Valid Article article, BindingResult result, Model m) {
		if (result.hasErrors()) {
			m.addAttribute(result.getFieldErrors());
		} else {
			article.setAuthorId(getUser().getId());
			long id = service.save(article);
			responseData(m, id);
		}
		return "admin/article/add-success";
	}

	@Override
	public String delete(@PathVariable("id") Long id, Model m) {
		service.delete(id);
		responseResult(m, true);
		return "";
	}
	
	@Override
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute(service.get(id));
		return "admin/article/add";
	}
	
	@Override
	public String modify(@PathVariable("id") Long id, @Valid Article article, BindingResult result, Model m) {
		service.update(article);
		responseData(m, id);
		return "admin/article/add-success";
	}
	
	@Override
	public String list(@PathVariable("index") int index, Model model) {
		model.addAttribute(service.listPage(index));
		return "admin/article/list";
	}
	
	@RequestMapping(value = "tops", method = RequestMethod.POST)
	public Model topable(@RequestParam("id") Long id, @RequestParam("topable") boolean topable, Model m) {
		service.updateTopable(id, topable);
		responseResult(m, true);
		return m;
	}
	
	@GetMapping("/fork")
	@ResponseBody
	public Object fork(@RequestParam("url") String url) {
		List<String> images = new ArrayList<String>();
		String html;
		String title;
		
		try {
			Document doc = Jsoup.connect(url).get();
			title = doc.title();
			Element content = doc.getElementById("js_content");
			Elements imgs = content.getElementsByTag("img");
			for (int i = 0; i < imgs.size(); i++) {
				Element img = imgs.get(i);
//				img.getElementsByAttribute("data-src");
				String dataSrc = img.attr("data-src");
				String dataType = img.attr("data-type");
				Response srcResp = Jsoup.connect(dataSrc).ignoreContentType(true).timeout(10000).execute();
				BufferedInputStream srcStream = srcResp.bodyStream();
				String imageFileName = getImageFileName(dataSrc, dataType);
				File imgFile = new File(getImageFolder(), imageFileName);
				imgFile.createNewFile();
				FileUtils.copyInputStreamToFile(srcStream, imgFile);
				String src = "img/" + imageFileName;
				img.attr("src", src);
				img.removeAttr("data-src");
				img.removeAttr("class");
				images.add(src);
			}
			html = content.html();
			ForkObj obj = new ForkObj();
			obj.setTitle(title);
			obj.setHtml(html);
			obj.setImages(images);
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private String getImageFileName(String dataSrc, String dataType) {
		if (dataType==null || dataType.trim().equals("")) {
			if (dataSrc.contains("wx_fmt=")) {
				dataType = dataSrc.substring(dataSrc.indexOf("wx_fmt=") + 7);
			}
		}
		return UUID.randomUUID() + "." + dataType;
	}
	
	private String getImageFolder() {
		try {
			return ResourceUtils.getURL("classpath:").getFile();
		} catch (FileNotFoundException e) {
			return new ApplicationHome(getClass()).getDir().getAbsolutePath();
		}
	}
}
