package com.hascode.plugin.confluence.xslt_processor.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.repository.XsltTemplateProvider;

public class XsltTransformerFilter implements Filter {
	private static final String PARAM_TEMPLATE_ID = "xsltransform";

	private static final Logger log = LoggerFactory
			.getLogger(XsltTransformerFilter.class);

	private final XsltTemplateProvider xsltTemplateProvider;

	public XsltTransformerFilter(final XsltTemplateProvider xsltTemplateProvider) {
		this.xsltTemplateProvider = xsltTemplateProvider;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res,
			final FilterChain chain) throws IOException, ServletException {
		log.debug("xslt-transformer-filter applied..");
		String templateId = req.getParameter(PARAM_TEMPLATE_ID);
		if (!StringUtils.isEmpty(templateId)) {
			log.info("trying to find xsl template for given id: {}", templateId);
			Optional<XsltTemplate> tpl = xsltTemplateProvider
					.getById(templateId);
			if (tpl.isPresent()) {
				XsltTemplate xslt = tpl.get();
				log.info("found a template for given id: {}. template: {}",
						templateId, xslt.toString());
				log.info("applying xsl template to request body: {}",
						IOUtils.toString(req.getReader()));
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

}
