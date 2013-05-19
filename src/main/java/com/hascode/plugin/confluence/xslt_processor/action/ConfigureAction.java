package com.hascode.plugin.confluence.xslt_processor.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.user.User;
import com.google.common.base.Optional;
import com.hascode.plugin.confluence.xslt_processor.entity.BasicXsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.repository.XsltTemplateProvider;
import com.opensymphony.webwork.ServletActionContext;

public class ConfigureAction extends ConfluenceActionSupport {
	private static final String RESULT_NO_PERMISSION = "no-permission";
	private static final String PARAM_EDIT_ID = "xslt-id";
	private static final String PARAM_ID = "id";
	private static final String PARAM_TEMPLATE = "xslt-template";
	private static final String PARAM_TITLE = "xslt-title";
	private static final String PARAM_ACTION = "action";
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(ConfigureAction.class);
	private final XsltTemplateProvider xsltTemplateProvider;
	private XsltTemplate templateEdited; // mutable

	public ConfigureAction(final XsltTemplateProvider xsltTemplateProvider) {
		this.xsltTemplateProvider = xsltTemplateProvider;
	}

	@Override
	public String execute() {
		if (!userIsAdministrator())
			return RESULT_NO_PERMISSION;
		return handleActions();
	}

	private boolean userIsAdministrator() {
		User user = getRemoteUser();
		return (user != null && permissionManager
				.isConfluenceAdministrator(user));
	}

	private String handleActions() {
		final String action = param(PARAM_ACTION);
		if ("save".equals(action)) {
			log.debug("saving a new template");
			saveTemplate();
		}
		if ("edit".equals(action)) {
			log.debug("saving a new template");
			editTemplate();
		}
		if ("delete".equals(action)) {
			log.debug("removing a template");
			removeTemplate();
		}
		return SUCCESS;
	}

	private void editTemplate() {
		final String id = param(PARAM_ID);
		Optional<XsltTemplate> template = xsltTemplateProvider.getById(id);
		if (template.isPresent()) {
			templateEdited = template.get();
		}

	}

	private void removeTemplate() {
		final String id = param(PARAM_ID);
		xsltTemplateProvider.remove(id);
		addActionMessage("msg.remove.success", new Object[] {});
	}

	private void saveTemplate() {
		final String id = param(PARAM_EDIT_ID);
		final String title = param(PARAM_TITLE);
		final String template = param(PARAM_TEMPLATE);
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(template)) {
			addActionError("error.data.missing", new Object[] {});
			return;
		}
		XsltTemplate xsltTemplate = new BasicXsltTemplate(id);
		xsltTemplate.setTitle(title);
		xsltTemplate.setTemplate(template);
		xsltTemplateProvider.save(xsltTemplate);
		addActionMessage("msg.save.success", new Object[] {});
	}

	private String param(final String key) {
		return ServletActionContext.getRequest().getParameter(key);
	}

	public List<XsltTemplate> getXsltTemplates() {
		return xsltTemplateProvider.getAll();
	}

	public final XsltTemplate getTemplateEdited() {
		return templateEdited;
	}
}
