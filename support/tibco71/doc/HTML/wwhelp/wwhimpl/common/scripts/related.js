// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHRelatedTopics_Object()
{
  this.mRelatedTopicList = new Array();
  this.mPopup            = new WWHPopup_Object("WWHFrame.WWHRelatedTopics.mPopup",
                                               "WWHFrame.WWHContentFrame.WWHDocumentFrame",
                                               WWHRelatedTopicsPopup_Translate,
                                               WWHRelatedTopicsPopup_Format,
                                               "WWHRelatedTopicsDIV", "WWHRelatedTopicsText", 10, 0, 0,
                                               WWHFrame.WWHHelp.mSettings.mRelatedTopics.mWidth);

  this.fClear        = WWHRelatedTopics_Clear;
  this.fAdd          = WWHRelatedTopics_Add;
  this.fHTML         = WWHRelatedTopics_HTML;
  this.fDisplayTopic = WWHRelatedTopics_DisplayTopic;
  this.fUpdate       = WWHRelatedTopics_Update;
  this.fShow         = WWHRelatedTopics_Show;
  this.fShowAtEvent  = WWHRelatedTopics_ShowAtEvent;
  this.fHide         = WWHRelatedTopics_Hide;
  this.fInlineHTML   = WWHRelatedTopics_InlineHTML;
}

function  WWHRelatedTopics_Clear()
{
  this.mRelatedTopicList.length = 0;
}

function  WWHRelatedTopics_Add(ParamText,
                               ParamContext,
                               ParamFileURL)
{
  this.mRelatedTopicList[this.mRelatedTopicList.length] = new WWHRelatedTopicEntry_Object(ParamText, ParamContext, ParamFileURL);
}

function  WWHRelatedTopics_HTML()
{
  var  HTML = new WWHStringBuffer_Object();
  var  Settings = WWHFrame.WWHHelp.mSettings.mRelatedTopics;
  var  MaxIndex;
  var  Index;
  var  ContextBook;


  for (MaxIndex = this.mRelatedTopicList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    ContextBook = WWHFrame.WWHHelp.mBooks.fGetContextBook(this.mRelatedTopicList[Index].mContext);
    if (ContextBook != null)
    {
      HTML.fAppend("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"2\">");
      HTML.fAppend("<tr>");
      HTML.fAppend("<td width=\"17\" valign=\"top\">");
      HTML.fAppend("<a");
      HTML.fAppend(" href=\"javascript:WWHFrame.WWHRelatedTopics.fDisplayTopic(" + Index + ");\">");
      HTML.fAppend("<img border=\"0\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images/doc.gif\" width=\"17\" height=\"17\">");
      HTML.fAppend("</a>");
      HTML.fAppend("</td>");
      HTML.fAppend("<td width=\"100%\" align=\"left\" valign=\"top\">");
      HTML.fAppend("<a");
      HTML.fAppend(" href=\"javascript:WWHFrame.WWHRelatedTopics.fDisplayTopic(" + Index + ");\"");
      HTML.fAppend(" style=\"text-decoration: none ; color: " + Settings.mForegroundColor + " ; " + Settings.mFontStyle + "\">");
      HTML.fAppend(this.mRelatedTopicList[Index].mText);
      HTML.fAppend("</a>");
      HTML.fAppend("</td>");
      HTML.fAppend("</tr>");
      HTML.fAppend("</table>\n");
    }
  }

  return HTML.fGetBuffer();
}

function  WWHRelatedTopics_DisplayTopic(ParamIndex)
{
  var  ContextBook;
  var  RelatedTopicURL = null;


  ContextBook = WWHFrame.WWHHelp.mBooks.fGetContextBook(this.mRelatedTopicList[ParamIndex].mContext);
  if (ContextBook != null)
  {
    RelatedTopicURL = WWHFrame.WWHHelp.mBaseURL + ContextBook.mDirectory + this.mRelatedTopicList[ParamIndex].mFileURL;

    // Hide popup to prevent JavaScript errors before displaying target document
    //
    this.fHide();

    WWHFrame.WWHHelp.fSetDocumentHREF(RelatedTopicURL, false);
  }
}

function  WWHRelatedTopics_Update()
{
  // Clear list of topics
  //
  this.fClear();

  // Load related topics if defined
  //
  if (typeof WWHFrame.WWHContentFrame.WWHDocumentFrame.WWHDefineRelatedTopics == "function")
  {
    WWHFrame.WWHContentFrame.WWHDocumentFrame.WWHDefineRelatedTopics(this);
  }
}

function  WWHRelatedTopicEntry_Object(ParamText,
                                      ParamContext,
                                      ParamFileURL)
{
  this.mText    = ParamText;
  this.mContext = ParamContext;
  this.mFileURL = ParamFileURL;
}

function  WWHRelatedTopics_Show()
{
  var  FakeEvent;


  // Create dummy event to pass to popup show command
  //
  FakeEvent = new WWHRelatedTopicsPopup_FakeEvent_Object();

  // Assign coordinates to event base on browser type
  // Place event at far right and allow popup code to handle repositioning for display
  //
  if (WWHFrame.WWHBrowserInfo.mBrowser == 1)  // Shorthand for Netscape 4.x
  {
    FakeEvent.layerX = WWHFrame.WWHContentFrame.WWHDocumentFrame.innerWidth + WWHFrame.WWHContentFrame.WWHDocumentFrame.pageXOffset;
    FakeEvent.layerY = WWHFrame.WWHContentFrame.WWHDocumentFrame.pageYOffset;
  }
  else if (WWHFrame.WWHBrowserInfo.mBrowser == 2)  // Shorthand for IE
  {
    FakeEvent.x = WWHFrame.WWHContentFrame.WWHDocumentFrame.document.body.clientWidth;
    FakeEvent.y = 0;
  }
  else if (WWHFrame.WWHBrowserInfo.mBrowser == 4)  // Shorthand for Netscape 6.x (Mozilla)
  {
    FakeEvent.layerX = WWHFrame.WWHContentFrame.WWHDocumentFrame.innerWidth + WWHFrame.WWHContentFrame.WWHDocumentFrame.pageXOffset;
    FakeEvent.layerY = WWHFrame.WWHContentFrame.WWHDocumentFrame.pageYOffset;
  }

  // Show popup
  //
  this.fShowAtEvent(FakeEvent);
}

function  WWHRelatedTopics_ShowAtEvent(ParamEvent)
{
  var  RelatedTopicsHTML;


  // Show popup
  //
  this.fUpdate();
  RelatedTopicsHTML = this.fHTML();
  if (RelatedTopicsHTML.length > 0)
  {
    this.mPopup.fShow(RelatedTopicsHTML, ParamEvent);
  }
}

function  WWHRelatedTopics_Hide()
{
  this.mPopup.fHide();
}

function  WWHRelatedTopics_InlineHTML()
{
  var  HTML = "";
  var  Settings = WWHFrame.WWHHelp.mSettings.mRelatedTopics;
  var  AnchorAttributes;
  var  ForegroundColor;
  var  BackgroundColor;  
  var  BorderColor;
  var  ImageDir;
  var  FontFamily = "";
  var  FontSize;


  if (Settings.mbInlineEnabled)
  {
    AnchorAttributes = "href=\"javascript:WWHShowRelatedTopicsHREF();\"";
    AnchorAttributes += " onClick=\"WWHShowRelatedTopicsPopup((document.all||document.getElementById||document.layers)?event:null);\"";

    ForegroundColor = Settings.mInlineForegroundColor;
    BackgroundColor = Settings.mInlineBackgroundColor;  
    BorderColor     = Settings.mInlineBorderColor;
    ImageDir        = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images";

    // Determine font family if running Netscape 4.x
    // Required due to errors processing style attributes
    //
    if (WWHFrame.WWHBrowserInfo.mBrowser == 1)  // Shorthand for Netscape 4.x
    {
      FontFamily = WWHStringUtilities_ExtractStyleAttribute("font-family", Settings.mInlineFontStyle);
      FontSize   = WWHStringUtilities_ExtractStyleAttribute("font-size", Settings.mInlineFontStyle);
    }

    HTML += "<div class=\"WWHInlineRelatedTopics\">";
    HTML += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
    HTML += "<tr>";
    HTML += "<td valign=\"bottom\">";
    HTML += "<nobr>";
    if (FontFamily.length > 0)
    {
      HTML += "<font face=\"" + FontFamily + "\" point-size=\"" + FontSize + "\" color=\"" + ForegroundColor + "\">";
    }
    HTML += "<a";
    if (FontFamily.length == 0)
    {
      HTML += " style=\"text-decoration: none ; color: " + ForegroundColor + " ; " + Settings.mInlineFontStyle + "\"";
    }
    HTML += " " + AnchorAttributes + ">";
    HTML += WWHFrame.WWHHelp.mMessages.mRelatedTopicsIconLabel;
    HTML += "</a>";
    HTML += "&nbsp;";
    if (FontFamily.length > 0)
    {
      HTML += "</font>";
    }
    HTML += "</nobr>";
    HTML += "</td>";
    HTML += "<td valign=\"bottom\">";
    HTML += "<a " + AnchorAttributes + ">";
    HTML += "<img border=\"0\" src=\"" + ImageDir + "/relatedi.gif\">";
    HTML += "</a>";
    HTML += "</td>";
    HTML += "</tr>";
    HTML += "</table>";
    HTML += "</div>";
  }

  return HTML;
}

function  WWHRelatedTopicsPopup_FakeEvent_Object()
{
}

function  WWHRelatedTopicsPopup_Translate(ParamText)
{
  return ParamText;
}

function  WWHRelatedTopicsPopup_Format(ParamWidth,
                                       ParamTextID,
                                       ParamText)
{
  var  FormattedText        = "";
  var  Settings             = WWHFrame.WWHHelp.mSettings.mRelatedTopics;
  var  ImageDir             = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images";
  var  BackgroundColor      = Settings.mBackgroundColor;
  var  BorderColor          = Settings.mBorderColor;
  var  TitleForegroundColor = Settings.mTitleForegroundColor;
  var  TitleBackgroundColor = Settings.mTitleBackgroundColor;
  var  ReqSpacer1w2h        = "<img src=\"" + ImageDir + "/spc1w2h.gif\" width=1 height=2>";
  var  ReqSpacer2w1h        = "<img src=\"" + ImageDir + "/spc2w1h.gif\" width=2 height=1>";
  var  ReqSpacer4w4h        = "<img src=\"" + ImageDir + "/spacer4.gif\" width=4 height=4>";
  var  Spacer1w2h           = ReqSpacer1w2h;
  var  Spacer2w1h           = ReqSpacer2w1h;
  var  Spacer4w4h           = ReqSpacer4w4h;


  // Netscape 6.x (Mozilla) renders table cells with graphics
  // incorrectly inside of <div> tags that are rewritten on the fly
  //
  if (WWHFrame.WWHBrowserInfo.mBrowser == 4)  // Shorthand for Netscape 6.x (Mozilla)
  {
    Spacer1w2h = "";
    Spacer2w1h = "";
    Spacer4w4h = "";
  }

  FormattedText += "<table width=\"" + ParamWidth + "\" border=0 cellspacing=0 cellpadding=0 bgcolor=\"" + BackgroundColor + "\">";
  FormattedText += " <tr>";
  FormattedText += "  <td height=2 colspan=6 bgcolor=\"" + BorderColor + "\">" + Spacer1w2h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=4 colspan=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 colspan=2 bgcolor=\"" + TitleBackgroundColor + "\">" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += "  <td>" + ReqSpacer4w4h + "</td>";
  FormattedText += "  <td bgcolor=\"" + TitleBackgroundColor + "\" width=\"100%\" align=\"left\" valign=\"middle\"><nobr><span style=\"" + Settings.mTitleFontStyle + " ; color: " + TitleForegroundColor + "\">" + ReqSpacer4w4h + "" + WWHFrame.WWHHelp.mMessages.mRelatedTopicsIconLabel + "</span></nobr></td>";
  FormattedText += "  <td bgcolor=\"" + TitleBackgroundColor + "\" width=\"16\" align=\"right\" valign=\"middle\"><nobr><a href=\"javascript:WWHFrame.WWHRelatedTopics.fHide();\"><img src=\"" + ImageDir + "/close.gif\" border=0 width=16 height=15></a>" + ReqSpacer4w4h + "</nobr></td>";
  FormattedText += "  <td>" + ReqSpacer4w4h + "</td>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 colspan=2 bgcolor=\"" + TitleBackgroundColor + "\">" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=4 colspan=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += "  <td>" + ReqSpacer4w4h + "</td>";
  FormattedText += "  <td colspan=2 width=\"100%\" id=\"" + ParamTextID + "\">" + ParamText + "</td>";
  FormattedText += "  <td>" + ReqSpacer4w4h + "</td>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td height=4 colspan=4>" + Spacer4w4h + "</td>";
  FormattedText += "  <td height=4 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td height=2 colspan=6 bgcolor=\"" + BorderColor + "\">" + Spacer1w2h + "</td>";
  FormattedText += " </tr>";
  FormattedText += "</table>";

  return FormattedText;
}
