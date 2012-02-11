// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHJavaScriptMessages_Object()
{
  // Set default messages
  //
  WWHJavaScriptMessages_Set_en(this);

  this.fSetByLocale = WWHJavaScriptMessages_SetByLocale;
}

function  WWHJavaScriptMessages_SetByLocale(ParamLocale)
{
  var  LocaleFunction = null;


  // Match locale
  //
  if ((ParamLocale.length > 1) &&
      (eval("typeof WWHJavaScriptMessages_Set_" + ParamLocale) == "function"))
  {
    LocaleFunction = eval("WWHJavaScriptMessages_Set_" + ParamLocale);
  }
  else if ((ParamLocale.length > 1) &&
           (eval("typeof WWHJavaScriptMessages_Set_" + ParamLocale.substring(0, 2)) == "function"))
  {
    LocaleFunction = eval("WWHJavaScriptMessages_Set_" + ParamLocale.substring(0, 2));
  }

  // Default already set, only override if locale found
  //
  if (LocaleFunction != null)
  {
    LocaleFunction(this);
  }
}

function  WWHJavaScriptMessages_Set_en(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "Loading data...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Contents";
  ParamMessages.mTabsIndexLabel  = "Index";
  ParamMessages.mTabsSearchLabel = "Search";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "The current page could not be found in the table of contents.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "The index word or phrase you chose occurs in multiple documents.";
  ParamMessages.mIndexSelectMessage2 = "Please choose one.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Go!";
  ParamMessages.mSearchScopeAllLabel       = "All Available Books";
  ParamMessages.mSearchDefaultMessage      = "Type in the word(s) to search for:";
  ParamMessages.mSearchSearchingMessage    = "(searching)";
  ParamMessages.mSearchNothingFoundMessage = "(no results)";
  ParamMessages.mSearchRankLabel           = "Rank";
  ParamMessages.mSearchTitleLabel          = "Title";
  ParamMessages.mSearchBookLabel           = "Book";
}

function  WWHJavaScriptMessages_Set_zh(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "\u6b63\u5728\u52a0\u8f7d\u6570\u636e...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "\u76ee\u5f55";
  ParamMessages.mTabsIndexLabel  = "\u7d22\u5f15";
  ParamMessages.mTabsSearchLabel = "\u641c\u7d22";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "\u76ee\u5f55\u4e2d\u627e\u4e0d\u5230\u5f53\u524d\u9875\u3002";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "\u6240\u9009\u7684\u7d22\u5f15\u5b57\u8bcd\u51fa\u73b0\u5728\u591a\u4e2a\u6587\u6863\u4e2d\u3002";
  ParamMessages.mIndexSelectMessage2 = "\u8bf7\u9009\u62e9\u4e00\u4e2a\u3002";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "\u5f00\u59cb\uff01";
  ParamMessages.mSearchScopeAllLabel       = "\u6240\u6709\u4e66\u7c4d";
  ParamMessages.mSearchDefaultMessage      = "\u952e\u5165\u8981\u641c\u7d22\u7684\u5355\u8bcd\uff1a";
  ParamMessages.mSearchSearchingMessage    = "\uff08\u641c\u7d22\uff09";
  ParamMessages.mSearchNothingFoundMessage = "\uff08\u65e0\u7ed3\u679c\uff09";
  ParamMessages.mSearchRankLabel           = "\u7ea7\u522b";
  ParamMessages.mSearchTitleLabel          = "\u6807\u9898";
  ParamMessages.mSearchBookLabel           = "\u4e66\u7c4d";
}

function  WWHJavaScriptMessages_Set_es(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "Cargando datos...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Contenido";
  ParamMessages.mTabsIndexLabel  = "\u00cdndice";
  ParamMessages.mTabsSearchLabel = "Buscar";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "Esta p\u00e1gina no se encontr\u00f3 en el \u00edndice de contenidos.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "La frase o palabra del \u00edndice elegida aparece en varios documentos.";
  ParamMessages.mIndexSelectMessage2 = "Elija uno.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Ir";
  ParamMessages.mSearchScopeAllLabel       = "Todos los libros disponibles";
  ParamMessages.mSearchDefaultMessage      = "Escriba las palabras que desee buscar:";
  ParamMessages.mSearchSearchingMessage    = "(buscando)";
  ParamMessages.mSearchNothingFoundMessage = "(ning\u00fan resultado)";
  ParamMessages.mSearchRankLabel           = "Clase";
  ParamMessages.mSearchTitleLabel          = "T\u00edtulo";
  ParamMessages.mSearchBookLabel           = "Libro";
}

function  WWHJavaScriptMessages_Set_sv(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "L\u00e4ser in data...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Inneh\u00e5ll";
  ParamMessages.mTabsIndexLabel  = "Index";
  ParamMessages.mTabsSearchLabel = "S\u00f6k";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "Det gick inte att hitta den aktuella sidan i inneh\u00e5llsf\u00f6rteckningen.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "Indexordet eller frasen du valde f\u00f6rekommer i flera dokument.";
  ParamMessages.mIndexSelectMessage2 = "V\u00e4lj ett dokument.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Visa!";
  ParamMessages.mSearchScopeAllLabel       = "Alla tillg\u00e4ngliga b\u00f6cker";
  ParamMessages.mSearchDefaultMessage      = "Ange ord du vill s\u00f6ka:";
  ParamMessages.mSearchSearchingMessage    = "(s\u00f6ker)";
  ParamMessages.mSearchNothingFoundMessage = "(inga resultat)";
  ParamMessages.mSearchRankLabel           = "Relevans";
  ParamMessages.mSearchTitleLabel          = "Rubrik";
  ParamMessages.mSearchBookLabel           = "Bok";
}

function  WWHJavaScriptMessages_Set_de(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "Daten werden geladen...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Inhalt";
  ParamMessages.mTabsIndexLabel  = "Index";
  ParamMessages.mTabsSearchLabel = "Suchen";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "Die aktuelle Seite wurde nicht im Inhalt gefunden.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "Das von Ihnen gew\u00e4hlte Indexwort bzw. der gew\u00e4hlte Indexbegriff tritt in mehreren Dokumenten auf.";
  ParamMessages.mIndexSelectMessage2 = "W\u00e4hlen Sie ein Dokument aus.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Suchen";
  ParamMessages.mSearchScopeAllLabel       = "Alle verf\u00fcgbaren B\u00fccher";
  ParamMessages.mSearchDefaultMessage      = "Geben Sie das Wort bzw. die Worte ein, nach denen gesucht werden soll:";
  ParamMessages.mSearchSearchingMessage    = "(Suchen)";
  ParamMessages.mSearchNothingFoundMessage = "(keine Ergebnisse)";
  ParamMessages.mSearchRankLabel           = "Rang";
  ParamMessages.mSearchTitleLabel          = "Titel";
  ParamMessages.mSearchBookLabel           = "Buch";
}

function  WWHJavaScriptMessages_Set_it(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "Caricamento dati in corso...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Contenuto";
  ParamMessages.mTabsIndexLabel  = "Indice";
  ParamMessages.mTabsSearchLabel = "Cerca";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "La pagina corrente non \u00e8 stata trovata nel Sommario.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "La parola o la frase cercata nell'indice \u00e8 presente in pi\u00f9 documenti.";
  ParamMessages.mIndexSelectMessage2 = "Sceglierne uno.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Vai!";
  ParamMessages.mSearchScopeAllLabel       = "Tutti i libri disponibili";
  ParamMessages.mSearchDefaultMessage      = "Digitare le parole da cercare:";
  ParamMessages.mSearchSearchingMessage    = "(ricerca in corso)";
  ParamMessages.mSearchNothingFoundMessage = "(nessun risultato)";
  ParamMessages.mSearchRankLabel           = "Classe";
  ParamMessages.mSearchTitleLabel          = "Titolo";
  ParamMessages.mSearchBookLabel           = "Libro";
}

function  WWHJavaScriptMessages_Set_pt(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "Carregando dados...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Conte\u00fado";
  ParamMessages.mTabsIndexLabel  = "\u00cdndice remissivo";
  ParamMessages.mTabsSearchLabel = "Procurar";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "A p\u00e1gina atual n\u00e3o p\u00f4de ser encontrada no Sum\u00e1rio.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "A palavra ou frase escolhida no \u00edndice remissivo consta de mais de um documento.";
  ParamMessages.mIndexSelectMessage2 = "Escolha um.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Prosseguir";
  ParamMessages.mSearchScopeAllLabel       = "Todos os livros dispon\u00edveis";
  ParamMessages.mSearchDefaultMessage      = "Digite a(s) palavra(s) a ser(em) procurada(s):";
  ParamMessages.mSearchSearchingMessage    = "(procurando)";
  ParamMessages.mSearchNothingFoundMessage = "(nenhum resultado)";
  ParamMessages.mSearchRankLabel           = "Escopo";
  ParamMessages.mSearchTitleLabel          = "T\u00edtulo";
  ParamMessages.mSearchBookLabel           = "Livro";
}

function  WWHJavaScriptMessages_Set_zh_tw(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "\u8f09\u5165\u8cc7\u6599...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "\u76ee\u9304";
  ParamMessages.mTabsIndexLabel  = "\u7d22\u5f15";
  ParamMessages.mTabsSearchLabel = "\u641c\u5c0b";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "\u76ee\u9304\u4e2d\u627e\u4e0d\u5230\u76ee\u524d\u7db2\u9801\u3002";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "\u60a8\u9078\u64c7\u7684\u6587\u5b57\u6216\u8a5e\u5f59\u51fa\u73fe\u5728\u591a\u500b\u6587\u4ef6\u4e2d\u3002";
  ParamMessages.mIndexSelectMessage2 = "\u8acb\u9078\u64c7\u5176\u4e2d\u4e00\u9805\u3002";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "\u958b\u59cb\uff01";
  ParamMessages.mSearchScopeAllLabel       = "\u6240\u6709\u53ef\u7528\u66f8\u7c4d";
  ParamMessages.mSearchDefaultMessage      = "\u8f38\u5165\u8981\u641c\u5c0b\u6587\u5b57\uff1a";
  ParamMessages.mSearchSearchingMessage    = "(\u641c\u5c0b\u4e2d)";
  ParamMessages.mSearchNothingFoundMessage = "(\u6c92\u6709\u7d50\u679c)";
  ParamMessages.mSearchRankLabel           = "\u95dc\u806f\u6027";
  ParamMessages.mSearchTitleLabel          = "\u6a19\u984c";
  ParamMessages.mSearchBookLabel           = "\u66f8\u540d";
}

function  WWHJavaScriptMessages_Set_ko(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "\ub370\uc774\ud130 \ub85c\ub4dc \uc911...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "\ucee8\ud150\uce20";
  ParamMessages.mTabsIndexLabel  = "\uc0c9\uc778";
  ParamMessages.mTabsSearchLabel = "\uac80\uc0c9";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "\ubaa9\ucc28\uc5d0\uc11c \ud604\uc7ac \ud398\uc774\uc9c0\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "\uc120\ud0dd\ud55c \uc0c9\uc778 \ub2e8\uc5b4 \ub610\ub294 \uad6c\ubb38\uc774 \uc5ec\ub7ec \ubb38\uc11c\uc5d0 \uc874\uc7ac\ud569\ub2c8\ub2e4.";
  ParamMessages.mIndexSelectMessage2 = "\ud558\ub098\ub97c \uc120\ud0dd\ud558\uc2ed\uc2dc\uc624.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "\uac80\uc0c9";
  ParamMessages.mSearchScopeAllLabel       = "\ubaa8\ub4e0 \ucc45";
  ParamMessages.mSearchDefaultMessage      = "\uac80\uc0c9\uc5b4\ub97c \uc785\ub825\ud558\uc2ed\uc2dc\uc624.";
  ParamMessages.mSearchSearchingMessage    = "(\uac80\uc0c9 \uc911)";
  ParamMessages.mSearchNothingFoundMessage = "(\uacb0\uacfc \uc5c6\uc74c)";
  ParamMessages.mSearchRankLabel           = "\ub4f1\uae09";
  ParamMessages.mSearchTitleLabel          = "\uc81c\ubaa9";
  ParamMessages.mSearchBookLabel           = "\ucc45";
}

function  WWHJavaScriptMessages_Set_fr(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "Chargement des donn\u00e9es...";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "Table des mati\u00e8res";
  ParamMessages.mTabsIndexLabel  = "Index";
  ParamMessages.mTabsSearchLabel = "Rechercher";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "Page introuvable dans la table des mati\u00e8res.";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "Le terme ou l'expression d'index que vous avez choisis apparaissent dans plusieurs documents.";
  ParamMessages.mIndexSelectMessage2 = "S\u00e9lectionnez-en un.";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Lancer";
  ParamMessages.mSearchScopeAllLabel       = "Tous les livres disponibles";
  ParamMessages.mSearchDefaultMessage      = "Saisissez un ou plusieurs mots cl\u00e9s\u00a0:";
  ParamMessages.mSearchSearchingMessage    = "(recherche en cours)";
  ParamMessages.mSearchNothingFoundMessage = "(aucun r\u00e9sultat)";
  ParamMessages.mSearchRankLabel           = "Pertinence";
  ParamMessages.mSearchTitleLabel          = "Titre";
  ParamMessages.mSearchBookLabel           = "Livre";
}

function  WWHJavaScriptMessages_Set_ja(ParamMessages)
{
  // Initialization Messages
  //
  ParamMessages.mInitializingMessage = "\u30c7\u30fc\u30bf\u3092\u8aad\u307f\u8fbc\u3093\u3067\u3044\u307e\u3059\u3002";

  // Tab Labels
  //
  ParamMessages.mTabsTOCLabel    = "\u76ee\u6b21";
  ParamMessages.mTabsIndexLabel  = "\u7d22\u5f15";
  ParamMessages.mTabsSearchLabel = "\u691c\u7d22";

  // TOC Messages
  //
  ParamMessages.mTOCFileNotFoundMessage = "\u73fe\u5728\u306e\u30da\u30fc\u30b8\u306f\u76ee\u6b21\u5185\u3067\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002";

  // Index Messages
  //
  ParamMessages.mIndexSelectMessage1 = "\u9078\u629e\u3057\u305f\u7d22\u5f15\u306e\u5358\u8a9e\u307e\u305f\u306f\u53e5\u306f\u3001\u8907\u6570\u306e\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u5185\u3067\u4f7f\u7528\u3055\u308c\u3066\u3044\u307e\u3059\u3002";
  ParamMessages.mIndexSelectMessage2 = "1 \u3064\u306e\u30c9\u30ad\u30e5\u30e1\u30f3\u30c8\u3092\u9078\u629e\u3057\u3066\u304f\u3060\u3055\u3044\u3002";

  // Search Messages
  //
  ParamMessages.mSearchButtonLabel         = "Go!";
  ParamMessages.mSearchScopeAllLabel       = "\u5229\u7528\u53ef\u80fd\u306a\u3059\u3079\u3066\u306e\u30d6\u30c3\u30af";
  ParamMessages.mSearchDefaultMessage      = "\u691c\u7d22\u5bfe\u8c61\u8a9e:";
  ParamMessages.mSearchSearchingMessage    = "(\u691c\u7d22\u4e2d)";
  ParamMessages.mSearchNothingFoundMessage = "(\u7d50\u679c\u306a\u3057)";
  ParamMessages.mSearchRankLabel           = "\u30e9\u30f3\u30af";
  ParamMessages.mSearchTitleLabel          = "\u30bf\u30a4\u30c8\u30eb";
  ParamMessages.mSearchBookLabel           = "\u30d6\u30c3\u30af";
}
