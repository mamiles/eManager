@rem NAME
@rem      $RCSfile: runapplication.bat,v $
@rem DESCRIPTION
@rem      [given below]
@rem DELTA
@rem      $Revision: 1.1 $
@rem      $Author: birgit $
@rem CREATED
@rem      $Date: 2002/10/22 10:49:42 $
@rem COPYRIGHT
@rem      Westhawk Ltd
@rem TO DO
@rem

@call .\setenv.bat

@set JAVA_HOME=C:\jdk1.1.7B
set CP=%TABLE%;%SNMPJAR%;%SOURCE%;.;

@%JAVA_HOME%\bin\java -classpath %CP% uk.co.westhawk.examplev3.%1
