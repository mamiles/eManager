// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHJava_Object()
{
  this.mbAppletLoading = false;
  this.mSettings       = new WWHJavaSettings_Object();
  this.mMessages       = new WWHJavaMessages_Object();

  this.fInit           = WWHJava_Init;
  this.fGetAppletURL   = WWHJava_GetAppletURL;
  this.fAppletLoading  = WWHJava_AppletLoading;
  this.fAppletLoaded   = WWHJava_AppletLoaded;
  this.fAppletUnloaded = WWHJava_AppletUnloaded;

  // Adding this here so it will be accessable from Java
  //
  this.fNormalizeSearchWords = WWHStringUtilities_NormalizeSearchWords;

  // Load up messages
  //
  this.mMessages.fSetByLocale(WWHFrame.WWHHelp.mLocale);
}

function  WWHJava_Init()
{
  // Load applet
  //
  WWHFrame.WWHNavigationFrame.location.replace(WWHStringUtilities_RestoreEscapedSpaces(this.fGetAppletURL()));

  // Load rest of help system
  //
  WWHHelp.fInitStage(0);
}

function  WWHJava_GetAppletURL()
{
  var  AppletURL = "";


  // Pick which Java applet based on platform/browser info
  //
  if (WWHFrame.WWHBrowserInfo.mBrowser == 1)  // Shorthand for Netscape
  {
    if (this.mSettings.mbSecure)
    {
      AppletURL = "wwhelp/wwhimpl/java/html/netscape.htm";
    }
    else
    {
      AppletURL = "wwhelp/wwhimpl/java/html/nosecns.htm";
    }
  }
  else  // Assume IE
  {
    if (this.mSettings.mbSecure)
    {
      if (WWHFrame.WWHBrowserInfo.mPlatform == 1)  // Shorthand for Windows
      {
        if (WWHFrame.WWHBrowserInfo.mbWindowsIE60)
        {
          AppletURL = "wwhelp/wwhimpl/java/html/ie60win.htm";
        }
        else
        {
          AppletURL = "wwhelp/wwhimpl/java/html/iewindow.htm";
        }
      }
      else if (WWHFrame.WWHBrowserInfo.mPlatform == 2)  // Shorthand for Macintosh
      {
        AppletURL = "wwhelp/wwhimpl/java/html/iemac.htm";
      }
      else
      {
        AppletURL = "wwhelp/wwhimpl/java/html/iemac.htm";
      }
    }
    else
    {
      if (WWHFrame.WWHBrowserInfo.mbWindowsIE60)
      {
        AppletURL = "wwhelp/wwhimpl/java/html/nosecie6.htm";
      }
      else
      {
        AppletURL = "wwhelp/wwhimpl/java/html/nosecie.htm";
      }
    }
  }

  // Prefix location
  //
  AppletURL = WWHFrame.WWHHelp.mBaseURL + AppletURL;

  return  AppletURL;
}

function  WWHJava_AppletLoading()
{
  this.mbAppletLoading = true;
}

function  WWHJava_AppletLoaded()
{
  var  RedirectURL;
  var  Parts;


  if ( ! WWHFrame.WWHHandler.mbInitialized)
  {
    // Confirm applet started loading
    // If it failed to load and we reach this point, switch to JavaScript
    //
    if (this.mbAppletLoading)
    {
      // Indicate that handler was initialized
      //
      WWHFrame.WWHHandler.mbInitialized = true;

      // Hide status message and show applet
      //
      if (WWHFrame.WWHBrowserInfo.mBrowser == 1)  // Shorthand for Netscape 4.x
      {
// Netscape 4.x does not support hiding/showing applets within DIV tags correctly
//
//        WWHFrame.WWHNavigationFrame.document.layers["WWHPleaseWaitDIV"].visibility = "hide";
//        WWHFrame.WWHNavigationFrame.document.layers["WWHAppletDIV"].visibility = "visible";
      }
      else if (WWHFrame.WWHBrowserInfo.mBrowser == 2)  // Shorthand for IE
      {
        WWHFrame.WWHNavigationFrame.document.all["WWHPleaseWaitDIV"].style.visibility = "hidden";
        WWHFrame.WWHNavigationFrame.document.all["WWHAppletDIV"].style.visibility = "visible";
      }
      else if (WWHFrame.WWHBrowserInfo.mBrowser == 4)  // Shorthand for Netscape 6.x (Mozilla)
      {
// Netscape 6.0 does not yet support LiveConnect
//
//        WWHFrame.WWHNavigationFrame.document.getElementById["WWHPleaseWaitDIV"].style.visibility = "hidden";
//        WWHFrame.WWHNavigationFrame.document.getElementById["WWHAppletDIV"].style.visibility = "visible";
      }

      // Initialize applet size if necessary (Netscape Only)
      //
      if (typeof WWHFrame.WWHNavigationFrame.WWHNavigationFrame_InitSize == "function")
      {
        setTimeout("WWHFrame.WWHNavigationFrame.WWHNavigationFrame_InitSize();", 1);
      }

      // Complete initialization
      //
      WWHFrame.WWHHelp.fHandlerInitialized();
    }
    else
    {
      if (WWHFrame.WWHHelp.mInitStage == 0)
      {
        // User hit back button after using the applet
        //
        RedirectURL = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/java/html/wwhelp.htm";
      }
      else
      {
        // Java failed to load, switch to JavaScript
        //
        RedirectURL = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/html/wwhelp.htm";
      }

      // Keep any URL parameters specified
      //
      if (WWHFrame.WWHHelp.mLocationURL.indexOf("?") != -1)
      {
        Parts = WWHFrame.WWHHelp.mLocationURL.split("?");
        RedirectURL += "?" + Parts[1];
      }

      // Delay required since this page is processing the action
      //
      setTimeout("WWHFrame.location.replace(\"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(RedirectURL) + "\");", 1);
    }
  }
}

function  WWHJava_AppletUnloaded()
{
  if (WWHFrame.WWHBrowserInfo.mBrowser != 1)  // Shorthand for Netscape
  {
    WWHFrame.WWHHandler.mbInitialized = false;
    this.mbAppletLoading = false;
  }
}
