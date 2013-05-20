package com.hascode.plugin.confluence.xslt_processor.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.user.User;
import com.google.common.base.Optional;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.repository.XsltTemplateProvider;
import com.hascode.plugin.confluence.xslt_processor.transformer.XsltPageTransformer;

public class XsltTransformerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(XsltTransformerServlet.class);
	private static final XsltPageTransformer transformer = new XsltPageTransformer();
	private final XsltTemplateProvider xsltTemplateProvider;
	private final PageManager pageManager;
	private final PermissionManager permissionManager;
	private final UserAccessor userAccessor;

	public XsltTransformerServlet(
			final XsltTemplateProvider xsltTemplateProvider,
			final PageManager pageManager,
			final PermissionManager permissionManager,
			final UserAccessor userAccessor) {
		this.xsltTemplateProvider = xsltTemplateProvider;
		this.pageManager = pageManager;
		this.permissionManager = permissionManager;
		this.userAccessor = userAccessor;
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

		ContentEntityObject ceo = pageManager.getById(ctx.getPageId().get());
		if (ceo == null) {
			printErrorMessage(ctx, res);
			return;
		}

		if (!userHasViewPermissions(req, ceo)) {
			printPermissionErrorMessage(res);
			return;
		}

		Optional<XsltTemplate> template = findTemplate(ctx);
		if (!template.isPresent()) {
			printErrorMessage(ctx, res);
			return;
		}

		printTransformationResult(res, template, ceo);
	}

	private void printPermissionErrorMessage(final HttpServletResponse res) {
		try {
			res.getWriter().append(
					"Sorry, insufficient permission to view this page");
		} catch (IOException e) {
			log.warn("io exception thrown", e);
		}
	}

	private boolean userHasViewPermissions(final HttpServletRequest req,
			final ContentEntityObject ceo) {
		String userName = req.getRemoteUser();
		if (StringUtils.isEmpty(userName)) {
			log.warn("no user name found in request .. skipping output");
			return false;
		}

		User user = userAccessor.getUser(userName);
		if (user == null) {
			log.warn("no user object for given name {} found", userName);
			return false;
		}

		return permissionManager.hasPermission(user, Permission.VIEW, ceo);
	}

	private void printTransformationResult(final HttpServletResponse res,
			final Optional<XsltTemplate> template, final ContentEntityObject ceo)
			throws IOException {
		final XsltTemplate xslt = template.get();
		final String content = ceo.getBodyAsString();
		log.info("transforming given content: {} with xsl template: {}",
				content, xslt);
		res.getWriter().append(transformer.transform(content, xslt));
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
