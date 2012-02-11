package com.cisco.eManager.eManager.network;

import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import com.tibco.tibrv.*;
import com.cisco.eManager.common.register.deRegistration.*;
import com.cisco.eManager.common.register.registration.*;
import com.cisco.eManager.common.register.reRegistration.*;
import com.cisco.eManager.common.register.solutionRegistration.*;
import com.cisco.eManager.common.util.RegistrationMsg;
import com.cisco.eManager.common.util.AppInstanceDeregistrationMsg;
import com.cisco.eManager.common.util.AppInstanceRegistrationMsg;
import com.cisco.eManager.common.util.AppInstanceReRegistrationMsg;
import com.cisco.eManager.common.util.SolutionRegistrationMsg;
import com.cisco.eManager.eManager.inventory.InventoryManager;
import com.cisco.eManager.eManager.util.GlobalProperties;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.*;

public class MessageListener
    extends Thread
    implements TibrvMsgCallback
{
    private static Logger logger = Logger.getLogger(MessageListener.class);

    public static final int MODE_APP_REGISTRATION = 1;
    public static final int MODE_APP_REREGISTRATION = 2;
    public static final int MODE_APP_DEREGISTRATION = 3;
    public static final int MODE_SOLUTION_REGISTRATION = 4;

    private int m_mode;
    private InventoryManager m_im;

    // RVD transport parameters
    private String m_service = null;
    private String m_network = null;
    private String m_daemon = null;

    // Subject we use to listen messages on
    private String m_subject;

    // Our unique CM name
    private String cmname = m_subject + ".cm";

    private TibrvQueue m_queue = null;
    private TibrvRvdTransport m_rvdTransport = null;
    private TibrvTransport m_transport = null;
    private TibrvListener m_listener = null;

    public MessageListener(String threadName, int mode) throws Exception
    {
        super(threadName);
        GlobalProperties gp = GlobalProperties.instance();
        m_service =
            gp.getProperties().getProperty("SYSTEM.tibrv.service", "7500");
        logger.debug("using service " + m_service);
        m_network =
            gp.getProperties().getProperty("SYSTEM.tibrv.network", "");
        logger.debug("using network " + m_network);
        m_daemon =
            gp.getProperties().getProperty("SYSTEM.tibrv.daemon", "tcp:7500");
        logger.debug("using daemon " + m_daemon);

        if ( mode == MODE_APP_REGISTRATION )
        {
            m_subject = AppInstanceRegistrationMsg.SUBJECT;
        }
        else if ( mode == MODE_APP_REREGISTRATION )
        {
            m_subject = AppInstanceReRegistrationMsg.SUBJECT;
        }
        else if ( mode == MODE_APP_DEREGISTRATION )
        {
            m_subject = AppInstanceDeregistrationMsg.SUBJECT;
        }
        else if ( mode == MODE_SOLUTION_REGISTRATION )
        {
            m_subject = SolutionRegistrationMsg.SUBJECT;
        }
        else
        {
            throw new Exception("invalid listening mode: " + mode);
        }
        m_mode = mode;
        m_im = InventoryManager.instance();
    }

    public void run()
    {
        // open Tibrv in native implementation
        try
        {
            Tibrv.open(Tibrv.IMPL_NATIVE);
        }
        catch (TibrvException e)
        {
            logger.error("Failed to open Tibrv in native implementation:");
            e.printStackTrace();
            System.exit(0);
        }

        // Create event queue, transport and listener
        try
        {
            m_queue = new TibrvQueue();
            m_transport = new TibrvRvdTransport(m_service, m_network, m_daemon);
            m_transport.setDescription("eManagerMessageListener");
            m_listener = new TibrvListener(m_queue,
                                           this,
                                           m_transport,
                                           m_subject,
                                           null);
            logger.info("listening on subject \"" + m_subject + "\"");
        }
        catch (TibrvException e)
        {
            logger.error("Failed to create queue, transport or listener: ");
            e.printStackTrace();
            System.exit(0);
        }

        // Report we are running Ok
        logger.debug("Listening on subject: " + m_subject);

        // Dispatch queue
        TibrvDispatcher disp = new TibrvDispatcher(m_queue);

        // This example never quits...
        // If we would close Tibrv this join() would go through,
        // but because we never close Tibrv we'll get stuck
        // inside the join() call forever.
        try
        {
            disp.join();
        }
        catch (InterruptedException e)
        {
            System.exit(0);
        }
    }

    public void onMsg(TibrvListener listener, TibrvMsg msg)
    {
        logger.debug("enter");
        logger.info("message received: message = " + msg.toString());
        logger.debug("listener subject = " + listener.getSubject());
        logger.debug("message subject = " + msg.getSendSubject());
        String reason = null;
        TibrvMsg reply = new TibrvMsg();
        String replySubject = msg.getReplySubject();
        if ( replySubject == null )
        {
            replySubject = listener.getSubject() + "Response";
            logger.debug("message does not provide a reply subject - using " +
                         "listener's reply subject \"" + replySubject + "\"");
        }
        else
        {
            logger.debug("using message's reply subject \"" + replySubject +
                         "\"");
        }

        try
        {
            reply.setSendSubject(replySubject);
            reply.update(RegistrationMsg.RESPONSE_RETURN_CODE,
                         RegistrationMsg.RESPONSE_RETURN_CODE_SUCCESS);

            // process the message
            // - extract the xml string, parsing it into an object suitable for
            //   handling the registration
            String xmlString = null;
            xmlString = (String)msg.get(RegistrationMsg.FIELD_NAME);
            if (xmlString == null)
            {
                reason = "null argument received in message";
                logger.info(reason);
                reply.update(RegistrationMsg.RESPONSE_RETURN_CODE,
                             RegistrationMsg.RESPONSE_RETURN_CODE_FAILURE);
                reply.update(RegistrationMsg.RESPONSE_DESCRIPTION, reason);
                m_transport.send(reply);
                return;
            }

            // process the message
            String results = null;
            StringReader strRdr = new StringReader(xmlString);
            switch (m_mode)
            {
                case MODE_APP_REGISTRATION:
                {
                    Registration message = null;
                    try
                    {
                        message =
                            (Registration)Registration.unmarshalRegistration(strRdr);
                    }
                    catch (ValidationException ex)
                    {
                        results = "ValidationException caught while unmarshaling " +
                                  "Registration message: " + ex;
                    }
                    catch (MarshalException ex)
                    {
                        results = "MarshalException caught while unmarshaling " +
                                  "Registration message: " + ex;
                    }
                    if (results == null)
                    {
                        results = m_im.newRegistration(message);
                    }
                    break;
                }
                case MODE_APP_REREGISTRATION:
                {
                    ReRegistration message = null;
                    try
                    {
                        message =
                            (ReRegistration)
                            ReRegistration.unmarshalReRegistration(strRdr);
                    }
                    catch (ValidationException ex1)
                    {
                        results = "ValidationException caught while unmarshaling " +
                                  "Registration message: " + ex1;
                    }
                    catch (MarshalException ex1)
                    {
                        results = "MarshalException caught while unmarshaling " +
                                  "Registration message: " + ex1;
                    }
                    if (results == null)
                    {
                        results = m_im.newReregistration(message);
                    }
                    break;
                }
                case MODE_APP_DEREGISTRATION:
                {
                    DeRegistration message = null;
                    try
                    {
                        logger.debug("unmarshalling xml stream into " +
                                     "DeRegistration object");
                        message =
                            (DeRegistration)
                            DeRegistration.unmarshalDeRegistration(strRdr);
                        logger.debug("message unmarshalled");
                    }
                    catch (MarshalException ex2)
                    {
                        logger.error("MarshallException caught: " + ex2);
                        results = "MarshalException caught while " +
                                  "unmarshaling Registration message: " + ex2;
                    }
                    catch (ValidationException ex2)
                    {
                        logger.error("ValidationException caught: " + ex2);
                        results = "ValidationException caught while " +
                                  "unmarshaling Registration message: " + ex2;
                    }
                    if (results == null)
                    {
                        logger.debug("calling " +
                                     "InventoryManager.newDeregistration()");
                        results = m_im.newDeregistration(message);
                    }
                    break;
                }
                case MODE_SOLUTION_REGISTRATION:
                {
                    SolutionRegistration message = null;
                    try
                    {
                        message =
                            (SolutionRegistration)
                            SolutionRegistration.unmarshalSolutionRegistration(strRdr);
                    }
                    catch (ValidationException ex3)
                    {
                        results = "ValidationException caught while unmarshaling " +
                                  "Registration message: " + ex3;
                    }
                    catch (MarshalException ex3)
                    {
                        results = "MarshalException caught while unmarshaling " +
                                  "Registration message: " + ex3;
                    }
                    if (results == null)
                    {
                        results = m_im.newSolutionRegistration(message);
                    }
                    break;
                }
                default:
                    results = "message handler not initialized to handle messages";
            }

            if (results != null)
            {
                if (!results.equals(""))
                {
                    reason = "non-null results received from processing message: " +
                             results;
                    logger.info(reason);
                    reply.update(RegistrationMsg.RESPONSE_RETURN_CODE,
                                 RegistrationMsg.RESPONSE_RETURN_CODE_FAILURE);
                    reply.update(RegistrationMsg.RESPONSE_DESCRIPTION, results);
                    m_transport.send(reply);
                    return;
                }
                else
                {
                    reason = "message successfully processed";
                    logger.info(reason);
                    reply.update(RegistrationMsg.RESPONSE_RETURN_CODE,
                                 RegistrationMsg.RESPONSE_RETURN_CODE_SUCCESS);
                    reply.update(RegistrationMsg.RESPONSE_DESCRIPTION,
                                 RegistrationMsg.RESPONSE_DESCRIPTION_SUCCESS);
                    m_transport.send(reply);
                    return;
                }
            }
            else
            {
                reason = "message successfully processed";
                logger.info(reason);
                reply.update(RegistrationMsg.RESPONSE_RETURN_CODE,
                             RegistrationMsg.RESPONSE_RETURN_CODE_SUCCESS);
                reply.update(RegistrationMsg.RESPONSE_DESCRIPTION,
                             RegistrationMsg.RESPONSE_DESCRIPTION_SUCCESS);
                m_transport.send(reply);
                return;
            }
        }
        catch(Exception e)
        {
            logger.error("exception caught while processing message: " + e);
            e.printStackTrace();
            try
            {
                reply.update(RegistrationMsg.RESPONSE_RETURN_CODE,
                             RegistrationMsg.RESPONSE_RETURN_CODE_FAILURE);
                reply.update(RegistrationMsg.RESPONSE_DESCRIPTION,
                             "unable to extract registration string from request");
                m_transport.send(reply);
            }
            catch (Exception e1)
            {
                logger.error("exception caught while sending response: "
                             + e1);
                e1.printStackTrace();
            }
        }
        return;
    }
}
