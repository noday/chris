

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		File root = new File("D:\\code\\work");
		File directory = new File(root, "1");
		File htmlFile = new File(directory, "a.html");
		File imageDirectory = new File(directory, "img");
		imageDirectory.mkdirs();
		
		Document doc = Jsoup.connect("https://mp.weixin.qq.com/s/h9kzOoFz_50LEe1rVkcwoQ").get();
		System.out.println(doc.title());
		Element author = doc.getElementById("js_name");
		System.out.println(author.html());
		Element content = doc.getElementById("js_content");
		Elements imgs = content.getElementsByTag("img");
		for (int i = 0; i < imgs.size(); i++) {
			Element img = imgs.get(i);
			img.getElementsByAttribute("data-src");
			String src = img.attr("data-src");
			String type = img.attr("data-type");
			Response srcResp = Jsoup.connect(src).ignoreContentType(true).timeout(10000).execute();
			BufferedInputStream srcStream = srcResp.bodyStream();
			File imgFile = new File(imageDirectory, i+"."+type);
			imgFile.createNewFile();
			FileUtils.copyInputStreamToFile(srcStream, imgFile);
			img.attr("src", "img/i"+"."+type);
			img.removeAttr("data-src");
			img.removeAttr("class");
			System.out.println(img.toString());
		}
		System.out.println(content.toString());
		
	}
}
