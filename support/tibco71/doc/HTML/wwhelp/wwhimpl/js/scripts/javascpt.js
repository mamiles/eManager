// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHJavaScript_Object()
{
  this.mSettings           = new WWHJavaScriptSettings_Object();
  this.mMessages           = new WWHJavaScriptMessages_Object();
  this.mbChangingTabs      = false;
  this.mCurrentTab         = -1;
  this.mPanels             = null;
  this.mTabs               = null;
  this.mMaxHTMLSegmentSize = 8192;  // Best tested value is 8192

  this.fInit                    = WWHJavaScript_Init;
  this.fStartChangeTab          = WWHJavaScript_StartChangeTab;
  this.fStartChangeTabWithDelay = WWHJavaScript_StartChangeTabWithDelay;
  this.fEndChangeTab            = WWHJavaScript_EndChangeTab;
  this.fSyncTOC                 = WWHJavaScript_SyncTOC;

  // Load up messages
  //
  this.mMessages.fSetByLocale(WWHFrame.WWHHelp.mLocale);
}

function  WWHJavaScript_Init()
{
  var  InitialTab;


  // Create panels and tabs objects
  //
  this.mPanels = new WWHPanels_Object();
  this.mTabs   = new WWHTabs_Object();

  // Set inital tab to display
  //
  InitialTab = this.mTabs.fInit();

  // Display tab and panel
  //
  this.fStartChangeTab(InitialTab);
}

function  WWHJavaScript_StartChangeTab(ParamIndex)
{
  if (( ! this.mbChangingTabs) &&
      ((this.mCurrentTab == -1) ||
       (this.mCurrentTab != ParamIndex)))
  {
    // Signal that we are changing tabs
    //
    this.mbChangingTabs = true;

    // Record current scroll position
    //
    if (this.mCurrentTab != -1)
    {
      this.mPanels.fSaveScrollPosition();
    }

    // Update tab index
    //
    this.mCurrentTab = ParamIndex;

    // Update tab frame and panel frame
    //
    this.mTabs.fDisplayTab();
    this.mPanels.fDisplayPanel();
  }
}

function  WWHJavaScript_StartChangeTabWithDelay(ParamIndex)
{
  setTimeout("WWHFrame.WWHJavaScript.fStartChangeTab(" + ParamIndex + ");", 1);
}

function  WWHJavaScript_EndChangeTab()
{
  // Check for frame set reloading (back or forward button used)
  //
  if (WWHFrame.WWHHelp.mInitStage > 0)
  {
    // Restore window position
    //
    this.mPanels.fRestoreScrollPosition();
  }

  // Signal that the change tab process is complete
  //
  this.mbChangingTabs = false;

  // Complete initialization process
  //
  if ( ! WWHFrame.WWHHandler.mbInitialized)
  {
    WWHFrame.WWHHandler.mbInitialized = true;
    WWHFrame.WWHHelp.fHandlerInitialized();
  }
}

function  WWHJavaScript_SyncTOC(ParamHREF)
{
  // Confirm TOC is available as a tab
  //
  if (this.mSettings.mTOC.mbShow)
  {
    var  BookFileHREF;


    // Confirm file is part of a known book
    //
    BookFileHREF = WWHFrame.WWHHelp.fGetBookFileHREF(ParamHREF);
    if (BookFileHREF != null)
    {
      var  BookIndexFileHREF;
      var  BookIndex;
      var  FileHREF;


      // Determine book index and file href
      //
      BookIndexFileHREF = WWHFrame.WWHHelp.mBooks.fGetBookIndexFileHREF(BookFileHREF)
      BookIndex = BookIndexFileHREF[0];
      FileHREF  = BookIndexFileHREF[1];

      // Sync outline if match found
      //
      if ((BookIndex != -1) &&
          (FileHREF  != null))
      {
        var  bVisible = false;


        // Determine visibility
        //
        if (this.mPanels.fGetCurrentPanelObject().mPanelTabTitle == this.mMessages.mTabsTOCLabel)
        {
          bVisible = true;
        }

        // Sync TOC
        //
        WWHFrame.WWHOutline.fSync(BookIndex, FileHREF, bVisible);

        // Change tabs if not visible
        //
        if ( ! bVisible)
        {
          var  TabIndex;
          var  Index;


          // Determine tab to display for TOC
          //
          TabIndex = -1;
          Index = 0;
          while ((TabIndex == -1) &&
                 (Index < WWHFrame.WWHJavaScript.mPanels.mPanelEntries.length))
          {
            if (WWHFrame.WWHJavaScript.mPanels.mPanelEntries[Index].mPanelObject.mPanelTabTitle == WWHFrame.WWHJavaScript.mMessages.mTabsTOCLabel)
            {
              TabIndex = Index;
            }

            Index++;
          }

          if (TabIndex != -1)
          {
            WWHFrame.WWHJavaScript.fStartChangeTab(TabIndex);
          }
        }
      }
    }
  }
}
