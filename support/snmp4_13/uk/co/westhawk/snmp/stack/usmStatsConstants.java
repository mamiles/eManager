// NAME
//      $RCSfile: usmStatsConstants.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.3 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2001, 2002 by Westhawk Ltd
 * <a href="www.westhawk.co.uk">www.westhawk.co.uk</a>
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 */

package uk.co.westhawk.snmp.stack;

/**
 * This interface contains the OIDs for the usmStats variables.
 * These variables are returned with the SNMPv3 usm security model when
 * an error occurs.
 *
 * They are part of the 
 * <a href="http://ietf.org/rfc/rfc2574.txt">RFC 2574</a> mib.
 *
 * @see SnmpContextv3
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.3 $ $Date: 2002/10/10 15:13:57 $
 */
public interface usmStatsConstants  
{
    static final String     version_id =
        "@(#)$Id: usmStatsConstants.java,v 3.3 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * The total number of packets received by the SNMP engine which
     * were dropped because they requested a securityLevel that was
     * unknown to the SNMP engine or otherwise unavailable. 
     */
    public final static String usmStatsUnsupportedSecLevels = "1.3.6.1.6.3.15.1.1.1";

    /**
     * The total number of packets received by the SNMP engine which were
     * dropped because they appeared outside of the authoritative SNMP
     * engine's window.
     */
    public final static String usmStatsNotInTimeWindows = "1.3.6.1.6.3.15.1.1.2"; 

    /**
     * The total number of packets received by the SNMP engine which
     * were dropped because they referenced a user that was not known to
     * the SNMP engine.  
     */
    public final static String usmStatsUnknownUserNames = "1.3.6.1.6.3.15.1.1.3";

    /**
     * The total number of packets received by the SNMP engine which
     * were dropped because they referenced an snmpEngineID that was not
     * known to the SNMP engine. 
     */
    public final static String usmStatsUnknownEngineIDs = "1.3.6.1.6.3.15.1.1.4";

    /**
     * The total number of packets received by the SNMP engine which
     * were dropped because they didn't contain the expected digest
     * value. 
     */
    public final static String usmStatsWrongDigests     = "1.3.6.1.6.3.15.1.1.5";

    /**
     * The total number of packets received by the SNMP engine which
     * were dropped because they could not be decrypted. 
     */
    public final static String usmStatsDecryptionErrors = "1.3.6.1.6.3.15.1.1.6";

    /**
     * The array with all the usmStats dotted OIDs in it.
     */
    public final static String [] usmStatsOids=
    {
        usmStatsUnsupportedSecLevels,
        usmStatsNotInTimeWindows,
        usmStatsUnknownUserNames,
        usmStatsUnknownEngineIDs,
        usmStatsWrongDigests,
        usmStatsDecryptionErrors
    };

    /**
     * The array with all the usmStats verbal OIDs in it.
     */
    public final static String [] usmStatsStrings=
    {
        "usmStatsUnsupportedSecLevels",
        "usmStatsNotInTimeWindows",
        "usmStatsUnknownUserNames",
        "usmStatsUnknownEngineIDs",
        "usmStatsWrongDigests",
        "usmStatsDecryptionErrors"
    };

}

