/*
 * Copyright (c) 2000-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)usertypes.c	1.9
 */

/*
 * usertypes - demonstrates how to do custom message types
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrv.h"

/* field type */
#define TIBRVMSG_CONTACT (222)

#define NAME_SIZE        (16)

/* Custom data-type, a C struct */

/*
 * Note that tibrvContact.age is a tibrv_u8,
 * thus avoiding endian issues. When utilizing
 * binary data that has endian issues, the user
 * is responsible for dealing with those issues.
 */

typedef struct {
    char                first_name[NAME_SIZE];
    char                last_name[NAME_SIZE];
    tibrv_u8            age;

} tibrvContact;


/* Handlers (Encoder/Decoder/Converter) */

tibrv_status
_tibrvEncode_Contact(
    char**              buffer,
    tibrv_u32           mem_available,
    tibrvMsgField*      field)
{
    tibrvContact* contact = (tibrvContact *)field->data.buf;

    if (!contact)
        return TIBRV_INVALID_ARG;

    if (mem_available < tibrvMsgData_ByteSize(sizeof(tibrvContact)))
        return TIBRV_NO_MEMORY;

    tibrvMsgData_CopyBytes(buffer, contact, sizeof(tibrvContact));

    return TIBRV_OK;
}

tibrv_status
_tibrvDecode_Contact(
    char**      buffer,
    tibrvMsgField*  field,
    tibrvMsgDataType*   data_type)
{
    tibrvContact*   contact;
    const void*     contact_ptr;
    tibrv_u32       size;

    contact = (tibrvContact *)tibrvMsgData_Malloc(sizeof(tibrvContact));
    if (!contact)
    return TIBRV_NO_MEMORY;

    tibrvMsgData_GetBytes(buffer, &contact_ptr, &size);
    if (size != sizeof(tibrvContact))
    {
    free(contact);
    return TIBRV_INVALID_ARG;
    }

    memcpy(contact, contact_ptr, size);

    field->data.buf = (void *)contact;
    field->size     = sizeof(tibrvContact);
    field->count    = 1;

    *data_type = tibrvMsgData_MallocBlock;

    return TIBRV_OK;
}

/* Converter only supports string conversion */
tibrv_status
_tibrvConvert_Contact(
    tibrvMsgField*  field,
    tibrv_u8        new_type,
    tibrvMsgDataType*   data_type)
{
    char        str[128];
    tibrvContact*   contact = (tibrvContact *)field->data.buf;

    if (new_type != TIBRVMSG_STRING)
    return TIBRV_CONVERSION_FAILED;

    sprintf(str, "\"first='%s', last='%s', age=%d\"",
        contact->first_name, contact->last_name, contact->age);

    field->data.str = calloc(1,strlen(str)+1);
    if ( field->data.str == NULL )
    return TIBRV_NO_MEMORY;

    strcpy((char*)field->data.str,str);

    field->size = strlen(field->data.str)+1;
    field->type = new_type;

    *data_type = tibrvMsgData_MallocBlock;

    return TIBRV_OK;
}

/* Convenience functions that allow intergration into the TIB/RV API */

static tibrv_status
tibrvMsg_AddContact(
    tibrvMsg        msg,
    const char*     field_name,
    tibrvContact*   contact)
{
    tibrvMsgField f;

    if (!contact)
    return TIBRV_INVALID_ARG;

    f.name      = field_name;
    f.id    = 0;
    f.size      = sizeof(tibrvContact);
    f.count     = 1;
    f.type      = TIBRVMSG_CONTACT;
    f.data.buf  = (void *)contact;

    return tibrvMsg_AddField(msg, &f);
}

static tibrv_status
tibrvMsg_UpdateContact(
    tibrvMsg        msg,
    const char*     field_name,
    tibrvContact*   contact)
{
    tibrvMsgField f;

    if (!contact)
    return TIBRV_INVALID_ARG;

    f.name      = field_name;
    f.id    = 0;
    f.size      = sizeof(tibrvContact);
    f.count     = 1;
    f.type      = TIBRVMSG_CONTACT;
    f.data.buf  = (void *)contact;

    return tibrvMsg_UpdateField(msg, &f);
}

static tibrv_status
tibrvMsg_GetContact(
    tibrvMsg        msg,
    const char*     field_name,
    tibrvContact**  contact)
{
    tibrvMsgField   f;
    tibrv_status    s;

    s = tibrvMsg_GetField(msg, field_name, &f);
    if (s != TIBRV_OK)
    return s;

    if (f.type != TIBRVMSG_CONTACT)
    return TIBRV_CONVERSION_FAILED;

    *contact = (tibrvContact *)f.data.buf;

    return TIBRV_OK;
}

int
main(int argc, char* argv[])
{
    tibrvMsg        msg;
    tibrvContact        jane = {"Jane", "Doe", 22};
    tibrvContact        john = {"John", "Doe", 22};
    tibrvContact*   result;
    tibrv_status    status;
    const char*     msgString;

    status = tibrvMsg_SetHandlers(TIBRVMSG_CONTACT,
                  _tibrvEncode_Contact,
                  _tibrvDecode_Contact,
                  _tibrvConvert_Contact);

    if (status != TIBRV_OK)
    {
    fprintf(stderr, "%s: Unable to set custom encoder/decoder/conv: %s\n",
        argv[0], tibrvStatus_GetText(status));
    exit(-1);
    }

    status = tibrvMsg_Create(&msg);

    status |= tibrvMsg_AddContact(msg, "jane", &jane);
    status |= tibrvMsg_AddContact(msg, "john", &john);

    if (status != TIBRV_OK)
    {
    fprintf(stderr, "%s: Failed to create and populate message: %s\n",
        argv[0], tibrvStatus_GetText(status));
    }

    if (tibrvMsg_ConvertToString(msg, &msgString) == TIBRV_OK)
    fprintf(stderr, "%s: Message converted to string : '%s'\n",
        argv[0], msgString);

    status = tibrvMsg_GetContact(msg, "john", &result);
    if (status == TIBRV_OK)
    {
    result->age = 44;
    status = tibrvMsg_UpdateContact(msg, "john", result);

    if (tibrvMsg_ConvertToString(msg, &msgString) == TIBRV_OK)
        fprintf(stderr, "%s: Message converted to string : '%s'\n",
            argv[0], msgString);
    }

    tibrvMsg_Destroy(msg);

    return 0;
}
