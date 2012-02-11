// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHTabs_Object()
{
  this.mWidth = null;

  this.fInit       = WWHTabs_Init;
  this.fDisplayTab = WWHTabs_DisplayTab;
  this.fHeadHTML   = WWHTabs_HeadHTML;
  this.fBodyHTML   = WWHTabs_BodyHTML;
}

function  WWHTabs_Init()
{
  var  InitialTab = -1;


  // Select first visible tab
  //
  if (WWHFrame.WWHJavaScript.mPanels.mPanelEntries.length > 0)
  {
    InitialTab = 0;

    // Calculate width based on number of displayed tabs
    //
    this.mWidth = "" + 100/WWHFrame.WWHJavaScript.mPanels.mPanelEntries.length + "%";
  }

  return InitialTab;
}

function  WWHTabs_DisplayTab()
{
  WWHFrame.WWHNavigationFrame.WWHTabsFrame.location.replace(WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/js/html/tabs.htm");
}

function  WWHTabs_HeadHTML()
{
  var  StylesHTML = "";


  // Generate style section
  //
  StylesHTML += "<style type=\"text/css\">\n";
  StylesHTML += " <!--\n";
  StylesHTML += "  a  { text-decoration: none ;\n";
  StylesHTML += "       color: " + WWHFrame.WWHJavaScript.mSettings.mTabs.mDefaultTabTextColor + " }\n";
  StylesHTML += "  th { color: " + WWHFrame.WWHJavaScript.mSettings.mTabs.mSelectedTabTextColor + " ;\n";
  StylesHTML += "       " + WWHFrame.WWHJavaScript.mSettings.mTabs.mFontStyle + " }\n";
  StylesHTML += "  td { color: " + WWHFrame.WWHJavaScript.mSettings.mTabs.mDefaultTabTextColor + " ;\n";
  StylesHTML += "       " + WWHFrame.WWHJavaScript.mSettings.mTabs.mFontStyle + " }\n";
  StylesHTML += " -->\n";
  StylesHTML += "</style>\n";

  return StylesHTML;
}

function  WWHTabs_BodyHTML()
{
  var  TabsHTML = "";
  var  Height = 21;
  var  MaxIndex;
  var  Index;
  var  CellType;
  var  BorderColor;
  var  BackgoundColor;
  var  WrapPrefix;
  var  WrapSuffix;
  var  OnClick;


  // Setup table for tab display
  //
  TabsHTML += "<table border=0 cellspacing=2 cellpadding=0 width=\"100%\">\n";
  TabsHTML += "<tr>\n";

  for (MaxIndex = WWHFrame.WWHJavaScript.mPanels.mPanelEntries.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    // Display anchor only if not selected
    //
    if (Index == WWHFrame.WWHJavaScript.mCurrentTab)
    {
      CellType = "th";
      BorderColor = WWHFrame.WWHJavaScript.mSettings.mTabs.mSelectedTabBorderColor;
      BackgroundColor = WWHFrame.WWHJavaScript.mSettings.mTabs.mSelectedTabColor;
      WrapPrefix = "<b>";
      WrapSuffix = "</b>";
      OnClick = "";
    }
    else
    {
      CellType = "td";
      BorderColor = WWHFrame.WWHJavaScript.mSettings.mTabs.mDefaultTabBorderColor;
      BackgroundColor = WWHFrame.WWHJavaScript.mSettings.mTabs.mDefaultTabColor;
      WrapPrefix = "<b><a href=\"javascript:WWHFrame.WWHJavaScript.fStartChangeTab(" + Index + ");\">";
      WrapSuffix = "</a></b>";
      OnClick = " onClick=\"WWHFrame.WWHJavaScript.fStartChangeTabWithDelay(" + Index + ");\"";
    }

    TabsHTML += "<td width=\"" + this.mWidth + "\" bgcolor=\"" + BorderColor + "\">";
    TabsHTML += "<table border=0 cellspacing=1 cellpadding=0 width=\"100%\">";
    TabsHTML += "<tr>";

    TabsHTML += "<" + CellType + " nowrap align=center height=" + Height + " width=\"" + this.mWidth + "\" bgcolor=\"" + BackgroundColor + "\"" + OnClick + ">";
    TabsHTML += WrapPrefix;
    TabsHTML += WWHFrame.WWHJavaScript.mPanels.mPanelEntries[Index].mPanelObject.mPanelTabTitle;
    TabsHTML += WrapSuffix;
    TabsHTML += "</" + CellType + ">";

    TabsHTML += "</tr>";
    TabsHTML += "</table>";
    TabsHTML += "</td>\n";
  }

  TabsHTML += "</tr>\n";
  TabsHTML += "</table>\n";

  return TabsHTML;
}
