/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this 
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program 
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/

#include "watchdog.h"
#include <string.h>

Properties::Properties ()
{
  maxProperties = 10;
  properties = new char*[maxProperties];
  numProperties = 0;
}

Properties::~Properties ()
{
  for (int i = 0; i < numProperties; i++) {
    delete [] properties[i];
  }
  delete [] properties;
}

void
Properties::addProperty (char* property)
{
  increaseProperties ();
  properties[numProperties] = new char [strlen (property) + 3];
  sprintf(properties[numProperties], "-D%s", property);
  numProperties++;
}

char**
Properties::getProperties ()
{
  increaseProperties ();
  properties[numProperties] = NULL;
  return properties;
}

void
Properties::increaseProperties ()
{
  if (numProperties >= maxProperties + 1) {
    int newMaxProperties = maxProperties * 2;
    char** newProperties = new char*[newMaxProperties];
    for (int i = 0; i < numProperties; i++) {
      newProperties[i] = properties[i];
    }
    delete [] properties;
    maxProperties = newMaxProperties;
    properties = newProperties;
  }
}

void
Properties::printProperties ()
{
  if (debugFlag) {
    debug ("Properties:\n");
    for (int i = 0; i < numProperties; i++) {
      debug ("  %s\n", properties[i]);
    }
  }
}

int
Properties::getNumProperties()
{
	return numProperties;
}


Boolean
Properties::hasProperty (char* propertyName)
{
  int len = strlen (propertyName);
  for (int i = 0; i < numProperties; i++) {
    if (strncmp (properties[i], propertyName, len) == 0
	&& properties[i][len] == '=') {
      return True;
    }
  }
  return False;
}

