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
package net.noday.chris.service.impl;

import java.util.List;

import net.noday.chris.dao.FileInfoDao;
import net.noday.chris.model.FileInfo;
import net.noday.chris.service.FileService;
import net.noday.core.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * chris FileServiceImpl
 *
 * @author <a href="http://www.noday.net">Noday</a>
 * @version , 2015-1-15
 * @since 
 */
@Service
public class FileServiceImpl implements FileService {

	@Autowired private FileInfoDao dao;
	
	/* (non-Javadoc)
	 * @see net.noday.chris.service.impl.FileService#get(long)
	 */
	@Override
	public FileInfo get(long id) {
		return dao.get(id);
	}
	
	/* (non-Javadoc)
	 * @see net.noday.chris.service.impl.FileService#save(net.noday.chris.model.Link)
	 */
	@Override
	public long save(FileInfo obj) {
		long aid = dao.save(obj);
		return aid;
	}
	
	/* (non-Javadoc)
	 * @see net.noday.chris.service.impl.FileService#update(net.noday.chris.model.Link)
	 */
	@Override
	public void update(FileInfo obj) {
		dao.update(obj);
	}
	
	/* (non-Javadoc)
	 * @see net.noday.chris.service.impl.FileService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		dao.delete(id);
	}
	
	/* (non-Javadoc)
	 * @see net.noday.chris.service.impl.FileService#findAll()
	 */
	@Override
	public List<FileInfo> findAll() {
		return dao.findAll();
	}
	
	/* (non-Javadoc)
	 * @see net.noday.chris.service.impl.FileService#listPage(int)
	 */
	@Override
	public Page<FileInfo> listPage(int index) {
		Page<FileInfo> page = new Page<FileInfo>(index, Page.DEFAULTSIZE);
		page.setRowCount(dao.findCount());
		page.setRows(dao.findByPage(page.getPageIndex(), page.getSize()));
		return page;
	}
	
}
