// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHHelp_GetLocale()
{
  var  Locale = "en";


  // Reset locale to correct language value
  //
  if ((typeof navigator.language != "undefined") &&
      (navigator.language != null))
  {
    Locale = navigator.language;
  }
  else if ((typeof navigator.userLanguage != "undefined") &&
           (navigator.userLanguage != null))
  {
    Locale = navigator.userLanguage;
  }

  // Convert everything to lowercase
  //
  Locale = Locale.toLowerCase();

  // Replace '-'s with '_'s
  //
  Locale = WWHStringUtilities_SearchReplace(Locale, "-", "_");

  return Locale;
}

function  WWHHelp_GetBaseURL(ParamURL)
{
  var  BaseURL;
  var  Parts;


  BaseURL = ParamURL.substring(0, ParamURL.lastIndexOf("/"));

  Parts = BaseURL.split("/wwhelp/wwhimpl/common/html");
  if (Parts[0] == BaseURL)
  {
    Parts = BaseURL.split("/wwhelp/wwhimpl/java/html");
  }
  if (Parts[0] == BaseURL)
  {
    Parts = BaseURL.split("/wwhelp/wwhimpl/js/html");
  }

  BaseURL = Parts[0] + "/";

  return BaseURL;
}

function  WWHHelp_GetURLParams(ParamURL)
{
  var  URLParams = new Array(null, null, null, null);


  // Check for possible context specification
  //
  if (ParamURL.indexOf("?") != -1)
  {
    var  Parts;
    var  ContextMarker = "context=";
    var  TopicMarker   = "topic=";
    var  FileMarker    = "file=";
    var  HREFMarker    = "href=";
    var  MaxIndex;
    var  Index;


    Parts = ParamURL.split("?");

    // Check for our context tags
    //
    if ((Parts[1].indexOf("&") != -1) &&
        (Parts[1].indexOf(ContextMarker) != -1) &&
        ((Parts[1].indexOf(TopicMarker) != -1) ||
         (Parts[1].indexOf(FileMarker) != -1)))
    {
      Parts = Parts[1].split("&");

      // Set context and topic, if specified
      //
      for (MaxIndex = Parts.length, Index = 0 ; Index < MaxIndex ; Index++)
      {
        if (Parts[Index].indexOf(ContextMarker) == 0)
        {
          URLParams[0] = Parts[Index].substring(ContextMarker.length, Parts[Index].length);
        }
        if (Parts[Index].indexOf(TopicMarker) == 0)
        {
          URLParams[1] = Parts[Index].substring(TopicMarker.length, Parts[Index].length);
        }
        if (Parts[Index].indexOf(FileMarker) == 0)
        {
          URLParams[2] = Parts[Index].substring(FileMarker.length, Parts[Index].length);
        }
      }

      // Make certain we have both a ContextTag and either a TopicTag or FileTag
      // Otherwise, reset them
      //
      if ((URLParams[0] == null) ||
          ((URLParams[1] == null) &&
           (URLParams[2] == null)))
      {
        URLParams[0] = null;
        URLParams[1] = null;
        URLParams[2] = null;
      }
    }
    else if (Parts[1].indexOf(HREFMarker) == 0) // Check for file tag
    {
      Parts = Parts[1].split("&");

      // Set href, if specified
      //
      for (MaxIndex = Parts.length, Index = 0 ; Index < MaxIndex ; Index++)
      {
        if (Parts[Index].indexOf(HREFMarker) == 0)
        {
          URLParams[3] = Parts[Index].substring(HREFMarker.length, Parts[Index].length);
        }
      }
    }
  }

  return URLParams;
}

function  WWHHelp_Object(ParamURL)
{
  this.mbInitialized   = false;
  this.mInitStage      = 0;
  this.mInitControls   = 0;
  this.mSettings       = new WWHCommonSettings_Object();
  this.mMessages       = new WWHCommonMessages_Object();
  this.mDocumentLoaded = null;
  this.mLocale         = WWHHelp_GetLocale();
  this.mLocationURL    = WWHStringUtilities_NormalizeURL(ParamURL);
  this.mBaseURL        = WWHHelp_GetBaseURL(this.mLocationURL);
  this.mHelpURLPrefix  = WWHStringUtilities_RestoreEscapedSpaces(this.mBaseURL);
  this.mContextDir     = null;
  this.mTopicTag       = null;
  this.mDocumentURL    = "";
  this.mPopup          = new WWHPopup_Object("WWHFrame.WWHHelp.mPopup",
                                             "WWHFrame.WWHContentFrame.WWHDocumentFrame",
                                             WWHPopupFormat_Translate,
                                             WWHPopupFormat_Format,
                                             "WWHPopupDIV", "WWHPopupText", 500, 12, 20,
                                             this.mSettings.mPopup.mWidth);
  this.mBookGroups     = new WWHBookGroups_Object();
  this.mBooks          = new WWHBookList_Object();

  this.fInitStage                = WWHHelp_InitStage;
  this.fHandlerInitialized       = WWHHelp_HandlerInitialized;
  this.fControlsLoaded           = WWHHelp_ControlsLoaded;
  this.fSetDocumentFrame         = WWHHelp_SetDocumentFrame;
  this.fSetDocumentHREF          = WWHHelp_SetDocumentHREF;
  this.fDetermineContextDocument = WWHHelp_DetermineContextDocument;
  this.fLoadTopicData            = WWHHelp_LoadTopicData;
  this.fProcessTopicResult       = WWHHelp_ProcessTopicResult;
  this.fDisplayContextDocument   = WWHHelp_DisplayContextDocument;
  this.fSetContextDocument       = WWHHelp_SetContextDocument;
  this.fGetBookFileHREF          = WWHHelp_GetBookFileHREF;
  this.fGetSyncPrevNext          = WWHHelp_GetSyncPrevNext;
  this.fHREFToTitle              = WWHHelp_HREFToTitle;
  this.fShowPopup                = WWHHelp_ShowPopup;
  this.fHidePopup                = WWHHelp_HidePopup;
  this.fClickedPopup             = WWHHelp_ClickedPopup;
  this.fUpdate                   = WWHHelp_Update;
  this.fDocumentBookkeeping      = WWHHelp_DocumentBookkeeping;

  // Load up messages
  //
  this.mMessages.fSetByLocale(this.mLocale);
}

function  WWHHelp_InitStage(ParamStage)
{
  if (( ! this.mbInitialized) &&
      (ParamStage == this.mInitStage))
  {
    // Perform actions for current init stage
    //
    switch (this.mInitStage)
    {
      case 0:  // Start initialization process
        // Alert the user if this browser is unsupported
        //
        if (WWHFrame.WWHBrowserInfo.mbUnsupported)
        {
          alert(WWHFrame.WWHHelp.mMessages.mBrowserNotSupported);
        }

        this.mInitControls = 0;
        WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsLeftFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init0.htm");
        break;

      case 1:  // Prep book data
        WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsLeftFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init1.htm");
        break;

      case 2:  // Load book data
        WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsLeftFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init2.htm");
        break;

      case 3:  // Handler setup
        // Initialize handler
        //
        WWHFrame.WWHHandler.fInit();
        break;

      case 4:  // Display controls
        WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsLeftFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/controll.htm");
        WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsRightFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/controlr.htm");
        WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHTitleFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/title.htm");
        break;

      case 5:  // Display document
        this.fSetDocumentFrame();
        this.mbInitialized = true;
        break;
    }

    // Increment stage
    //
    this.mInitStage++;
  }
}

function  WWHHelp_HandlerInitialized()
{
  if (WWHFrame.WWHHelp.mInitStage > 0)
  {
    if (WWHFrame.WWHHandler.mbInitialized)
    {
      WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsRightFrame.location.replace(this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init3.htm");
    }
  }
}

function  WWHHelp_ControlsLoaded()
{
  if ( ! WWHFrame.WWHHelp.mbInitialized)
  {
    // Only go to final init stage once all control frames have loaded
    //
    this.mInitControls++;
    if (this.mInitControls > 2)
    {
      WWHFrame.WWHHelp.fInitStage(5);
    }
  }
}

function  WWHHelp_SetDocumentFrame()
{
  var  DocumentLoaded;
  var  ContextDocumentURL;


  // Preserve current document if user clicked forward or back to see it
  //
  if (this.mDocumentLoaded != null)
  {
    DocumentLoaded = this.mDocumentLoaded;

    this.mDocumentLoaded = null;
    this.fUpdate(DocumentLoaded);
  }
  else
  {
    ContextDocumentURL = this.fDetermineContextDocument();
    if (ContextDocumentURL != null)
    {
      this.fSetDocumentHREF(ContextDocumentURL, true);
    }
    else  // Load topic data to determine document to display
    {
      this.fSetDocumentHREF(this.mBaseURL + "wwhelp/wwhimpl/common/html/document.htm", true);
    }
  }
}

function  WWHHelp_SetDocumentHREF(ParamURL,
                                  bParamReplace)
{
  if (ParamURL.length > 0)
  {
    var  EscapedURL = ParamURL;


    EscapedURL = WWHStringUtilities_RestoreEscapedSpaces(EscapedURL);
    EscapedURL = WWHStringUtilities_EscapeURLForJavaScriptAnchor(EscapedURL);

    if (bParamReplace)
    {
      setTimeout("WWHFrame.WWHContentFrame.WWHDocumentFrame.location.replace(\"" + EscapedURL + "\");", 1);
    }
    else
    {
      setTimeout("WWHFrame.WWHContentFrame.WWHDocumentFrame.location = \"" + EscapedURL + "\";", 1);
    }
  }
}

function  WWHHelp_DetermineContextDocument()
{
  var  ContextDocumentURL = null;
  var  URLParams          = WWHHelp_GetURLParams(this.mLocationURL);


  // Check for context specification
  //
  if (URLParams[3] != null)  // href specified
  {
    ContextDocumentURL = this.mBaseURL + URLParams[3];
  }
  else if (URLParams[0] != null)  // context specified
  {
    var  ContextBook;


    // Determine book directory
    //
    ContextBook = this.mBooks.fGetContextBook(URLParams[0]);
    if (ContextBook != null)
    {
      if (URLParams[2] != null)  // file specified
      {
        ContextDocumentURL = this.mBaseURL + ContextBook.mDirectory + URLParams[2];
      }
      else if (URLParams[1] != null)  // topic specified
      {
        // Setup for a topic search
        //
        this.mContextDir = ContextBook.mDirectory;
        this.mTopicTag   = URLParams[1];

        this.mDocumentURL = "";
      }
    }
    else  // Display splash page if nothing else found
    {
      ContextDocumentURL = this.mBaseURL + "wwhelp/wwhimpl/common/html/default.htm";
    }
  }
  else  // Display splash page if nothing else found
  {
    ContextDocumentURL = this.mBaseURL + "wwhelp/wwhimpl/common/html/default.htm";
  }

  return ContextDocumentURL;
}

function  WWHHelp_LoadTopicData()
{
  var  LoadTopicDataHTML = "";


  LoadTopicDataHTML += "<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + this.mHelpURLPrefix + WWHStringUtilities_RestoreEscapedSpaces(this.mContextDir) + "wwhdata/common/topics.js\"></script>";
  LoadTopicDataHTML += "<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + this.mHelpURLPrefix + "wwhelp/wwhimpl/common/scripts/documt1s.js\"></script>";

  return LoadTopicDataHTML;
}

function  WWHHelp_ProcessTopicResult(ParamTopicURL)
{
  if (ParamTopicURL != null)
  {
    this.mDocumentURL = this.mBaseURL + this.mContextDir + ParamTopicURL;
  }
}

function  WWHHelp_DisplayContextDocument()
{
  WWHFrame.WWHHelp.fSetDocumentHREF(this.mDocumentURL, true);
}

function  WWHHelp_GetURLPrefix(ParamURL)
{
  var  URLPrefix  = null;
  var  WorkingURL = "";
  var  Parts;
  var  Index;


  // Standardize URL for processing
  //
  WorkingURL = ParamURL;

  // Strip any URL parameters
  //
  if (WorkingURL.indexOf("?") != -1)
  {
    Parts = WorkingURL.split("?");
    WorkingURL = Parts[0];
  }

  // Confirm URL in wwhelp hierarchy
  //
  if (((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/common/html/switch.htm")) != -1) ||
      ((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/common/html/wwhelp.htm")) != -1) ||
      ((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/java/html/wwhelp.htm"))   != -1) ||
      ((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/js/html/wwhelp.htm"))     != -1))
  {
    URLPrefix = WorkingURL.substring(0, Index);
  }
  else
  {
    // Look for match on top level "wwhelp.htm" file
    //
    Index = WorkingURL.lastIndexOf("/");
    if ((Index != -1) &&
       (Index == WorkingURL.indexOf("/wwhelp.htm")))
    {
      URLPrefix = WorkingURL.substring(0, Index);
    }
  }

  return URLPrefix;
}

function  WWHHelp_SetContextDocument(ParamURL)
{
  var  URL = WWHStringUtilities_NormalizeURL(ParamURL);
  var  CurrentURLPrefix;
  var  NewURLPrefix;
  var  DocumentURL;


  // Confirm URL under same hierarchy
  //
  CurrentURLPrefix = WWHHelp_GetURLPrefix(this.mLocationURL);
  NewURLPrefix     = WWHHelp_GetURLPrefix(URL);
  if ((CurrentURLPrefix != null) &&
      (NewURLPrefix     != null) &&
      (CurrentURLPrefix == NewURLPrefix))
  {
    // Check if in single topic mode
    //
    if (typeof WWHFrame.WWHNavigationFrame == "undefined")
    {
      // Check for required switch to frameset with navigation
      //
      WWHFrame.WWHSwitch.fProcessURL(ParamURL);
      if ( ! WWHFrame.WWHSwitch.mbSingle)
      {
        // Switch to frameset with navigation
        //
        if (WWHFrame.WWHSwitch.mParameters.length > 0)
        {
          // Context and topic supplied, use them
          //
          setTimeout("WWHFrame.location = \"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(ParamURL) + "\";", 1);
        }
        else
        {
          // Just switch to frameset with navigation and preserve the current document
          //
          DocumentURL = WWHStringUtilities_NormalizeURL(WWHFrame.WWHContentFrame.WWHDocumentFrame.location.href);
          DocumentURL = WWHFrame.WWHHelp.fGetBookFileHREF(DocumentURL);
          WWHFrame.WWHSwitch.fExec(false, WWHFrame.WWHHelp.mHelpURLPrefix + "/wwhelp/wwhimpl/common/html/wwhelp.htm?href=" + WWHStringUtilities_RestoreEscapedSpaces(DocumentURL));
        }
      }
      else
      {
        // Update document frame
        //
        this.mLocationURL = URL;
        this.fSetDocumentFrame();
      }
    }
    else
    {
      // Update document frame
      //
      this.mLocationURL = URL;
      this.fSetDocumentFrame();
    }
  }
  else
  {
    // Some other help system requested, redirect to it
    //
    setTimeout("WWHFrame.location = \"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(ParamURL) + "\";", 1);
  }
}

function  WWHHelp_GetBookFileHREF(ParamHREF)
{
  var  BookFileHREF = null;


  // Confirm HREF can be in same hierarchy as BaseURL
  //
  if ((this.mBaseURL.length > 0) &&
      (ParamHREF.length > this.mBaseURL.length))
  {
    var  Prefix;
    var  Suffix;


    Prefix = ParamHREF.substring(0, this.mBaseURL.length);
    Suffix = ParamHREF.substring(this.mBaseURL.length, ParamHREF.length);

    // Confirm HREF definitely is in same hierarchy as BaseURL
    //
    if (Prefix == this.mBaseURL)
    {
      BookFileHREF = Suffix;
    }
  }

  return BookFileHREF;
}

function  WWHHelp_GetSyncPrevNext(ParamHREF)
{
  var  ResultArray = new Array(null, null, null);
  var  Parts;
  var  AbsoluteHREF;
  var  BookFileHREF;


  // Trim named anchor entries
  //
  Parts = ParamHREF.split("#");
  AbsoluteHREF = Parts[0];

  BookFileHREF = this.fGetBookFileHREF(AbsoluteHREF);
  if (BookFileHREF != null)
  {
    if (BookFileHREF == "wwhelp/wwhimpl/common/html/default.htm")
    {
      ResultArray[2] = this.mBooks.fBookFileIndiciesToHREF(0, 0);
    }
    else
    {
      ResultArray = this.mBooks.fGetSyncPrevNext(BookFileHREF);
    }

    // Prefix with BaseURL if defined
    //

    // Current
    //
    if (ResultArray[0] != null)
    {
      ResultArray[0] = this.mBaseURL + ResultArray[0];
    }

    // Previous
    //
    if (ResultArray[1] != null)
    {
      ResultArray[1] = this.mBaseURL + ResultArray[1];
    }

    // Next
    //
    if (ResultArray[2] != null)
    {
      ResultArray[2] = this.mBaseURL + ResultArray[2];
    }
  }
  else
  {
    // Unknown document, enable next button to go to first known page
    //
    ResultArray[2] = this.mBaseURL + this.mBooks.fBookFileIndiciesToHREF(0, 0);
  }

  return ResultArray;
}

function  WWHHelp_HREFToTitle(ParamHREF)
{
  var  HREFTitle;
  var  Parts;
  var  AbsoluteHREF;
  var  BookFileHREF;


  // Trim named anchor entries
  //
  Parts = ParamHREF.split("#");
  AbsoluteHREF = Parts[0];

  BookFileHREF = this.fGetBookFileHREF(AbsoluteHREF);
  Parts = this.mBooks.fGetBookIndexFileHREF(BookFileHREF);
  if (Parts[0] >= 0)
  {
    HREFTitle = this.mBooks.fHREFToTitle(Parts[0], Parts[1]);
  }
  else
  {
    Parts = AbsoluteHREF.split("/");
    HREFTitle = AbsoluteHREF[AbsoluteHREF.length - 1];
  }

  return HREFTitle;
}

function  WWHHelp_ShowPopup(ParamContext,
                            ParamLink,
                            ParamEvent)
{
  var  PopupHTML;


  PopupHTML = this.mBooks.fGetPopupHTML(ParamContext, ParamLink);
  if ((PopupHTML != null) &&
      (PopupHTML.length > 0))
  {
    this.mPopup.fShow(PopupHTML, ParamEvent);
  }
}

function  WWHHelp_HidePopup()
{
  this.mPopup.fHide();
}

function  WWHHelp_ClickedPopup(ParamContext,
                               ParamLink)
{
  var  Link = WWHStringUtilities_NormalizeURL(ParamLink);
  var  LinkHREF = null;
  var  Book;


  Book = this.mBooks.fGetContextBook(ParamContext);
  if (Book != null)
  {
    // Clickable popup?
    //
    if (Book.mPopups.fIsPopupClickable(ParamLink))
    {
      // Hide the popup if it is visible
      //
      this.fHidePopup();

      WWHFrame.WWHHelp.fSetDocumentHREF(this.mBaseURL + Book.mDirectory + Link, false);
    }
  }
}

function  WWHHelp_Update(ParamURL)
{
  var  URL;


  if (this.mInitStage > 0)
  {
    URL = WWHStringUtilities_NormalizeURL(ParamURL);

    WWHFrame.WWHHandler.fUpdate(URL);

    this.fDocumentBookkeeping(URL);
  }
  else if (ParamURL.indexOf("wwhelp/wwhimpl/common/html/default.htm") == -1)
  {
    this.mDocumentLoaded = ParamURL;
  }
}

function  WWHHelp_DocumentBookkeeping(ParamURL)
{
  // Highlight search words
  //
  if (typeof WWHFrame.WWHHighlightWords != "undefined")
  {
    WWHFrame.WWHHighlightWords.fExec();
  }

  // Update controls
  //
  WWHFrame.WWHControls.fUpdateHREF(ParamURL);

  // Update window title, if possible
  //
  if (ParamURL.indexOf("wwhelp/wwhimpl/common/html/default.htm") == -1)
  {
    if (WWHFrame.WWHBrowserInfo.mBrowser != 1)  // Shorthand for Netscape
    {
      WWHFrame.document.title = this.fHREFToTitle(ParamURL);
    }
  }
}
