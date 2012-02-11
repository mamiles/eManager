package com.cisco.eManager.common.util;

import java.util.*;
import com.cisco.eManager.common.inventory.*;

public abstract class RegistrationMsg
{
    public final static String FIELD_NAME = "DATA";
    public final static String RESPONSE_RETURN_CODE = "RETURN_CODE";
    public final static int RESPONSE_RETURN_CODE_SUCCESS = 0;
    public final static int RESPONSE_RETURN_CODE_FAILURE = -1;
    public final static String RESPONSE_DESCRIPTION = "DESCRIPTION";
    public final static String RESPONSE_DESCRIPTION_SUCCESS =
        "message successfully processed";

    public RegistrationMsg()
    {
        // ctor
    }

    public abstract String toXml();

    public abstract void fromXml(String xmlString);
}
