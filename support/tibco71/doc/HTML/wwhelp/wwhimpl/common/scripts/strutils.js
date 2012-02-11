// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHStringUtilities_SearchReplace(ParamString,
                                           ParamSearchString,
                                           ParamReplaceString)
{
  var  ResultString;
  var  Index;


  ResultString = ParamString;

  if ((ParamSearchString.length > 0) &&
      (ResultString.length > 0))
  {
    Index = 0;
    while ((Index = ResultString.indexOf(ParamSearchString, Index)) != -1)
    {
      ResultString = ResultString.substring(0, Index) + ParamReplaceString + ResultString.substring(Index + ParamSearchString.length, ResultString.length);
      Index += ParamReplaceString.length;
    }
  }

  return ResultString;
}

function  WWHStringUtilities_EscapeHTML(ParamHTML)
{
  var  EscapedHTML = ParamHTML;


  // Escape problematic characters
  // & < > "
  //
  EscapedHTML = WWHStringUtilities_SearchReplace(EscapedHTML, "&", "&amp;");
  EscapedHTML = WWHStringUtilities_SearchReplace(EscapedHTML, "<", "&lt;");
  EscapedHTML = WWHStringUtilities_SearchReplace(EscapedHTML, ">", "&gt;");
  EscapedHTML = WWHStringUtilities_SearchReplace(EscapedHTML, "\"", "&quot;");

  return EscapedHTML;
}

function  WWHStringUtilities_UnescapeHTML(ParamHTML)
{
  var  Text = ParamHTML;
  var  EscapedExpression;
  var  EscapedCharacter;
  var  CharacterCode;
  var  JavaScriptCharacter;


  // Unescape problematic characters
  //
  // & < > "
  //
  Text = WWHStringUtilities_SearchReplace(Text, "&amp;", "&");
  Text = WWHStringUtilities_SearchReplace(Text, "&lt;", "<");
  Text = WWHStringUtilities_SearchReplace(Text, "&gt;", ">");
  Text = WWHStringUtilities_SearchReplace(Text, "&quot;", "\"");

  // If any still exist, replace them with normal character
  //
  if (Text.indexOf("&#") != -1)
  {
    EscapedExpression = new RegExp("&#([0-9]+);");

    while (EscapedExpression.test(Text))
    {
      EscapedCharacter = EscapedExpression.lastMatch();
      CharacterCode = parseInt(EscapedExpression.$1);

      // Turn character code into escaped JavaScript character
      //
      JavaScriptCharacter = eval("\\u" + WWHStringUtilities_DecimalToHex(CharacterCode));

      // Replace in string
      //
      Text = WWHStringUtilities_SearchReplace(Text, EscapedCharacter, JavaScriptCharacter);
    }
  }

  return Text;
}

function  WWHStringUtilities_DecimalToHex(ParamNumber)
{
  var  HexNumber = "";


  HexNumber += WWHStringUtilities_HexDigit(ParamNumber >> 12);
  HexNumber += WWHStringUtilities_HexDigit(ParamNumber >>  8);
  HexNumber += WWHStringUtilities_HexDigit(ParamNumber >>  4);
  HexNumber += WWHStringUtilities_HexDigit(ParamNumber >>  0);

  return HexNumber;
}

function  WWHStringUtilities_HexDigit(ParamDigit)
{
  var  HexDigit;
  var  MaskedDigit = ParamDigit & 0x0F;


  // Translate to hex characters 'a' - 'f' if necessary
  //
  if (MaskedDigit == 10)
  {
    HexDigit = "a";
  }
  else if (MaskedDigit == 11)
  {
    HexDigit = "b";
  }
  else if (MaskedDigit == 12)
  {
    HexDigit = "c";
  }
  else if (MaskedDigit == 13)
  {
    HexDigit = "d";
  }
  else if (MaskedDigit == 14)
  {
    HexDigit = "e";
  }
  else if (MaskedDigit == 15)
  {
    HexDigit = "f";
  }
  else
  {
    HexDigit = MaskedDigit;
  }

  return HexDigit;
}

function  WWHStringUtilities_NormalizeURL(ParamURL)
{
  var  URL = ParamURL;


  // Unescape URL for most browsers
  //
  if (WWHFrame.WWHBrowserInfo.mbUnescapeHREFs)
  {
    URL = unescape(URL);
  }
  else  // IE unescapes everything automatically, except &
  {
    URL = WWHStringUtilities_SearchReplace(URL, "%26", "&");
  }

  // Standardize protocol case
  //
  if (URL.indexOf(":") != -1)
  {
    var  MaxIndex;


    Parts = URL.split(":");

    URL = Parts[0].toLowerCase();
    for (MaxIndex = Parts.length, Index = 1 ; Index < MaxIndex ; Index++)
    {
      URL += ":" + Parts[Index];
    }
  }

  // Handle drive letters under Windows
  //
  if (WWHFrame.WWHBrowserInfo.mPlatform == 1)  // Shorthand for Windows
  {
    var  DrivePattern = new RegExp("^file:[/]+([a-zA-Z])[:\|][/](.*)$", "i");
    var  DrivePatternMatch;


    DrivePatternMatch = DrivePattern.exec(URL);
    if (DrivePatternMatch != null)
    {
      URL = "file:///" + DrivePatternMatch[1] + ":/" + DrivePatternMatch[2];
    }
  }

  return URL;
}

function  WWHStringUtilities_RestoreEscapedSpaces(ParamURL)
{
  // Workaround for stupid Netscape 4.x bug
  //
  var  StringWithSpace = "x x";
  var  EscapedURL = ParamURL;


  if (WWHFrame.WWHBrowserInfo.mbUnescapeHREFs)
  {
    EscapedURL = WWHStringUtilities_SearchReplace(EscapedURL, StringWithSpace.substring(1, 2), "%20");
  }

  return EscapedURL;
}

function  WWHStringUtilities_EscapeURLForJavaScriptAnchor(ParamURL)
{
  var  EscapedURL = ParamURL;


  // Escape problematic characters
  // \ " ' < >
  //
  EscapedURL = WWHStringUtilities_SearchReplace(EscapedURL, "\\", "\\\\");
  EscapedURL = WWHStringUtilities_SearchReplace(EscapedURL, "\"", "\\u0022");
  EscapedURL = WWHStringUtilities_SearchReplace(EscapedURL, "'", "\\u0027");
  EscapedURL = WWHStringUtilities_SearchReplace(EscapedURL, "<", "\\u003c");
  EscapedURL = WWHStringUtilities_SearchReplace(EscapedURL, ">", "\\u003e");

  return EscapedURL;
}

function  WWHStringUtilities_EscapeForJavaScript(ParamString)
{
  var  EscapedString = ParamString;


  // Escape problematic characters
  // \ " '
  //
  EscapedString = WWHStringUtilities_SearchReplace(EscapedString, "\\", "\\\\");
  EscapedString = WWHStringUtilities_SearchReplace(EscapedString, "\"", "\\u0022");
  EscapedString = WWHStringUtilities_SearchReplace(EscapedString, "'", "\\u0027");
  EscapedString = WWHStringUtilities_SearchReplace(EscapedString, "\n", "\\u000a");
  EscapedString = WWHStringUtilities_SearchReplace(EscapedString, "\r", "\\u000d");

  return EscapedString;
}

function  WWHStringUtilities_EscapeRegExp(ParamWord)
{
  var  WordRegExpPattern = ParamWord;


  // Escape special characters
  // \ ( ) [ ] . ? + ^ $
  //
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "\\", "\\\\");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, ".", "\\.");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "?", "\\?");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "+", "\\+");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "|", "\\|");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "^", "\\^");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "$", "\\$");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "(", "\\(");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, ")", "\\)");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "{", "\\{");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "}", "\\}");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "[", "\\[");
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "]", "\\]");

  // Windows IE 4.0 is brain dead
  //
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "/", "[/]");

  // Convert * to .*
  //
  WordRegExpPattern = WWHStringUtilities_SearchReplace(WordRegExpPattern, "*", ".*");

  return WordRegExpPattern;
}

function  WWHStringUtilities_WordToRegExpPattern(ParamWord)
{
  var  WordRegExpPattern;


  // Escape special characters
  // Convert * to .*
  //
  WordRegExpPattern = WWHStringUtilities_EscapeRegExp(ParamWord);

  // Add ^ and $ to force whole string match
  //
  WordRegExpPattern = "^" + WordRegExpPattern + "$";

  return WordRegExpPattern;
}

function  WWHStringUtilities_WordToRegExpWithSpacePattern(ParamWord)
{
  var  WordRegExpPattern;


  // Escape special characters
  // Convert * to .*
  //
  WordRegExpPattern = WWHStringUtilities_EscapeRegExp(ParamWord);

  // Add ^ and $ to force whole string match
  // Allow trailing whitespace
  //
  WordRegExpPattern = "^" + WordRegExpPattern + " *$";

  return WordRegExpPattern;
}

function  WWHStringUtilities_ExtractStyleAttribute(ParamAttribute,
                                                   ParamFontStyle)
{
  var  Attribute = "";
  var  AttributeIndex;
  var  AttributeStart;


  AttributeIndex = ParamFontStyle.indexOf(ParamAttribute, 0);
  if (AttributeIndex != -1)
  {
    AttributeStart = ParamFontStyle.indexOf(":", AttributeIndex);

    if (AttributeStart != -1)
    {
      AttributeStart += 1;

      AttributeEnd = ParamFontStyle.indexOf(";", AttributeStart);
      if (AttributeEnd == -1)
      {
        AttributeEnd = ParamFontStyle.length;
      }

      Attribute = ParamFontStyle.substring(AttributeStart + 1, AttributeEnd);
    }
  }

  return Attribute;
}

function  WWHStringUtilities_NormalizeSearchWords(ParamSearchWords)
{
  // Workaround for stupid Netscape 4.x bug
  //
  var  StringWithSpace = "x x";
  var  Parts;
  var  MaxIndex;
  var  Index;
  var  bStripChar;
  var  CharAsInt;
  var  CharIndex;


  // Handle some common cases
  //
  Parts = ParamSearchWords.split(StringWithSpace.substring(1, 2));
  for (MaxIndex = Parts.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    // Remove leading characters @ % $
    //
    bStripChar = true;
    while ((bStripChar) &&
           (Parts[Index].length > 0))
    {
      bStripChar = false;

      if ((Parts[Index].indexOf("@") == 0) ||
          (Parts[Index].indexOf("%") == 0))
      {
        bStripChar = true;
      }
      else if (Parts[Index].indexOf("$") == 0)
      {
        // Handle $ in front of numbers
        //
        if (Parts[Index].length > 1)
        {
          CharAsInt = parseInt(Parts[Index].substring(1, 2));
          bStripChar = ((CharAsInt >= 0) && (CharAsInt <= 9)) ? false : true;
        }
        else
        {
          bStripChar = true;
        }
      }

      if (bStripChar)
      {
        Parts[Index] = Parts[Index].substring(1, Parts[Index].length);
      }
    }

    // Remove trailing characters ( ) % $ ; ,
    //
    bStripChar = true;
    while ((bStripChar) &&
           (Parts[Index].length > 0))
    {
      bStripChar = false;

      CharIndex = Parts[Index].length - 1;

      if ((Parts[Index].lastIndexOf("(") == CharIndex) ||
          (Parts[Index].lastIndexOf(")") == CharIndex) ||
          (Parts[Index].lastIndexOf("%") == CharIndex) ||
          (Parts[Index].lastIndexOf("$") == CharIndex) ||
          (Parts[Index].lastIndexOf(";") == CharIndex) ||
          (Parts[Index].lastIndexOf(",") == CharIndex))
      {
        bStripChar = true;
      }

      if (bStripChar)
      {
        Parts[Index] = Parts[Index].substring(0, CharIndex);
      }
    }

    if (Parts[Index].length > 0)
    {
      // Replace embedded characters @ : ,
      //
      Parts[Index] = WWHStringUtilities_SearchReplace(Parts[Index], "@", " ");
      Parts[Index] = WWHStringUtilities_SearchReplace(Parts[Index], ":", " ");
      Parts[Index] = WWHStringUtilities_SearchReplace(Parts[Index], ",", " ");
    }
  }

  return Parts.join(" ");
}

function  WWHStringBuffer_Object()
{
  this.mStringList        = new Array();
  this.mStringListEntries = 0;
  this.mSize              = 0;

  this.fSize      = WWHStringBuffer_Size;
  this.fReset     = WWHStringBuffer_Reset;
  this.fAppend    = WWHStringBuffer_Append;
  this.fGetBuffer = WWHStringBuffer_GetBuffer;
}

function  WWHStringBuffer_Size()
{
  return this.mSize;
}

function  WWHStringBuffer_Reset()
{
  this.mStringListEntries = 0;
  this.mSize              = 0;
}

function  WWHStringBuffer_Append(ParamString)
{
  this.mSize += ParamString.length;
  this.mStringList[this.mStringListEntries] = ParamString;
  this.mStringListEntries++;
}

function  WWHStringBuffer_GetBuffer()
{
  this.mStringList.length = this.mStringListEntries;

  return this.mStringList.join("");
}
