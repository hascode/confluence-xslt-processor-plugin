package com.hascode.plugin.confluence.xslt_processor.entity;

import java.util.UUID;

public class BasicXsltTemplate implements XsltTemplate,
		Comparable<XsltTemplate> {
	private String id;
	private String title;
	private String template;

	public BasicXsltTemplate() {
		ensureId();
	}

	private void ensureId() {
		if (getId() == null) {
			this.id = UUID.randomUUID().toString();
		}
	}

	@Override
	public final String getId() {
		return id;
	}

	@Override
	public final void setId(final String id) {
		this.id = id;
	}

	@Override
	public final String getTitle() {
		return title;
	}

	@Override
	public final void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public final String getTemplate() {
		return template;
	}

	@Override
	public final void setTemplate(final String template) {
		this.template = template;
	}

	@Override
	public int compareTo(final XsltTemplate o) {
		return getTitle().compareTo(o.getTitle());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicXsltTemplate other = (BasicXsltTemplate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BasicXsltTemplate [id=" + id + ", title=" + title
				+ ", template=" + template + "]";
	}

}
