package com.hascode.plugin.confluence.xslt_processor.transformer;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;

public class XsltPageTransformer {
	private static final TransformerFactory factory = TransformerFactory
			.newInstance();
	private static final String DTD = "<!DOCTYPE doctypeName [<!ENTITY nbsp \"&#160;\">]>";
	private static final String WRAPPER = "<wrapper xmlns:ac=\"http://atlassian.com\" xmlns:ri=\"http://atlassian2.com\">%s</wrapper>";

	public String transform(final String pageContent,
			final XsltTemplate template) {
		final String content = String.format(WRAPPER, pageContent);
		Source xslt = new StreamSource(new StringReader(template.getTemplate()));
		Transformer transformer;
		try {
			transformer = factory.newTransformer(xslt);
			Source text = new StreamSource(new StringReader(content));
			StringWriter sw = new StringWriter();
			transformer.transform(text, new StreamResult(sw));
			return sw.toString();
		} catch (TransformerConfigurationException e) {
			return e.getMessage();
		} catch (TransformerException e) {
			return e.getMessageAndLocation();
		} catch (NullPointerException e) {
			return e.getMessage();
		}
	}
}
