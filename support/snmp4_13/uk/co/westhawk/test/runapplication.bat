@rem NAME
@rem      $RCSfile: runapplication.bat,v $
@rem DESCRIPTION
@rem      [given below]
@rem DELTA
@rem      $Revision: 1.1 $
@rem      $Author: birgit $
@rem CREATED
@rem      $Date: 2002/11/11 15:02:32 $
@rem COPYRIGHT
@rem      Westhawk Ltd
@rem TO DO
@rem

@call .\setenv.bat

@set JAVA_HOME=C:\jre1.3.0
set CP=%TABLE%;%SNMPJAR%;%SOURCE%;.;%XML%;

%JAVA_HOME%\bin\java -classpath %CP% uk.co.westhawk.test.%1
