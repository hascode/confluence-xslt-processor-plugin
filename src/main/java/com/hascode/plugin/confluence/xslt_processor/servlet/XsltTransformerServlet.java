package com.hascode.plugin.confluence.xslt_processor.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.pages.PageManager;
import com.google.common.base.Optional;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.repository.XsltTemplateProvider;

public class XsltTransformerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(XsltTransformerServlet.class);

	private final XsltTemplateProvider xsltTemplateProvider;
	private final PageManager pageManager;

	public XsltTransformerServlet(
			final XsltTemplateProvider xsltTemplateProvider,
			final PageManager pageManager) {
		this.xsltTemplateProvider = xsltTemplateProvider;
		this.pageManager = pageManager;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse res) throws IOException, ServletException {
		log.debug("xslt-transformer-servlet called..");

		TransformationRequestContext ctx = TransformationRequestContext
				.newFromHttpRequest(req);
		if (!ctx.isValid()) {
			printErrorMessage(ctx, res);
			return;
		}

		Optional<XsltTemplate> template = findTemplate(ctx);
		if (!template.isPresent()) {
			printErrorMessage(ctx, res);
			return;
		}

		ContentEntityObject ceo = pageManager.getById(ctx.getPageId().get());
		if (ceo == null) {
			printErrorMessage(ctx, res);
			return;
		}

		final XsltTemplate xslt = template.get();
		final String content = ceo.getBodyAsString();
		log.info("transforming given content: {} with xsl template: {}",
				content, xslt);
	}

	private Optional<XsltTemplate> findTemplate(
			final TransformationRequestContext ctx) {
		final String templateId = ctx.getTemplateId().get();
		log.info("trying to find xsl template for given id: {}", templateId);
		return xsltTemplateProvider.getById(templateId);
	}

	private void printErrorMessage(final TransformationRequestContext ctx,
			final HttpServletResponse res) {
		try {
			res.getWriter().append(
					"Invalid request - given context: " + ctx.toString());
		} catch (IOException e) {
		}
	}
}
