package com.hascode.plugin.confluence.xslt_processor.repository;

import com.google.common.base.Predicate;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;

public class XsltTitlePredicate implements Predicate<XsltTemplate> {
	private final String referenceTitle;

	public XsltTitlePredicate(final String referenceTitle) {
		this.referenceTitle = referenceTitle;
	}

	@Override
	public boolean apply(final XsltTemplate template) {
		return referenceTitle.equals(template.getTitle());
	}

}
