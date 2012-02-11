<!-- 
 *
 * NAME
 *      $RCSfile: xmlTojava.xsl,v $
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
 * This xsl stylesheet will create a java source file with stub methods for 
 * all the scalar and column elements that are to be found in this xml
 * file.
 * 
 * Note, this stylesheet is an example. It if definitely not fool-proof!
 * 
 * Change the variable 'packagename',
 * Change the variable 'classname',
-->

<!-- 
 * Sets the package name. This can be amended by the user.
-->
<xsl:variable name="packagename" select="'uk.co.westhawk.snmp.pdu'"/>

<!-- 
 * Sets the class name. This can be amended by the user.
-->
<xsl:variable name="classname" select="'StorageGetNextPdu'"/>


<!-- Gets all the scalars -->
<xsl:variable name="scalars" select="/*/*/scalar"/>
<!-- Gets all the columns -->
<xsl:variable name="columns" select="/*/*/*/*/column"/>


<xsl:output indent="no" method="text"/>
<xsl:strip-space elements="*"/>


<!--
 * The main template;
 *
 * - print the cvs header
 * - print the package
 * - print the import
 *
 * - print the class javadoc header
 * - print the class
 *
 * - print the static variables
 * - print the class variables
 *
 * - print the 2 constructors
 * - print the method addOids()
 * - print the method new_value()
 * - print the method isValid()
 *
 * - print the get/set method for each OID
 *
 * - print the method toString()
-->
<xsl:template match="/">

<xsl:call-template name="print_cvs_header"/>
package <xsl:value-of select="$packagename"/>;

import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;
import java.util.*;

<xsl:call-template name="print_class_javadoc"/>
public class <xsl:value-of select="$classname"/> extends GetNextPdu_vec
{
<xsl:call-template name="print_class_ident"/>

<xsl:variable name="no_scalars" select="count($scalars)"/>
<xsl:variable name="no_columns" select="count($columns)"/>

<xsl:apply-templates mode="static_oid" select="$scalars | $columns"/>
   
    public final static int NO_SCAL = <xsl:value-of select="$no_scalars"/>;
    public final static int NO_COL = <xsl:value-of select="$no_columns"/>;
    public final static int NO_OID = NO_SCAL + NO_COL;

    public final static String scal_oids[] = 
    {
<xsl:apply-templates mode="static_name" select="$scalars"/>
<xsl:text>    };</xsl:text>

    public final static String col_oids[] = 
    {
<xsl:apply-templates mode="static_name" select="$columns"/>
<xsl:text>    };</xsl:text>

    public final static String all_oids[] = 
    {
<xsl:apply-templates mode="static_name" select="$scalars | $columns"/>
<xsl:text>    };</xsl:text>

<xsl:text>

</xsl:text>

<xsl:apply-templates mode="variable" select="$scalars | $columns"/>
    private varbind[] varbinds;
    private boolean valid = false;

/**
 * Constructor.
 *
 * @param con The context of the request
 */
public <xsl:value-of select="$classname"/>(SnmpContextBasisFace con)
{
    super(con, NO_OID);
    varbinds = new varbind[NO_OID];
    for (int i=0; i&lt;NO_OID; i++)
    {
        varbinds[i] = null;
    } 
    valid = false;
}

/**
 * Constructor that will send the first request immediately.
 *
 * @param con The context of the request
 * @param o the Observer that will be notified when the answer is
 * received
 */
public <xsl:value-of select="$classname"/>(SnmpContextBasisFace con, Observer o)
throws PduException, java.io.IOException
{
    this(con);
    addOids(null);
    if (o != null)
    {
        addObserver(o);
    }
    send();
}

/**
 * The method addOids is the basic for the GetNext functionality.
 *
 * If old is null, it initialises the varbinds from all_oids.
 * If old is not null, it copies the column OIDs from the 
 * old <xsl:value-of select="$classname"/> object.
 * so the request continues where the previous one left.
 *
 * Note, the scalars and the columns OIDs are handles differently. The 
 * scalars are always copied from the original scal_oids, only the
 * column OIDs are copied from the old 
 * <xsl:value-of select="$classname"/> object.
 */
public void addOids(<xsl:value-of select="$classname"/> old)
{
    if (old != null)
    {
        for (int i=0; i&lt;NO_SCAL; i++)
        {
            varbinds[i] = new varbind(scal_oids[i]);
        }
        for (int i=NO_SCAL; i&lt;NO_OID; i++)
        {
            varbinds[i] = old.varbinds[i];
        }
    }
    else
    {
        for (int i=0; i&lt;NO_OID; i++)
        {
            varbinds[i] = new varbind(all_oids[i]);
        }
    }

    for (int i=0; i&lt;NO_OID; i++)
    {
        addOid(varbinds[i]);
    }
}

/**
 * The value of the request is set. This will be called by
 * Pdu.fillin().
 *
 * I check if the variables are still in range.
 * I do this because I'm only interessed in a part of the MIB. If I
 * would not do this check, I'll get the whole MIB from the starting
 * point, instead of the variables in the table.
 *
 * @param n the index of the value
 * @param a_var the value
 * @see Pdu#new_value 
 */
protected void new_value(int n, varbind res)
{
    varbinds[n] = res;

    if (getErrorStatus() == AsnObject.SNMP_ERR_NOERROR)
    {
        AsnObjectId oid = varbinds[n].getOid();
        AsnObject value = varbinds[n].getValue();

        if (varbinds[n].getOid().toString().startsWith(all_oids[n]))
        {
            try
            {
                switch (n)
                {

<xsl:apply-templates mode="case_call" select="$scalars | $columns"/>

<xsl:text>                    default:
                        valid = false;
</xsl:text>
                }
            }
            catch (ClassCastException exc)
            {
                valid = false;
            }

            if (n == NO_OID-1)
            {
                valid = true;
            }
        }
        else
        {
            setErrorStatus(AsnObject.SNMP_ERR_NOSUCHNAME);
            setErrorIndex(n);
        }
    }
}

/**
 * Returns if this set of values is valid.
 */
public boolean isValid()
{
    return valid;
}
<xsl:text>
</xsl:text>

<xsl:apply-templates mode="stub" select="$scalars | $columns"/>

public String toString()
{
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("[");
<xsl:apply-templates mode="to_string_variable" select="$scalars | $columns"/>
    buffer.append(", valid=").append(valid);
    buffer.append("]");
    return buffer.toString();
}

} 
</xsl:template>




<!--
 * Print the cvs header
-->
<xsl:template name="print_cvs_header">
<xsl:text>// NAME
//      $RCSfile: xmlTojava.xsl,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.1 $
// CREATED
//      $Date: 2002/10/29 14:19:34 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//</xsl:text>
</xsl:template>




<!--
 * Print the class javadoc 
-->
<xsl:template name="print_class_javadoc">
<xsl:text disable-output-escaping="no">
/**
 * The class </xsl:text><xsl:value-of select="$classname"/>.<xsl:text disable-output-escaping="no">
 *
 * This file is auto generated by XSLT, using libsmi and Xalan. 
 * See the &lt;snmp dir>/stubs/mib2java.sh directory.
 *
 * Make sure that this file is named </xsl:text>
<xsl:value-of select="$classname"/>
<xsl:text disable-output-escaping="no">.java 
 * and that it is moved to the directory where this package lives. If
 * these 2 things are not done, this class will not compile correctly!!
 *
 * @author &lt;a href="mailto:birgit@westhawk.co.uk">Birgit Arkesteijn&lt;/a>
 * @version $Revision: 1.1 $ $Date: 2002/10/29 14:19:34 $
 */</xsl:text>
</xsl:template>




<!--
 * Print the version_id
-->
<xsl:template name="print_class_ident">
<xsl:text>    private static final String version_id =
        "@(#)$Id: xmlTojava.xsl,v 1.1 2002/10/29 14:19:34 birgit Exp $ Copyright Westhawk Ltd";

</xsl:text>
</xsl:template>



<!--
 * Define the final static OID string;
 *     public final static String variable_OID = "a.b.c.d.e.f.g";
-->
<xsl:template match="scalar | column" mode="static_oid">
    <xsl:text>    public final static String </xsl:text>
    <xsl:value-of select="@name"/>
    <xsl:text>_OID = "</xsl:text>
    <xsl:value-of select="@oid"/>
    <xsl:text>";
</xsl:text>
</xsl:template>



<!--
 * Print the name of the static variable with a comma. This will be used in
 * an array;
 *     variable_OID,
-->
<xsl:template match="scalar | column" mode="static_name">
    <xsl:text>        </xsl:text><xsl:value-of select="@name"/>_OID,
</xsl:template>



<!--
 * Print the name of the variable to be using in toString() method;
 *    buffer.append(", variablename=").append(variablename);
-->
<xsl:template match="scalar | column" mode="to_string_variable">
    <xsl:text>    buffer.append("</xsl:text>
    <xsl:if test="position() != 1">
        <xsl:text>, </xsl:text>
    </xsl:if>
    <xsl:value-of select="@name"/>
    <xsl:text>=").append(</xsl:text>
    <xsl:value-of select="@name"/>
    <xsl:text>);
</xsl:text>
</xsl:template>



<!--
 * Define the private class variable name;
 *   private XXX variablename;
-->
<xsl:template match="scalar | column" mode="variable">
    <xsl:variable name="asnType">
        <xsl:call-template name="getAsnType">
            <xsl:with-param name="syntax" select="syntax"/>
        </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="javaType">
        <xsl:call-template name="getJavaType">
            <xsl:with-param name="asntype" select="$asnType"/>
        </xsl:call-template>
    </xsl:variable>

    <xsl:text>    private </xsl:text>
    <xsl:value-of select="$javaType"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="@name"/>; 
</xsl:template>



<!--
 * Print the get and set methods for this oid;
 * public void setVariable(AsnObject new_value)
 * {
 *       variablename = new_value;
 * }
 * public XXXX getVariable()
 * {
 *       return variablename;
 * }
-->
<xsl:template match="scalar | column" mode="stub">
    <xsl:variable name="name">
        <xsl:value-of select="@name"/>
    </xsl:variable>
    <xsl:variable name="method">
        <xsl:call-template name="firstToUpper">
            <xsl:with-param name="text" select="$name"/>
        </xsl:call-template>
    </xsl:variable>

    <xsl:variable name="asnType">
        <xsl:call-template name="getAsnType">
            <xsl:with-param name="syntax" select="syntax"/>
        </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="javaType">
        <xsl:call-template name="getJavaType">
            <xsl:with-param name="asntype" select="$asnType"/>
        </xsl:call-template>
    </xsl:variable>


<xsl:text>
/**
 * Sets the value of </xsl:text><xsl:value-of select="$name"/>.<xsl:text>
 *
 * </xsl:text>
<xsl:value-of select="description"/>
<xsl:text>
 */</xsl:text>
public void set<xsl:value-of select="$method"/>(AsnObject new_value)
{
    <xsl:value-of select="$asnType"/> obj = (<xsl:value-of select="$asnType"/>) new_value;
    <xsl:value-of select="$name"/> = obj.getValue();
}
<xsl:text>
/**
 * Returns the value of </xsl:text><xsl:value-of select="$name"/>.<xsl:text>
 *
 * </xsl:text>
<xsl:value-of select="description"/>
<xsl:text>
 */</xsl:text>
public <xsl:value-of select="$javaType"/> get<xsl:value-of select="$method"/>()
{
    return <xsl:value-of select="$name"/>;
}
</xsl:template>



<!--
 * Call the method in a case;
 * case XX:
 *     setVariable(value);
 *     break;
-->
<xsl:template match="scalar | column" mode="case_call">
    <xsl:variable name="name">
        <xsl:value-of select="@name"/>
    </xsl:variable>
    <xsl:variable name="method">
        <xsl:call-template name="firstToUpper">
            <xsl:with-param name="text" select="$name"/>
        </xsl:call-template>
    </xsl:variable>

    <xsl:variable name="asnType">
        <xsl:call-template name="getAsnType">
            <xsl:with-param name="syntax" select="syntax"/>
        </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="javaType">
        <xsl:call-template name="getJavaType">
            <xsl:with-param name="asntype" select="$asnType"/>
        </xsl:call-template>
    </xsl:variable>


    <!-- 
        Note, I tried to call last() here, using a xsl:if
        but somehow it screws up this template.
        So don't use it.
    -->
    <xsl:variable name="position" select="position()"/>
    <xsl:text>                    case </xsl:text>
    <xsl:value-of select="$position - 1"/>:
    <xsl:text>                        set</xsl:text>
    <xsl:value-of select="$method"/>(value);
    <xsl:text>                        break;
</xsl:text>
</xsl:template>



<!--
 * Returns the AsnObject subclass, based on the syntax node, .
 * I.e. AsnOctets, AsnObjectId, AsnInteger, AsnUnsInteger, AsnUnsInteger64.
 * 
 * If the type is not recognised, just print the type. The generated java
 * source file will not compile properly in this case.
-->
<xsl:template name="getAsnType">
<xsl:param name="syntax"/>

    <xsl:variable name="type">
        <xsl:apply-templates select="$syntax"/>
    </xsl:variable>

    <xsl:variable name="ltype">
        <xsl:call-template name="toLower">
            <xsl:with-param name="text" select="$type"/>
        </xsl:call-template>
    </xsl:variable>
    
    <xsl:choose>
        <xsl:when test="$ltype = 'octetstring' 
                        or $ltype = 'ipaddress'
                        or $ltype = 'opaque' 
                        or $ltype = 'displaystring' 
                        or $ltype = 'dateandtime'">
            <xsl:text>AsnOctets</xsl:text>
        </xsl:when>
        <xsl:when test="$ltype = 'autonomoustype'">
            <xsl:text>AsnObjectId</xsl:text>
        </xsl:when>
        <xsl:when test="$ltype = 'integer32'">
            <xsl:text>AsnInteger</xsl:text>
        </xsl:when>
        <xsl:when test="$ltype = 'counter32' 
                        or $ltype = 'gauge32' 
                        or $ltype = 'timeticks' 
                        or $ltype = 'unsigned32'">
            <xsl:text>AsnUnsInteger</xsl:text>
        </xsl:when>
        <xsl:when test="$ltype = 'counter64' ">
            <xsl:text>AsnUnsInteger64</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$type"/>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>




<!--
 * Returns the java type, base on the AsnObject subclass.
 * I.e. String, int, long.
 * 
 * If the type is not recognised, just print the type. The generated java
 * source file will not compile properly in this case.
-->
<xsl:template name="getJavaType">
<xsl:param name="asntype"/>
    <xsl:choose>
        <xsl:when test="$asntype = 'AsnOctets'
                        or $asntype = 'AsnObjectId'">
            <xsl:text>String</xsl:text>
        </xsl:when>
        <xsl:when test="$asntype = 'AsnInteger'">
            <xsl:text>int</xsl:text>
        </xsl:when>
        <xsl:when test="$asntype = 'AsnUnsInteger'">
            <xsl:text>long</xsl:text>
        </xsl:when>
        <xsl:when test="$asntype = 'AsnUnsInteger64'">
            <xsl:text>long</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$asntype"/>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>




<!--
 * Template that applies to a syntax node that has a type subnode. It
 * returns the type.
-->
<xsl:template match="syntax/type">
<xsl:value-of select="@name"/>
</xsl:template>

<!--
 * Template that applies to a syntax node that has a typedef subnode. It
 * returns the basetype.
-->
<xsl:template match="syntax/typedef">
<xsl:value-of select="@basetype"/>
</xsl:template>




<!--
 * Change the first characted of text into upper case;
 *   text -> Text
 *   Text -> Text
-->
<xsl:template name="firstToUpper">
<xsl:param name="text"/>

    <xsl:variable name="firstChar" select="substring($text, 1, 1)"/>
    <xsl:variable name="uFirstChar">
        <xsl:call-template name="toUpper">
            <xsl:with-param name="text" select="$firstChar"/>
        </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="tailName" select="substring($text, 2)"/>
    <xsl:variable name="method" select="concat($uFirstChar, $tailName)"/>
    <xsl:value-of select="$method"/>
</xsl:template>



<!--
 * Change the whole word to upper case;
 *   text -> TEXT
 *   Text -> TEXT
-->
<xsl:template name="toUpper">
<xsl:param name="text"/>
    <xsl:variable name="utext" select="translate($text,
                    'abcdefghijklmnopqrstuvwxyz',
    'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    <xsl:value-of select="$utext"/>
</xsl:template>




<!--
 * Change the whole word to upper case;
 *   text -> text
 *   Text -> text
-->
<xsl:template name="toLower">
<xsl:param name="text"/>
    <xsl:variable name="ltext" select="translate($text,
                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
    'abcdefghijklmnopqrstuvwxyz')"/>
    <xsl:value-of select="$ltext"/>
</xsl:template>


</xsl:stylesheet>

