package com.hascode.plugin.confluence.xslt_processor.entity;


public interface XsltTemplate {
	void setId(final String id);

	String getId();

	String getTitle();

	void setTitle(final String title);

	String getTemplate();

	void setTemplate(final String template);
}
