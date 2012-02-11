// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHOutlineImagingFast_Object()
{
  this.mIterator      = new WWHOutlineIterator_Object(true);
  this.mImageSrcDir   = WWHOutlineImaging_ImageSrcDir();
  this.mEventString   = WWHPopup_EventString();
  this.mHTMLSegment   = new WWHStringBuffer_Object();

  this.fGetIconURL     = WWHOutlineImaging_GetIconURL;
  this.fGetPopupAction = WWHOutlineImaging_GetPopupAction;
  this.fGetLink        = WWHOutlineImaging_GetLink;

  this.fGenerateStyles = WWHOutlineImagingFast_GenerateStyles;
  this.fReset          = WWHOutlineImagingFast_Reset;
  this.fAdvance        = WWHOutlineImagingFast_Advance;
  this.fOpenLevel      = WWHOutlineImagingFast_OpenLevel;
  this.fCloseLevel     = WWHOutlineImagingFast_CloseLevel;
  this.fSameLevel      = WWHOutlineImagingFast_SameLevel;
  this.fDisplayEntry   = WWHOutlineImagingFast_DisplayEntry;
  this.fUpdateEntry    = WWHOutlineImagingFast_UpdateEntry;
  this.fRevealEntry    = WWHOutlineImagingFast_RevealEntry;
}

function  WWHOutlineImagingFast_GenerateStyles()
{
  var  StyleBuffer = new WWHStringBuffer_Object();


  StyleBuffer.fAppend("<style type=\"text/css\">\n");
  StyleBuffer.fAppend(" <!--\n");
  StyleBuffer.fAppend("  a  { text-decoration: none ;\n");
  StyleBuffer.fAppend("       color: " + WWHFrame.WWHJavaScript.mSettings.mTOC.mEnabledColor + " }\n");
  StyleBuffer.fAppend("  ul { list-style-type: none ;\n");
  StyleBuffer.fAppend("       padding-left: 0pt ;\n");
  StyleBuffer.fAppend("       margin-top: 1pt ;\n");
  StyleBuffer.fAppend("       margin-bottom: 1pt ;\n");
  StyleBuffer.fAppend("       margin-left: 0pt }\n");
  StyleBuffer.fAppend("  li { margin-top: 1pt ;\n");
  StyleBuffer.fAppend("       margin-bottom: 1pt ;\n");
  StyleBuffer.fAppend("       " + WWHFrame.WWHJavaScript.mSettings.mTOC.mFontStyle + " ;\n");
  StyleBuffer.fAppend("       margin-left: " + WWHFrame.WWHJavaScript.mSettings.mTOC.mIndent + "pt }\n");
  StyleBuffer.fAppend("  li.book { margin-left: 0pt }\n");
  StyleBuffer.fAppend(" -->\n");
  StyleBuffer.fAppend("</style>\n");

  return StyleBuffer.fGetBuffer();
}

function  WWHOutlineImagingFast_Reset()
{
  this.mIterator.fReset(WWHFrame.WWHOutline.mTopEntry);
}

function  WWHOutlineImagingFast_Advance(ParamMaxHTMLSegmentSize)
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
      if (Entry.mLevel == 0)
      {
        this.mHTMLSegment.fAppend("<li class=book id=i" + Entry.mID + ">");
      }
      else
      {
        this.mHTMLSegment.fAppend("<li id=i" + Entry.mID + ">");
      }
      this.mHTMLSegment.fAppend(this.fDisplayEntry(Entry));
    }
  }

  return (this.mHTMLSegment.fSize() > 0);  // Return true if segment created
}

function  WWHOutlineImagingFast_OpenLevel()
{
  this.mHTMLSegment.fAppend("<ul>\n");
}

function  WWHOutlineImagingFast_CloseLevel()
{
  this.mHTMLSegment.fAppend("</li>\n");
  this.mHTMLSegment.fAppend("</ul>\n");
}

function  WWHOutlineImagingFast_SameLevel()
{
  this.mHTMLSegment.fAppend("</li>\n");
}

function  WWHOutlineImagingFast_DisplayEntry(ParamEntry)
{
  var  EntryHTML = "";
  var  IconURL = this.fGetIconURL(ParamEntry);


  if (ParamEntry.mChildren == null)
  {
    EntryHTML += "<nobr>";
    EntryHTML += this.fGetLink(ParamEntry, "<img width=17 height=17 border=0 src=\"" + IconURL + "\"> ");
    EntryHTML += "</nobr>\n";
  }
  else
  {
    if (ParamEntry.mbExpanded)
    {
      EntryHTML += "<nobr>";
      EntryHTML += "<a href=\"javascript:fC(" + ParamEntry.mID + ");\" " + this.fGetPopupAction(ParamEntry) + ">";
      EntryHTML += "<img width=17 height=17 border=0 src=\"" + IconURL + "\"></a>";
      EntryHTML += " " + this.fGetLink(ParamEntry, null);
      EntryHTML += "</nobr>\n";
    }
    else
    {
      EntryHTML += "<nobr>";
      EntryHTML += "<a href=\"javascript:fE(" + ParamEntry.mID + ");\" " + this.fGetPopupAction(ParamEntry) + ">";
      EntryHTML += "<img width=17 height=17 border=0 src=\"" + IconURL + "\"></a>";
      EntryHTML += " " + this.fGetLink(ParamEntry, null);
      EntryHTML += "</nobr>\n";
    }
  }

  return EntryHTML;
}

function  WWHOutlineImagingFast_UpdateEntry(ParamEntry)
{
  var  ElementID = "i" + ParamEntry.mID;
  var  EntryHTML = "";


  // Get entry display
  //
  EntryHTML = this.fDisplayEntry(ParamEntry);

  // Reset iterator to process current entry's children
  //
  this.mIterator.fReset(ParamEntry);

  // Process display of children
  //
  if (this.fAdvance(-1))
  {
    // Result already stored in this.mHTMLSegment
    //
  }

  // Close down any popups we had going to prevent JavaScript errors
  //
  WWHFrame.WWHJavaScript.mPanels.mPopup.fHide();

  // Update HTML
  //
  if ((WWHFrame.WWHBrowserInfo.mBrowser == 2) ||  // Shorthand for IE
      (WWHFrame.WWHBrowserInfo.mBrowser == 3))    // Shorthand for iCab
  {
    WWHFrame.WWHNavigationFrame.WWHPanelFrame.document.all[ElementID].innerHTML = EntryHTML + this.mHTMLSegment.fGetBuffer();
  }
  else if (WWHFrame.WWHBrowserInfo.mBrowser == 4)  // Shorthand for Netscape 6.0
  {
    WWHFrame.WWHNavigationFrame.WWHPanelFrame.document.getElementById(ElementID).innerHTML = EntryHTML + this.mHTMLSegment.fGetBuffer();
  }
}

function  WWHOutlineImagingFast_RevealEntry(ParamEntry,
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
    // Expand parent entry to reveal target entry
    //
    if (LastClosedParentEntry != null)
    {
      this.fUpdateEntry(LastClosedParentEntry);
    }

    // Display target
    //
    WWHFrame.WWHJavaScript.mPanels.fJumpToAnchor();
  }
}
