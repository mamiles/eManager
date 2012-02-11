// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHSearch_Object()
{
  this.mbPanelInitialized      = false;
  this.mPanelAnchor            = null;
  this.mPanelTabTitle          = WWHFrame.WWHJavaScript.mMessages.mTabsSearchLabel;
  this.mbBookSearchInfoLoaded  = false;
  this.mInitIndex              = 0;
  this.mBookSearchInfoList     = new Array();
  this.mSavedSearchWords       = "";
  this.mSavedSearchScope       = 0;
  this.mSearchWordList         = new Array();
  this.mSearchWordRegExpList   = new Array();
  this.mBookIndex              = 0;
  this.mBookMatchesList        = new Array();
  this.mCombinedResults        = new WWHSearchResults_Object();

  this.fInitHeadHTML           = WWHSearch_InitHeadHTML;
  this.fInitBodyHTML           = WWHSearch_InitBodyHTML;
  this.fInitLoadBookSearchInfo = WWHSearch_InitLoadBookSearchInfo;
  this.fHeadHTML               = WWHSearch_HeadHTML;
  this.fStartHTMLSegments      = WWHSearch_StartHTMLSegments;
  this.fAdvanceHTMLSegment     = WWHSearch_AdvanceHTMLSegment;
  this.fGetHTMLSegment         = WWHSearch_GetHTMLSegment;
  this.fEndHTMLSegments        = WWHSearch_EndHTMLSegments;
  this.fHoverTextTranslate     = WWHSearch_HoverTextTranslate;
  this.fHoverTextFormat        = WWHSearch_HoverTextFormat;
  this.fDisplaySearchForm      = WWHSearch_DisplaySearchForm;
  this.fSubmit                 = WWHSearch_Submit;
  this.fSetSearchWords         = WWHSearch_SetSearchWords;
  this.fCheckForMatch          = WWHSearch_CheckForMatch;
  this.fSearchComplete         = WWHSearch_SearchComplete;
  this.fCombineResults         = WWHSearch_CombineResults;
  this.fShowEntry              = WWHSearch_ShowEntry;
}

function  WWHSearch_InitHeadHTML()
{
  var  InitHeadHTML = "";


  return InitHeadHTML;
}

function  WWHSearch_InitBodyHTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  BookList = WWHFrame.WWHHelp.mBooks.mBookList;
  var  MaxIndex;
  var  Index;
  var  MaxBookIndex;
  var  BookIndex;
  var  BookDirectory;


  if ( ! this.mbBookSearchInfoLoaded)
  {
    // Display initializing message
    //
    HTML.fAppend("<h2>" + WWHFrame.WWHJavaScript.mMessages.mInitializingMessage + "</h2>\n");

    // Load search info
    //
    this.mInitIndex = 0;
    for (MaxIndex = BookList.length, Index = 0 ; Index < MaxIndex ; Index++)
    {
      // Reference search info
      //
      HTML.fAppend("<script language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + WWHStringUtilities_RestoreEscapedSpaces(BookList[Index].mDirectory) + "wwhdata/js/search.js\"></script>\n");

      // Load search info for current book
      //
      HTML.fAppend("<script language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/scripts/search1s.js\"></script>\n");
    }
  }
  else  // Perform search
  {
    // Display searching message
    //
    HTML.fAppend("<h2>" + WWHFrame.WWHJavaScript.mMessages.mSearchSearchingMessage + "</h2>\n");

    // Handle single book search
    //
    if (this.mSavedSearchScope > 0)
    {
      MaxBookIndex = this.mSavedSearchScope;
      BookIndex    = this.mSavedSearchScope - 1;
    }
    else
    {
      MaxBookIndex = BookList.length;
      BookIndex    = 0;
    }

    // Generate search actions
    //
    this.mBookIndex = BookIndex;
    for ( ; BookIndex < MaxBookIndex ; BookIndex++)
    {
      BookDirectory = BookList[BookIndex].mDirectory;

      for (MaxIndex = this.mBookSearchInfoList[BookIndex].mSearchFileCount, Index = 0 ; Index < MaxIndex ; Index++)
      {
        HTML.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + WWHStringUtilities_RestoreEscapedSpaces(BookDirectory) + "wwhdata/js/search/search" + Index + ".js\"></script>\n");
        HTML.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/scripts/search2s.js\"></script>\n");
      }

      HTML.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/scripts/search3s.js\"></script>\n");
    }

    HTML.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/scripts/search4s.js\"></script>\n");
  }

  return HTML.fGetBuffer();
}

function  WWHSearch_InitLoadBookSearchInfo(ParamSearchFileCount,
                                           ParamMinimumWordLength,
                                           ParamSearchSkipWordsFunc)
{
  // Load book search info
  //
  this.mBookSearchInfoList[this.mInitIndex] = new WWHBookSearchInfo_Object(ParamSearchFileCount, ParamMinimumWordLength);
  ParamSearchSkipWordsFunc(this.mBookSearchInfoList[this.mInitIndex]);

  // Create match objects for each book
  //
  this.mBookMatchesList[this.mBookMatchesList.length] = new WWHSearchBookMatches_Object();

  // Increment init book index
  //
  this.mInitIndex++;

  // Mark initialized if done
  //
  if (this.mInitIndex == WWHFrame.WWHHelp.mBooks.mBookList.length)
  {
    this.mbPanelInitialized = true;
    this.mbBookSearchInfoLoaded = true;
  }
}

function  WWHSearch_HeadHTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  Settings = WWHFrame.WWHJavaScript.mSettings.mSearch;


  // Generate style section
  //
  HTML.fAppend("<style type=\"text/css\">\n");
  HTML.fAppend(" <!--\n");
  HTML.fAppend("  a  {\n");
  HTML.fAppend("       text-decoration: none;\n");
  HTML.fAppend("       color: " + Settings.mEnabledColor + ";\n");
  HTML.fAppend("     }\n");
  HTML.fAppend("  p  {\n");
  HTML.fAppend("       margin-top: 1pt;\n");
  HTML.fAppend("       margin-bottom: 1pt;\n");
  HTML.fAppend("       " + Settings.mFontStyle + ";\n");
  HTML.fAppend("     }\n");
  HTML.fAppend("  ol {\n");
  HTML.fAppend("       margin-top: 1pt;\n");
  HTML.fAppend("       margin-bottom: 1pt;\n");
  if (Settings.mbShowRank)
  {
    HTML.fAppend("       " + Settings.mFontStyle + ";\n");
  }
  else
  {
    HTML.fAppend("       list-style: none;\n");
  }
  HTML.fAppend("     }\n");
  HTML.fAppend("  li {\n");
  HTML.fAppend("       margin-top: 2pt;\n");
  HTML.fAppend("       margin-bottom: 0pt;\n");
  HTML.fAppend("       " + Settings.mFontStyle + ";\n");
  HTML.fAppend("     }\n");
  HTML.fAppend(" -->\n");
  HTML.fAppend("</style>\n");

  return HTML.fGetBuffer();
}

function  WWHSearch_StartHTMLSegments()
{
  var  HTML = new WWHStringBuffer_Object();


  if (this.mbPanelInitialized)
  {
    HTML.fAppend(this.fDisplaySearchForm());

    if (this.mSavedSearchWords.length == 0)
    {
      HTML.fAppend("<h3>" + WWHFrame.WWHJavaScript.mMessages.mSearchDefaultMessage + "</h3>\n");
    }
    else if ((typeof this.mCombinedResults.mEntries != "undefined") &&
             (this.mCombinedResults.mEntries.length > 0))
    {
      this.mCombinedResults.fDisplayReset();
    }
    else
    {
      HTML.fAppend("<h3>" + WWHFrame.WWHJavaScript.mMessages.mSearchNothingFoundMessage + "</h3>\n");
    }
  }

  // Define accessor functions to reduce file size
  //
  HTML.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\">\n");
  HTML.fAppend(" <!--\n");
  HTML.fAppend("  function  fC(ParamEntryID)\n");
  HTML.fAppend("  {\n");
  HTML.fAppend("    WWHFrame.WWHSearch.fShowEntry(ParamEntryID);\n");
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
  HTML.fAppend(" // -->\n");
  HTML.fAppend("</script>\n");

  return HTML.fGetBuffer();
}

function  WWHSearch_AdvanceHTMLSegment()
{
  var  bSegmentCreated = false;


  if (this.mbPanelInitialized)
  {
    bSegmentCreated = this.mCombinedResults.fDisplayAdvance();
  }

  return bSegmentCreated;
}

function  WWHSearch_GetHTMLSegment()
{
  return this.mCombinedResults.mHTMLSegment.fGetBuffer();
}

function  WWHSearch_EndHTMLSegments()
{
  return "";
}

function  WWHSearch_HoverTextTranslate(ParamEntryID)
{
  var  HTML     = "";
  var  BookList = WWHFrame.WWHHelp.mBooks.mBookList;
  var  Settings = WWHFrame.WWHJavaScript.mSettings.mSearch;
  var  Messages = WWHFrame.WWHJavaScript.mMessages;
  var  Entry;
  var  Rank = "";
  var  Title;
  var  Book = "";
  var  Format;


  // Retrieve specified entry
  //
  Entry = this.mCombinedResults.mEntries[ParamEntryID];

  // Get Rank
  //
  if (Settings.mbShowRank)
  {
    Rank = Math.floor((Entry.mScore / this.mCombinedResults.mMaxScore) * 100) + "%";
  }

  // Get Title
  //
  Title = Entry.mTitle;

  // Get Book
  //
  if (Settings.mbShowBook)
  {
    if ((BookList.length > 1) &&                 // More than one book exists
        (this.mCombinedResults.mSortedBy == 1))  // By Score
    {
      Book = BookList[Entry.mBookIndex].mTitle;
    }
  }

  // Format for display
  //
  if ((Rank.length == 0) &&
      (Book.length == 0))
  {
    // Simple format, just the title
    //
    HTML = Title;
  }
  else
  {
    Format = " align=\"left\" valign=\"top\"><span style=\"" + WWHFrame.WWHJavaScript.mSettings.mHoverText.mFontStyle + "\">";

    // Complex format, requires a table
    //
    HTML += "<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">";
    if (Rank.length > 0)
    {
      HTML += "<tr>";
      HTML += "<th" + Format + Messages.mSearchRankLabel + "</span></th>";
      HTML += "<td" + Format + Rank + "</span></td>";
      HTML += "</tr>";
    }
    HTML += "<tr>";
    HTML += "<th" + Format + Messages.mSearchTitleLabel + "</span></th>";
    HTML += "<td" + Format + Title + "</span></td>";
    HTML += "</tr>";
    if (Book.length > 0)
    {
      HTML += "<tr>";
      HTML += "<th" + Format + Messages.mSearchBookLabel + "</span></th>";
      HTML += "<td" + Format + Book + "</span></td>";
      HTML += "</tr>";
    }
    HTML += "</table>";

    // IE 5.0 on the Macintosh drops the last table for some reason
    //
    if (WWHFrame.WWHBrowserInfo.mbMacIE50)
    {
      HTML += "<table><tr><td></td></tr></table>";
    }
  }

  return HTML;
}

function  WWHSearch_HoverTextFormat(ParamWidth,
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

function  WWHSearch_DisplaySearchForm()
{
  var  HTML = "";
  var  BookList = WWHFrame.WWHHelp.mBooks.mBookList;


  HTML += "<form name=\"WWHSearchForm\" onSubmit=\"WWHFrame.WWHSearch.fSubmit();\">\n";
  HTML += "<nobr>\n";
  HTML += "<input type=\"text\" name=\"WWHSearchWordsText\" size=20 value=\"" + this.mSavedSearchWords + "\">\n";
  HTML += "<input type=\"submit\" value=\"" + WWHFrame.WWHJavaScript.mMessages.mSearchButtonLabel + "\">\n";
  HTML += "</nobr>\n";

  if (BookList.length > 1)
  {
    var  SelectedIndex = this.mSavedSearchScope - 1;
    var  MaxIndex;
    var  Index;


    HTML += "<br>\n";
    HTML += "<select name=\"WWHSearchScope\">\n";
    HTML += "<option>" + WWHFrame.WWHJavaScript.mMessages.mSearchScopeAllLabel + "</option>\n";
    for (MaxIndex = BookList.length, Index = 0 ; Index < MaxIndex ; Index++)
    {
      if (Index == SelectedIndex)
      {
        HTML += "<option selected>";
      }
      else
      {
        HTML += "<option>";
      }

      HTML += BookList[Index].mTitle + "</option>\n";
    }
    HTML += "</select>\n";
  }

  HTML += "</form>\n";

  return HTML;
}

function  WWHSearch_Submit()
{
  var  SearchForm = WWHFrame.WWHNavigationFrame.WWHPanelFrame.document.forms["WWHSearchForm"];
  var  NewSearchWords;
  var  NewSearchScope;
  var  bSearchChanged = false;


  // Update search words
  //
  NewSearchWords = SearchForm.elements["WWHSearchWordsText"].value;
  if (NewSearchWords != this.mSavedSearchWords)
  {
    this.mSavedSearchWords = NewSearchWords;

    bSearchChanged = true;
  }

  // Update search scope
  //
  if (WWHFrame.WWHHelp.mBooks.mBookList.length > 1)
  {
    NewSearchScope = SearchForm.elements["WWHSearchScope"].selectedIndex;
    if (NewSearchScope != this.mSavedSearchScope)
    {
      this.mSavedSearchScope = NewSearchScope;

      bSearchChanged = true;
    }
  }

  // Perform search if something changed
  //
  if (bSearchChanged)
  {
    if (this.mSavedSearchWords.length > 0)
    {
      var  MaxIndex;
      var  Index;


      // Clear previous results
      //
      for (MaxIndex = this.mBookMatchesList.length, Index = 0 ; Index < MaxIndex ; Index++)
      {
        this.mBookMatchesList[Index].fClear();
      }
      this.mCombinedResults.fClear();

      // Save/restore current position
      //
      WWHFrame.WWHJavaScript.mPanels.fSaveScrollPosition();

      // Perform search
      //
      this.fSetSearchWords(WWHStringUtilities_NormalizeSearchWords(this.mSavedSearchWords));
      this.mbPanelInitialized = false;
      setTimeout("WWHFrame.WWHJavaScript.mPanels.fDisplayPanel();", 50);  // Need to finish submit
    }
  }

  return bSearchChanged;
}

function  WWHSearch_SetSearchWords(ParamSearchWordsString)
{
  // Workaround for stupid Netscape 4.x bug
  //
  var  StringWithSpace = "x x";
  var  SearchWordList;
  var  MaxIndex;
  var  Index;
  var  SearchWord;
  var  SearchRegExpPattern;


  // Clear search words
  //
  this.mSearchWordList.length = 0;
  this.mSearchWordRegExpList.length = 0;

  // Add search words to hash
  //
  SearchWordList = ParamSearchWordsString.split(StringWithSpace.substring(1, 2));
  for (MaxIndex = SearchWordList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    // Skip 0 length words
    //
    if (SearchWordList[Index].length > 0)
    {
      // Add to search words hash
      //
      SearchWord = SearchWordList[Index].toLowerCase();
      SearchRegExpPattern = WWHStringUtilities_WordToRegExpPattern(SearchWord);

      this.mSearchWordList[this.mSearchWordList.length] = SearchWord;
      this.mSearchWordRegExpList[this.mSearchWordRegExpList.length] = new RegExp(SearchRegExpPattern, "i");
    }
  }
}

function  WWHSearch_CheckForMatch(ParamSearchFunc)
{
  var  Count;
  var  MaxIndex;
  var  Index;
  var  BookSearchInfoEntry;
  var  SearchPattern;


  Count = 0;
  for (MaxIndex = this.mSearchWordList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    BookSearchInfoEntry = this.mBookSearchInfoList[this.mBookIndex]

    if (this.mBookSearchInfoList[this.mBookIndex].fValidSearchWord(this.mSearchWordList[Index]))
    {
      BookMatchesListEntry = this.mBookMatchesList[this.mBookIndex];

      BookMatchesListEntry.fSetMatchedWordIndex(Count);

      SearchPattern = this.mSearchWordRegExpList[Index];
      SearchPattern.t = SearchPattern.test;

      ParamSearchFunc(SearchPattern, BookMatchesListEntry);

      Count++;
    }
  }
}

function  WWHSearch_SearchComplete()
{
  // Set flag so that results can be displayed
  //
  this.mbPanelInitialized = true;

  // Combine results for display
  //
  this.fCombineResults();

  // Sort results based on single or multi-book display
  //
  if ((WWHFrame.WWHHelp.mBooks.mBookList.length == 1) ||
      (this.mSavedSearchScope > 0))
  {
    this.mCombinedResults.fSortByBookIndex();
  }
  else
  {
    this.mCombinedResults.fSortByScore();
  }
}

function  WWHSearch_CombineResults()
{
  var  MaxBookIndex;
  var  BookIndex;
  var  BookMatches;
  var  BookListEntry;
  var  FileID;
  var  FileIndex;


  this.mCombinedResults.fClear();
  for (MaxBookIndex = this.mBookMatchesList.length, BookIndex = 0 ; BookIndex < MaxBookIndex ; BookIndex++)
  {
    BookMatches = this.mBookMatchesList[BookIndex];
    BookListEntry = WWHFrame.WWHHelp.mBooks.mBookList[BookIndex];

    // Add results
    //
    BookMatches.fJoinFileScores();
    for (FileID in BookMatches.mFileScores)
    {
      FileIndex = parseInt(FileID.substring(1, FileID.length));

      this.mCombinedResults.fAddEntry(BookIndex, FileIndex, BookMatches.mFileScores[FileID], BookListEntry.mFiles.fFileIndexToTitle(FileIndex));
    }
  }
}

function  WWHSearch_ShowEntry(ParamIndex)
{
  this.mCombinedResults.fShowEntry(ParamIndex);
}

function  WWHBookSearchInfo_Object(ParamSearchFileCount,
                                   ParamMinimumWordLength)
{
  this.mSearchFileCount   = ParamSearchFileCount;
  this.mMinimumWordLength = ParamMinimumWordLength;
  this.mSkipWords         = new WWHBookSearchInfo_SkipWords_Object();

  this.fAddSkipWord     = WWHBookSearchInfo_AddSkipWord;
  this.fA               = WWHBookSearchInfo_AddSkipWord;
  this.fValidSearchWord = WWHBookSearchInfo_ValidSearchWord;
}

function  WWHBookSearchInfo_AddSkipWord(ParamSkipWord)
{
  if (ParamSkipWord.length > 0)
  {
    this.mSkipWords[ParamSkipWord + "~"] = 1;
  }
}

function  WWHBookSearchInfo_ValidSearchWord(ParamSearchWord)
{
  var  bValid = true;


  if ((ParamSearchWord.length < this.mMinimumWordLength) ||
      (typeof this.mSkipWords[ParamSearchWord + "~"] == "number"))
  {
    bValid = false;
  }

  return bValid;
}

function  WWHBookSearchInfo_SkipWords_Object()
{
}

function  WWHSearchBookMatches_Object()
{
  this.mFirstMatchedWordIndex = -1;
  this.mMatchedWordIndex      = -1;
  this.mWordFileScores        = new Array();
  this.mFileScores            = new WWHSearchBookMatches_FileScores_Object();

  this.fClear               = WWHSearchBookMatches_Clear;
  this.fSetMatchedWordIndex = WWHSearchBookMatches_SetMatchedWordIndex;
  this.fAddMatches          = WWHSearchBookMatches_AddMatches;
  this.f                    = WWHSearchBookMatches_AddMatches;  // For smaller search files
  this.fJoinFileScores      = WWHSearchBookMatches_JoinFileScores;
}

function  WWHSearchBookMatches_Clear()
{
  this.mFirstMatchedWordIndex = -1;
  this.mMatchedWordIndex      = -1;
  this.mWordFileScores.length = 0;
  this.mFileScores            = new WWHSearchBookMatches_FileScores_Object();
}

function  WWHSearchBookMatches_SetMatchedWordIndex(ParamMatchedWordIndex)
{
  this.mMatchedWordIndex = ParamMatchedWordIndex;
  if (ParamMatchedWordIndex == this.mWordFileScores.length)
  {
    this.mWordFileScores[this.mWordFileScores.length] = new WWHSearchBookMatches_FileScores_Object();
  }
}

function  WWHSearchBookMatches_AddMatches(ParamMatchString)
{
  var  MatchList = null;
  var  WordFileScoresEntry;
  var  MaxIndex;
  var  Index;
  var  FileID;
  var  Score;


  if (typeof ParamMatchString != "undefined")
  {
    MatchList = ParamMatchString.split(",");
  }

  if ((MatchList != null) &&
      (MatchList.length > 0))
  {
    WordFileScoresEntry = this.mWordFileScores[this.mMatchedWordIndex];

    // Add all entries to word file score entry
    //
    for (MaxIndex = MatchList.length, Index = 0 ; Index < MaxIndex ; Index += 2)
    {
      FileID = "i" + MatchList[Index];
      Score  = MatchList[Index + 1];

      WordFileScoresEntry[FileID] = parseInt(Score);
    }
  }
}

function  WWHSearchBookMatches_JoinFileScores()
{
  var  WordFileScoresEntry;


  this.mFileScores = new WWHSearchBookMatches_FileScores_Object();
  for (MaxIndex = this.mWordFileScores.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    WordFileScoresEntry = this.mWordFileScores[Index];

    if (Index == 0)
    {
      // Add all entries if first entry
      //
      this.mFileScores = WordFileScoresEntry;
    }
    else
    {
      // Remove all entries not found in results set
      //
      for (FileID in this.mFileScores)
      {
        if (typeof WordFileScoresEntry[FileID] == "number")
        {
          this.mFileScores[FileID] += WordFileScoresEntry[FileID];
        }
        else
        {
          delete this.mFileScores[FileID];
        }
      }
    }
  }
}

function  WWHSearchBookMatches_FileScores_Object()
{
}

function  WWHSearchResults_Object()
{
  this.mSortedBy     = null;
  this.mEntries      = new Array();
  this.mMaxScore     = 0;
  this.mDisplayIndex = 0;
  this.mHTMLSegment  = new WWHStringBuffer_Object();
  this.mEventString  = WWHPopup_EventString();

  this.fClear           = WWHSearchResults_Clear;
  this.fAddEntry        = WWHSearchResults_AddEntry;
  this.fSortByScore     = WWHSearchResults_SortByScore;
  this.fSortByBookIndex = WWHSearchResults_SortByBookIndex;
  this.fDisplayReset    = WWHSearchResults_DisplayReset;
  this.fDisplayAdvance  = WWHSearchResults_DisplayAdvance;
  this.fGetPopupAction  = WWHSearchResults_GetPopupAction;
  this.fShowEntry       = WWHSearchResults_ShowEntry;
}

function  WWHSearchResults_Clear()
{
  this.mSortedBy       = null;
  this.mEntries.length = 0;
  this.mMaxScore       = 0;
}

function  WWHSearchResults_AddEntry(ParamBookIndex,
                                    ParamFileIndex,
                                    ParamScore,
                                    ParamTitle)
{
  // Add a new entry
  //
  this.mEntries[this.mEntries.length] = new WWHSearchResultsEntry_Object(ParamBookIndex,
                                                                         ParamFileIndex,
                                                                         ParamScore,
                                                                         ParamTitle);

  // Bump mMaxScore if necessary
  //
  if (ParamScore > this.mMaxScore)
  {
    this.mMaxScore = ParamScore;
  }
}

function  WWHSearchResults_SortByScore()
{
  this.mSortedBy = 1;  // By Score

  if (this.mEntries.length > 0)
  {
    this.mEntries = this.mEntries.sort(WWHSearchResultsEntry_ByScoreByBookIndexByTitleFileIndexURL);
  }
}

function  WWHSearchResults_SortByBookIndex()
{
  this.mSortedBy = 2;  // By BookIndex

  if (this.mEntries.length > 0)
  {
    this.mEntries = this.mEntries.sort(WWHSearchResultsEntry_ByBookIndexByScoreByTitleFileIndexURL);
  }
}

function  WWHSearchResults_DisplayReset()
{
  this.mDisplayIndex = 0;
}

function  WWHSearchResults_DisplayAdvance()
{
  var  bSegmentCreated = false;
  var  Settings = WWHFrame.WWHJavaScript.mSettings.mSearch;
  var  HTML;
  var  MaxHTMLSegmentSize;
  var  BookList;
  var  MaxIndex;
  var  Index;
  var  Entry;
  var  ByBookDetect;


  // Insure that there is something to display
  //
  if ((this.mSortedBy != null) &&
      (this.mEntries.length > 0))
  {
    MaxHTMLSegmentSize = WWHFrame.WWHJavaScript.mMaxHTMLSegmentSize;
    this.mHTMLSegment.fReset();
    BookList = WWHFrame.WWHHelp.mBooks.mBookList;
    ByBookDetect = -1;

    // If this is the first entry, display the headers and open the list
    //
    if (this.mDisplayIndex == 0)
    {
      HTML = "";

      HTML += "<p><nobr><b>";

      // Display column headers
      //
      if (Settings.mbShowRank)
      {
        HTML += WWHFrame.WWHJavaScript.mMessages.mSearchRankLabel + " ";
      }
      HTML += WWHFrame.WWHJavaScript.mMessages.mSearchTitleLabel;
      if (Settings.mbShowBook)
      {
        if ((BookList.length > 1) &&  // More than one book exists
            (this.mSortedBy == 1))    // By Score
        {
          HTML += ", " + WWHFrame.WWHJavaScript.mMessages.mSearchBookLabel;
        }
      }
      HTML += "</b></nobr></p>\n";

      HTML += "<nobr><ol>\n";

      this.mHTMLSegment.fAppend(HTML);
    }

    // Display result entries
    //
    MaxIndex = this.mEntries.length;
    Index = this.mDisplayIndex;
    while ((this.mHTMLSegment.fSize() < MaxHTMLSegmentSize) &&
           (Index < MaxIndex))
    {
      HTML = "";

      Entry = this.mEntries[Index];

      if ((BookList.length > 1) &&  // More than one book exists
          (this.mSortedBy == 2))    // By BookIndex
      {
        if (ByBookDetect != Entry.mBookIndex)
        {
// HACK BEN
// ByBook only used for single books
// No book title will be emitted
//
//          HTML += "<p><nobr>&nbsp;</nobr></p>";
//          HTML += "<p><nobr>" + BookList[Entry.mBookIndex].mTitle + "</nobr></p>";

          ByBookDetect = Entry.mBookIndex;
        }
      }

      // Display Rank
      //
      if (Settings.mbShowRank)
      {
        HTML += "<li value=\"" + Math.floor((Entry.mScore / this.mMaxScore) * 100) + "\">";
      }
      else
      {
        HTML += "<li>";
      }

      // Display Title
      //
      HTML += "<a href=\"javascript:fC(" + Index + ");\"" + this.fGetPopupAction(Index) + ">";
      HTML += Entry.mTitle;
      HTML += "</a>";

      // Display Book
      //
      if (Settings.mbShowBook)
      {
        if ((BookList.length > 1) &&  // More than one book exists
            (this.mSortedBy == 1))    // By Score
        {
          HTML += ", " + BookList[Entry.mBookIndex].mTitle;
        }
      }

      HTML += "</li>\n";

      this.mHTMLSegment.fAppend(HTML);

      Index++;
    }

    // Record current display index so we can pick up where we left off
    //
    this.mDisplayIndex = Index;
    if (this.mHTMLSegment.fSize() > 0)
    {
      bSegmentCreated = true;
    }

    // If this is the last entry, close the list
    //
    if (this.mDisplayIndex == this.mEntries.length)
    {
      this.mHTMLSegment.fAppend("</ol></nobr>\n");
    }
  }

  return bSegmentCreated;
}

function  WWHSearchResults_GetPopupAction(ParamEntryIndex)
{
  var  PopupAction = "";


  if (WWHFrame.WWHJavaScript.mSettings.mHoverText.mbEnabled)
  {
    PopupAction += " onMouseOver=\"fS('" + ParamEntryIndex + "', " + this.mEventString + ");\"";
    PopupAction += " onMouseOut=\"fH();\"";
  }

  return PopupAction;
}

function  WWHSearchResults_ShowEntry(ParamIndex)
{
  var  Entry;
  var  BookListEntry;
  var  URL;


  // Update highlight words
  //
  WWHFrame.WWHHighlightWords.fSetWordList(WWHStringUtilities_NormalizeSearchWords(WWHFrame.WWHSearch.mSavedSearchWords));

  // Display document
  //
  Entry = this.mEntries[ParamIndex];
  BookListEntry = WWHFrame.WWHHelp.mBooks.mBookList[Entry.mBookIndex];
  URL = WWHFrame.WWHHelp.mBaseURL + BookListEntry.mDirectory + BookListEntry.mFiles.fFileIndexToHREF(Entry.mFileIndex);
  WWHFrame.WWHHelp.fSetDocumentHREF(URL, false);
}

function  WWHSearchResultsEntry_Object(ParamBookIndex,
                                       ParamFileIndex,
                                       ParamScore,
                                       ParamTitle)
{
  this.mBookIndex = ParamBookIndex;
  this.mFileIndex = ParamFileIndex;
  this.mScore     = ParamScore;
  this.mTitle     = ParamTitle;
}

function  WWHSearchResultsEntry_ByScoreByBookIndexByTitleFileIndexURL(ParamAlphaEntry,
                                                                      ParamBetaEntry)
{
  var  Result;


  Result = WWHSearchResultsEntry_CompareByScore(ParamAlphaEntry, ParamBetaEntry);
  if (Result == 0)
  {
    Result = WWHSearchResultsEntry_CompareByBookIndex(ParamAlphaEntry, ParamBetaEntry);
  }
  if (Result == 0)
  {
    Result = WWHSearchResultsEntry_CompareByTitleFileIndexURL(ParamAlphaEntry, ParamBetaEntry);
  }

  return Result;
}

function  WWHSearchResultsEntry_ByBookIndexByScoreByTitleFileIndexURL(ParamAlphaEntry,
                                                                      ParamBetaEntry)
{
  var  Result;


  Result = WWHSearchResultsEntry_CompareByBookIndex(ParamAlphaEntry, ParamBetaEntry);
  if (Result == 0)
  {
    Result = WWHSearchResultsEntry_CompareByScore(ParamAlphaEntry, ParamBetaEntry);
  }
  if (Result == 0)
  {
    Result = WWHSearchResultsEntry_CompareByTitleFileIndexURL(ParamAlphaEntry, ParamBetaEntry);
  }

  return Result;
}

function  WWHSearchResultsEntry_CompareByScore(ParamAlphaEntry,
                                               ParamBetaEntry)
{
  var  Result = 0;


  // Sort by score
  //
  if (ParamAlphaEntry.mScore < ParamBetaEntry.mScore)
  {
    Result = 1;
  }
  else if (ParamAlphaEntry.mScore > ParamBetaEntry.mScore)
  {
    Result = -1;
  }

  return Result;
}

function  WWHSearchResultsEntry_CompareByBookIndex(ParamAlphaEntry,
                                                   ParamBetaEntry)
{
  var  Result = 0;


  if (ParamAlphaEntry.mBookIndex < ParamBetaEntry.mBookIndex)
  {
    Result = -1;
  }
  else if (ParamAlphaEntry.mBookIndex > ParamBetaEntry.mBookIndex)
  {
    Result = 1;
  }

  return Result;
}

function  WWHSearchResultsEntry_CompareByTitleFileIndexURL(ParamAlphaEntry,
                                                           ParamBetaEntry)
{
  var  Result = 0;
  var  BookList;
  var  AlphaBookEntry;
  var  BetaBookEntry;
  var  AlphaURL;
  var  BetaURL;


  // Sort by Title
  //
  if (ParamAlphaEntry.mTitle < ParamBetaEntry.mTitle)
  {
    Result = -1;
  }
  else if (ParamAlphaEntry.mTitle > ParamBetaEntry.mTitle)
  {
    Result = 1;
  }
  // Sort by FileIndex
  //
  else if (ParamAlphaEntry.mFileIndex < ParamBetaEntry.mFileIndex)
  {
    Result = -1;
  }
  else if (ParamAlphaEntry.mFileIndex > ParamBetaEntry.mFileIndex)
  {
    Result = 1;
  }
  // Sort by URL
  //
  else
  {
    BookList = WWHFrame.WWHHelp.mBooks.mBookList;

    AlphaBookEntry = BookList[ParamAlphaEntry.mBookIndex];
    BetaBookEntry  = BookList[ParamBetaEntry.mBookIndex];

    AlphaURL = WWHFrame.WWHHelp.mBaseURL + AlphaBookEntry.mDirectory + AlphaBookEntry.mFiles.fFileIndexToHREF(ParamAlphaEntry.mFileIndex);
    BetaURL  = WWHFrame.WWHHelp.mBaseURL + BetaBookEntry.mDirectory + BetaBookEntry.mFiles.fFileIndexToHREF(ParamBetaEntry.mFileIndex);

    if (AlphaURL < BetaURL)
    {
      Result = -1;
    }
    else if (AlphaURL > BetaURL)
    {
      Result = 1;
    }
  }

  return Result;
}
