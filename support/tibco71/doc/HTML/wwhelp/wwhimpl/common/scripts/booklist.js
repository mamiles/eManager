// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHBook_Object(ParamDirectory)
{
  // Set values from callbacks
  //
  this.mDirectory = null;
  this.mTitle     = null;
  this.mContext   = null;
  this.mFiles     = new WWHFileList_Object();
  this.mPopups    = new WWHPopupHash_Object();

  // Fix up directory
  //
  if (ParamDirectory == ".")
  {
    this.mDirectory = "";
  }
  else
  {
    this.mDirectory = ParamDirectory + "/";
  }

  this.fInit = WWHBook_Init;
}

function  WWHBook_Init(ParamTitle,
                       ParamContext,
                       ParamFilesFunc,
                       ParamPopupsFunc)
{
  this.mTitle   = ParamTitle;
  this.mContext = ParamContext;

  // Load files
  //
  ParamFilesFunc(this.mFiles);

  // Load popups
  //
  ParamPopupsFunc(this.mPopups);
}

function  WWHBookList_Object()
{
  this.mInitIndex = 0;
  this.mBookList  = new Array();

  this.fInit_AddBookDir        = WWHBookList_Init_AddBookDir;
  this.fInit_BookData_Script   = WWHBookList_Init_BookData_Script;
  this.fInit_AddBook           = WWHBookList_Init_AddBook;
  this.fInit_IncrementIndex    = WWHBookList_Init_IncrementIndex;
  this.fHREFToFileIndex        = WWHBookList_HREFToFileIndex;
  this.fHREFToTitle            = WWHBookList_HREFToTitle;
  this.fGetBookIndexFileHREF   = WWHBookList_GetBookIndexFileHREF;
  this.fBookFileIndiciesToHREF = WWHBookList_BookFileIndiciesToHREF;
  this.fGetSyncPrevNext        = WWHBookList_GetSyncPrevNext;
  this.fGetContextBook         = WWHBookList_GetContextBook;
  this.fIsPopupClickable       = WWHBookList_IsPopupClickable;
  this.fGetPopupHTML           = WWHBookList_GetPopupHTML;
}

function  WWHBookList_Init_AddBookDir(ParamBookDir)
{
  this.mBookList[this.mBookList.length] = new WWHBook_Object(ParamBookDir);
}

function  WWHBookList_Init_BookData_Script()
{
  var  Scripts  = new WWHStringBuffer_Object();
  var  MaxIndex = 0;
  var  Index    = 0;
  var  BookDirectory;


  this.mInitIndex = 0;
  for (MaxIndex = this.mBookList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    BookDirectory = WWHFrame.WWHHelp.mHelpURLPrefix + WWHStringUtilities_RestoreEscapedSpaces(this.mBookList[Index].mDirectory);

    Scripts.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + BookDirectory + "wwhdata/common/mtitle.js\"></script>\n");
    Scripts.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + BookDirectory + "wwhdata/common/context.js\"></script>\n");
    Scripts.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + BookDirectory + "wwhdata/common/files.js\"></script>\n");
    Scripts.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + BookDirectory + "wwhdata/common/popups.js\"></script>\n");

    Scripts.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/scripts/bklist1s.js\"></script>\n");
  }

  return Scripts.fGetBuffer();
}

function  WWHBookList_Init_AddBook(ParamTitle,
                                   ParamContext,
                                   ParamFilesFunc,
                                   ParamPopupsFunc)
{
  // Update book information
  //
  this.mBookList[this.mInitIndex].fInit(ParamTitle, ParamContext, ParamFilesFunc, ParamPopupsFunc);
}

function  WWHBookList_Init_IncrementIndex()
{
  this.mInitIndex++;
}

function  WWHBookList_HREFToFileIndex(ParamIndex,
                                      ParamHREF)
{
  return this.mBookList[ParamIndex].mFiles.fHREFToIndex(ParamHREF);
}

function  WWHBookList_HREFToTitle(ParamIndex,
                                  ParamHREF)
{
  return this.mBookList[ParamIndex].mFiles.fHREFToTitle(ParamHREF);
}

function  WWHBookList_GetBookIndexFileHREF(ParamHREF)
{
  var  ResultArray = new Array(-1, null);
  var  LongestMatchIndex;
  var  MaxIndex;
  var  Index;


  // Find the book directory
  //
  LongestMatchIndex = -1;
  for (MaxIndex = this.mBookList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    if (ParamHREF.indexOf(this.mBookList[Index].mDirectory) == 0)
    {
      if (LongestMatchIndex == -1)
      {
        LongestMatchIndex = Index;
      }
      else if (this.mBookList[Index].mDirectory.length > this.mBookList[LongestMatchIndex].mDirectory.length)
      {
        LongestMatchIndex = Index;
      }
    }
  }

  // If LongestMatchIndex is valid, we found our book directory
  //
  if (LongestMatchIndex != -1)
  {
    var  Parts;
    var  FileHREF;


    // Set FileHREF to be just the file portion
    //
    if (this.mBookList[LongestMatchIndex].mDirectory.length > 0)
    {
      FileHREF = ParamHREF.substring(this.mBookList[LongestMatchIndex].mDirectory.length, ParamHREF.length);
    }
    else
    {
      FileHREF = ParamHREF;
    }

    ResultArray[0] = LongestMatchIndex;
    ResultArray[1] = FileHREF;
  }

  return ResultArray;
}

function  WWHBookList_BookFileIndiciesToHREF(ParamBookIndex,
                                             ParamFileIndex)
{
  return this.mBookList[ParamBookIndex].mDirectory + this.mBookList[ParamBookIndex].mFiles.fFileIndexToHREF(ParamFileIndex);
}

function  WWHBookList_GetSyncPrevNext(ParamHREF)
{
  var  ResultArray = new Array(null, null, null);
  var  ParsedHREF;
  var  BookIndex;
  var  FileHREF;
  var  FileIndex = -1;


  // Get BookIndex and FileHREF
  //
  BookIndexFileHREF = this.fGetBookIndexFileHREF(ParamHREF)
  BookIndex = BookIndexFileHREF[0];
  FileHREF  = BookIndexFileHREF[1];

  // If we've got a BookIndex, continue
  //
  if (BookIndex >= 0)
  {
    FileIndex = this.mBookList[BookIndex].mFiles.fHREFToIndex(escape(FileHREF));
  }

  // Set return results
  //
  if ((BookIndex >= 0) &&
      (FileIndex >= 0))
  {
    // Set sync
    //
    ResultArray[0] = ParamHREF;  // Indicates file found, sync possible

    // Set previous
    //
    if (FileIndex > 0)
    {
      ResultArray[1] = this.fBookFileIndiciesToHREF(BookIndex, FileIndex - 1);
    }
    else
    {
      if (BookIndex > 0)
      {
        ResultArray[1] = this.fBookFileIndiciesToHREF(BookIndex - 1, this.mBookList[BookIndex - 1].mFiles.mFileList.length - 1);
      }
    }

    // Set next
    //
    if ((FileIndex + 1) < this.mBookList[BookIndex].mFiles.mFileList.length)
    {
      ResultArray[2] = this.fBookFileIndiciesToHREF(BookIndex, FileIndex + 1);
    }
    else
    {
      if (((BookIndex + 1) < this.mBookList.length) &&
          (this.mBookList[BookIndex + 1].mFiles.mFileList.length > 0))
      {
        ResultArray[2] = this.fBookFileIndiciesToHREF(BookIndex + 1, 0);
      }
    }
  }

  return ResultArray;
}

function  WWHBookList_GetContextBook(ParamContext)
{
  var  ResultBook = null;
  var  MaxIndex;
  var  Index;


  for (MaxIndex = this.mBookList.length, Index = 0 ; Index < MaxIndex ; Index++)
  {
    if (this.mBookList[Index].mContext == ParamContext)
    {
      ResultBook = this.mBookList[Index];
    }
  }

  return ResultBook;
}

function  WWHBookList_IsPopupClickable(ParamContext,
                                       ParamLink)
{
  var  bPopupClickable = false;
  var  Book;


  Book = this.fGetContextBook(ParamContext)
  if (Book != null)
  {
    bPopupClickable = Book.mPopups.fIsPopupClickable(ParamLink);
  }

  return bPopupClickable;
}

function  WWHBookList_GetPopupHTML(ParamContext,
                                   ParamLink)
{
  var  PopupHTML = null;
  var  Book;


  Book = this.fGetContextBook(ParamContext)
  if (Book != null)
  {
    PopupHTML = Book.mPopups.fGetPopupHTML(ParamLink);
  }

  return PopupHTML;
}
