package com.hascode.plugin.confluence.xslt_processor.servlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Optional;

public class TransformationRequestContext {
	private static final String PARAM_PAGEID = "pageId";
	private static final String PARAM_TEMPLATE_ID = "tpl";

	private String pageId;
	private String templateId;

	public final Optional<Long> getPageId() {
		try {
			return Optional.of(Long.parseLong(pageId));
		} catch (Exception e) {
			return Optional.absent();
		}
	}

	public final void setPageId(final String pageId) {
		this.pageId = pageId;
	}

	public final Optional<String> getTemplateId() {
		return (StringUtils.isEmpty(templateId)) ? Optional.<String> absent()
				: Optional.of(templateId);
	}

	public final void setTemplateId(final String templateId) {
		this.templateId = templateId;
	}

	public boolean isValid() {
		return getPageId().isPresent() && getTemplateId().isPresent();
	}

	public static TransformationRequestContext newFromHttpRequest(
			final HttpServletRequest req) {
		TransformationRequestContext ctx = new TransformationRequestContext();
		ctx.setPageId(req.getParameter(PARAM_PAGEID));
		ctx.setTemplateId(req.getParameter(PARAM_TEMPLATE_ID));
		return ctx;
	}

	@Override
	public String toString() {
		return "TransformationRequestContext [pageId=" + pageId
				+ ", templateId=" + templateId + "]";
	}
}
