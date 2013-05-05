package com.hascode.plugin.confluence.xslt_processor.repository;

import java.util.List;

import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;

public interface XsltTemplateProvider {
	void save(XsltTemplate template);

	List<XsltTemplate> getAll();

	void remove(String id);
}
