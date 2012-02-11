// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHHandler_Object()
{
  this.mbInitialized = false;

  this.fInit    = WWHHandler_Init;
  this.fUpdate  = WWHHandler_Update;
  this.fSyncTOC = WWHHandler_SyncTOC;
}

function  WWHHandler_Init()
{
  WWHFrame.WWHJavaScript.fInit();
}

function  WWHHandler_Update(ParamURL)
{
}

function  WWHHandler_SyncTOC(ParamURL)
{
  WWHFrame.WWHJavaScript.fSyncTOC(ParamURL);
}
