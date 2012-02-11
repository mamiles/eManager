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
  this.mbInitialized = true;
  WWHFrame.WWHHelp.fHandlerInitialized();
}

function  WWHHandler_Update(ParamURL)
{
}

function  WWHHandler_SyncTOC(ParamURL)
{
  var  DocumentURL;


  // Switch to frameset with navigation
  //
  DocumentURL = WWHFrame.WWHHelp.fGetBookFileHREF(ParamURL);
  WWHFrame.WWHSwitch.fExec(false, WWHFrame.WWHHelp.mHelpURLPrefix + "/wwhelp/wwhimpl/common/html/wwhelp.htm?href=" + WWHStringUtilities_RestoreEscapedSpaces(DocumentURL));
}
