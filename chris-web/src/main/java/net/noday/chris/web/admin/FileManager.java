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

import javax.validation.Valid;

import net.noday.chris.model.FileInfo;
import net.noday.chris.service.FileService;
import net.noday.core.web.GeneralController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * chris FileManager
 *
 * @author <a href="http://www.noday.net">Noday</a>
 * @version , 2015-1-15
 * @since 
 */
@Controller @RequestMapping("/admin/files")
public class FileManager extends GeneralController<FileInfo, Long> {

//	private static final Logger log = Logger.getLogger(FileManager.class);
	
	@Autowired private FileService service;

	@Override
	public String create() {
		return "admin/link/add";
	}

	@Override
	public String save(@Valid FileInfo obj, BindingResult result, Model m) {
		if (result.hasErrors()) {
			responseValidError(m, result);
		} else {
			long id = service.save(obj);
			responseData(m, id);
		}
		return "admin/link/add-success";
	}

	@Override
	public String delete(@PathVariable("id") Long id, Model m) {
		service.delete(id);
		responseResult(m, true);
		return "admin/link/add-success";
	}

	@Override
	public String edit(@PathVariable("id") Long id, Model m) {
		m.addAttribute(service.get(id));
		return "admin/link/add";
	}

	@Override
	public String modify(@PathVariable("id") Long id, @Valid FileInfo obj, BindingResult result, Model m) {
		if (result.hasErrors()) {
			responseValidError(m, result);
		} else {
//			service.update(obj);
			responseData(m, id);
		}
		return "admin/link/add-success";
	}

	@Override
	public String list(@PathVariable("index") int index, Model m) {
		m.addAttribute(service.listPage(index));
		return "admin/link/list";
	}
	
}
