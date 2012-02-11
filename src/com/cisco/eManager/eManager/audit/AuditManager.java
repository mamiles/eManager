package com.cisco.eManager.eManager.audit;

import javax.swing.*;

import org.exolab.castor.xml.*;

import org.apache.log4j.*;

import com.tibco.tibrv.TibrvException;

import java.util.Date;
import java.util.Iterator;
import java.util.Collection;

import java.io.StringWriter;
import java.io.StringReader;

import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditAction;
import com.cisco.eManager.common.audit.AuditLogEntry;
import com.cisco.eManager.common.audit.AuditLogSearchCriteria;
import com.cisco.eManager.common.audit.AuditLogDeletionCriteria;
import com.cisco.eManager.common.audit.EmanagerAuditException;
import com.cisco.eManager.common.audit.EmanagerAuditStatusCode;

import com.cisco.eManager.common.audit2.AuditMgrMsg;
import com.cisco.eManager.common.audit2.AuditMgrResp;
import com.cisco.eManager.common.audit2.AuditLogEntries;
import com.cisco.eManager.common.audit2.DeleteAuditLogEntries;
import com.cisco.eManager.common.audit2.RetrieveAuditLogEntries;

import com.cisco.eManager.common.util.StatusResp;
import com.cisco.eManager.common.util.Rc;
import com.cisco.eManager.common.util.AccessType;

import com.cisco.eManager.common.database.EmanagerDatabaseException;

import com.cisco.eManager.eManager.database.DatabaseInterface;

import com.cisco.eManager.eManager.network.ClientTibcoMessageHandler;

public class AuditManager implements ClientTibcoMessageHandler
{
    private static Logger logger = Logger.getLogger(AuditManager.class);

    private static AuditManager auditManager = null;

    /**
     * @roseuid 3F4E5F870164
     */
    private AuditManager()
    {
    }

    public AuditLogEntry addAuditLogEntry (AuditDomain domain,
					   AuditAction action,
					   String subject,
					   Date time,
					   String userId,
					   String description) throws EmanagerAuditException,
								      EmanagerDatabaseException
    {
	if (domain == null) {
	    EmanagerAuditException e;

	    e = new EmanagerAuditException (EmanagerAuditStatusCode.MalformedDomain,
					    EmanagerAuditStatusCode.MalformedDomain.getStatusCodeDescription() +
					    domain);
	    throw e;
	}

	if (action == null) {
	    EmanagerAuditException e;

	    e = new EmanagerAuditException (EmanagerAuditStatusCode.MalformedAction,
					    EmanagerAuditStatusCode.MalformedAction.getStatusCodeDescription() +
					    action);
	    throw e;
	}

	if (subject == null ||
	    subject.trim().length() == 0) {
	    EmanagerAuditException e;

	    e = new EmanagerAuditException (EmanagerAuditStatusCode.MalformedSubject,
					    EmanagerAuditStatusCode.MalformedSubject.getStatusCodeDescription() +
					    subject);
	    throw e;
	}

	if (time == null) {
	    EmanagerAuditException e;

	    e = new EmanagerAuditException (EmanagerAuditStatusCode.MalformedTime,
					    EmanagerAuditStatusCode.MalformedTime.getStatusCodeDescription() +
					    time);
	    throw e;
	}

	if (userId == null ||
	    userId.trim().length() == 0) {
	    EmanagerAuditException e;

	    e = new EmanagerAuditException (EmanagerAuditStatusCode.MalformedUserId,
					    EmanagerAuditStatusCode.MalformedUserId.getStatusCodeDescription() +
					    userId);
	    throw e;
	}

	if (description == null ||
	    description.trim().length() == 0) {
	    EmanagerAuditException e;

	    e = new EmanagerAuditException (EmanagerAuditStatusCode.MalformedDescription,
					    EmanagerAuditStatusCode.MalformedDescription.getStatusCodeDescription() +
					    description);
	    throw e;
	}

	return DatabaseInterface.instance().createAuditLogEntry (domain,
								 action,
								 subject.trim(),
								 time,
								 userId.trim(),
								 description.trim());
    }

    /**
     * @param criteria
     * @return Collection
     */
    public Collection retrieveAuditLogEntries(AuditLogSearchCriteria criteria) throws EmanagerDatabaseException
    {
	return DatabaseInterface.instance().retrieveAuditLogEntries (criteria);
    }

    /**
     * @param criteria
     * @roseuid 3F4648F8035B
     */
    public void deleteAuditLogEntries(AuditLogDeletionCriteria criteria) throws EmanagerDatabaseException
    {
	DatabaseInterface.instance().removeAuditLogEntries (criteria);
    }

    /**
     * @return com.cisco.eManager.eManager.event.EventManager
     * @roseuid 3F53929D009E
     */
    public synchronized static AuditManager instance()
    {
        if (auditManager == null) {
	    auditManager = new AuditManager();
	}

        return auditManager;
    }

    public String handleRequest(String xmlStream,
				String userName,
				AccessType accessType) {
        StringWriter response = new StringWriter();
        AuditMgrMsg msg = null;
        AuditMgrResp resp = new AuditMgrResp();
	StatusResp statusResponse;
	Rc rc;

	String failureMessage = "";

	rc = new Rc();
	statusResponse = new StatusResp();
	statusResponse.setRc (rc);


        try
        {
            msg = AuditMgrMsg.unmarshal(new StringReader(xmlStream));

            if (msg.getRetrieveAuditLogEntries() != null) {
		Iterator                iter;
		Collection              auditLogList;
                RetrieveAuditLogEntries retrieveAuditLogEntriesMsg;
		AuditLogSearchCriteria  searchCriteria;
		AuditLogEntries         auditLogEntryResponse;
		AuditLogEntry           details;

		try {
		    retrieveAuditLogEntriesMsg = msg.getRetrieveAuditLogEntries();
		    searchCriteria = new AuditLogSearchCriteria (retrieveAuditLogEntriesMsg);
		    auditLogList = retrieveAuditLogEntries (searchCriteria);
		    if (auditLogList != null &&
			auditLogList.size() != 0) {
			iter = auditLogList.iterator();
			while (iter.hasNext()) {
			    details = (AuditLogEntry) iter.next();
			    auditLogEntryResponse = new AuditLogEntries();
                            details.populateTransportObject (auditLogEntryResponse);
			    resp.addAuditLogEntries (auditLogEntryResponse);
			}
		    } else {
                        statusResponse.setDescription ("No audit log entries found.");
                        resp.setNoDataResponse ("");
                    }

		    rc.setSuccess (1);
		}
		catch (EmanagerDatabaseException e) {
		    rc.setFailure (1);
		    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
		}
            } else if (msg.getDeleteAuditLogEntries() != null) {
		DeleteAuditLogEntries deleteAuditLogEntriesMsg;
		AuditLogDeletionCriteria criteria;

                criteria = null;
                try {
                    deleteAuditLogEntriesMsg = msg.getDeleteAuditLogEntries();
                    criteria = new AuditLogDeletionCriteria (deleteAuditLogEntriesMsg);
                    if (accessType == AccessType.WRITE) {
                        deleteAuditLogEntries(criteria);
                        rc.setSuccess (1);
                        resp.setNoDataResponse ("");
                    } else {
                        rc.setFailure (1);
                        statusResponse.setDescription ("Authorization failure: Insufficient permissions.");
                        resp.setNoDataResponse ("");
                        failureMessage = AuditGlobals.AuthorizationFailureIndicator;
		    }
                }
                catch (EmanagerDatabaseException e) {
                    rc.setFailure (1);
                    statusResponse.setDescription ("Database exception encountered: " + e.getMessage());
                    resp.setNoDataResponse ("");
                    failureMessage = AuditGlobals.ExecutionFailureIndicator;
		}

		// fix
		// need to implement criteria.toString()
		addAuditLogEntry (AuditDomain.Emanager,
				  AuditAction.Delete,
				  AuditGlobals.AuditSubjectKey + "=many",
				  new Date(),
				  userName,
				  failureMessage + "Criteria:" + criteria.toString());
            } else {
                resp.setNoDataResponse ("");
		rc.setWarning (1);
		statusResponse.setDescription ("No request encountered.");
            }

	    statusResponse.marshal (response);
	    resp.marshal (response);
        }
        catch (ValidationException ex)
        {
            logger.error("XML Validation Exception: " + ex.getMessage());
            resp.setNoDataResponse ("");
            rc.setFailure (1);
            statusResponse.setDescription ("Validation exception encountered w/xml message: " +
                                           ex.getMessage());
            try {
                statusResponse.marshal (response);
                resp.marshal(response);
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (MarshalException ex)
        {
            logger.error("XML Marshal Exception: " + ex.getMessage());
            resp.setNoDataResponse ("");
            rc.setFailure (1);
            statusResponse.setDescription ("Marshal exception encountered w/xml message: " +
                                           ex.getMessage());
            try {
                statusResponse.marshal (response);
                resp.marshal(response);
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }
        catch (Exception ex) {
            logger.error("Exception: " + ex.getMessage());
            resp.setNoDataResponse ("");
            rc.setFailure (1);
            statusResponse.setDescription ("Exception encountered: " + ex.getMessage());
            try {
                statusResponse.marshal (response);
                resp.marshal(response);
            }
            catch (ValidationException ex1) {
            }
            catch (MarshalException ex1) {
            }
        }

        return response.toString();
    }
}
