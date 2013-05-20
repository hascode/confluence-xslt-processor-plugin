package com.hascode.plugin.confluence.xslt_processor.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.atlassian.modzdetector.IOUtils;
import com.hascode.plugin.confluence.xslt_processor.entity.BasicXsltTemplate;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;

public class XsltPageTransformerTest {
	static String SIMPLE_XSLT_TEMPLATE = "/sample-template.xslt";
	static String COMPLEX_XSLT_TEMPLATE = "/complex-template.xslt";
	static String CONFLUENCE_PAGE_XML = "/sample-confluence-page-content.xml";

	XsltPageTransformer transformer = new XsltPageTransformer();

	@Test
	public void shouldTransformConfluencePage() throws Exception {
		String xsltTemplate = IOUtils.toString(getClass().getResourceAsStream(
				SIMPLE_XSLT_TEMPLATE));
		String xmlContent = IOUtils.toString(getClass().getResourceAsStream(
				CONFLUENCE_PAGE_XML));

		XsltTemplate template = new BasicXsltTemplate();
		template.setTemplate(xsltTemplate);
		String result = transformer.transform(xmlContent, template);
		assertFalse("result should not be empty", StringUtils.isEmpty(result));
		assertThat(result, containsString("<h1>TEST</h1>"));
	}

	@Test
	public void shouldTransformComplexTemplate() throws Exception {
		String xsltTemplate = IOUtils.toString(getClass().getResourceAsStream(
				COMPLEX_XSLT_TEMPLATE));
		String xmlContent = IOUtils.toString(getClass().getResourceAsStream(
				CONFLUENCE_PAGE_XML));

		XsltTemplate template = new BasicXsltTemplate();
		template.setTemplate(xsltTemplate);
		String result = transformer.transform(xmlContent, template);
		assertFalse("result should not be empty", StringUtils.isEmpty(result));
		assertThat(result, containsString("What is a space?"));
		assertThat(result, containsString("What is the Dashboard?"));
		assertThat(result, containsString("What is a wiki?"));
	}
}
