//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#include <AmiList.h>

int AmiList::removeAll()
{
  AmiLink *pLink;

  pLink = link;
  while(!(pLink->pNext->IsAnchor)) remove(pLink->pNext);

  // Remove everything after the anchor.
  pLink = pLink->pNext;
  while(!(pLink->pNext->IsAnchor)) remove(pLink->pNext);

  return(1);
}

AmiLink* AmiList::insert(void * inpObject)
{
  AmiLink * inpNewLink;
  AmiLink * inpLink = link->pPrev;

  inpNewLink = new AmiLink;
  if (inpNewLink != (AmiLink *)0 )
  {
    inpNewLink->pNext     = inpLink->pNext;
    inpNewLink->pPrev     = inpLink;
    inpNewLink->IsAnchor  = 0;
    inpNewLink->pObject   = inpObject;

    inpLink->pNext->pPrev = inpNewLink;
    inpLink->pNext        = inpNewLink;

    return(inpNewLink);
  }

  return((AmiLink *)0);
}

int AmiList::remove(AmiLink * inpLink)
{
  if (!inpLink->IsAnchor)
  {
    inpLink->pNext->pPrev = inpLink->pPrev;
    inpLink->pPrev->pNext = inpLink->pNext;

    memset(inpLink, 0, sizeof(AmiLink));
    delete inpLink;

    return(1);
  }

  return(0);
}
