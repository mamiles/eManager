/*
 * Copyright 1999-2000 TIBCO Software Inc.
 * ALL RIGHTS RESERVED.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)Tibrvft.xs	1.6
 */


#include "EXTERN.h"
#include "perl.h"
#include "XSUB.h"

#include <tibrv/tibrv.h>
#include <tibrv/ft.h>

/*************************************************************************/
/*                          callback functions                           */
/*************************************************************************/
typedef struct
{
    tibrvId           id;
    SV *              perl_cb_func;
    SV *              perl_cb_func_closure;
    SV *              perl_on_complete_func;
} perl_tibrvftId, *plftid;


void perl_tibrvftMemberCB( tibrvId        event,
                           const char*    groupName,
		           tibrvftAction  action,
		           void*          closure)
{
	dSP;

	plftid  perl_info=(plftid)closure;

	ENTER;
	SAVETMPS;

	PUSHMARK(sp);
	EXTEND(sp, 4);
	PUSHs(sv_2mortal(newSViv((IV)perl_info)));
	PUSHs(sv_2mortal(newSVpv((char*)groupName,0)));
	PUSHs(sv_2mortal(newSViv((IV)action)));
	PUSHs(sv_2mortal(newSVsv(perl_info->perl_cb_func_closure)));
	PUTBACK;

	perl_call_sv(perl_info->perl_cb_func,G_DISCARD);

	SPAGAIN;

	FREETMPS;
	LEAVE;
}

void perl_tibrvftMonitorCB( tibrvId        event,
                            const char*    groupName,
		            tibrv_u32      numActiveMembers,
		            void*          closure)
{
	dSP;

	plftid  perl_info=(plftid)closure;

	ENTER;
	SAVETMPS;

	PUSHMARK(sp);
	EXTEND(sp, 4);
	PUSHs(sv_2mortal(newSViv((IV)perl_info)));
	PUSHs(sv_2mortal(newSVpv((char*)groupName,0)));
	PUSHs(sv_2mortal(newSViv((IV)numActiveMembers)));
	PUSHs(sv_2mortal(newSVsv(perl_info->perl_cb_func_closure)));
	PUTBACK;

	perl_call_sv(perl_info->perl_cb_func,G_DISCARD);

	SPAGAIN;

	FREETMPS;
	LEAVE;
}


void perl_tibrvftOnCompleteCB( tibrvId    id,
                               void*      closure)
{
	dSP;

	plftid perl_info=(plftid)closure;

	ENTER;
	SAVETMPS;

	PUSHMARK(sp);
	EXTEND(sp, 2);
	PUSHs(sv_2mortal(newSViv((IV)perl_info)));
	PUSHs(sv_2mortal(newSVsv(perl_info->perl_cb_func_closure)));
	PUTBACK;

	perl_call_sv(perl_info->perl_on_complete_func,G_DISCARD);

	SPAGAIN;

	FREETMPS;
	LEAVE;

}

static int
not_here(char *s)
{
    croak("%s not implemented on this architecture", s);
    return -1;
}

static double
constant(char *name, int arg)
{
    errno = 0;
    switch (*name) {
    case 'A':
	break;
    case 'B':
	break;
    case 'C':
	break;
    case 'D':
	break;
    case 'E':
	break;
    case 'F':
	break;
    case 'G':
	break;
    case 'H':
	break;
    case 'I':
	break;
    case 'J':
	break;
    case 'K':
	break;
    case 'L':
	break;
    case 'M':
	break;
    case 'N':
	break;
    case 'O':
	break;
    case 'P':
	break;
    case 'Q':
	break;
    case 'R':
	break;
    case 'S':
	break;
    case 'T':
	break;
    case 'U':
	break;
    case 'V':
	break;
    case 'W':
	break;
    case 'X':
	break;
    case 'Y':
	break;
    case 'Z':
	break;
    }
    errno = EINVAL;
    return 0;

not_there:
    errno = ENOENT;
    return 0;
}


MODULE = Tibrvft		PACKAGE = Tibrvft

PROTOTYPES: ENABLE

double
constant(name,arg)
	char *		name
	int		arg



const char *
tibrvft_Version()
CODE:
	RETVAL=tibrvft_Version();
OUTPUT:
RETVAL

tibrv_status
tibrvftMember_Create(member, queue, callback, transport, groupName, weight, activeGoal, heartbeatInterval, preparationInterval, activationInterval, closure)
	plftid 	        member
	tibrvQueue	queue
	SV*	        callback
	tibrvTransport	transport
	const char *	groupName
	tibrv_u16	weight
	tibrv_u16	activeGoal
	tibrv_f64	heartbeatInterval
	tibrv_f64	preparationInterval
	tibrv_f64	activationInterval
	SV*	        closure
CODE:
	plftid ft_mbr;

	ft_mbr=(plftid)calloc(sizeof(perl_tibrvftId),1);
        if( ft_mbr )
	{
	RETVAL=tibrvftMember_Create(&ft_mbr->id, 
                                    queue, 
                                    perl_tibrvftMemberCB, 
	                            transport, groupName, weight, 
                                    activeGoal, heartbeatInterval, 
                                    preparationInterval,
	                            activationInterval, 
                                    (void*)ft_mbr);
        if ( RETVAL == TIBRV_OK )
        {
            ft_mbr->perl_cb_func = newSVsv(callback);
	    ft_mbr->perl_cb_func_closure = newSVsv(closure);
        }
        member = ft_mbr;
        }
OUTPUT:
member
RETVAL

tibrv_status
tibrvftMember_Destroy(member)
	plftid	member
CODE:
	RETVAL=tibrvftMember_Destroy(member->id);
	if( RETVAL == TIBRV_OK )
	{
	    SvREFCNT_dec(member->perl_cb_func);
	    SvREFCNT_dec(member->perl_cb_func_closure);
	    free( member );
        }
OUTPUT:
RETVAL

tibrv_status
tibrvftMember_DestroyEx(member, completeCallback)
	plftid	member
	SV*	completeCallback
CODE:
	member->perl_on_complete_func = newSVsv(completeCallback);
	RETVAL=tibrvftMember_DestroyEx(member->id,
	                               perl_tibrvftOnCompleteCB);
OUTPUT:
RETVAL

tibrv_status
tibrvftMember_GetQueue(member, queue)
	plftid	        member
	tibrvQueue 	queue
CODE:
	RETVAL=tibrvftMember_GetQueue(member->id, &queue);
OUTPUT:
queue
RETVAL

tibrv_status
tibrvftMember_GetTransport(member, transport)
	plftid	        member
	tibrvTransport 	transport
CODE:
	RETVAL=tibrvftMember_GetTransport( member->id, &transport );
OUTPUT:
transport
RETVAL

tibrv_status
tibrvftMember_GetGroupName(member, groupName)
	plftid	        member
	const char *	groupName
CODE:
	RETVAL=tibrvftMember_GetGroupName( member->id, &groupName );
OUTPUT:
groupName
RETVAL

tibrv_status
tibrvftMember_GetWeight(member, weight)
	plftid	        member
	tibrv_u16 	weight
CODE:
	RETVAL=tibrvftMember_GetWeight(member->id, &weight);
OUTPUT:
weight
RETVAL

tibrv_status
tibrvftMember_SetWeight(member, weight)
	plftid 	        member
	tibrv_u16	weight
CODE:
	RETVAL=tibrvftMember_SetWeight( member->id, weight );
OUTPUT:
RETVAL

#
# monitor
#
tibrv_status
tibrvftMonitor_Create(monitor, queue, callback, transport, groupName, lostInterval, closure)
	plftid   	monitor
	tibrvQueue	queue
	SV* 	        callback
	tibrvTransport	transport
	const char *	groupName
	tibrv_f64	lostInterval
	SV*	closure
CODE:
	plftid ft_mbr=NULL;

	ft_mbr=(plftid)calloc(sizeof(perl_tibrvftId),1);
        if( ft_mbr )
	{
	    RETVAL=tibrvftMonitor_Create(&ft_mbr->id, 
                                         queue, 
                                         perl_tibrvftMonitorCB, 
	                                 transport, groupName,
	                                 lostInterval, 
                                         (void*)ft_mbr);

            if ( RETVAL == TIBRV_OK )
	    {
                ft_mbr->perl_cb_func         = newSVsv(callback);
	        ft_mbr->perl_cb_func_closure = newSVsv(closure);
            }
            monitor = ft_mbr;
        }
OUTPUT:
monitor
RETVAL

tibrv_status
tibrvftMonitor_Destroy(monitor)
	plftid	monitor
CODE:
	RETVAL=tibrvftMonitor_Destroy(monitor->id);

	if( RETVAL == TIBRV_OK )
	{
	    SvREFCNT_dec(monitor->perl_cb_func);
	    SvREFCNT_dec(monitor->perl_cb_func_closure);
	    free( monitor );
        }
OUTPUT:
RETVAL

tibrv_status
tibrvftMonitor_DestroyEx(monitor, completeCallback)
	plftid	monitor
	SV*	completeCallback
CODE:
	monitor->perl_on_complete_func = newSVsv(completeCallback);

	RETVAL=tibrvftMonitor_DestroyEx(monitor->id,
	                                perl_tibrvftOnCompleteCB);

	if( RETVAL == TIBRV_OK )
	{
	    SvREFCNT_dec(monitor->perl_cb_func);
	    SvREFCNT_dec(monitor->perl_cb_func_closure);
	    free( monitor );
        }
OUTPUT:
RETVAL

tibrv_status
tibrvftMonitor_GetQueue(monitor, queue)
	plftid	        monitor
	tibrvQueue 	queue
CODE:
	RETVAL=tibrvftMonitor_GetQueue(monitor->id,&queue);
OUTPUT:
queue
RETVAL

tibrv_status
tibrvftMonitor_GetTransport(monitor, transport)
	plftid  	monitor
	tibrvTransport 	transport
CODE:
	RETVAL=tibrvftMonitor_GetTransport(monitor->id, &transport);
OUTPUT:
transport
RETVAL

tibrv_status
tibrvftMonitor_GetGroupName(monitor, groupName)
	plftid	monitor
	const char *	groupName
CODE:
	RETVAL=tibrvftMonitor_GetGroupName(monitor->id, &groupName);
OUTPUT:
groupName
RETVAL

