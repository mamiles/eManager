// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHJavaMessages_Object()
{
  // Set default messages
  //
  WWHJavaMessages_Set_en(this);

  this.fSetByLocale = WWHJavaMessages_SetByLocale;
}

function  WWHJavaMessages_SetByLocale(ParamLocale)
{
  var  LocaleFunction = null;


  // Match locale
  //
  if ((ParamLocale.length > 1) &&
      (eval("typeof WWHJavaMessages_Set_" + ParamLocale) == "function"))
  {
    LocaleFunction = eval("WWHJavaMessages_Set_" + ParamLocale);
  }
  else if ((ParamLocale.length > 1) &&
           (eval("typeof WWHJavaMessages_Set_" + ParamLocale.substring(0, 2)) == "function"))
  {
    LocaleFunction = eval("WWHJavaMessages_Set_" + ParamLocale.substring(0, 2));
  }

  // Default already set, only override if locale found
  //
  if (LocaleFunction != null)
  {
    LocaleFunction(this);
  }
}

function  WWHJavaMessages_Set_en(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Please wait while the Java applet loads...";
}

function  WWHJavaMessages_Set_zh(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Java \u5e94\u7528\u5c0f\u7a0b\u5e8f\u6b63\u5728\u52a0\u8f7d\uff0c\u8bf7\u7a0d\u4faf...";
}

function  WWHJavaMessages_Set_es(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Espere mientras se carga el applet Java...";
}

function  WWHJavaMessages_Set_sv(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "V\u00e4nta medan Java-appletprogrammet l\u00e4ses in...";
}

function  WWHJavaMessages_Set_de(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Warten Sie, w\u00e4hrend das Java-Applet geladen wird...";
}

function  WWHJavaMessages_Set_it(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Attendere mentre le applet di Java vengono caricate...";
}

function  WWHJavaMessages_Set_pt(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Aguarde enquando o applet Java \u00e9 carregado...";
}

function  WWHJavaMessages_Set_zh_tw(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Java applet \u8f09\u5165\u4e2d\uff0c\u8acb\u7a0d\u5019...";
}

function  WWHJavaMessages_Set_ko(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "\uc790\ubc14 \uc560\ud50c\ub9bf\uc744 \ub85c\ub4dc\ud558\ub294 \ub3d9\uc548 \uc7a0\uc2dc \uae30\ub2e4\ub9ac\uc2ed\uc2dc\uc624...";
}

function  WWHJavaMessages_Set_fr(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Veuillez patienter pendant le chargement de l'applet Java...";
}

function  WWHJavaMessages_Set_ja(ParamMessages)
{
  // Startup Message
  //
  ParamMessages.mPleaseWait = "Java \u30a2\u30d7\u30ec\u30c3\u30c8\u304c\u8aad\u307f\u8fbc\u307e\u308c\u308b\u307e\u3067\u3057\u3070\u3089\u304f\u304a\u5f85\u3061\u304f\u3060\u3055\u3044\u3002";
}
