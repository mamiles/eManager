//
// Copyright (c) 1997-2001 TIBCO Software, Inc. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// TIBCO Software Inc.
//

#ifndef _INCLUDED_AmiList_
#define _INCLUDED_AmiList_

#include <ami.h>

class AmiList;

class AmiLink {
  AmiLink * pNext;     /* Pointer to next     link in list.*/
  AmiLink * pPrev;     /* Pointer to previous link in list.*/
  int       IsAnchor;  /* True if anchor link.             */
  void *    pObject;   /* Associated linked object.        */

  friend class AmiList;
  friend class AmiSession;
  friend class AmiMethod;
};

class AmiList {
public:

  AmiList()
  {
    link = new AmiLink;
    link->pNext = link;  
    link->pPrev = link;  
    link->IsAnchor = 1;
    link->pObject  = (void *)0;
  }

  virtual ~AmiList()
  {
    removeAll();
    delete link;
  }

  int removeAll();
  AmiLink* insert(void * inpObject);
  int remove(AmiLink * inpLink);

private:

  AmiLink * link;

  friend class AmiSession;
  friend class AmiMethod;
};

#endif // _INCLUDED_AmiList_
