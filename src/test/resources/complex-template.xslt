<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ac="http://atlassian.com" xmlns:ri="http://atlassian2.com" exclude-result-prefixes="ac ri">
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/">
		<xsl:for-each select="//ac:rich-text-body//strong">
			<xsl:value-of select="."></xsl:value-of>
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>