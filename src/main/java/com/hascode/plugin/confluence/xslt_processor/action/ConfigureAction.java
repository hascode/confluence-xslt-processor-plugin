package com.hascode.plugin.confluence.xslt_processor.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.hascode.plugin.confluence.xslt_processor.entity.BasicXsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.repository.XsltTemplateProvider;
import com.opensymphony.webwork.ServletActionContext;

public class ConfigureAction extends ConfluenceActionSupport {
	private static final String PARAM_ID = "id";
	private static final String PARAM_TEMPLATE = "xslt-template";
	private static final String PARAM_TITLE = "xslt-title";
	private static final String PARAM_ACTION = "action";
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(ConfigureAction.class);
	private final XsltTemplateProvider xsltTemplateProvider;

	public ConfigureAction(final XsltTemplateProvider xsltTemplateProvider) {
		this.xsltTemplateProvider = xsltTemplateProvider;
	}

	@Override
	public String execute() {
		final String action = param(PARAM_ACTION);
		if ("save".equals(action)) {
			log.debug("saving a new template");
			saveTemplate();
		}
		if ("delete".equals(action)) {
			log.debug("removing a template");
			removeTemplate();
		}
		return SUCCESS;
	}

	private void removeTemplate() {
		final String id = param(PARAM_ID);
		xsltTemplateProvider.remove(id);
	}

	private void saveTemplate() {
		final String title = param(PARAM_TITLE);
		final String template = param(PARAM_TEMPLATE);
		XsltTemplate xsltTemplate = new BasicXsltTemplate();
		xsltTemplate.setTitle(title);
		xsltTemplate.setTemplate(template);
		xsltTemplateProvider.save(xsltTemplate);
	}

	private String param(final String key) {
		return ServletActionContext.getRequest().getParameter(key);
	}

	public List<XsltTemplate> getXsltTemplates() {
		return xsltTemplateProvider.getAll();
	}
}
