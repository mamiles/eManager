/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)ft.h	2.13
 */

#ifndef _INCLUDED_tibrvft_h
#define _INCLUDED_tibrvft_h

#include "tibrv.h"

#if defined(__cplusplus)
extern "C" {
#endif

typedef tibrvId                 tibrvftMember;
typedef tibrvId                 tibrvftMonitor;

#define TIBRVFT_ADV_SOURCE      "RVFT"

typedef enum
{
    TIBRVFT_PREPARE_TO_ACTIVATE = 1,
    TIBRVFT_ACTIVATE            = 2,
    TIBRVFT_DEACTIVATE          = 3

} tibrvftAction;

extern const char*
tibrvft_Version(void);

/* Callback definitions */

typedef void
(*tibrvftMemberCallback) (
    tibrvftMember               member,
    const char*                 groupName,
    tibrvftAction               action,
    void*                       closure);

typedef void
(*tibrvftMemberOnComplete) (
    tibrvftMember               member,
    void*                       closure);

typedef void
(*tibrvftMonitorCallback) (
    tibrvftMonitor              monitor,
    const char*                 groupName,
    tibrv_u32                   numActiveMembers,
    void*                       closure);

typedef void
(*tibrvftMonitorOnComplete) (
    tibrvftMonitor              monitor,
    void*                       closure);

/* TIBRVFT Member declarations */

extern tibrv_status
tibrvftMember_Create(
    tibrvftMember*              member,
    tibrvQueue                  queue,
    tibrvftMemberCallback       callback,
    tibrvTransport              transport,
    const char*                 groupName,
    tibrv_u16                   weight,
    tibrv_u16                   activeGoal,
    tibrv_f64                   heartbeatInterval,
    tibrv_f64                   preparationInterval,
    tibrv_f64                   activationInterval,
    const void*                 closure);

extern tibrv_status
tibrvftMember_Destroy(
    tibrvftMember               member);

extern tibrv_status
tibrvftMember_DestroyEx(
    tibrvftMember               member,
    tibrvftMemberOnComplete     completeCallback);

extern tibrv_status
tibrvftMember_GetQueue(
    tibrvftMember               member,
    tibrvQueue*                 queue);

extern tibrv_status
tibrvftMember_GetTransport(
    tibrvftMember               member,
    tibrvTransport*             transport);

extern tibrv_status
tibrvftMember_GetGroupName(
    tibrvftMember               member,
    const char**                groupName);

extern tibrv_status
tibrvftMember_GetWeight(
    tibrvftMember               member,
    tibrv_u16*                  weight);

extern tibrv_status
tibrvftMember_SetWeight(
    tibrvftMember               member,
    tibrv_u16                   weight);


/* TIBRVFT Monitor declarations */

extern tibrv_status
tibrvftMonitor_Create(
    tibrvftMonitor*             monitor,
    tibrvQueue                  queue,
    tibrvftMonitorCallback      callback,
    tibrvTransport              transport,
    const char*                 groupName,
    tibrv_f64                   lostInterval,
    const void*                 closure);

extern tibrv_status
tibrvftMonitor_Destroy(
    tibrvftMonitor              monitor);

extern tibrv_status
tibrvftMonitor_DestroyEx(
    tibrvftMonitor              monitor,
    tibrvftMonitorOnComplete    completeCallback);

extern tibrv_status
tibrvftMonitor_GetQueue(
    tibrvftMonitor              monitor,
    tibrvQueue*                 queue);

extern tibrv_status
tibrvftMonitor_GetTransport(
    tibrvftMonitor              monitor,
    tibrvTransport*             transport);

extern tibrv_status
tibrvftMonitor_GetGroupName(
    tibrvftMonitor              monitor,
    const char**                groupName);

#ifdef  __cplusplus
}
#endif

#endif /* _INCLUDED_tibrvft_h */
