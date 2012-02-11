
/*
 * Copyright (c) 2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrvmultisend.c	1.9
 */


/*
 * tibrvmultisend - sample Rendezvous multi-field message publisher
 *
 * This program publishes a message with one or more fields on a
 * specified subject.  Both the subject and the fields must be
 * supplied as command parameters.  Fields with embedded spaces
 * should be quoted.  Fields are specified by the syntax
 *          [[name][,type]=]data
 * Name may not be a quoted string and may not contain comma or
 * equal sign characters.  If the field data contains either comma
 * or equal sign, an equal sign must preceed the data field. Type
 * is a string which represents one of the scalar rv datatypes
 * (string, bool, i8, u8, i16, u16, i32, u32, i64, u64, f32, f64,
 * opaque, xml).  If no name is specified, the field will be named
 * "DATA".  If no type is given or the type is not recognized,
 * the field will be added as a string.  This program does not
 * process datetime, ip address, or array field types.
 *
 * Optionally the user may specify communication parameters for
 * tibrvTransport_Create.  If none are specified, default values
 * are used.  For information on default values for these parameters,
 * please see the TIBCO/Rendezvous Concepts manual.
 *
 * Normally a listener such as tibrvlisten should be started first.
 *
 * Examples:
 *
 *  Publish a message on subject a.b.c and default parameters:
 *   tibrvmultisend a.b.c "This is my first field" "This is my second field"
 *
 *  Publish a message on subject a.b.c using port 7566:
 *   tibrvmultisend -service 7566 a.b.c field1,i16=12345 field2,f64=12345.6789
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tibrv/tibrv.h"

#define MIN_PARMS       (1)             /* Must have at least a subject */
#define FIELD_NAME      "DATA"

void
usage(void)
{
    fprintf(stderr,"Usage: tibrvmultisend [-service service]\n");
    fprintf(stderr,"                      [-network network]\n");
    fprintf(stderr,"                      [-daemon daemon]\n");
    fprintf(stderr,"                      <subject>\n");
    fprintf(stderr,"                      <[[name][,type]=data] ....>\n");
    fprintf(stderr," TIB/Rendezvous types datatypes accepted by tibrvmultisend include:\n");
    fprintf(stderr,"         string, opaque, xml, bool, \n");
    fprintf(stderr,"         i8, i16, i32, i64,            (signed integer)\n");
    fprintf(stderr,"         u8, u16, u32, u64,            (unsigned integer)\n");
    fprintf(stderr,"         f32, f64                      (floating point )\n");
    exit(1);
}

/*********************************************************************/
/* get_InitParms:  Get from the command line the parameters that can */
/*                 be passed to tibrvTransport_Create().             */
/*                                                                   */
/*                 returns the index for where any additional        */
/*                 parameters can be found.                          */
/*********************************************************************/
int
get_InitParms(
    int         argc,
    char*       argv[],
    int         min_parms,
    char**      serviceStr,
    char**      networkStr,
    char**      daemonStr)
{
    int i=1;

    if ( argc-1 < min_parms )
        usage();

    if (strcmp(argv[i], "-help") == 0 || strcmp(argv[i], "-h") == 0 ||
        strcmp(argv[i], "?") == 0)
        usage();

    while ( i+2 <= argc && *argv[i] == '-' )
    {
        if (strcmp(argv[i], "-service") == 0)
        {
            *serviceStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-network") == 0)
        {
            *networkStr = argv[i+1];
            i+=2;
        }
        else if (strcmp(argv[i], "-daemon") == 0)
        {
            *daemonStr = argv[i+1];
            i+=2;
        }
        else
        {
            usage();
        }
    }

    return( i );
}

int
main(int argc, char **argv)
{
    tibrv_status        err;
    tibrvTransport      transport;
    tibrvMsg            message;

    int                 currentArg;
    int                 subjectLocation;

    char*               serviceStr = NULL;
    char*               networkStr = NULL;
    char*               daemonStr  = NULL;

    char*               progname = argv[0];

    char*               comma;
    char*               equal;

    char                fieldname[255];
    char                fieldtype[64];
    char                fielddata[512];
    char                fldstring[512];
    char*               stopstring;
    const char*         msgstring;

    char*               begin = NULL;
    char*               end = NULL;

    int                 len;
    tibrv_bool          overflow;
    tibrv_bool          precision;

    /*
     * Parse arguments for possible optional parameter pairs.
     * These must precede the subject and message strings.
     */
    currentArg = get_InitParms(argc, argv, MIN_PARMS, &serviceStr,
                               &networkStr, &daemonStr );

    /*
     * Create internal TIB/Rendezvous machinery
     */
    err = tibrv_Open();
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to open TIB/RV --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Initialize the transport with the given parameters or default NULLs.
     */

    err = tibrvTransport_Create(&transport, serviceStr,
                                networkStr, daemonStr);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to initialize transport --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }

    tibrvTransport_SetDescription(transport, progname);

    /*
     * Create message
     */
    err = tibrvMsg_Create(&message);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to create message --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }


    /*
     * Step through command line, getting first the subject,
     * then adding each field to the message.
     */

    subjectLocation = currentArg++;

    /* Set the subject name */
    err = tibrvMsg_SetSendSubject(message, argv[subjectLocation]);

    if ( err != TIBRV_OK)
    {
        fprintf(stderr, "%s: %s in setting send subject to \"%s\"\n",
                progname, tibrvStatus_GetText(err),
                argv[subjectLocation]);
        exit(1);
    }


    if (currentArg < argc) printf("Add fields to message:\n");

    while(currentArg < argc)
    {
        /* Set the defaults */
        strcpy(fieldname,FIELD_NAME);
        strcpy(fieldtype,"string");

        /* Locate the punctuation */
        equal = strchr(argv[currentArg],(int)'=');
        comma = strchr(argv[currentArg],(int)',');

        begin = argv[currentArg];

        /*
         * If we don't have an equal sign, it's all field data.  If we,
         * do copy the field name and/or field type.
         */
        if (equal)
        {
            end = equal;
            if (comma != NULL) end = comma;
            len = end - begin;
            if (len > 0)
            {
                memcpy(fieldname, begin, len);
                fieldname[len] = '\0';
            }
            if (comma != NULL)
            {
                begin = comma + 1;
                len = equal - begin;
                memcpy(fieldtype, begin, len);
                fieldtype[len] = '\0';
            }
            begin = equal + 1;
        }

        /*
         * Copy the data for the field.
         */
        end = argv[currentArg] + strlen(argv[currentArg]);
        len = end - begin;
        memcpy(fielddata, begin, len);
        fielddata[len] = '\0';

        stopstring = "";
        overflow = TIBRV_FALSE;
        precision = TIBRV_FALSE;

        /*
         * Check the data type.  Convert the data to an appropriate C data
         * type, check for overflow, and add to the message.  To process
         * array datatypes, the program must be modified to parse the input
         * string and assign individual values as elements of an array of
         * suitable type.  For hints on processing datetime values, see
         * "Add DateTime" in Chapter 3 Messages in the TIB/Rendezvous C
         * Reference manual.
         */
        if (strcmp(fieldtype,"BOOL") == 0 ||
            strcmp(fieldtype,"bool") == 0)
        {
            /* Boolean is true if it starts with Y or T, false otherwise */
            tibrv_bool data = (0 == strcspn(fielddata,"YyTt"));
            if (data == TIBRV_TRUE) sprintf(fldstring,"TRUE");
            else sprintf(fldstring,"FALSE");
            err = tibrvMsg_AddBool(message, fieldname, data);
        }
        else if(strcmp(fieldtype,"U8") == 0 ||
                strcmp(fieldtype,"u8") == 0)
        {
            tibrv_u64 data = strtoul(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", (tibrv_u8) data);
            overflow = (data != (tibrv_u8) data);
            err = tibrvMsg_AddU8(message, fieldname, (tibrv_u8) data);
        }
        else if (strcmp(fieldtype,"I8") == 0 ||
                 strcmp(fieldtype,"i8") == 0)
        {
            tibrv_i64 data = strtol(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", (tibrv_i8) data);
            overflow = (data != (tibrv_i8) data);
            err = tibrvMsg_AddI8(message, fieldname,
                                 (tibrv_i8) data);
        }
        else if (strcmp(fieldtype,"U16") == 0 ||
                 strcmp(fieldtype,"u16") == 0)
        {
            tibrv_u64 data = strtol(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", (tibrv_u16) data);
            overflow = (data != (tibrv_u16) data);
            err = tibrvMsg_AddU16(message, fieldname,
                                  (tibrv_u16) data);
        }
        else if (strcmp(fieldtype,"I16") == 0 ||
                 strcmp(fieldtype,"i16") == 0)
        {
            tibrv_i64 data = strtol(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", (tibrv_i16) data);
            overflow = (data != (tibrv_i16) data);
            err = tibrvMsg_AddI16(message, fieldname,
                                  (tibrv_i16) data);
        }
        else if (strcmp(fieldtype,"U32") == 0 ||
                 strcmp(fieldtype,"u32") == 0)
        {
            tibrv_u64 data = strtoul(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", (tibrv_u32) data);
            overflow = (data != (tibrv_u32) data);
            err = tibrvMsg_AddU32(message, fieldname,
                                  (tibrv_u32) data);
        }
        else if (strcmp(fieldtype,"I32") == 0 ||
                 strcmp(fieldtype,"i32") == 0)
        {
            tibrv_i64 data = strtol(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", (tibrv_i32) data);
            overflow = (data != (tibrv_i32) data);
            err = tibrvMsg_AddI32(message, fieldname,
                                  (tibrv_i32) data);
        }
        else if (strcmp(fieldtype,"U64") == 0 ||
                 strcmp(fieldtype,"u64") == 0)
        {
            tibrv_u32 data = strtoul(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", data);
            err = tibrvMsg_AddU64(message, fieldname, (tibrv_u64) data);
        }
        else if (strcmp(fieldtype,"I64") == 0 ||
                 strcmp(fieldtype,"i64") == 0)
        {
            tibrv_i32 data = strtol(fielddata,&stopstring,10);
            sprintf(fldstring,"%d", data);
            err = tibrvMsg_AddI64(message, fieldname, (tibrv_i64) data);
        }
        else if (strcmp(fieldtype,"F32") == 0 ||
                 strcmp(fieldtype,"f32") == 0)
        {
            tibrv_f64 data64 = strtod(fielddata, &stopstring);
            tibrv_f32 data32 = (tibrv_f32) strtod(fielddata,
                                                  &stopstring);
            sprintf(fldstring, "%e", data32);
            precision = (data64 != data32);
            err = tibrvMsg_AddF32(message, fieldname, data32);
        }
        else if (strcmp(fieldtype,"F64") == 0 ||
                 strcmp(fieldtype,"f64") == 0)
        {
            tibrv_f64 data = strtod(fielddata,&stopstring);
            sprintf(fldstring,"%e", data);
            err = tibrvMsg_AddF64(message, fieldname, data);
        }
        else if (strcmp(fieldtype,"STRING") == 0 ||
                 strcmp(fieldtype,"string") == 0)
        {
            sprintf(fldstring, "%s", fielddata);
            err = tibrvMsg_AddString(message, fieldname,
                                     fielddata);
        }
        else if (strcmp(fieldtype,"XML") == 0 ||
                 strcmp(fieldtype,"xml") == 0)
        {
            sprintf(fldstring, "%s", fielddata);
            err = tibrvMsg_AddXml(message, fieldname, fielddata,
                                  strlen(fielddata));
        }
        else if (strcmp(fieldtype,"OPAQUE") == 0 ||
                 strcmp(fieldtype,"opaque") == 0)
        {
            sprintf(fldstring, "%s", fielddata);
            err = tibrvMsg_AddOpaque(message, fieldname,
                                     fielddata, strlen(fielddata));
        }
        else
        {
            /* Default to string if type is not recognized */
            sprintf(fldstring, "%s", fielddata);
            strcpy(fieldtype,"(unknown, default to string)");
            err = tibrvMsg_AddString(message, fieldname, fielddata);
        }


        /*
         * If the field add succeeded, display the name, type, and data.
         * If it failed, display an error message.
         */
        if (err == TIBRV_OK)
        {
            printf("    %s   \ttype: %s  \tdata: %s",
                   fieldname, fieldtype, fldstring);
            if (overflow)  printf ("    (overflow)");
            if (precision) printf ("    (precision/rounding)");
            if (stopstring != "" &&
                stopstring < fielddata + strlen(fielddata))
                printf ("    (invalid characters: %s)",
                        stopstring);
            printf("\n");
        } else {
            fprintf(stderr,
                    "%s: Failed to add field --%s\n\tfield %s   type %s   data %s\n'",
                    progname, tibrvStatus_GetText(err), fieldname,
                    fieldtype, fldstring);
        }

        currentArg++;
    }

    /*
     * Conert the message to string and display.  Publish the message.
     */
    err = tibrvMsg_ConvertToString(message, &msgstring);

    printf("Publishing: subject=%s \"%s\"\n",
           argv[subjectLocation], msgstring);

    err = tibrvTransport_Send(transport, message);
    if (err != TIBRV_OK)
    {
        fprintf(stderr, "%s: Failed to send message --%s\n",
                progname, tibrvStatus_GetText(err));
        exit(1);
    }

    /*
     * Destroy message
     */
    tibrvMsg_Destroy(message);

    /*
     * tibrv_Close() will destroy the transport and guarantee delivery.
     */

    tibrv_Close();

    exit(0);
}
