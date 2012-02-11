// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHOutlineImagingSafe_Object()
{
  this.mIterator    = new WWHOutlineIterator_Object(true);
  this.mImageSrcDir = WWHOutlineImaging_ImageSrcDir();
  this.mEventString = WWHPopup_EventString();
  this.mHTMLSegment = new WWHStringBuffer_Object();

  this.fGetIconURL     = WWHOutlineImaging_GetIconURL;
  this.fGetPopupAction = WWHOutlineImaging_GetPopupAction;
  this.fGetLink        = WWHOutlineImaging_GetLink;

  this.fGenerateStyles = WWHOutlineImagingSafe_GenerateStyles;
  this.fReset          = WWHOutlineImagingSafe_Reset;
  this.fAdvance        = WWHOutlineImagingSafe_Advance;
  this.fOpenLevel      = WWHOutlineImagingSafe_OpenLevel;
  this.fCloseLevel     = WWHOutlineImagingSafe_CloseLevel;
  this.fSameLevel      = WWHOutlineImagingSafe_SameLevel;
  this.fDisplayEntry   = WWHOutlineImagingSafe_DisplayEntry;
  this.fUpdateEntry    = WWHOutlineImagingSafe_UpdateEntry;
  this.fRevealEntry    = WWHOutlineImagingSafe_RevealEntry;
}

function  WWHOutlineImagingSafe_GenerateStyles()
{
  var  StyleBuffer = new WWHStringBuffer_Object();
  var  MaxLevel;
  var  Level;


  StyleBuffer.fAppend("<style type=\"text/css\">\n");
  StyleBuffer.fAppend(" <!--\n");
  StyleBuffer.fAppend("  a { text-decoration: none ;\n");
  StyleBuffer.fAppend("      color: " + WWHFrame.WWHJavaScript.mSettings.mTOC.mEnabledColor + " }\n");
  StyleBuffer.fAppend("  p { margin-top: 1pt ;\n");
  StyleBuffer.fAppend("      margin-bottom: 1pt ;\n");
  StyleBuffer.fAppend("      " + WWHFrame.WWHJavaScript.mSettings.mTOC.mFontStyle + " }\n");
  for (MaxLevel = WWHFrame.WWHOutline.mMaxLevel, Level = 0 ; Level <= MaxLevel ; Level++)
  {
    StyleBuffer.fAppend("  p.l" + Level + " { margin-left: " + (WWHFrame.WWHJavaScript.mSettings.mTOC.mIndent * Level) + "pt }\n");
  }
  StyleBuffer.fAppend(" -->\n");
  StyleBuffer.fAppend("</style>\n");

  return StyleBuffer.fGetBuffer();
}

function  WWHOutlineImagingSafe_Reset()
{
  this.mIterator.fReset(WWHFrame.WWHOutline.mTopEntry);
}

function  WWHOutlineImagingSafe_Advance(ParamMaxHTMLSegmentSize)
{
  var  Entry;


  this.mHTMLSegment.fReset();
  while (((ParamMaxHTMLSegmentSize == -1) ||
          (this.mHTMLSegment.fSize() < ParamMaxHTMLSegmentSize)) &&
         (this.mIterator.fAdvance(this)))
  {
    Entry = this.mIterator.mEntry;

    // Process current entry
    //
    if (Entry.mbShow)
    {
      this.mHTMLSegment.fAppend(this.fDisplayEntry(Entry));
    }
  }

  return (this.mHTMLSegment.fSize() > 0);  // Return true if segment created
}

function  WWHOutlineImagingSafe_OpenLevel()
{
}

function  WWHOutlineImagingSafe_CloseLevel()
{
}

function  WWHOutlineImagingSafe_SameLevel()
{
}

function  WWHOutlineImagingSafe_DisplayEntry(ParamEntry)
{
  var  EntryHTML = "";
  var  IconURL = this.fGetIconURL(ParamEntry);


  if (ParamEntry.mChildren == null)
  {
    EntryHTML += "<p class=l" + ParamEntry.mLevel + ">";
    EntryHTML += "<nobr>";
    EntryHTML += this.fGetLink(ParamEntry, "<img width=17 height=17 border=0 src=\"" + IconURL + "\"> ");
    EntryHTML += "</nobr>";
    EntryHTML += "</p>\n";
  }
  else
  {
    if (ParamEntry.mbExpanded)
    {
      EntryHTML += "<p class=l" + ParamEntry.mLevel + ">";
      EntryHTML += "<nobr>";
      EntryHTML += "<a href=\"javascript:fC(" + ParamEntry.mID + ");\" " + this.fGetPopupAction(ParamEntry) + ">";
      EntryHTML += "<img width=17 height=17 border=0 src=\"" + IconURL + "\"></a>";
      EntryHTML += " " + this.fGetLink(ParamEntry, null);
      EntryHTML += "</nobr>";
      EntryHTML += "</p>\n";
    }
    else
    {
      EntryHTML += "<p class=l" + ParamEntry.mLevel + ">";
      EntryHTML += "<nobr>";
      EntryHTML += "<a href=\"javascript:fE(" + ParamEntry.mID + ");\" " + this.fGetPopupAction(ParamEntry) + ">";
      EntryHTML += "<img width=17 height=17 border=0 src=\"" + IconURL + "\"></a>";
      EntryHTML += " " + this.fGetLink(ParamEntry, null);
      EntryHTML += "</nobr>";
      EntryHTML += "</p>\n";
    }
  }

  return EntryHTML;
}

function  WWHOutlineImagingSafe_UpdateEntry(ParamEntry)
{
  var  EntryURL;


  // Save/restore current position
  //
  WWHFrame.WWHJavaScript.mPanels.fSaveScrollPosition();

  // Close down any popups we had going to prevent JavaScript errors
  //
  WWHFrame.WWHJavaScript.mPanels.mPopup.fHide();

  // Reload page to display expanded/collapsed entry
  //
  WWHFrame.WWHJavaScript.mPanels.fDisplayPanel();
}

function  WWHOutlineImagingSafe_RevealEntry(ParamEntry,
                                            bParamVisible)
{
  var  ParentEntry;
  var  LastClosedParentEntry = null;


  // Expand out enclosing entries
  //
  ParentEntry = ParamEntry.mParent;
  while (ParentEntry != null)
  {
    if ( ! ParentEntry.mbExpanded)
    {
      ParentEntry.mbExpanded = true;
      LastClosedParentEntry = ParentEntry;
    }

    ParentEntry = ParentEntry.mParent;
  }

  // Set target entry
  //
  WWHFrame.WWHOutline.mPanelAnchor = "t" + ParamEntry.mID;

  // Update display
  //
  if (bParamVisible)
  {
    // Update display if entry not already visible
    //
    if (LastClosedParentEntry != null)
    {
      this.fUpdateEntry(ParamEntry);
    }

    // Display target
    //
    WWHFrame.WWHJavaScript.mPanels.fJumpToAnchor();
  }
}
