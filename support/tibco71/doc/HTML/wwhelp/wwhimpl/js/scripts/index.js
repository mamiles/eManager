// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHIndex_Object()
{
  this.mbPanelInitialized = false;
  this.mPanelAnchor       = null;
  this.mPanelTabTitle     = WWHFrame.WWHJavaScript.mMessages.mTabsIndexLabel;
  this.mInitIndex         = 0;
  this.mOptions           = new WWHIndexOptions_Object();
  this.mTopEntry          = new WWHIndexTopEntry_Object();
  this.mMaxLevel          = 0;
  this.mEntryCount        = 0;
  this.mSeeAlsoArray      = new Array();
  this.mSectionIndex      = null;
  this.mSectionCache      = new WWHSectionCache_Object();
  this.mIterator          = new WWHIndexIterator_Object();
  this.mHTMLSegment       = new WWHStringBuffer_Object();
  this.mEventString       = WWHPopup_EventString();
  this.mClickedEntry      = null;

  this.fInitHeadHTML          = WWHIndex_InitHeadHTML;
  this.fInitBodyHTML          = WWHIndex_InitBodyHTML;
  this.fInitLoadBookIndex     = WWHIndex_InitLoadBookIndex;
  this.fAddSeeAlsoEntry       = WWHIndex_AddSeeAlsoEntry;
  this.fProcessSeeAlsoEntries = WWHIndex_ProcessSeeAlsoEntries;
  this.fHeadHTML              = WWHIndex_HeadHTML;
  this.fStartHTMLSegments     = WWHIndex_StartHTMLSegments;
  this.fAdvanceHTMLSegment    = WWHIndex_AdvanceHTMLSegment;
  this.fGetHTMLSegment        = WWHIndex_GetHTMLSegment;
  this.fEndHTMLSegments       = WWHIndex_EndHTMLSegments;
  this.fHoverTextTranslate    = WWHIndex_HoverTextTranslate;
  this.fHoverTextFormat       = WWHIndex_HoverTextFormat;
  this.fGetPopupAction        = WWHIndex_GetPopupAction;
  this.fThresholdExceeded     = WWHIndex_ThresholdExceeded;
  this.fGetSectionNavigation  = WWHIndex_GetSectionNavigation;
  this.fChangeSection         = WWHIndex_ChangeSection;
  this.fSelectionListHeadHTML = WWHIndex_SelectionListHeadHTML;
  this.fSelectionListBodyHTML = WWHIndex_SelectionListBodyHTML;
  this.fDisplayLink           = WWHIndex_DisplayLink;
  this.fGetEntry              = WWHIndex_GetEntry;
  this.fClickedEntry          = WWHIndex_ClickedEntry;
  this.fClickedSeeAlsoEntry   = WWHIndex_ClickedSeeAlsoEntry;

  // Set options
  //
  WWHJavaScriptSettings_Index_DisplayOptions(this.mOptions);
}

function  WWHIndex_InitHeadHTML()
{
  var  InitHeadHTML = "";


  return InitHeadHTML;
}

function  WWHIndex_InitBodyHTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  BookList = WWHFrame.WWHHelp.mBooks.mBookList;
  var  bFoundRequiredGroupOptions = false;
  var  RequiredGroup = null;
  var  MaxIndex;
  var  Index;
  var  bRequiredGroupShowBuckets;
  var  ItemKey;
  var  Item;


  // Display initializing message
  //
  HTML.fAppend("<h2>" + WWHFrame.WWHJavaScript.mMessages.mInitializingMessage + "</h2>\n");

  // Load index data
  //
  this.mInitIndex = 0;
  for (MaxIndex = BookList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    // Reference Index data
    //
    HTML.fAppend("<script language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + WWHStringUtilities_RestoreEscapedSpaces(BookList[Index].mDirectory) + "wwhdata/js/index.js\"></script>\n");

    // Load Index data for current book
    //
    HTML.fAppend("<script language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/scripts/index1s.js\"></script>\n");
  }

  // Confirm required group options are defined
  //
  for (MaxIndex = this.mOptions.mGroupList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    if (this.mOptions.mGroupList[Index].length == 0)  // Required Group
    {
      bFoundRequiredGroupOptions = true;
    }
  }

  // Create required group options if not found
  //
  if ( ! bFoundRequiredGroupOptions)
  {
    this.mOptions.fGroup("", true, false, "");
  }

  // Create always display entries
  //
  for (MaxIndex = this.mOptions.mGroupList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    if (this.mOptions.mGroupList[Index].length == 0)  // Required group
    {
      RequiredGroup = this.mTopEntry.fGetGroup(this.mOptions.mGroupList[Index]);
    }
    else if (this.mOptions.mGroupInfoHash[this.mOptions.mGroupList[Index] + "~"].mbAlwaysDisplay)  // Always display grouping
    {
      this.mTopEntry.fGetGroup(this.mOptions.mGroupList[Index]);
    }
  }

  // Determine if buckets are going to be hidden
  //
  bRequiredGroupShowBuckets = this.mOptions.mGroupInfoHash["" + "~"].mbShowBuckets;

  // Add require items to required group
  //
  for (ItemKey in this.mOptions.mGroupMapping)
  {
    if (this.mOptions.mGroupMapping[ItemKey].length == 0)  // Required item
    {
      Item = ItemKey.substring(0, ItemKey.length - 1);
      RequiredGroup.fGetBucket(Item, bRequiredGroupShowBuckets);
    }
  }

  return HTML.fGetBuffer();
}

function  WWHIndex_InitLoadBookIndex(ParamAddIndexEntriesFunc)
{
  // Load Index
  //
  ParamAddIndexEntriesFunc(this.mTopEntry);

  // Increment init book index
  //
  this.mInitIndex++;

  // Check if done
  //
  if (this.mInitIndex == WWHFrame.WWHHelp.mBooks.mBookList.length)
  {
    // Process see also entries to set up links between source and target
    // Do this before the top level hashes are cleared by the sort children call
    //
    this.fProcessSeeAlsoEntries();

    // Sort top level entries
    //
    if (this.mTopEntry.mChildrenSortArray == null)
    {
      WWHIndexEntry_SortChildren(this.mTopEntry);
    }

    // Panel is initialized
    //
    this.mbPanelInitialized = true;
  }
}

function  WWHIndex_AddSeeAlsoEntry(ParamEntry)
{
  this.mSeeAlsoArray[this.mSeeAlsoArray.length] = ParamEntry;
}

function  WWHIndex_ProcessSeeAlsoEntries()
{
  var  MaxIndex;
  var  Index;
  var  FirstChar;
  var  GroupTitle;
  var  SeeAlsoTargetEntry;
  var  GroupEntry;
  var  BucketEntry;


  for (MaxIndex = this.mSeeAlsoArray.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    SeeAlsoEntry = this.mSeeAlsoArray[Index];

    // Determine location of see also target
    //
    if (SeeAlsoEntry.mSeeAlso.length > 0)
    {
      FirstChar = SeeAlsoEntry.mSeeAlso.substring(0, 1);

      // toUpperCase is conditional to handle numbers
      //
      FirstChar = (typeof FirstChar.toUpperCase == "function") ? FirstChar.toUpperCase() : FirstChar;

      GroupTitle = WWHFrame.WWHIndex.mOptions.mGroupMapping[FirstChar + "~"];
      if (typeof GroupTitle == "undefined")
      {
        GroupTitle = "";  // Put in required grouping
      }

      // Access target entry
      //
      SeeAlsoTargetEntry = null;
      GroupEntry = this.mTopEntry.mChildren[GroupTitle + "~"];
      if ((typeof GroupEntry != "undefined") &&
          (GroupEntry.mChildren != null))
      {
        BucketEntry = GroupEntry.mChildren[FirstChar + "~"];
        if ((typeof BucketEntry != "undefined") &&
            (BucketEntry.mChildren != null))
        {
          SeeAlsoTargetEntry = BucketEntry.mChildren[SeeAlsoEntry.mSeeAlso + "~"];
          if (typeof SeeAlsoTargetEntry == "undefined")
          {
            SeeAlsoTargetEntry = null;
          }
        }
      }

      // Setup links between source and destination
      //
      if (SeeAlsoTargetEntry != null)
      {
        // See if target entry is already tagged
        //
        if (typeof SeeAlsoTargetEntry.mSeeAlsoTargetName == "undefined")
        {
          // Update target entry
          //
          SeeAlsoTargetEntry.mSeeAlsoTargetName = "s" + Index;
        }

        // Update source entry
        //
        SeeAlsoEntry.mSeeAlsoTargetName = SeeAlsoTargetEntry.mSeeAlsoTargetName;
        SeeAlsoEntry.mSeeAlsoTargetEntryKey = (GroupTitle.length > 0) ? GroupTitle : FirstChar;
      }
    }
  }
}

function  WWHIndex_HeadHTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  MaxLevel;
  var  Level;


  // Generate style section
  //
  HTML.fAppend("<style type=\"text/css\">\n");
  HTML.fAppend(" <!--\n");
  HTML.fAppend("  a.navigation { text-decoration: none ;\n");
  HTML.fAppend("                 color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mNavigationEnabledColor + " }\n");
  HTML.fAppend("  p.navigation { margin-top: 1pt ;\n");
  HTML.fAppend("                 margin-bottom: 1pt ;\n");
  HTML.fAppend("                 color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mNavigationCurrentColor + " ;\n");
  HTML.fAppend("                 " + WWHFrame.WWHJavaScript.mSettings.mIndex.mNavigationFontStyle + " }\n");
  HTML.fAppend("  a            { text-decoration: none ;\n");
  HTML.fAppend("                 color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mEnabledColor + " }\n");
  HTML.fAppend("  a.AnchorOnly { text-decoration: none ;\n");
  HTML.fAppend("                 color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mDisabledColor + " }\n");
  HTML.fAppend("  p            { margin-top: 1pt ;\n");
  HTML.fAppend("                 margin-bottom: 1pt ;\n");
  HTML.fAppend("                 color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mDisabledColor + " ;\n");
  HTML.fAppend("                 " + WWHFrame.WWHJavaScript.mSettings.mIndex.mFontStyle + " }\n");
  for (MaxLevel = this.mMaxLevel + 1, Level = 0 ; Level <= MaxLevel ; Level++)
  {
    HTML.fAppend("  p.l" + Level + " { margin-left: " + (WWHFrame.WWHJavaScript.mSettings.mIndex.mIndent * Level) + "pt }\n");
  }
  HTML.fAppend(" -->\n");
  HTML.fAppend("</style>\n");

  return HTML.fGetBuffer();
}

function  WWHIndex_StartHTMLSegments()
{
  var  HTML = new WWHStringBuffer_Object();


  if (this.fThresholdExceeded())
  {
    // Display sections of the index rather than the whole thing
    //

    // Select first entry if section not already picked
    //
    if (this.mSectionIndex == null)
    {
      this.mSectionIndex = 0;
    }

    // Calculate section navigation if not already cached
    //
    if (typeof this.mSectionCache[this.mSectionIndex] == "undefined")
    {
      this.mSectionCache[this.mSectionIndex] = this.fGetSectionNavigation(this.mSectionIndex);
    }

    // Display section selection
    //
    HTML.fAppend(this.mSectionCache[this.mSectionIndex]);
    HTML.fAppend("<p>&nbsp;</p>\n");
  }
  else
  {
    // Display whole index
    //
    this.mSectionIndex = null;
  }

  // Setup iterator for display
  //
  this.mIterator.fReset(this.mSectionIndex, this.fThresholdExceeded());

  // Define accessor functions to reduce file size
  //
  HTML.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\">\n");
  HTML.fAppend(" <!--\n");
  HTML.fAppend("  function  fC(ParamEntryInfo)\n");
  HTML.fAppend("  {\n");
  HTML.fAppend("    WWHFrame.WWHIndex.fClickedEntry(ParamEntryInfo);\n");
  HTML.fAppend("  }\n");
  HTML.fAppend("\n");
  HTML.fAppend("  function  fA(ParamEntryInfo)\n");
  HTML.fAppend("  {\n");
  HTML.fAppend("    WWHFrame.WWHIndex.fClickedSeeAlsoEntry(ParamEntryInfo);\n");
  HTML.fAppend("  }\n");
  HTML.fAppend("\n");
  HTML.fAppend("  function  fS(ParamEntryID,\n");
  HTML.fAppend("               ParamEvent)\n");
  HTML.fAppend("  {\n");
  HTML.fAppend("    WWHFrame.WWHJavaScript.mPanels.mPopup.fShow(ParamEntryID, ParamEvent);\n");
  HTML.fAppend("  }\n");
  HTML.fAppend("\n");
  HTML.fAppend("  function  fH()\n");
  HTML.fAppend("  {\n");
  HTML.fAppend("    WWHFrame.WWHJavaScript.mPanels.mPopup.fHide();\n");
  HTML.fAppend("  }\n");
  HTML.fAppend("  function  fN(ParamSectionIndex)\n");
  HTML.fAppend("  {\n");
  HTML.fAppend("    WWHFrame.WWHIndex.fChangeSection(ParamSectionIndex);\n");
  HTML.fAppend("  }\n");
  HTML.fAppend(" // -->\n");
  HTML.fAppend("</script>\n");

  return HTML.fGetBuffer();
}

function  WWHIndex_AdvanceHTMLSegment()
{
  var  MaxHTMLSegmentSize = WWHFrame.WWHJavaScript.mMaxHTMLSegmentSize;
  var  Entry;
  var  MaxIndex;
  var  Index;
  var  BaseEntryInfo = "";
  var  EntryInfo;
  var  EntryAnchorName;
  var  EntryPrefix;
  var  EntrySuffix;


  // Add index in top entry to entry info if IteratorScope != TopEntry
  //
  if (this.mSectionIndex != null)
  {
    BaseEntryInfo += this.mSectionIndex;
  }

  this.mHTMLSegment.fReset();
  while ((this.mHTMLSegment.fSize() < MaxHTMLSegmentSize) &&
         (this.mIterator.fAdvance()))
  {
    Entry = this.mIterator.mEntry;

    // Check to see if this entry should be displayed
    //
    if ((Entry.mbBucket) &&
        ( ! Entry.mbShow))
    {
      // Don't display hidden buckets
      //

      // Do display break between other entries and first required entry
      //
      if (Entry.mbFirstRequiredEntry)
      {
        this.mHTMLSegment.fAppend("<p>&nbsp;</p>\n");
      } 
    }
    else
    {
      // Insert breaks between sections
      //
      if ((Entry.mbGroup) ||
          (Entry.mbBucket))
      {
        this.mHTMLSegment.fAppend("<p>&nbsp;</p>\n");
      }

      // See if entry needs a named anchor target
      //
      if (typeof Entry.mSeeAlsoTargetName == "string")
      {
        EntryAnchorName = " name=\"" + Entry.mSeeAlsoTargetName + "\"";
      }
      else
      {
        EntryAnchorName = "";
      }

      // Determine entry type
      //
      if (Entry.mbGroup)
      {
        EntryPrefix = "<b>";
        EntrySuffix = "</b>";
      }
      else if (Entry.mbBucket)
      {
        EntryPrefix = "<b>";
        EntrySuffix = "</b>";
      }
      else if (typeof Entry.mSeeAlso == "string")
      {
        if (typeof Entry.mSeeAlsoTargetName == "string")
        {
          // Use position stack for link info
          //
          EntryInfo = BaseEntryInfo;
          for (MaxIndex = this.mIterator.mPositionStack.length, Index = 0 ; Index < MaxIndex ; Index++)
          {
            if (EntryInfo.length > 0)
            {
              EntryInfo += ":";
            }
            EntryInfo += this.mIterator.mPositionStack[Index];
          }

          EntryPrefix = "<i><a href=\"javascript:fA('" + EntryInfo + "');\"" + this.fGetPopupAction(EntryInfo) + ">";
          EntrySuffix = "</a></i>";
        }
        else
        {
          EntryPrefix = "<i>";
          EntrySuffix = "</i>";
        }
      }
      else if (Entry.mBookLinks != null)
      {
        // Use position stack for link info
        //
        EntryInfo = BaseEntryInfo;
        for (MaxIndex = this.mIterator.mPositionStack.length, Index = 0 ; Index < MaxIndex ; Index++)
        {
          if (EntryInfo.length > 0)
          {
            EntryInfo += ":";
          }
          EntryInfo += this.mIterator.mPositionStack[Index];
        }

        EntryPrefix = "<a" + EntryAnchorName + " href=\"javascript:fC('" + EntryInfo + "');\"" + this.fGetPopupAction(EntryInfo) + ">";
        EntrySuffix = "</a>";
      }
      else if (EntryAnchorName.length > 0)
      {
        EntryPrefix = "<a class=\"AnchorOnly\"" + EntryAnchorName + ">";
        EntrySuffix = "</a>";
      }
      else
      {
        EntryPrefix = "";
        EntrySuffix = "";
      }

      this.mHTMLSegment.fAppend("<p class=l" + (this.mIterator.mPositionStack.length - this.mIterator.mStackOffset) + "><nobr>" + EntryPrefix + Entry.mText + EntrySuffix + "</nobr></p>\n");
    }
  }

  return (this.mHTMLSegment.fSize() > 0);
}

function  WWHIndex_GetHTMLSegment()
{
  return this.mHTMLSegment.fGetBuffer();
}

function  WWHIndex_EndHTMLSegments()
{
  var  HTML = "";


  if ((this.mOptions.mThreshold <= 0) ||
      (this.mEntryCount < this.mOptions.mThreshold))
  {
    ;  // Nothing to do
  }
  else  // Display sections of the Index rather than the whole thing
  {
    // Display section selection
    //
// HACK BEN
//    HTML += "<p>&nbsp;</p>\n";
//    HTML += this.mSectionCache[this.mSectionIndex];
  }

  return HTML;
}

function  WWHIndex_HoverTextTranslate(ParamEntryInfo)
{
  var  Entry;


  // Locate specified entry
  //
  Entry = this.fGetEntry(ParamEntryInfo);

  return Entry.mText;
}

function  WWHIndex_HoverTextFormat(ParamWidth,
                                   ParamTextID,
                                   ParamText)
{
  var  FormattedText   = "";
  var  ForegroundColor = WWHFrame.WWHJavaScript.mSettings.mHoverText.mForegroundColor;
  var  BackgroundColor = WWHFrame.WWHJavaScript.mSettings.mHoverText.mBackgroundColor;  
  var  BorderColor     = WWHFrame.WWHJavaScript.mSettings.mHoverText.mBorderColor;
  var  ImageDir        = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images";
  var  ReqSpacer1w2h   = "<img src=\"" + ImageDir + "/spc1w2h.gif\" width=1 height=2>";
  var  ReqSpacer2w1h   = "<img src=\"" + ImageDir + "/spc2w1h.gif\" width=2 height=1>";
  var  ReqSpacer1w7h   = "<img src=\"" + ImageDir + "/spc1w7h.gif\" width=1 height=7>";
  var  ReqSpacer5w1h   = "<img src=\"" + ImageDir + "/spc5w1h.gif\" width=5 height=1>";
  var  Spacer1w2h      = ReqSpacer1w2h;
  var  Spacer2w1h      = ReqSpacer2w1h;
  var  Spacer1w7h      = ReqSpacer1w7h;
  var  Spacer5w1h      = ReqSpacer5w1h;


  // Netscape 6.x (Mozilla) renders table cells with graphics
  // incorrectly inside of <div> tags that are rewritten on the fly
  //
  if (WWHFrame.WWHBrowserInfo.mBrowser == 4)  // Shorthand for Netscape 6.x (Mozilla)
  {
    Spacer1w2h = "";
    Spacer2w1h = "";
    Spacer1w7h = "";
    Spacer5w1h = "";
  }

  FormattedText += "<table width=\"" + ParamWidth + "\" border=0 cellspacing=0 cellpadding=0 bgcolor=\"" + BackgroundColor + "\">";
  FormattedText += " <tr>";
  FormattedText += "  <td height=2 colspan=5 bgcolor=\"" + BorderColor + "\">" + Spacer1w2h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=7 colspan=3>" + Spacer1w7h + "</td>";
  FormattedText += "  <td height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += "  <td>" + ReqSpacer5w1h + "</td>";
  FormattedText += "  <td width=\"100%\" id=\"" + ParamTextID + "\" style=\"color: " + ForegroundColor + " ; " + WWHFrame.WWHJavaScript.mSettings.mHoverText.mFontStyle + "\">" + ParamText + "</td>";
  FormattedText += "  <td>" + ReqSpacer5w1h + "</td>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=7 colspan=3>" + Spacer1w7h + "</td>";
  FormattedText += "  <td height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=2 colspan=5 bgcolor=\"" + BorderColor + "\">" + Spacer1w2h + "</td>";
  FormattedText += " </tr>";
  FormattedText += "</table>";

  return FormattedText;
}

function  WWHIndex_GetPopupAction(ParamEntryInfo)
{
  var  PopupAction = "";


  if (WWHFrame.WWHJavaScript.mSettings.mHoverText.mbEnabled)
  {
    PopupAction += " onMouseOver=\"fS('" + ParamEntryInfo + "', " + this.mEventString + ");\"";
    PopupAction += " onMouseOut=\"fH();\"";
  }

  return PopupAction;
}

function  WWHIndex_ThresholdExceeded()
{
  var  bThresholdExceeded;


  if ((this.mOptions.mThreshold > 0) &&
      (this.mEntryCount > this.mOptions.mThreshold))
  {
    bThresholdExceeded = true;
  }
  else
  {
    bThresholdExceeded = false;
  }

  return bThresholdExceeded;
}

function  WWHIndex_GetSectionNavigation(ParamSection)
{
  var  SectionNavHTML = "";
  var  SectionArray;
  var  MaxIndex;
  var  Index;


  SectionNavHTML += "<p class=\"navigation\">";

  // Calculate section selection
  //
  SectionArray = this.mTopEntry.mChildrenSortArray;
  for (MaxIndex = SectionArray.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    // Add spacers if necessary
    //
    if (Index > 0)
    {
      SectionNavHTML += this.mOptions.mSeperator;
    }

    // Display section with or without link as necessary
    //
    if (Index == this.mSectionIndex)  // Currently being displayed
    {
      SectionNavHTML += SectionArray[Index].mText;
    }
    else if ((SectionArray[Index].mChildren == null) &&         // Always display group
             (SectionArray[Index].mChildrenSortArray == null))  // SortArray null before sort, hash null after
    {
      SectionNavHTML += "<font color=\"" + WWHFrame.WWHJavaScript.mSettings.mIndex.mNavigationDisabledColor + "\">" + SectionArray[Index].mText + "</font>";
    }
    else
    {
      SectionNavHTML += "<a class=\"navigation\" href=\"javascript:fN(" + Index + ");\">" + SectionArray[Index].mText + "</a>";
    }
  }

  SectionNavHTML += "</p>";

  return SectionNavHTML;
}

function  WWHIndex_ChangeSection(ParamSectionIndex)
{
  // Set section
  //
  this.mSectionIndex = ParamSectionIndex;

  // Reload panel
  //
  WWHFrame.WWHJavaScript.mPanels.fClearScrollPosition();
  WWHFrame.WWHJavaScript.mPanels.fDisplayPanel();
}

function  WWHIndex_SelectionListHeadHTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  Level;


  HTML.fAppend("<style type=\"text/css\">\n");
  HTML.fAppend(" <!--\n");
  HTML.fAppend("  a { text-decoration: none ;\n");
  HTML.fAppend("      color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mEnabledColor + " }\n");
  HTML.fAppend("  p { margin-top: 1pt ;\n");
  HTML.fAppend("      margin-bottom: 1pt ;\n");
  HTML.fAppend("      color: " + WWHFrame.WWHJavaScript.mSettings.mIndex.mDisabledColor + " ;\n");
  HTML.fAppend("      " + WWHFrame.WWHJavaScript.mSettings.mIndex.mFontStyle + " }\n");
  for (Level = 1 ; Level < 3 ; Level++)
  {
    HTML.fAppend("  p.l" + Level + " { margin-left: " + (WWHFrame.WWHJavaScript.mSettings.mIndex.mIndent * Level) + "pt }\n");
  }
  HTML.fAppend("  h2 { " + WWHFrame.WWHJavaScript.mSettings.mIndex.mFontStyle + " }\n");
  HTML.fAppend(" -->\n");
  HTML.fAppend("</style>\n");

  return HTML.fGetBuffer();
}

function  WWHIndex_SelectionListBodyHTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  BookList = WWHFrame.WWHHelp.mBooks.mBookList;
  var  MaxBookIndex;
  var  BookIndex;
  var  BookListEntry;
  var  LinkArray;
  var  MaxLinkIndex;
  var  LinkIndex;
  var  Parts;
  var  PrevLinkFileIndex;
  var  LinkFileIndex;
  var  LinkAnchor;
  var  DocumentURL;


  // Display multiple entry message
  //
  HTML.fAppend("<h2>");
  HTML.fAppend(WWHFrame.WWHJavaScript.mMessages.mIndexSelectMessage1 + " ");
  HTML.fAppend(WWHFrame.WWHJavaScript.mMessages.mIndexSelectMessage2);
  HTML.fAppend("</h2>\n");

  // Display text of entry clicked
  //
  HTML.fAppend("<p><b>" + this.mClickedEntry.mText + "</b></p>\n");

  // Display each book's link for this entry
  //
  for (MaxBookIndex = BookList.length, BookIndex = 0 ; BookIndex < MaxBookIndex ; BookIndex++)
  {
    if (typeof this.mClickedEntry.mBookLinks[BookIndex] != "undefined")
    {
      BookListEntry = BookList[BookIndex];

      // Write the book's title
      //
      HTML.fAppend("<p>&nbsp;</p>\n");
      HTML.fAppend("<p class=l1><nobr><b>" + BookListEntry.mTitle + "</b>");

      // Sort link array to group files with anchors
      //
      // Use for loop to copy entries to workaround bug/problem in IE 5.0 on Windows
      //
      LinkArray = new Array();
      for (MaxLinkIndex = this.mClickedEntry.mBookLinks[BookIndex].length, LinkIndex = 0 ; LinkIndex < MaxLinkIndex ; LinkIndex++)
      {
        LinkArray[LinkIndex] = this.mClickedEntry.mBookLinks[BookIndex][LinkIndex];
      }
      LinkArray = LinkArray.sort();

      // Now display file links
      //
      PrevLinkFileIndex = null;
      for (MaxLinkIndex = LinkArray.length, LinkIndex = 0 ; LinkIndex < MaxLinkIndex ; LinkIndex++)
      {
        // Determine link file index and anchor
        //
        Parts = LinkArray[LinkIndex].split("#");
        LinkFileIndex = parseInt(Parts[0]);
        if (Parts.length > 1)
        {
          LinkAnchor = "#" + Parts[1];
        }
        else
        {
          LinkAnchor = "";
        }

        // Determine if all links for a single document have been processed
        //
        if ((PrevLinkFileIndex == null) ||
            (LinkFileIndex != PrevLinkFileIndex))
        {
          HTML.fAppend("</nobr></p>\n");

          // Build up absolute link URL
          //
          DocumentURL = WWHFrame.WWHHelp.mBaseURL + BookListEntry.mDirectory + BookListEntry.mFiles.fFileIndexToHREF(LinkFileIndex) + LinkAnchor;
          DocumentURL = WWHStringUtilities_RestoreEscapedSpaces(DocumentURL);
          DocumentURL = WWHStringUtilities_EscapeURLForJavaScriptAnchor(DocumentURL);

          HTML.fAppend("<p class=l2><nobr>");
          HTML.fAppend("<a href=\"javascript:WWHFrame.WWHIndex.fDisplayLink('" + DocumentURL + "');\">");
          HTML.fAppend(BookListEntry.mFiles.fFileIndexToTitle(LinkFileIndex) + "</a>");
        }
        else
        {
          // Build up absolute link URL
          //
          DocumentURL = WWHFrame.WWHHelp.mBaseURL + BookListEntry.mDirectory + BookListEntry.mFiles.fFileIndexToHREF(LinkFileIndex) + LinkAnchor;
          DocumentURL = WWHStringUtilities_RestoreEscapedSpaces(DocumentURL);
          DocumentURL = WWHStringUtilities_EscapeURLForJavaScriptAnchor(DocumentURL);

          HTML.fAppend(",&nbsp;");
          HTML.fAppend("<a href=\"javascript:WWHFrame.WWHIndex.fDisplayLink('" + DocumentURL + "');\">");
          HTML.fAppend((LinkIndex + 1) + "</a>");
        }

        PrevLinkFileIndex = LinkFileIndex;
      }

      HTML.fAppend("</nobr></p>\n");
    }
  }

  return HTML.fGetBuffer();
}

function  WWHIndex_DisplayLink(ParamURL)
{
  WWHFrame.WWHHelp.fSetDocumentHREF(ParamURL, false);
}

function  WWHIndex_GetEntry(ParamEntryInfo)
{
  var  Entry = null;
  var  EntryInfoParts;
  var  MaxIndex;
  var  Index;


  // Locate specified entry
  //
  Entry = this.mTopEntry;
  EntryInfoParts = ParamEntryInfo.split(":");
  for (MaxIndex = EntryInfoParts.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    Entry = Entry.mChildrenSortArray[EntryInfoParts[Index]];
  }

  return Entry;
}

function  WWHIndex_ClickedEntry(ParamEntryInfo)
{
  var  Entry;
  var  BookCount;
  var  BookIndex;
  var  BookListEntry;
  var  Parts;
  var  LinkFileIndex;
  var  LinkAnchor;
  var  DocumentURL;


  // Locate specified entry
  //
  Entry = this.fGetEntry(ParamEntryInfo);

  // Display target document or selection list
  //
  BookCount = 0;
  for (BookIndex in Entry.mBookLinks)
  {
    BookCount++;
  }

  // See if this is a single entry
  //
  if ((BookCount == 1) &&
      (Entry.mBookLinks[BookIndex].length == 1))
  {
    BookListEntry = WWHFrame.WWHHelp.mBooks.mBookList[BookIndex];

    // Determine link file index and anchor
    //
    Parts = Entry.mBookLinks[BookIndex][0].split("#");
    LinkFileIndex = parseInt(Parts[0]);
    if (Parts.length > 1)
    {
      LinkAnchor = "#" + Parts[1];
    }
    else
    {
      LinkAnchor = "";
    }

    // Set Document
    //
    DocumentURL = WWHFrame.WWHHelp.mBaseURL + BookListEntry.mDirectory + BookListEntry.mFiles.fFileIndexToHREF(LinkFileIndex) + LinkAnchor;
  }
  else
  {
    // Display selection list
    //
    this.mClickedEntry = Entry;
    DocumentURL = WWHFrame.WWHHelp.mBaseURL + "wwhelp/wwhimpl/js/html/indexsel.htm";
  }

  this.fDisplayLink(DocumentURL);
}

function  WWHIndex_ClickedSeeAlsoEntry(ParamEntryInfo)
{
  var  Entry;
  var  TargetSectionIndex;
  var  MaxIndex;
  var  Index;


  // Locate specified entry
  //
  Entry = this.fGetEntry(ParamEntryInfo);

  // Confirm entry has target information
  //
  if ((typeof Entry.mSeeAlsoTargetName == "string") &&
      (typeof Entry.mSeeAlsoTargetEntryKey == "string"))
  {
    TargetSectionIndex = null;

    // Determine if we need to jump to another page
    //
    if (this.fThresholdExceeded())
    {
      MaxIndex = this.mTopEntry.mChildrenSortArray.length;
      Index = 0;
      while ((TargetSectionIndex == null) &&
             (Index < MaxIndex))
      {
        if (this.mTopEntry.mChildrenSortArray[Index].mText == Entry.mSeeAlsoTargetEntryKey)
        {
          TargetSectionIndex = Index;
        }

        Index++;
      }
    }

    // Set target entry
    //
    this.mPanelAnchor = Entry.mSeeAlsoTargetName;

    if (((TargetSectionIndex != null) &&
         (this.mSectionIndex != null)) &&
        (TargetSectionIndex != this.mSectionIndex))
    {
      // Need to switch to proper section
      //
      this.fChangeSection(TargetSectionIndex);
    }
    else
    {
      // We're on the right page, so just jump to the correct entry
      //
      WWHFrame.WWHJavaScript.mPanels.fJumpToAnchor();
    }
  }
}

function  WWHIndexIterator_Object()
{
  this.mIteratorScope      = null;
  this.mEntry              = null;
  this.mParentStack        = new Array();
  this.mPositionStack      = new Array();
  this.mbThresholdExceeded = false;
  this.mStackOffset        = 1;

  this.fReset   = WWHIndexIterator_Reset;
  this.fAdvance = WWHIndexIterator_Advance;
}

function  WWHIndexIterator_Reset(ParamIndex,
                                 bParamThresholdExceeded)
{
  if (ParamIndex == null)  // Iterate buckets as well!
  {
    this.mIteratorScope = WWHFrame.WWHIndex.mTopEntry;
  }
  else
  {
    this.mIteratorScope = WWHFrame.WWHIndex.mTopEntry.mChildrenSortArray[ParamIndex];
  }
  this.mEntry                = this.mIteratorScope;
  this.mParentStack.length   = 0;
  this.mPositionStack.length = 0;
  this.mbThresholdExceeded   = bParamThresholdExceeded;
  this.mStackOffset          = 1;
}

function  WWHIndexIterator_Advance()
{
  // Advance to the next visible entry
  //
  if (this.mEntry != null)
  {
    // Check for children
    //
    if (this.mEntry.mChildren != null)
    {
      // Determine sort order if necessary
      //
      if (this.mEntry.mChildrenSortArray == null)
      {
        WWHIndexEntry_SortChildren(this.mEntry);
      }
    }

    // Process children
    //
    if (this.mEntry.mChildrenSortArray != null)
    {
      // Modify stack offset as needed
      //
      if (( ! this.mbThresholdExceeded) &&
          (this.mEntry.mbBucket) &&
          ( ! this.mEntry.mbShow))
      {
        this.mStackOffset += 1;
      }

      this.mParentStack[this.mParentStack.length] = this.mEntry;
      this.mPositionStack[this.mPositionStack.length] = 0;
      this.mEntry = this.mEntry.mChildrenSortArray[0];
    }
    // If we've reached the iterator scope, we're done
    //
    else if (this.mEntry == this.mIteratorScope)
    {
      this.mEntry = null;
    }
    else
    {
      var  ParentEntry;
      var  StackTop;


      ParentEntry = this.mParentStack[this.mParentStack.length - 1];
      this.mEntry = null;

      // Find next child of parent entry
      //
      while (ParentEntry != null)
      {
        // Increment position
        //
        StackTop = this.mPositionStack.length - 1;
        this.mPositionStack[StackTop]++;

        // Confirm this is a valid entry
        //
        if (this.mPositionStack[StackTop] < ParentEntry.mChildrenSortArray.length)
        {
          // Return the parent's next child
          //
          this.mEntry = ParentEntry.mChildrenSortArray[this.mPositionStack[StackTop]];

          // Signal break from loop
          //
          ParentEntry = null;
        }
        else
        {
          // Last child of parent, try up a level
          //
          if (ParentEntry == this.mIteratorScope)
          {
            ParentEntry = null;
          }
          else
          {
            // Restore stack offset as needed
            //
            if (( ! this.mbThresholdExceeded) &&
                (ParentEntry.mbBucket) &&
                ( ! ParentEntry.mbShow))
            {
              this.mStackOffset -= 1;
            }

            ParentEntry = ParentEntry.mParent;
            this.mParentStack.length--;
            this.mPositionStack.length--;

            ParentEntry = this.mParentStack[this.mParentStack.length - 1];
          }
        }
      }
    }
  }

  return (this.mEntry != null);
}

function  WWHIndexTopEntry_Object()
{
  this.mLevel             = -1;
  this.mChildren          = null;
  this.mChildrenSortArray = null;

  this.fGetGroup = WWHIndexTopEntry_GetGroup;
  this.fAddEntry = WWHIndexTopEntry_AddEntry;
  this.fA        = WWHIndexTopEntry_AddEntry;
}

function  WWHIndexTopEntry_GetGroup(ParamGroupTitle)
{
  var  GroupEntry;


  // See if this object has any children
  //
  if (this.mChildren == null)
  {
    this.mChildren = new WWHIndexEntryHash_Object();
  }

  // Access entry, creating it if it doesn't exist
  //
  GroupEntry = this.mChildren[ParamGroupTitle + "~"];
  if (typeof GroupEntry == "undefined")
  {
    GroupEntry = new WWHIndexEntry_Object(0, 0, WWHStringUtilities_EscapeHTML(ParamGroupTitle), null, null);
    GroupEntry.mbGroup = true;
    this.mChildren[ParamGroupTitle + "~"] = GroupEntry;
  }

  return GroupEntry;
}

function  WWHIndexTopEntry_AddEntry(ParamText,
                                    ParamLinks,
                                    ParamSortKey,
                                    ParamSeeAlso)
{
  var  FirstChar = "";
  var  GroupTitle;
  var  GroupEntry;
  var  BucketEntry;
  var  NewEntry;


  // Grab first character
  //
  if ((typeof ParamSortKey == "string") &&
      (ParamSortKey.length > 0))
  {
    FirstChar = ParamSortKey.substring(0, 1);

    // Check for encoded characters
    //
    if (FirstChar == "&")
    {
      FirstChar = WWHStringUtilities_UnescapeHTML(ParamSortKey);

      FirstChar = FirstChar.substring(0, 1);
    }

    // toUpperCase is conditional to handle numbers
    //
    FirstChar = (typeof FirstChar.toUpperCase == "function") ? FirstChar.toUpperCase() : FirstChar;
  }
  else if (ParamText.length > 0)
  {
    FirstChar = ParamText.substring(0, 1);

    // Check for encoded characters
    //
    if (FirstChar == "&")
    {
      FirstChar = WWHStringUtilities_UnescapeHTML(ParamText);

      FirstChar = FirstChar.substring(0, 1);
    }

    // toUpperCase is conditional to handle numbers
    //
    FirstChar = (typeof FirstChar.toUpperCase == "function") ? FirstChar.toUpperCase() : FirstChar;
  }

  // Check for a group
  //
  GroupTitle = WWHFrame.WWHIndex.mOptions.mGroupMapping[FirstChar + "~"];
  if (typeof GroupTitle == "undefined")
  {
    GroupTitle = "";  // Put in required grouping
  }

  // Access group
  //
  GroupEntry = this.fGetGroup(GroupTitle);

  // Access bucket
  //
  BucketEntry = GroupEntry.fGetBucket(FirstChar, WWHFrame.WWHIndex.mOptions.mGroupInfoHash[GroupTitle + "~"].mbShowBuckets);

  // Add entry
  //
  NewEntry = BucketEntry.fAddEntry(ParamText, ParamLinks, ParamSortKey, ParamSeeAlso);

  return NewEntry;
}

function  WWHIndexEntry_Object(ParamBookIndex,
                               ParamLevel,
                               ParamText,
                               ParamLinks,
                               ParamSeeAlso)
{
  this.mText              = ParamText;
  this.mBookLinks         = null;
  this.mLevel             = ParamLevel;
  this.mChildren          = null;
  this.mChildrenSortArray = null;

  if (typeof ParamSeeAlso == "string")
  {
    this.mSeeAlso = ParamSeeAlso;
  }

  this.fGetBucket = WWHIndexEntry_GetBucket;
  this.fAddEntry  = WWHIndexEntry_AddEntry;
  this.fA         = WWHIndexEntry_AddEntry;

  // Bump MaxLevel if exceeded
  //
  if (WWHFrame.WWHIndex.mMaxLevel < this.mLevel)
  {
    WWHFrame.WWHIndex.mMaxLevel = this.mLevel;
  }

  // Bump entry count
  //
  WWHFrame.WWHIndex.mEntryCount++;

  // Add links
  //
  if ((typeof ParamLinks != "undefined") &&
      (ParamLinks != null))
  {
    this.mBookLinks = new WWHIndexEntryBookHash_Object();
    this.mBookLinks[ParamBookIndex] = ParamLinks;
  }
}

function  WWHIndexEntry_GetBucket(ParamText,
                                  bParamShow)
{
  var  BucketEntry;


  // See if this object has any children
  //
  if (this.mChildren == null)
  {
    this.mChildren = new WWHIndexEntryHash_Object();
  }

  // Access entry, creating it if it doesn't exist
  //
  BucketEntry = this.mChildren[ParamText + "~"];
  if (typeof BucketEntry == "undefined")
  {
    var  Level;


    // Keep level the same if buckets not visible
    //
    if (bParamShow)
    {
      Level = this.mLevel + 1;
    }
    else
    {
      Level = this.mLevel;
    }

    BucketEntry = new WWHIndexEntry_Object(0, Level, WWHStringUtilities_EscapeHTML(ParamText), null, null);
    BucketEntry.mbBucket            = true;
    BucketEntry.mbShow              = bParamShow;
    this.mChildren[ParamText + "~"] = BucketEntry;
  }

  return BucketEntry;
}

function  WWHIndexEntry_AddEntry(ParamText,
                                 ParamLinks,
                                 ParamSortKey,
                                 ParamSeeAlso)
{
  var  HashKey;
  var  ChildEntry;
  var  BookIndex = WWHFrame.WWHIndex.mInitIndex;
  var  Links     = null;


  // Set links if entries exist
  //
  if ((typeof ParamLinks != "undefined") &&
      (ParamLinks != null) &&
      (ParamLinks.length > 0))
  {
    Links = ParamLinks;
  }

  // See if this object has any children
  //
  if (this.mChildren == null)
  {
    this.mChildren = new WWHIndexEntryHash_Object();
  }

  // Access entry, creating it if it doesn't exist
  //
  HashKey = ((typeof ParamSortKey == "string") ? ParamSortKey : "") + ParamText;
  ChildEntry = this.mChildren[HashKey + "~"];
  if (typeof ChildEntry == "undefined")
  {
    ChildEntry = new WWHIndexEntry_Object(BookIndex, this.mLevel + 1, ParamText, Links, ParamSeeAlso);
    this.mChildren[HashKey + "~"] = ChildEntry;

    // Add entry to see also collection if it is a see also entry
    //
    if (typeof ParamSeeAlso == "string")
    {
      WWHFrame.WWHIndex.fAddSeeAlsoEntry(ChildEntry);
    }
  }
  else  // Child entry exists, update with new information
  {
    // Add book links
    //
    if (Links != null)
    {
      if (ChildEntry.mBookLinks == null)
      {
        ChildEntry.mBookLinks = new WWHIndexEntryBookHash_Object();
      }

      if (typeof ChildEntry.mBookLinks[BookIndex] == "undefined")
      {
        ChildEntry.mBookLinks[BookIndex] = Links;
      }
      else  // Append new links
      {
        var  BookLinks = ChildEntry.mBookLinks[BookIndex];
        var  MaxIndex;
        var  Index;


        for (MaxIndex = Links.length, Index = 0 ; Index < MaxIndex ; Index++)
        {
          BookLinks[BookLinks.length] = Links[Index];
        }
      }
    }
  }

  return ChildEntry;
}

function  WWHIndexEntry_SortChildren(ParamEntry)
{
  var  SortedArray;


  // Sort top entry according to group order
  //
  if (ParamEntry == WWHFrame.WWHIndex.mTopEntry)
  {
    var  GroupList = WWHFrame.WWHIndex.mOptions.mGroupList;
    var  MaxGroupIndex;
    var  GroupIndex;
    var  GroupEntry;


    // Accumulate entries in sort order
    //
    SortedArray = new Array();
    for (MaxGroupIndex = GroupList.length, GroupIndex = 0 ; GroupIndex < MaxGroupIndex ; GroupIndex++)
    {
      GroupEntry = ParamEntry.mChildren[GroupList[GroupIndex] + "~"];
      if (typeof GroupEntry != "undefined")
      {
        if (GroupList[GroupIndex].length == 0)  // Required group list found
        {
          // Sort required group entries
          //
          WWHIndexEntry_SortChildren(GroupEntry);

          // Add children to sorted array
          //
          for (MaxIndex = GroupEntry.mChildrenSortArray.length, Index = 0 ; Index < MaxIndex ; Index++)
          {
            SortedArray[SortedArray.length] = GroupEntry.mChildrenSortArray[Index];

            // Mark first required group entry
            //
            if (Index == 0)
            {
              SortedArray[SortedArray.length - 1].mbFirstRequiredEntry = true;
            }
          }
        }
        else
        {
          SortedArray[SortedArray.length] = GroupEntry;
        }
      }
    }
  }
  else
  {
    var  UnsortedArray;
    var  HashKey;
    var  SortKey;
    var  MaxIndex;
    var  Index;


    // Accumulate hash keys
    //
    UnsortedArray = new Array();
    for (HashKey in ParamEntry.mChildren)
    {
      // toUpperCase is conditional to handle numbers
      //
      SortKey = ((typeof HashKey.toUpperCase == "function") ? HashKey.toUpperCase() : HashKey) + "\n" + HashKey;
      UnsortedArray[UnsortedArray.length] = SortKey;
    }

    // Sort array
    //
    SortedArray = UnsortedArray.sort();

    // Replace sort keys with entries
    //
    for (MaxIndex = SortedArray.length, Index = 0 ; Index < MaxIndex ; Index++)
    {
      SortKey = SortedArray[Index];
      HashKey = SortKey.substring(SortKey.indexOf("\n") + 1, SortKey.length)

      SortedArray[Index] = ParamEntry.mChildren[HashKey];
    }
  }

  // Set children sort array
  // Clear hash table as it is no longer needed
  //
  ParamEntry.mChildrenSortArray = SortedArray;
  ParamEntry.mChildren = null;
}

function  WWHIndexEntryHash_Object()
{
}

function  WWHIndexEntryBookHash_Object()
{
}

function  WWHSectionCache_Object()
{
}

function  WWHIndexOptions_Object()
{
  this.mThreshold     = 0;
  this.mGroupList     = new Array();
  this.mGroupInfoHash = new WWHIndexOptionsGroupInfoHash_Object();
  this.mGroupMapping  = new WWHIndexOptionsGroupMapping_Object();

  this.fSetThreshold = WWHIndexOptions_SetThreshold;
  this.fSetSeperator = WWHIndexOptions_SetSeperator;
  this.fGroup        = WWHIndexOptions_Group;
}

function  WWHIndexOptions_SetThreshold(ParamThreshold)
{
  this.mThreshold = ParamThreshold;
}

function  WWHIndexOptions_SetSeperator(ParamSeperator)
{
  this.mSeperator = ParamSeperator;
}

function  WWHIndexOptions_Group(ParamGroupTitle,
                                bParamAlwaysDisplay,
                                bParamShowBuckets,
                                ParamGroupEntries)
{
  var  GroupEntries;
  var  MaxIndex;
  var  Index;


  // Record display order
  //
  this.mGroupList[this.mGroupList.length] = ParamGroupTitle;

  // Record group info
  //
  if (ParamGroupTitle != "")
  {
    this.mGroupInfoHash[ParamGroupTitle + "~"] = new WWHIndexOptionsGroupInfo_Object(bParamAlwaysDisplay, bParamShowBuckets);
  }
  else  // Handle required group
  {
    this.mGroupInfoHash[ParamGroupTitle + "~"] = new WWHIndexOptionsGroupInfo_Object(true, bParamShowBuckets);
  }

  // Record entries for each group
  //
  GroupEntries = ParamGroupEntries.split("");
  for (MaxIndex = GroupEntries.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    if ((ParamGroupTitle.length == 0) ||                                        // Required items always have precendence
        (typeof this.mGroupMapping[GroupEntries[Index] + "~"] == "undefined"))  // Otherwise, prevent reseting entries
    {
      this.mGroupMapping[GroupEntries[Index] + "~"] = ParamGroupTitle;
    }
  }
}

function  WWHIndexOptionsGroupInfoHash_Object()
{
}

function  WWHIndexOptionsGroupInfo_Object(bParamAlwaysDisplay,
                                          bParamShowBuckets)
{
  this.mbAlwaysDisplay = bParamAlwaysDisplay;
  this.mbShowBuckets   = bParamShowBuckets;
}

function  WWHIndexOptionsGroupMapping_Object()
{
}
