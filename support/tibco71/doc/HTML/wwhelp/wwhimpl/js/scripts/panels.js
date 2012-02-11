// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHPanelsEntry_Object(ParamPanelObject)
{
  this.mPanelObject    = ParamPanelObject;
  this.mScrollPosition = new Array(0, 0);
}

function  WWHPanels_Object()
{
  this.mPanelEntries = new Array();
  this.mPopup        = new WWHPopup_Object("WWHFrame.WWHJavaScript.mPanels.mPopup",
                                           "WWHFrame.WWHNavigationFrame.WWHPanelFrame",
                                           WWHPanelHoverText_Translate, WWHPanelHoverText_Format,
                                           "WWHPanelPopupDIV", "WWHPanelPopupText", 1000, 12, 20,
                                           WWHFrame.WWHJavaScript.mSettings.mHoverText.mWidth);

  this.fClearScrollPosition   = WWHPanels_ClearScrollPosition;
  this.fSaveScrollPosition    = WWHPanels_SaveScrollPosition;
  this.fRestoreScrollPosition = WWHPanels_RestoreScrollPosition;
  this.fJumpToAnchor          = WWHPanels_JumpToAnchor;
  this.fGetCurrentPanelObject = WWHPanels_GetCurrentPanelObject;
  this.fDisplayPanel          = WWHPanels_DisplayPanel;
  this.fPanelLoaded           = WWHPanels_PanelLoaded;

  // Add visible panels
  //
  if (WWHFrame.WWHJavaScript.mSettings.mTOC.mbShow)
  {
    this.mPanelEntries[this.mPanelEntries.length] = new WWHPanelsEntry_Object(WWHFrame.WWHOutline);
  }
  if (WWHFrame.WWHJavaScript.mSettings.mIndex.mbShow)
  {
    this.mPanelEntries[this.mPanelEntries.length] = new WWHPanelsEntry_Object(WWHFrame.WWHIndex);
  }
  if (WWHFrame.WWHJavaScript.mSettings.mSearch.mbShow)
  {
    this.mPanelEntries[this.mPanelEntries.length] = new WWHPanelsEntry_Object(WWHFrame.WWHSearch);
  }
}

function  WWHPanels_ClearScrollPosition()
{
  var  CurrentTab = WWHFrame.WWHJavaScript.mCurrentTab;


  this.mPanelEntries[CurrentTab].mScrollPosition[0] = 0;
  this.mPanelEntries[CurrentTab].mScrollPosition[1] = 0;
}

function  WWHPanels_SaveScrollPosition()
{
  var  CurrentTab = WWHFrame.WWHJavaScript.mCurrentTab;


  if ((WWHFrame.WWHBrowserInfo.mBrowser == 1) ||  // Shorthand for Netscape
      (WWHFrame.WWHBrowserInfo.mBrowser == 4))    // Shorthand for Netscape 6.0 (Mozilla)
  {
    this.mPanelEntries[CurrentTab].mScrollPosition[0] = WWHFrame.WWHNavigationFrame.WWHPanelFrame.window.pageXOffset;
    this.mPanelEntries[CurrentTab].mScrollPosition[1] = WWHFrame.WWHNavigationFrame.WWHPanelFrame.window.pageYOffset;
  }
  else if (WWHFrame.WWHBrowserInfo.mBrowser == 2)  // Shorthand for IE
  {
    // Test required to avoid JavaScript error under IE5.5 on Windows
    //
    if (typeof WWHFrame.WWHNavigationFrame.WWHPanelFrame.document.body == "undefined")
    {
      this.mPanelEntries[CurrentTab].mScrollPosition[0] = 0;
      this.mPanelEntries[CurrentTab].mScrollPosition[1] = 0;
    }
    else
    {
      this.mPanelEntries[CurrentTab].mScrollPosition[0] = WWHFrame.WWHNavigationFrame.WWHPanelFrame.document.body.scrollLeft;
      this.mPanelEntries[CurrentTab].mScrollPosition[1] = WWHFrame.WWHNavigationFrame.WWHPanelFrame.document.body.scrollTop;
    }
  }
}

function  WWHPanels_RestoreScrollPosition()
{
  var  PanelEntry     = this.mPanelEntries[WWHFrame.WWHJavaScript.mCurrentTab];
  var  ScrollPosition = PanelEntry.mScrollPosition;


  // See if a target position has been specified
  //
  if (PanelEntry.mPanelObject.mPanelAnchor != null)
  {
    this.fJumpToAnchor();
  }
  else
  {
    // setTimeout required for correct operation in Netscape 6.0
    //
    setTimeout("WWHFrame.WWHNavigationFrame.WWHPanelFrame.window.scroll(" + ScrollPosition[0] + ", " + ScrollPosition[1] + ");", 10);
  }
}

function  WWHPanels_JumpToAnchor()
{
  var  PanelObject = this.fGetCurrentPanelObject();
  var  bEnableNavigatorWorkaround = false;


  if (WWHFrame.WWHBrowserInfo.mBrowser == 1)  // Shorthand for Netscape
  {
    // Navigator reloads the page if the hash isn't already defined
    //
    if (WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.hash.length == 0)
    {
      bEnableNavigatorWorkaround = true;
    }
  }

  // Jump to anchor
  //
  WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.hash = PanelObject.mPanelAnchor;

  // Navigator reloads the page if the hash isn't already defined
  //
  if ( ! bEnableNavigatorWorkaround)
  {
    PanelObject.mPanelAnchor = null;
  }
}

function  WWHPanels_GetCurrentPanelObject()
{
  return this.mPanelEntries[WWHFrame.WWHJavaScript.mCurrentTab].mPanelObject;
}

function  WWHPanels_DisplayPanel()
{
  var  PanelObject = this.fGetCurrentPanelObject();
  var  ExtraAction = "";


  if ((WWHFrame.WWHBrowserInfo.mBrowser == 1) ||  // Shorthand for Netscape
      (WWHFrame.WWHBrowserInfo.mBrowser == 4))    // Shorthand for Netscape 6.0 (Mozilla)
  {
    // Navigator has trouble if the hash is defined
    //
    if (WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.hash.length != 0)
    {
      ExtraAction = "WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.hash = \"\"; ";

      if (WWHFrame.WWHBrowserInfo.mBrowser == 4)  // Shorthand for Netscape 6.0 (Mozilla)
      {
        ExtraAction += "WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.replace(\"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/html/panel.htm") + "\"); "
      }
    }
  }

  // Close down any popups we had going to prevent JavaScript errors
  //
  this.mPopup.fHide();

  if ( ! PanelObject.mbPanelInitialized)
  {
    setTimeout(ExtraAction + "WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.replace(\"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/html/panelini.htm") + "\");", 1);
  }
  else
  {
    setTimeout(ExtraAction + "WWHFrame.WWHNavigationFrame.WWHPanelFrame.location.replace(\"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/html/panel.htm") + "\");", 1);
  }
}

function  WWHPanels_PanelLoaded()
{
  WWHFrame.WWHJavaScript.fEndChangeTab();
}

function  WWHPanelHoverText_Translate(ParamEntryID)
{
  var  PanelObject = WWHFrame.WWHJavaScript.mPanels.fGetCurrentPanelObject();


  return PanelObject.fHoverTextTranslate(ParamEntryID);
}

function  WWHPanelHoverText_Format(ParamWidth,
                                   ParamTextID,
                                   ParamText)
{
  var  PanelObject = WWHFrame.WWHJavaScript.mPanels.fGetCurrentPanelObject();


  return PanelObject.fHoverTextFormat(ParamWidth, ParamTextID, ParamText);
}
