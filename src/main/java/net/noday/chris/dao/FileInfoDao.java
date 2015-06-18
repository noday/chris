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

import net.noday.chris.model.FileInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * chris FileInfoDao
 *
 * @author <a href="http://www.noday.net">Noday</a>
 * @version , 2015-1-15
 * @since 
 */
@Repository
public class FileInfoDao {

	@Autowired private JdbcTemplate jdbc;
	@Autowired private NamedParameterJdbcTemplate namedJdbc;
	
	public long save(FileInfo obj) {
		String sql = "insert into FileInfo(name,url,description,rank,category_id)" +
				" values(:name,:url,:description,:rank,:categoryId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql, new BeanPropertySqlParameterSource(obj), keyHolder);
        return keyHolder.getKey().longValue();
	}
	
	public void update(FileInfo obj) {
		String sql = "update FileInfo set name=?,url=?,rank=?,description=? where id=?";
		jdbc.update(sql);
	}
	
	public FileInfo get(Long id) {
		String sql = "select * from FileInfo a where a.id=?";
		FileInfo a = jdbc.queryForObject(sql, new BeanPropertyRowMapper<FileInfo>(FileInfo.class), id);
		return a;
	}
	
	public void delete(Long id) {
		String sql = "delete from FileInfo where id=?";
		jdbc.update(sql, id);
	}
	
	public List<FileInfo> findAll() {
		String sql = "select * from FileInfo a order by rank";
		List<FileInfo> list = jdbc.query(sql, new BeanPropertyRowMapper<FileInfo>(FileInfo.class));
		return list;
	}
	
	public List<FileInfo> findByPage(int index, int size) {
		String sql = "select * from FileInfo a order by rank limit ?,?";
		List<FileInfo> list = jdbc.query(sql, new BeanPropertyRowMapper<FileInfo>(FileInfo.class), (index-1)*size, size);
		return list;
	}
	
	public int findCount() {
		String sql = "select count(a.id) from FileInfo a";
		int count = jdbc.queryForInt(sql);
		return count;
	}
	
}
