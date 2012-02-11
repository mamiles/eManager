/*
 * Copyright (c) 2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)usertypes.cpp	1.3
 */
/*
 * usertypes - demonstrates how to use custom data types
 *             in TIB/Rendezvous message using C++.
 *
 * Although there is no C++ interface to the custom field data
 * engine, this examples shows how to approach custom types in C++
 * by combining the C-level TIB/Rendezvous functions and C++ helper
 * class to make it convenient to add and retrieve objects of
 * custom classes. The implementor of custom encoders will have to
 * learn some of TIB/Rendezvous C-level API but all users will only
 * have to learn the convenience class in C++.
 * 
 * This example introduces two custom classes Contact and Address and
 * demonstrates how to create custom encoders and decoders for those
 * classes. It also demonstrates how to create another class which 
 * hides implementational details from the user and introduces methods
 * such as:
 *
 * TibrvStatus MyTibrvMsg::addContact(TibrvMsg& msg, Contact& contact);
 * TibrvStatus MyTibrvMsg::getContact(TibrvMsg& msg, Contact& contact);
 *
 * The C-level example "usertypes.c" demonstrates the technique when the
 * user program allocates blocks of memory which are then managed by the
 * TIB/Rendezvous message engine. Such approach does not work in C++ if the
 * objects retrieved from TibrvMsg are C++ objects which must be deleted
 * with the C++ "delete" operator.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <time.h>

#include "tibrv/tibrvcpp.h"

/*
 * Maximum name length
 */
#define NAME_SIZE  24

/*-------------------------------------------------------------------- 
 * Custom field types
 *------------------------------------------------------------------*/

#define TIBRVMSG_CONTACT (TIBRVMSG_USER_FIRST+10)
#define TIBRVMSG_ADDRESS (TIBRVMSG_USER_FIRST+11)

/*-------------------------------------------------------------------- 
 * Contact class keeps first name of a person, last name and age.
 *------------------------------------------------------------------*/

class Contact 
{
public:

    Contact();
    Contact(const char* init_first_name, 
            const char* init_last_name,
            tibrv_u8    init_age);
    
    char                first_name[NAME_SIZE];
    char                last_name[NAME_SIZE];
    tibrv_u8            age;
};

Contact::Contact() 
{
    strcpy(first_name,"");
    strcpy(last_name,"");
    age = 0;
}

Contact::Contact(const char* init_first_name,
                 const char* init_last_name,
                 tibrv_u8    init_age)
{
    // assume length is O'k
    strcpy(first_name,init_first_name);
    strcpy(last_name,init_last_name);
    age = init_age;
}

/*-------------------------------------------------------------------- 
 * Another class is the Address which unlike Contact keeps
 * pointers to strings.
 *------------------------------------------------------------------*/

class Address
{
public:

     Address();
     Address(const char* init_addr_line, 
             const char* init_city, 
             const char* init_state, 
             tibrv_u32 init_zip);
    ~Address();

    void  setAddrLine(const char* addrLine);
    
    char*               addr_line;  // address line
    char                city[24];   // city name
    char                state[3];   // 2-letter abbreviation of state
    tibrv_u32           zip_code;   // zip code
};

Address::Address() 
{
    addr_line = NULL;
    strcpy(city,"");
    strcpy(state,"");
    zip_code = 0;
}

 Address::Address(const char* init_addr_line, 
         const char* init_city, 
         const char* init_state, 
         tibrv_u32 init_zip)
{
     addr_line = NULL;
     setAddrLine(init_addr_line);
     
     // assume length of parameters are O'k
     strcpy(city,init_city);
     strcpy(state,init_state);
     
     zip_code = init_zip;
}

Address::~Address() 
{
    if (addr_line != NULL)
        free(addr_line);
}

// We do not check for out-of-memory conditions
// in this example
void Address::setAddrLine(const char* line)
{
    if (addr_line != NULL)
        free(addr_line);

    addr_line = NULL;

    if (line != NULL) 
    {
        addr_line = (char*)malloc(strlen(line)+1);
        strcpy(addr_line,line);
    }
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg class has static methods to add and retrieve
 * our custom classes to/from TibrvMsg object.
 * We didn't subclass TibrvMsg because in the message callback we 
 * receive TibrvMsg object and have to be able to retrieve Contact
 * objects. A class with static methods is a good solution.
 *------------------------------------------------------------------*/

class MyTibrvMsg
{
public:
    
    static TibrvStatus addContact   (TibrvMsg& msg, 
                                     const char* fieldName, Contact& contact);
    static TibrvStatus getContact   (TibrvMsg& msg, 
                                     const char* fieldName, Contact& contact);
    static TibrvStatus updateContact(TibrvMsg& msg, 
                                     const char* fieldName, Contact& contact);

    static TibrvStatus addAddress   (TibrvMsg& msg, 
                                     const char* fieldName, Address& address);
    static TibrvStatus getAddress   (TibrvMsg& msg, 
                                     const char* fieldName, Address& address);
    static TibrvStatus updateAddress(TibrvMsg& msg, 
                                     const char* fieldName, Address& address);

};

/*-------------------------------------------------------------------- 
 * Encoder of Contact class.
 *------------------------------------------------------------------*/

tibrv_status
Contact_Encoder(
    char**              buffer,
    tibrv_u32           mem_available,
    tibrvMsgField*      field)
{
    // We are given a pointer to the object
    // in the field.data.buf field...
    Contact* contact = (Contact *)field->data.buf;

    // Check the pointer is not NULL
    if (!contact)
        return TIBRV_INVALID_ARG;

    // Check we have enough bytes in the buffer.
    // If the buffer is too small we return an error and
    // TIB/RV will reallocate the buffer and call us again.
    if (sizeof(Contact) > mem_available)
        return TIBRV_NO_MEMORY;
    
    // Since Contact is a simple class we can use straight binary copy.
    // Notice CopyBytes sets both field size and field data
    // in the message buffer and advances the buffer pointer. 
    // So, all we have to do is to call it.
    tibrvMsgData_CopyBytes(buffer, contact, sizeof(Contact));

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * Decoder of Contact class 
 *------------------------------------------------------------------*/

tibrv_status
Contact_Decoder(
    char**              buffer,
    tibrvMsgField*      field,
    tibrvMsgDataType*   data_type)
{
    // Get the size of the data and it also moves
    // the buffer pointer over the field size bytes
    tibrvMsgData_GetSize(buffer, &field->size);

    // Store current buffer pointer.
    // Pay attention that we are given pointer to a pointer...
    field->data.buf = (void*)(*buffer);
    
    // We must advance the buffer pointer
    // to step over the field data
    (*buffer) += field->size;

    // This tells TIB/Rendezvous message engine
    // that we didn't allocate anything
    *data_type = tibrvMsgData_Primitive;

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * Contact Converter, only supports convertion to a string
 *------------------------------------------------------------------*/

tibrv_status
Contact_Converter(
    tibrvMsgField*      field,
    tibrv_u8            new_type,
    tibrvMsgDataType*   data_type)
{
    char     str[128];
    Contact* contact = (Contact*)field->data.buf;

    if (new_type != TIBRVMSG_STRING)
        return TIBRV_CONVERSION_FAILED;

    sprintf(str, "\"first='%s', last='%s', age=%d\"", 
            contact->first_name, contact->last_name, contact->age);

    if (!(field->data.str = strdup(str)))
        return TIBRV_NO_MEMORY;
    
    field->size = strlen(field->data.str)+1;
    field->type = new_type;

    *data_type = tibrvMsgData_MallocBlock;

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * Encoder of Address class.
 *------------------------------------------------------------------*/

tibrv_status
Address_Encoder(
    char**              buffer,
    tibrv_u32           mem_available,
    tibrvMsgField*      field)
{
    // Check the pointer is not NULL
    if (!field->data.buf)
        return TIBRV_INVALID_ARG;

    // Check we have enough space in the buffer
    if (field->size > mem_available)
        return TIBRV_NO_MEMORY;

    // Since we have already converted the object
    // into bytes in the convenience method, all we have to
    // do here is copy the bytes
    tibrvMsgData_CopyBytes(buffer, field->data.buf, field->size);

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * Decoder of Address class. Notice it is same as
 * Contact decoder so we could actually use the same decoder for both
 * classes. Actual extraction of the data from the message buffer
 * into the object is done by the C++ method. 
 *------------------------------------------------------------------*/

tibrv_status
Address_Decoder(
    char**              buffer,
    tibrvMsgField*      field,
    tibrvMsgDataType*   data_type)
{
    // Get the size of the data and it also moves
    // the buffer pointer over the field size bytes
    tibrvMsgData_GetSize(buffer, &field->size);

    // Store current buffer pointer.
    // Pay attention that we are given pointer to a pointer...
    field->data.buf = (void*)(*buffer);
    
    // We must advance the buffer pointer
    // to step over the field data
    (*buffer) += field->size;

    // This tells TIB/Rendezvous message engine
    // that we didn't allocate anything
    *data_type = tibrvMsgData_Primitive;

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * Address Converter, only supports convertion to a string
 *------------------------------------------------------------------*/

tibrv_status
Address_Converter(
    tibrvMsgField*      field,
    tibrv_u8            new_type,
    tibrvMsgDataType*   data_type)
{
    if (new_type != TIBRVMSG_STRING)
        return TIBRV_CONVERSION_FAILED;

    // Convert buffer into an object
    // and then print it.

    Address address;
    const char* str;
    TibrvMsg addrmsg((char*)field->data.buf);

    addrmsg.getString("addr",str);
    address.setAddrLine(str);
    
    addrmsg.getString("city",str);
    strcpy(address.city,str);
    
    addrmsg.getString("state",str);
    strcpy(address.state,str);
    
    addrmsg.getU32("zip",address.zip_code);

    char fieldString[256];

    sprintf(fieldString, "\"address='%s', city='%s', state='%s', zip_code=%05d\"", 
            address.addr_line,address.city,address.state,address.zip_code);

    if (!(field->data.str = strdup(fieldString)))
        return TIBRV_NO_MEMORY;
    
    field->size = strlen(field->data.str)+1;
    field->type = new_type;

    // Tell the TIB/Rendezvous message engine that
    // we have allocated a blcok of memory.
    // The message engine will maintain the allocated
    // block and free it when message is detroyed or
    // ClearReferences() function is called.
    *data_type = tibrvMsgData_MallocBlock;

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg::addContact 
 *------------------------------------------------------------------*/

TibrvStatus
MyTibrvMsg::addContact(TibrvMsg& msg, const char* field_name, Contact& contact)
{
    TibrvMsgField field;
    
    field.name      = field_name;
    field.id        = 0;
    field.size      = sizeof(Contact);
    field.count     = 1;
    field.type      = TIBRVMSG_CONTACT;
    field.data.buf  = (void *)&contact;

    return msg.addField(field);    
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg::updateContact 
 *------------------------------------------------------------------*/

TibrvStatus
MyTibrvMsg::updateContact(TibrvMsg& msg, const char* field_name, Contact& contact)
{
    TibrvMsgField field;
    
    field.name      = field_name;
    field.id        = 0;
    field.size      = sizeof(Contact);
    field.count     = 1;
    field.type      = TIBRVMSG_CONTACT;
    field.data.buf  = (void *)&contact;

    return msg.updateField(field);    
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg::getContact 
 *------------------------------------------------------------------*/

TibrvStatus
MyTibrvMsg::getContact(TibrvMsg& msg, const char* field_name, Contact& contact)
{
    TibrvMsgField field;
    TibrvStatus   status;

    status = msg.getField(field_name,field);
    if (status != TIBRV_OK)
        return status;

    if (field.size != sizeof(Contact))
        return TIBRV_INVALID_ARG;

    memcpy(&contact,field.data.buf,field.size);

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg::addAddress
 *
 * Because Address object keeps a pointer to the string and also
 * it has tibrv_u32 data member, which must be taken care of because
 * of the byte-order issue, we use TibrvMsg to convert the object
 * into byte array in a system-independent manner.
 *------------------------------------------------------------------*/

TibrvStatus
MyTibrvMsg::addAddress(TibrvMsg& msg, const char* field_name, Address& address)
{
    TibrvMsg addrmsg;
    addrmsg.addString ( "addr" , address.addr_line );
    addrmsg.addString ( "city" , address.city      );
    addrmsg.addString ( "state", address.state     );
    addrmsg.addU32    ( "zip"  , address.zip_code  );
    
    const void* bytes;
    tibrv_u32 size;

    addrmsg.getByteSize(size);
    addrmsg.getAsBytes(bytes);

    TibrvMsgField field;
    
    field.name      = field_name;
    field.id        = 0;
    field.count     = 1;
    field.size      = size;
    field.type      = TIBRVMSG_ADDRESS;
    field.data.buf  = (void*)bytes;

    return msg.addField(field);    
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg::updateAddress
 *
 *------------------------------------------------------------------*/

TibrvStatus
MyTibrvMsg::updateAddress(TibrvMsg& msg, const char* field_name, Address& address)
{
    TibrvMsg addrmsg;
    addrmsg.addString ( "addr" , address.addr_line );
    addrmsg.addString ( "city" , address.city      );
    addrmsg.addString ( "state", address.state     );
    addrmsg.addU32    ( "zip"  , address.zip_code  );
    
    const void* bytes;
    tibrv_u32 size;

    addrmsg.getByteSize(size);
    addrmsg.getAsBytes(bytes);

    fprintf(stderr,"SIZE=%d\n",size);
    
    TibrvMsgField field;
    
    field.name      = field_name;
    field.id        = 0;
    field.count     = 1;
    field.size      = size;
    field.type      = TIBRVMSG_ADDRESS;
    field.data.buf  = (void*)bytes;

    return msg.updateField(field);    
}

/*-------------------------------------------------------------------- 
 * MyTibrvMsg::getAddress
 *------------------------------------------------------------------*/

TibrvStatus
MyTibrvMsg::getAddress(TibrvMsg& msg, const char* field_name, Address& address)
{
    TibrvMsgField field;
    TibrvStatus   status;

    status = msg.getField(field_name,field);
    if (status != TIBRV_OK)
        return status;

    const char* str;

    // Create a message from bytes and recover object
    // from the message.
    TibrvMsg addrmsg((char*)field.data.buf);

    addrmsg.getString("addr",str);
    address.setAddrLine(str);
    
    addrmsg.getString("city",str);
    strcpy(address.city,str);
    
    addrmsg.getString("state",str);
    strcpy(address.state,str);
    
    addrmsg.getU32("zip",address.zip_code);

    return TIBRV_OK;
}

/*-------------------------------------------------------------------- 
 * main()
 *
 * We completely ignore all error checking to keep the text simple.
 *------------------------------------------------------------------*/

int
main(int argc, char* argv[])
{
    TibrvMsg    msg;
    TibrvStatus status;
    const char* msgString;
    Contact contact;
    Address address;

    // Setup handlers for TIBRVMSG_CONTACT data type
    tibrvMsg_SetHandlers(TIBRVMSG_CONTACT,
                         Contact_Encoder, Contact_Decoder, 
                         Contact_Converter);

    // Setup handlers for TIBRVMSG_ADDRESS data type
    tibrvMsg_SetHandlers(TIBRVMSG_ADDRESS,
                         Address_Encoder, Address_Decoder,
                         Address_Converter);

    // Add Contact field
    MyTibrvMsg::addContact(msg,"contact",*(new Contact("Paul","Doe",31)));

    // Add Address field
    MyTibrvMsg::addAddress(msg,"address",*(new Address("100 Main Street","Dreamtown","CA",12345)));

    fprintf(stderr,"\nFields added.\n\n");
    
    // Retrieve and print the Contact field
    MyTibrvMsg::getContact(msg,"contact",contact);
    fprintf(stderr,"Contact: %s, %s: age=%d\n",
                    contact.last_name,contact.first_name,contact.age);

    // Retrieve and print the Address field
    MyTibrvMsg::getAddress(msg,"address",address);
    fprintf(stderr,"Address: %s\n",address.addr_line);
    fprintf(stderr,"         %s, %s %05d\n",address.city,address.state,address.zip_code);

    // Print entire message to make sure our converters work
    msg.convertToString(msgString);
    fprintf(stderr,"\nMessage converted to string:\n%s\n",msgString);

    // Now check if "Update" works correctly.
        
    // Update Contact field
    MyTibrvMsg::updateContact(msg,"contact",*(new Contact("Benedict","Smith",34)));

    // Update Address field
    MyTibrvMsg::updateAddress(msg,"address",*(new Address("10001 Very Long Street","Lakeside View","CA",12345)));

    fprintf(stderr,"\nFields updated.\n\n");
    
    // Retrieve and print the Contact field
    MyTibrvMsg::getContact(msg,"contact",contact);
    fprintf(stderr,"Contact: %s, %s: age=%d\n",
                    contact.last_name,contact.first_name,contact.age);

    // Retrieve and print the Address field
    MyTibrvMsg::getAddress(msg,"address",address);
    fprintf(stderr,"Address: %s\n",address.addr_line);
    fprintf(stderr,"         %s, %s %05d\n",address.city,address.state,address.zip_code);

    // Print entire message to make sure our converters work
    msg.convertToString(msgString);
    fprintf(stderr,"\nMessage converted to string:\n%s\n",msgString);

    return 0;
}
