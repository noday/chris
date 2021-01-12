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
package net.noday.chris.dao;

import java.util.List;

import net.noday.chris.model.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * chris ArticleDao
 *
 * @author <a href="http://www.noday.net">Noday</a>
 * @version , 2012-11-25
 * @since 
 */
@Repository
public class ArticleDao {

	@Autowired private JdbcTemplate jdbc;
	@Autowired private NamedParameterJdbcTemplate namedJdbc;
	
	public long save(Article article) {
		String sql = "insert into article(title,description,content,alias,url,author_id,cover,category_id,tags,sign_name)" +
				" values(:title,:description,:content,:alias,:url,:authorId,:cover,:categoryId,:tags,:signName)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql, new BeanPropertySqlParameterSource(article), keyHolder);
        return keyHolder.getKey().longValue();
	}
	
	public void update(Article a) {
		String sql = "update article set title=?,content=?,tags=?,sign_name=? where id=?";
		jdbc.update(sql, a.getTitle(), a.getContent(), a.getTags(), a.getSignName(), a.getId());
	}
	
	public Article get(Long id) {
		String sql = "select * from article a where a.id=?";
		Article a = jdbc.queryForObject(sql, new BeanPropertyRowMapper<Article>(Article.class), id);
		return a;
	}
	
	public void delete(Long id) {
		String sql = "delete from article where id=?";
		jdbc.update(sql, id);
	}
	
	public void updateTopable(Long id, boolean topable) {
		String sql = "update article set topable=? where id=?";
		jdbc.update(sql, topable, id);
	}
	
	public void updateViewCount(long id) {
		String sql = "update article set view_count=view_count+1 where id=?";
		jdbc.update(sql, id);
	}
	
	public List<Article> findByPage(int index, int size) {
		String sql = "select * from article a order by topable desc,create_time desc limit ?,?";
		List<Article> list = jdbc.query(sql, new BeanPropertyRowMapper<Article>(Article.class), (index-1)*size, size);
		return list;
	}
	
	public int findCount() {
		String sql = "select count(a.id) from article a";
		int count = jdbc.queryForObject(sql, Integer.class);
		return count;
	}
	
	public List<Article> findByPage4Tag(int index, int size, String tagName) {
		String sql = "select a.* from article a,tag_ref b,tag c where a.id=b.target_id and b.tag_id=c.id and c.name=? order by topable desc,create_time desc limit ?,?";
		List<Article> list = jdbc.query(sql, new BeanPropertyRowMapper<Article>(Article.class), tagName, (index-1)*size, size);
		return list;
	}
	
	public int findCount4Tag(String tagName) {
		String sql = "select count(a.id) from article a,tag_ref b,tag c where a.id=b.target_id and b.tag_id=c.id and c.name=?";
		int count = jdbc.queryForObject(sql, Integer.class, tagName);
		return count;
	}
	
	public List<Article> findMostView(int amount) {
		String sql = "select * from article a order by a.view_count desc limit 0,?";
		List<Article> list = jdbc.query(sql, new BeanPropertyRowMapper<Article>(Article.class), amount);
		return list;
	}
	public List<Article> findRecent(int amount) {
		String sql = "select * from article a order by a.create_time desc limit 0,?";
		List<Article> list = jdbc.query(sql, new BeanPropertyRowMapper<Article>(Article.class), amount);
		return list;
	}
	public List<Article> findMostReply(int amount) {
		String sql = "select * from article a order by a.comment_count desc limit 0,?";
		List<Article> list = jdbc.query(sql, new BeanPropertyRowMapper<Article>(Article.class), amount);
		return list;
	}
}
