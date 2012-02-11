// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHSwitch_Object()
{
  this.mParameters = "";
  this.mbSingle    = false;
  this.mbForceJS   = false;
  this.mSettings   = new WWHCommonSettings_Object();

  this.fExec        = WWHSwitch_Exec;
  this.fProcessURL  = WWHSwitch_ProcessURL;
  this.fJavaEnabled = WWHSwitch_JavaEnabled;
  this.fJavaCapable = WWHSwitch_JavaCapable;
  this.fSwitch      = WWHSwitch_Switch;
}

function  WWHSwitch_Exec(bParamNormalizeURL,
                         ParamURL)
{
  var  TargetURL   = ParamURL;
  var  bUseJava    = false;
  var  FrameSetURL = "";


  // Normalize URL if necessary
  //
  if (bParamNormalizeURL)
  {
    TargetURL = WWHStringUtilities_NormalizeURL(ParamURL);
  }

  // Process parameters
  //
  this.fProcessURL(TargetURL);

  // Use single frame if specified
  //
  if (this.mbSingle)
  {
    FrameSetURL = "../../common/html/wwhelp.htm";
  }
  else
  {
    // Determine if Java version can be used
    //
    if (this.mbForceJS)
    {
      bUseJava = false;
    }
    else if (this.mSettings.mbForceJavaScript)
    {
      bUseJava = false;
    }
    else
    {
      // See if Java is enabled
      //
      if (this.fJavaEnabled())
      {
        bUseJava = this.fJavaCapable();
      }
    }

    // Pick frameset to use
    //
    if (bUseJava)
    {
      FrameSetURL = "../../java/html/wwhelp.htm";
    }
    else
    {
      FrameSetURL = "../../js/html/wwhelp.htm";
    }
  }

  // Switch to frameset
  //
  this.fSwitch(FrameSetURL);
}

function  WWHSwitch_ProcessURL(ParamURL)
{
  // Reset members
  //
  this.mParameters = "";
  this.mbSingle    = false;
  this.mbForceJS   = false;

  if (ParamURL.indexOf("?") != -1)
  {
    var  Parts;
    var  Parameters;


    Parts = ParamURL.split("?");
    Parameters = Parts[1];

    if (Parameters.indexOf("&") != -1)
    {
      var  MaxIndex;
      var  Index;


      // Reset parameters to drop single and forcejs if present
      //
      this.mParameters = "";

      Parts = Parameters.split("&");
      for (MaxIndex = Parts.length, Index = 0 ; Index < MaxIndex ; Index++)
      {
        if (Parts[Index] == "single=true")
        {
          this.mbSingle = true;
        }
        else if (Parts[Index] == "forcejs=true")
        {
          this.mbForceJS = true;
        }
        else
        {
          if (this.mParameters.length > 0)
          {
            this.mParameters += "&";
          }

          this.mParameters += Parts[Index];
        }
      }
    }
    else if (Parameters == "single=true")
    {
      this.mParameters = "";
      this.mbSingle    = true;
    }
    else if (Parameters == "forcejs=true")
    {
      this.mParameters = "";
      this.mbForceJS   = true;
    }
    else
    {
      this.mParameters = Parameters;
    }

    // Prefix parameters with "?" if parameters exist
    //
    if (this.mParameters.length > 0)
    {
      this.mParameters = "?" + this.mParameters;
    }
  }
}

function  WWHSwitch_JavaEnabled()
{
  var  bJavaEnabled = false;


  // Check if Java is enabled
  //
  if ((typeof navigator != "undefined") &&
      (navigator != null))
  {
    bJavaEnabled = navigator.javaEnabled();
  }

  return bJavaEnabled;
}

function  WWHSwitch_JavaCapable()
{
  var  bJavaCapable = false;
  var  Browser  = WWHFrame.WWHBrowserInfo.mBrowser;
  var  Platform = WWHFrame.WWHBrowserInfo.mPlatform;


  // Determine if platform supports Java version
  //
  if (Browser == 1)  // Shorthand for Netscape
  {
    if (Platform == 1)  // Shorthand for Windows
    {
      bJavaCapable = true;  // Java works on NS for Windows
    }
    else if (Platform == 2)  // Shorthand for Macintosh
    {
      bJavaCapable = false;  // Java doesn't work on NS for Macintosh
    }
    else
    {
      bJavaCapable = false;  // Java is too slow on NS for UNIX
    }
  }
  else if (Browser == 4)  // Shorthand for Netscape
  {
    // LiveConnect broken under Netscape 6.0 on Windows and Mac
    //
    bJavaCapable = false;  // Play it safe.
  }
  else  // Assume IE
  {
    if (Platform == 1)  // Shorthand for Windows
    {
      bJavaCapable = true;  // Java works on IE for Windows

      if (WWHFrame.WWHBrowserInfo.mbIEWindowsXP)
      {
        bJavaCapable = false;  // Java not included under Windows XP
      }
    }
    else if (Platform == 2)  // Shorthand for Macintosh
    {
      bJavaCapable = false;  // LiveConnect not currently working on Macintosh
    }
    else
    {
      bJavaCapable = false;  // Java doesn't work on IE for UNIX
    }
  }

  return bJavaCapable;
}

function  WWHSwitch_Switch(ParamFrameSetURL)
{
  var  SwitchURL;


  // Add parameters to redirect
  //
  SwitchURL = ParamFrameSetURL + this.mParameters;

  // Switch to desired frameset
  // Delay required since this page is processing the action
  //
  setTimeout("location.replace(\"" + WWHStringUtilities_EscapeURLForJavaScriptAnchor(SwitchURL) + "\");", 1);
}
