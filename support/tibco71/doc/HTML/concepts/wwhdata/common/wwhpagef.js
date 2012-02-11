// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHGetWWHFrame(ParamToBookDir)
{
  var  Frame = null;


  // Set reference to top level help frame
  //
  if ((typeof parent.WWHHelp != "undefined") &&
      (parent.WWHHelp != null))
  {
    Frame = eval("parent");
  }
  else if ((typeof parent.parent.WWHHelp != "undefined") &&
           (parent.parent.WWHHelp != null))
  {
    Frame = eval("parent.parent");
  }

  // Redirect if Frame is null
  //
  if (Frame == null)
  {
    var  bPerformRedirect = true;
    var  Agent;


    // No redirect if running Netscape 4.x
    //
    Agent = navigator.userAgent.toLowerCase();
    if ((Agent.indexOf("mozilla") != -1) &&
        (Agent.indexOf("spoofer") == -1) &&
        (Agent.indexOf("compatible") == -1))
    {
      var  MajorVersion;


      MajorVersion = parseInt(navigator.appVersion)
      if (MajorVersion < 5)
      {
        bPerformRedirect = false;  // Skip redirect for Netscape 4.x
      }
    }

    if (bPerformRedirect)
    {
      var  BaseFilename;


      BaseFilename = location.href.substring(location.href.lastIndexOf("/") + 1, location.href.length);

      if (ParamToBookDir.length > 0)
      {
        var  RelativePathList = ParamToBookDir.split("/");
        var  PathList         = location.href.split("/");
        var  BaseList = new Array();
        var  MaxIndex;
        var  Index;


        PathList.length--;
        for (MaxIndex = RelativePathList.length, Index = 0 ; Index < MaxIndex ; Index++)
        {
          if (RelativePathList[Index] == ".")
          {
            ;  // Do nothing!
          }
          else if (RelativePathList[Index] == "..")
          {
            if (BaseList.length == 0)
            {
              BaseList[BaseList.length] = PathList[PathList.length - 1];
              PathList.length = PathList.length - 1;
            }
            else
            {
              BaseList.length--;
            }
          }
          else
          {
            BaseList[BaseList.length] = RelativePathList[Index];
          }
        }

        BaseFilename = BaseList.join("/") + BaseFilename;
      }

      location.replace(WWHToWWHelpDirectory() + ParamToBookDir + "wwhelp/wwhimpl/common/html/wwhelp.htm?context=" + WWHBookData_Context() + "&file=" + BaseFilename);
    }
  }

  return Frame;
}

function  WWHShowPopup(ParamContext,
                       ParamLink,
                       ParamEvent)
{
  if (WWHFrame != null)
  {
    if ((ParamEvent == null) &&
        (typeof window.event != "undefined"))
    {
      ParamEvent = window.event;  // Older IE browsers only store event in window.event
    }

    WWHFrame.WWHHelp.fShowPopup(ParamContext, ParamLink, ParamEvent);
  }
}

function  WWHHidePopup()
{
  if (WWHFrame != null)
  {
    WWHFrame.WWHHelp.fHidePopup();
  }
}

function  WWHClickedPopup(ParamContext,
                          ParamLink)
{
  if (WWHFrame != null)
  {
    WWHFrame.WWHHelp.fClickedPopup(ParamContext, ParamLink);
  }
}

function  WWHUpdate()
{
  if (WWHFrame != null)
  {
    WWHFrame.WWHHelp.fUpdate(location.href);
  }
}

function  WWHAddRelatedTopic(ParamText,
                             ParamContext,
                             ParamFileURL)
{
  if (WWHFrame != null)
  {
    WWHFrame.WWHRelatedTopics.fAdd(ParamText, ParamContext, ParamFileURL);
  }
}

function  WWHRelatedTopicsInlineHTML()
{
  var  HTML = "";


  if (WWHFrame != null)
  {
    HTML = WWHFrame.WWHRelatedTopics.fInlineHTML();
  }

  return HTML;
}

function  WWHShowRelatedTopicsHREF()
{
  // Nothing to do.
  //
}

function  WWHShowRelatedTopicsPopup(ParamEvent)
{
  if (WWHFrame != null)
  {
    if ((ParamEvent == null) &&
        (typeof window.event != "undefined"))
    {
      ParamEvent = window.event;  // Older IE browsers only store event in window.event
    }

    WWHFrame.WWHRelatedTopics.fShowAtEvent(ParamEvent);
  }
}

function  WWHRelatedTopicsDivTag()
{
  var  RelatedTopicsDivTag = "";


  if (WWHFrame != null)
  {
    RelatedTopicsDivTag = WWHFrame.WWHRelatedTopics.mPopup.fDivTagText();
  }

  return RelatedTopicsDivTag;
}

function  WWHPopupDivTag()
{
  var  PopupDivTag = "";


  if (WWHFrame != null)
  {
    PopupDivTag = WWHFrame.WWHHelp.mPopup.fDivTagText();
  }

  return PopupDivTag;
}
