package com.hascode.plugin.confluence.xslt_processor.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XsltTransformerFilter implements Filter {
	private static final Logger log = LoggerFactory
			.getLogger(XsltTransformerFilter.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res,
			final FilterChain chain) throws IOException, ServletException {
		log.debug("xslt-transformer-filter applied..");
		String templateName = req.getParameter("xsltransform");
		if (!StringUtils.isEmpty(templateName)) {
			log.info("trying to apply xsl template {}", templateName);
			System.err.println("XXXXXXXXXXXXXXXXXXX");
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

}
