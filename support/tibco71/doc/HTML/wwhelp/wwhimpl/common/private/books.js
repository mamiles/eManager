// Copyright (c) 2000-2001 Quadralay Corporation.  All rights reserved.
//

function  WWHBookGroups_Books(ParamTop)
{


  ParamTop.fAddDirectory("concepts", null, null, null, null);
  ParamTop.fAddDirectory("install", null, null, null, null);
  ParamTop.fAddDirectory("adm", null, null, null, null);
  ParamTop.fAddDirectory("config", null, null, null, null);
  ParamTop.fAddDirectory("c_ref", null, null, null, null);
  ParamTop.fAddDirectory("cpp_ref", null, null, null, null);
  ParamTop.fAddDirectory("java_ref", null, null, null, null);
  ParamTop.fAddDirectory("com_ref", null, null, null, null);
}

function  WWHBookGroups_ShowBooks()
{
  return true;
}

function  WWHBookGroups_ExpandAllAtTop()
{
  return false;
}
