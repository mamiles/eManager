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
  // Java has already started loading
  // May have finished loading before this stage was reached
  //
  if (this.mbInitialized)
  {
    WWHFrame.WWHHelp.fHandlerInitialized();
  }
}

function  WWHHandler_Update(ParamURL)
{
  if (WWHFrame.WWHBrowserInfo.mPlatform != 2)  // Shorthand for Macintosh
  {
    WWHFrame.WWHNavigationFrame.document.applets[0].updateFavorites(ParamURL);
  }
}

function  WWHHandler_SyncTOC(ParamURL)
{
  if (WWHFrame.WWHBrowserInfo.mPlatform != 2)  // Shorthand for Macintosh
  {
    WWHFrame.WWHNavigationFrame.document.applets[0].syncTOC(ParamURL);
  }
}
