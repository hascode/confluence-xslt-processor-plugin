package com.hascode.plugin.confluence.xslt_processor.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hascode.plugin.confluence.xslt_processor.entity.XsltTemplate;

public class BasicXsltTemplateProvider implements XsltTemplateProvider {
	private static final String KEY = "com.hascode.xsltprocessor";
	private static final Logger log = LoggerFactory
			.getLogger(BasicXsltTemplateProvider.class);
	private final BandanaManager bandanaManager;

	public BasicXsltTemplateProvider(final BandanaManager bandanaManager) {
		this.bandanaManager = bandanaManager;
	}

	@Override
	public void save(final XsltTemplate template) {
		log.info("saving given xslt template: {}", template);
		if (template == null || template.getId() == null
				|| StringUtils.isEmpty(template.getTitle())) {
			log.warn("template, template title or template id is null");
			throw new NullPointerException();
		}
		Map<String, XsltTemplate> templates = getTemplateMap();
		templates.put(template.getId(), template);
		bandanaManager.setValue(ConfluenceBandanaContext.GLOBAL_CONTEXT, KEY,
				templates);
	}

	private Map<String, XsltTemplate> getTemplateMap() {
		@SuppressWarnings("unchecked")
		Map<String, XsltTemplate> templates = (Map<String, XsltTemplate>) bandanaManager
				.getValue(ConfluenceBandanaContext.GLOBAL_CONTEXT, KEY);
		if (templates == null) {
			templates = new HashMap<String, XsltTemplate>();
		}
		return templates;
	}

	@Override
	public List<XsltTemplate> getAll() {
		return Lists.newArrayList(getTemplateMap().values());
	}

	@Override
	public void remove(final String id) {
		log.info("removing template for given id: {}", id);
		Map<String, XsltTemplate> templates = getTemplateMap();
		if (templates.containsKey(id)) {
			log.debug("template found for given id: {}", id);
			templates.remove(id);
			bandanaManager.setValue(ConfluenceBandanaContext.GLOBAL_CONTEXT,
					KEY, templates);
		}
	}

	@Override
	public Optional<XsltTemplate> getById(final String id) {
		log.info("fetching template for given id: {}", id);
		Map<String, XsltTemplate> templates = getTemplateMap();
		if (!templates.containsKey(id))
			return Optional.absent();
		return Optional.of(templates.get(id));
	}

	@Override
	// TODO:
	public Optional<XsltTemplate> getByTitle(final String title) {
		log.info("fetching template for given title: {}", title);
		Map<String, XsltTemplate> templates = getTemplateMap();
		Predicate<XsltTemplate> byTitle = new XsltTitlePredicate(title);
		Map<String, XsltTemplate> found = Maps.filterValues(templates, byTitle);
		return Optional.absent();
	}

}
