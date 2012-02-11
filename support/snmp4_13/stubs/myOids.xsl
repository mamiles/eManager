<!-- 
 *
 * NAME
 *      $RCSfile: myOids.xsl,v $
 * DESCRIPTION
 *      [given below]
 * DELTA
 *      $Revision: 1.1 $
 *      $Author: birgit $
 * CREATED
 *      $Date: 2002/10/29 14:19:34 $
 * COPYRIGHT
 *      Westhawk Ltd
 * TO DO
 *
 *
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<!--
 * This xsl stylesheet will process a xml file that is generated 
 * with smidump. It might not work if smidump generated any errors.
 * 
 * This stylesheet will generate another xml file, that only contains
 * information about the OIDs I'm interested in.
 * The OIDs I'm interested in are defined in the template
 * 'doYouWantThisOid'.
 * 
 * Note, this stylesheet is an example. It if definitely not fool-proof!
 * 
 * Amend the template 'doYouWantThisOid' to your own liking.
-->

<xsl:output indent="yes" method="xml"/>


<!--
 * This template lists all the OIDs that I'm interested in.
 * 
 * It is assumed that each OID is a scalar or a column.
-->
<xsl:template name="doYouWantThisOid">
<xsl:param name="oid"/>
    <xsl:choose>
      <xsl:when test="$oid = 'hrStorageIndex'">1</xsl:when>
      <xsl:when test="$oid = 'hrStorageType'">1</xsl:when>
      <xsl:when test="$oid = 'hrStorageDescr'">1</xsl:when>
      <xsl:when test="$oid = 'hrStorageAllocationUnits'">1</xsl:when>
      <xsl:when test="$oid = 'hrStorageSize'">1</xsl:when>
      <xsl:when test="$oid = 'hrStorageUsed'">1</xsl:when>
      <xsl:when test="$oid = 'hrStorageAllocationFailures'">1</xsl:when>

      <xsl:when test="$oid = 'hrSystemUptime'">1</xsl:when>
      <xsl:otherwise>0</xsl:otherwise>
    </xsl:choose>
</xsl:template>



<!--
 * Copies an element with its attribute recursively.
-->
<xsl:template match="node() | @*">
    <xsl:copy>
        <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
</xsl:template>



<!--
 * Don't copy the attributes of smi.
-->
<xsl:template match="smi" >
        <xsl:copy>
            <xsl:apply-templates select="node()" />
        </xsl:copy>
</xsl:template>



<!--
 * Don't copy the nodes: module, imports, node, groups, compliances.
-->
<xsl:template match="module | imports | node | groups | compliances" />



<!--
 * Only copy the scalars or column I'm interested in.
-->
<xsl:template match="scalar | column" >
    <xsl:variable name="localname" select="local-name()"/>
    <xsl:variable name="wantIt">
        <xsl:call-template name="doYouWantThisOid">
            <xsl:with-param name="oid" select="@name"/>
        </xsl:call-template>
    </xsl:variable>
    <xsl:choose>
        <xsl:when test="$wantIt = 1">
             <xsl:copy>
                <xsl:apply-templates select="@* | node()" />
            </xsl:copy>
        </xsl:when>
        <xsl:otherwise/>
    </xsl:choose>
</xsl:template>

</xsl:stylesheet>

