// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHHighlightWords_Object()
{
  this.mWords = null;

  this.fSetWordList = WWHHighlightWords_SetWordList;
  this.fExec        = WWHHighlightWords_Exec;
}

function  WWHHighlightWords_SetWordList(ParamWords)
{
  if (WWHFrame.WWHHelp.mSettings.mbHighlightingEnabled)
  {
    this.mWords = ParamWords;
  }
  else
  {
    this.mWords = null;
  }
}

function  WWHHighlightWords_Exec()
{
  var  WordList;
  var  MaxIndex;
  var  Index;
  var  WordExpressions;
  var  LongestWordExpression;
  var  MaxExpressionIndex;
  var  ExpressionIndex;
  var  ExpressionHash = null;
  var  ExpressionEntry;
  var  Expression;
  var  LongestWordExpressionKey;
  var  NewHighlightedWords = null;
  var  HighlightedWords = null;
  var  TextRange;
  var  LastCharTextRange;
  var  bMatchFound;
  var  bFirstMatch;
  var  NewHighlightedWordsKey;


  if (this.mWords != null)
  {
    // Only works under IE on Windows
    //
    if ((WWHFrame.WWHBrowserInfo.mBrowser == 2) &&  // Shorthand for IE
        (WWHFrame.WWHBrowserInfo.mPlatform == 1))   // Shorthand for Windows
    {
      ExpressionHash   = new WWHHighlightWords_ExpressionHash_Object();
      HighlightedWords = new WWHHighlightWords_HighlightedWords_Object();
      bFirstMatch = true;

      // Access search words
      //
      WordList = this.mWords.split(" ");
      for (MaxIndex = WordList.length, Index = 0 ; Index < MaxIndex ; Index++)
      {
        if (WordList[Index].length > 0)
        {
          // Determine longest sub-expression between '*'
          //
          WordExpressions = WordList[Index].split("*");
          LongestWordExpression = "";
          for (MaxExpressionIndex = WordExpressions.length, ExpressionIndex = 0 ; ExpressionIndex < MaxExpressionIndex ; ExpressionIndex++)
          {
            if (WordExpressions[ExpressionIndex].length > LongestWordExpression.length)
            {
              LongestWordExpression = WordExpressions[ExpressionIndex];
            }
          }

          // Store search expression keyed by longest sub-expression
          //
          ExpressionEntry = ExpressionHash[LongestWordExpression + "~"];
          if (typeof ExpressionEntry == "undefined")
          {
            ExpressionEntry = new WWHHighlightWords_ExpressionEntry_Object();
            ExpressionHash[LongestWordExpression + "~"] = ExpressionEntry;
          }
          Expression = WWHStringUtilities_WordToRegExpWithSpacePattern(WordList[Index]);
          ExpressionEntry.mExpressions[ExpressionEntry.mExpressions.length] = new RegExp(Expression, "i");
        }
      }

      // Search document based on longest sub-expressions
      //
      for (LongestWordExpressionKey in ExpressionHash)
      {
        LongestWordExpression = LongestWordExpressionKey.substring(0, LongestWordExpressionKey.length - 1);
        NewHighlightedWords = new WWHHighlightWords_HighlightedWords_Object();

        TextRange = WWHFrame.WWHContentFrame.WWHDocumentFrame.document.body.createTextRange();
        TextRange.collapse();
        while (TextRange.findText(LongestWordExpression, 1))
        {
          TextRange.expand("word");
          ExpressionEntry = ExpressionHash[LongestWordExpression + "~"];

          // Check word against search expression
          //
          bMatchFound = false;
          MaxExpressionIndex = ExpressionEntry.mExpressions.length;
          ExpressionIndex = 0;
          while (( ! bMatchFound) &&
                 (ExpressionIndex < MaxExpressionIndex))
          {
            if (ExpressionEntry.mExpressions[ExpressionIndex].test(TextRange.text))
            {
              // Highlight text if not processed already
              //
              if (typeof HighlightedWords[TextRange.text + "~"] == "undefined")
              {
                // Record text highlighted for this expression
                //
                NewHighlightedWords[TextRange.text + "~"] = true;

                // Try to trim off trailing whitespace or .s
                //
                LastCharTextRange = TextRange.duplicate();
                LastCharTextRange.moveStart("character", TextRange.text.length - 1);

                if ((LastCharTextRange.text == " ") ||
                    (LastCharTextRange.text == ",") ||
                    (LastCharTextRange.text == "."))
                {
                  TextRange.moveEnd("character", -1);
                }

                TextRange.pasteHTML("<span style='background: " + WWHFrame.WWHHelp.mSettings.mHighlightingBackgroundColor + " ; color: " + WWHFrame.WWHHelp.mSettings.mHighlightingForegroundColor + "'>" + TextRange.htmlText + "</span>");

                if (bFirstMatch)
                {
                  TextRange.scrollIntoView();

                  bFirstMatch = false;
                }

                bMatchFound = true;
              }
            }

            ExpressionIndex++;
          }

          TextRange.collapse(false);
        }

        // Add highlighted words to hash
        //
        for (NewHighlightedWordsKey in NewHighlightedWords)
        {
          HighlightedWords[NewHighlightedWordsKey] = true;
        }
      }
    }
  }

  // Highlight words only once
  //
  this.mWords = null;
}

function  WWHHighlightWords_ExpressionHash_Object()
{
}

function  WWHHighlightWords_ExpressionEntry_Object()
{
  this.mExpressions = new Array();
}

function  WWHHighlightWords_HighlightedWords_Object()
{
}
