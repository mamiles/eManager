// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHCommonMessages_Object()
{
  // Set default messages
  //
  WWHCommonMessages_Set_en(this);

  this.fSetByLocale = WWHCommonMessages_SetByLocale;
}

function  WWHCommonMessages_SetByLocale(ParamLocale)
{
  var  LocaleFunction = null;


  // Match locale
  //
  if ((ParamLocale.length > 1) &&
      (eval("typeof WWHCommonMessages_Set_" + ParamLocale) == "function"))
  {
    LocaleFunction = eval("WWHCommonMessages_Set_" + ParamLocale);
  }
  else if ((ParamLocale.length > 1) &&
           (eval("typeof WWHCommonMessages_Set_" + ParamLocale.substring(0, 2)) == "function"))
  {
    LocaleFunction = eval("WWHCommonMessages_Set_" + ParamLocale.substring(0, 2));
  }

  // Default already set, only override if locale found
  //
  if (LocaleFunction != null)
  {
    LocaleFunction(this);
  }
}

function  WWHCommonMessages_Set_en(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Show Navigation";
  ParamMessages.mSyncIconLabel           = "Show in Contents";
  ParamMessages.mPrevIconLabel           = "Previous";
  ParamMessages.mNextIconLabel           = "Next";
  ParamMessages.mRelatedTopicsIconLabel  = "Related Topics";
  ParamMessages.mEmailIconLabel          = "E-mail";
  ParamMessages.mPrintIconLabel          = "Print";
  ParamMessages.mBookmarkIconLabel       = "Bookmark";
  ParamMessages.mBookmarkLinkMessage     = "Right-click link and add it to your bookmarks.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Your web browser does not support the necessary features\\nrequired to view this page properly.  Supported browsers are:\\n\\n  IE4 and later on Windows and UNIX\\n  IE5 and later on Mac\\n  Netscape 6.1 and later on Windows, Mac, and UNIX\\n  Netscape 4.x on Windows, Mac, and UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_zh(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "\u663e\u793a\u5bfc\u822a";
  ParamMessages.mSyncIconLabel           = "\u5728\u76ee\u5f55\u4e2d\u663e\u793a";
  ParamMessages.mPrevIconLabel           = "\u4e0a\u4e00\u9875";
  ParamMessages.mNextIconLabel           = "\u4e0b\u4e00\u9875";
  ParamMessages.mRelatedTopicsIconLabel  = "\u76f8\u5173\u4e3b\u9898";
  ParamMessages.mEmailIconLabel          = "\u7535\u5b50\u90ae\u4ef6";
  ParamMessages.mPrintIconLabel          = "\u6253\u5370";
  ParamMessages.mBookmarkIconLabel       = "\u4e66\u7b7e";
  ParamMessages.mBookmarkLinkMessage     = "\u53f3\u952e\u5355\u51fb\u94fe\u63a5\uff0c\u5c06\u5176\u6dfb\u52a0\u5230\u4e66\u7b7e\u4e2d\u3002";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "\u60a8\u7684 Web \u6d4f\u89c8\u5668\u4e0d\u652f\u6301\u6b63\u786e\u67e5\u770b\u672c\u9875\u8981\u6c42\u7684\\n\u5fc5\u9700\u529f\u80fd\u3002 \u652f\u6301\u7684\u6d4f\u89c8\u5668\u6709\uff1a\\n\\nIE4 \u53ca\u66f4\u9ad8\u7248\u672c\uff08Windows \u548c UNIX\uff09\\nIE5 \u53ca\u66f4\u9ad8\u7248\u672c\uff08Mac\uff09\\nNetscape 6.1 \u53ca\u66f4\u9ad8\u7248\u672c\uff08Windows\u3001Mac \u548c UNIX\uff09\\nNetscape 4.x\uff08Windows\u3001Mac \u548c UNIX\uff09\n\n\n\n";
}

function  WWHCommonMessages_Set_es(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Mostrar barra de navegaci\u00f3n";
  ParamMessages.mSyncIconLabel           = "Mostrar en Contenido";
  ParamMessages.mPrevIconLabel           = "Atr\u00e1s";
  ParamMessages.mNextIconLabel           = "Adelante";
  ParamMessages.mRelatedTopicsIconLabel  = "Temas relacionados";
  ParamMessages.mEmailIconLabel          = "E-mail";
  ParamMessages.mPrintIconLabel          = "Imprimir";
  ParamMessages.mBookmarkIconLabel       = "Marcador";
  ParamMessages.mBookmarkLinkMessage     = "Haga clic con el bot\u00f3n derecho del mouse en el v\u00ednculo para agregarlo a la lista de favoritos.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Su explorador de Internet no es compatible con las funciones\\nnecesarias para ver esta p\u00e1gina correctamente. Los exploradores compatibles son:\\n\\n IE4 y posteriores para Windows y UNIX\\n IE5 y posteriores para Mac\\n Netscape 6.1 y posteriores para Windows, Mac y UNIX\\n Netscape 4.x para Windows, Mac y UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_sv(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Visa navigering";
  ParamMessages.mSyncIconLabel           = "Visa i inneh\u00e5ll";
  ParamMessages.mPrevIconLabel           = "F\u00f6reg\u00e5ende";
  ParamMessages.mNextIconLabel           = "N\u00e4sta";
  ParamMessages.mRelatedTopicsIconLabel  = "N\u00e4rliggande information";
  ParamMessages.mEmailIconLabel          = "E-post";
  ParamMessages.mPrintIconLabel          = "Skriv ut";
  ParamMessages.mBookmarkIconLabel       = "Bokm\u00e4rke";
  ParamMessages.mBookmarkLinkMessage     = "H\u00f6gerklicka p\u00e5 l\u00e4nken och l\u00e4gg till den till dina bokm\u00e4rken.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Webbl\u00e4saren har inte de funktioner som kr\u00e4vs f\u00f6r\\natt sidan ska visas p\u00e5 r\u00e4tt s\u00e4tt. Webbl\u00e4sare som kan anv\u00e4ndas \u00e4r:\\n\\n IE4 och senare i Windows och UNIX\\n IE5 och senare i Mac\\n Netscape 6.1 och senare i Windows, Mac och UNIX\\n Netscape 4.x i Windows, Mac och UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_de(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Navigation anzeigen";
  ParamMessages.mSyncIconLabel           = "Im Inhalt anzeigen";
  ParamMessages.mPrevIconLabel           = "Zur\u00fcck";
  ParamMessages.mNextIconLabel           = "Weiter";
  ParamMessages.mRelatedTopicsIconLabel  = "Verwandte Themen";
  ParamMessages.mEmailIconLabel          = "E-Mail";
  ParamMessages.mPrintIconLabel          = "Drucken";
  ParamMessages.mBookmarkIconLabel       = "Lesezeichen";
  ParamMessages.mBookmarkLinkMessage     = "Klicken Sie mit der rechten Maustaste auf die Verkn\u00fcpfung, und f\u00fcgen Sie sie Ihren Lesezeichen hinzu.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Ihr Webbrowser unterst\u00fctzt die zum ordnungsgem\u00e4\u00dfen Anzeigen\\ndieser Seite erforderlichen Funktionen nicht. Folgende Browser werden unterst\u00fctzt:\\n\\n IE4 und h\u00f6her f\u00fcr Windows und UNIX\\n IE5 und h\u00f6her f\u00fcr Mac\\n Netscape 6.1 und h\u00f6her f\u00fcr Windows, Mac und UNIX\\n Netscape 4.x f\u00fcr Windows, Mac und UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_it(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Mostra navigazione";
  ParamMessages.mSyncIconLabel           = "Mostra in Contenuto";
  ParamMessages.mPrevIconLabel           = "Precedente";
  ParamMessages.mNextIconLabel           = "Avanti";
  ParamMessages.mRelatedTopicsIconLabel  = "Argomenti correlati";
  ParamMessages.mEmailIconLabel          = "E-mail";
  ParamMessages.mPrintIconLabel          = "Stampa";
  ParamMessages.mBookmarkIconLabel       = "Segnalibro";
  ParamMessages.mBookmarkLinkMessage     = "Fare clic con il tasto destro del mouse per aggiungere ai Segnalibri.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Il browser Web in uso non supporta le funzioni necessarie\\nper visualizzare correttamente questa pagina. I browser supportati sono:\\n\\n IE4 e versioni successive su Windows e UNIX\\n IE5 e versioni successive su Mac\\n Netscape 6.1 e versioni successive per Windows, Mac e UNIX\\n Netscape 4.x su Windows, Mac e UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_pt(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Mostrar navega\u00e7\u00e3o";
  ParamMessages.mSyncIconLabel           = "Mostrar em conte\u00fado";
  ParamMessages.mPrevIconLabel           = "Voltar";
  ParamMessages.mNextIconLabel           = "Avan\u00e7ar";
  ParamMessages.mRelatedTopicsIconLabel  = "T\u00f3picos relacionados";
  ParamMessages.mEmailIconLabel          = "E-mail";
  ParamMessages.mPrintIconLabel          = "Imprimir";
  ParamMessages.mBookmarkIconLabel       = "Favoritos";
  ParamMessages.mBookmarkLinkMessage     = "Clique com o bot\u00e3o direito no link para adicion\u00e1-lo aos seus Favoritos.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Seu navegador da web n\u00e3o suporta os recursos\\nnecess\u00e1rios \u00e0 visualiza\u00e7\u00e3o desta p\u00e1gina. Os navegadores suportados s\u00e3o:\\n\\n IE4 e vers\u00e3o posterior para Windows e UNIX\\n IE5 e vers\u00e3o posterior para Mac\\n Netscape 6.1 e vers\u00e3o posterior para Windows, Mac e UNIX\\n Netscape 4.x para Windows, Mac e UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_zh_tw(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "\u986f\u793a\u5c0e\u89bd";
  ParamMessages.mSyncIconLabel           = "\u986f\u793a\u5728\u76ee\u9304\u4e2d";
  ParamMessages.mPrevIconLabel           = "\u4e0a\u4e00\u9801";
  ParamMessages.mNextIconLabel           = "\u4e0b\u4e00\u9801";
  ParamMessages.mRelatedTopicsIconLabel  = "\u76f8\u95dc\u4e3b\u984c";
  ParamMessages.mEmailIconLabel          = "\u9ede\u5b50\u90f5\u4ef6";
  ParamMessages.mPrintIconLabel          = "\u5217\u5370";
  ParamMessages.mBookmarkIconLabel       = "\u66f8\u7c64";
  ParamMessages.mBookmarkLinkMessage     = "\u53f3\u9375\u6309\u4e00\u4e0b\u9023\u7d50\uff0c\u5c07\u5b83\u52a0\u5165\u5230\u66f8\u7c64\u4e2d\u3002";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Web \u700f\u89bd\u5668\u4e0d\u652f\u63f4\u6b63\u78ba\u6aa2\u8996\u672c\u9801\u9700\u8981\u7684\u529f\u80fd\u3002 \u652f\u63f4\u7684\u700f\u89bd\u5668\u5305\u62ec\uff1a\\n\\n Windows \u8207 UNIX \u4e0a\u652f\u63f4 IE 4 \u6216\u66f4\u65b0\u7248\u672c\\n Mac \u4e0a\u652f\u63f4 IE5 \u6216\u66f4\u65b0\u7248\u672c\\n Windows\u3001Mac \u8207 UNIX \u4e0a\u652f\u63f4 Netscape 6.1\\n Windows\u3001Mac \u8207 UNIX \u4e0a\u652f\u63f4 Netscape 4.x\n\n\n\n";
}

function  WWHCommonMessages_Set_ko(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "\ub124\ube44\uac8c\uc774\uc158 \ud45c\uc2dc";
  ParamMessages.mSyncIconLabel           = "\ucee8\ud150\uce20\uc5d0\uc11c \ud45c\uc2dc";
  ParamMessages.mPrevIconLabel           = "\uc774\uc804";
  ParamMessages.mNextIconLabel           = "\ub2e4\uc74c";
  ParamMessages.mRelatedTopicsIconLabel  = "\uad00\ub828 \ud56d\ubaa9";
  ParamMessages.mEmailIconLabel          = "\uba54\uc77c";
  ParamMessages.mPrintIconLabel          = "\uc778\uc1c4";
  ParamMessages.mBookmarkIconLabel       = "\ubd81\ub9c8\ud06c";
  ParamMessages.mBookmarkLinkMessage     = "\ub9c1\ud06c\ub97c \ub9c8\uc6b0\uc2a4 \uc624\ub978\ucabd \ub2e8\ucd94\ub85c \ud074\ub9ad\ud558\uc5ec \ubd81\ub9c8\ud06c\uc5d0 \ucd94\uac00\ud569\ub2c8\ub2e4.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "\uc6f9 \ube0c\ub77c\uc6b0\uc800\uac00 \uc774 \ud398\uc774\uc9c0\ub97c \uc62c\ubc14\ub974\uac8c \ud45c\uc2dc\ud558\ub294 \ub370 \\n\ud544\uc694\ud55c \uae30\ub2a5\uc744 \uc9c0\uc6d0\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4. \uc9c0\uc6d0\ub418\ub294 \ube0c\ub77c\uc6b0\uc800:\\n\\nInternet Explorer 4 \uc774\uc0c1(Windows, UNIX )\\n Internet Explorer 5 \uc774\uc0c1(Mac)\\n Netscape 6.1 \uc774\uc0c1(Windows, Mac, UNIX)\\n Netscape 4.x(Windows, Mac, UNIX)\n\n\n\n";
}

function  WWHCommonMessages_Set_fr(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "Navigation";
  ParamMessages.mSyncIconLabel           = "Afficher dans la table des mati\u00e8res";
  ParamMessages.mPrevIconLabel           = "Pr\u00e9c\u00e9dent";
  ParamMessages.mNextIconLabel           = "Suivant";
  ParamMessages.mRelatedTopicsIconLabel  = "Rubriques associ\u00e9es";
  ParamMessages.mEmailIconLabel          = "E-mail";
  ParamMessages.mPrintIconLabel          = "Imprimer";
  ParamMessages.mBookmarkIconLabel       = "Ajouter aux Favoris";
  ParamMessages.mBookmarkLinkMessage     = "Cliquez sur ce lien avec le bouton droit de la souris et ajoutez-le \u00e0 vos Favoris.";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "Votre navigateur Web ne prend pas en charge les fonctions\\nrequises pour visualiser cette page de mani\u00e8re correcte. Les navigateurs pris en charge sont\u00a0:\\n\\n IE4 et version ult\u00e9rieure sous Windows et UNIX\\n IE5 et version ult\u00e9rieure sur Mac\\n Netscape 6.1 et version ult\u00e9rieure sous Windows, Mac et UNIX\\n Netscape 4.x sous Windows, Mac et UNIX\n\n\n\n";
}

function  WWHCommonMessages_Set_ja(ParamMessages)
{
  // Icon Labels
  //
  ParamMessages.mShowNavigationIconLabel = "\u30ca\u30d3\u30b2\u30fc\u30b7\u30e7\u30f3 \u30d0\u30fc\u306e\u8868\u793a";
  ParamMessages.mSyncIconLabel           = "\u5185\u5bb9\u306e\u8868\u793a";
  ParamMessages.mPrevIconLabel           = "\u524d\u3078";
  ParamMessages.mNextIconLabel           = "\u6b21\u3078";
  ParamMessages.mRelatedTopicsIconLabel  = "\u95a2\u9023\u30c8\u30d4\u30c3\u30af";
  ParamMessages.mEmailIconLabel          = "\u96fb\u5b50\u30e1\u30fc\u30eb";
  ParamMessages.mPrintIconLabel          = "\u5370\u5237";
  ParamMessages.mBookmarkIconLabel       = "\u30d6\u30c3\u30af\u30de\u30fc\u30af";
  ParamMessages.mBookmarkLinkMessage     = "\u30ea\u30f3\u30af\u3092\u53f3\u30af\u30ea\u30c3\u30af\u3057\u3066\u3001\u30d6\u30c3\u30af\u30de\u30fc\u30af\u306b\u8ffd\u52a0\u3057\u307e\u3059\u3002";

  // Browser support messages
  //
  ParamMessages.mBrowserNotSupported = "\u4f7f\u7528\u4e2d\u306e\u30d6\u30e9\u30a6\u30b6\u306f\u3001\u3053\u306e\u30da\u30fc\u30b8\u3092\u6b63\u3057\u304f\u8868\u793a\u3059\u308b\u305f\u3081\u306b\\n\u5fc5\u8981\u306a\u6a5f\u80fd\u3092\u30b5\u30dd\u30fc\u30c8\u3057\u3066\u3044\u307e\u305b\u3093\u3002 \u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u308b\u30d6\u30e9\u30a6\u30b6: \\n\\nWindows \u304a\u3088\u3073 UNIX \u4e0a\u3067\u5b9f\u884c\u3055\u308c\u308b IE4 \u4ee5\u964d\\nMac \u4e0a\u3067\u5b9f\u884c\u3055\u308c\u308b IE5 \u4ee5\u964d\\nWindows\u3001Mac\u3001UNIX \u4e0a\u3067\u5b9f\u884c\u3055\u308c\u308b Netscape 6.1 \u4ee5\u964d\\nWindows\u3001Mac\u3001UNIX \u4e0a\u3067\u5b9f\u884c\u3055\u308c\u308b Netscape 4.x\n\n\n\n";
}
