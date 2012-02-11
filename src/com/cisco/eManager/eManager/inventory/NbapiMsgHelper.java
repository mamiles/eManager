//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\NbapiMsgHelper.java

package com.cisco.eManager.eManager.inventory;

import java.util.*;

import com.cisco.eManager.common.inventory2.*;

public class NbapiMsgHelper
{
    public static ManagedObjectId[]
        appInstanceIds(com.cisco.eManager.eManager.inventory.host.Host imHost,
                       com.cisco.eManager.eManager.inventory.appType.AppType imAppType)
    {
        List imAppInstanceIds = imHost.appInstances(imAppType.id());
        int appInstanceCount = imAppInstanceIds.size();
        ManagedObjectId[] appInstanceIds = new ManagedObjectId[appInstanceCount];
        Iterator iter = imAppInstanceIds.iterator();
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance imAppInstance = null;
        for (int i = 0; i < appInstanceCount; i++)
        {
            imAppInstance =
                (com.cisco.eManager.eManager.inventory.appInstance.AppInstance)iter.next();
            appInstanceIds[i] = toManagedObjectId(imAppInstance.id());
        }
        return appInstanceIds;
    }

    public static Method[]
        methods(com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation imInstrumentation)
    {
        List imMethods = imInstrumentation.methods();
        int methodCount = imMethods.size();
        Method[] methods = new Method[methodCount];
        Iterator iter = imMethods.iterator();
        com.cisco.eManager.eManager.inventory.instrumentation.Method imMethod = null;
        for (int i = 0; i < methodCount; i++)
        {
            imMethod =
                (com.cisco.eManager.eManager.inventory.instrumentation.Method)iter.next();
            methods[i] = toMethod(imMethod);
        }
        return methods;
    }

    public static AppInstance
        toAppInstance(
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance imAppInstance)
    {
        AppInstance appInstance = new AppInstance();

        appInstance.setAppTypeId(toManagedObjectId(imAppInstance.appTypeId()));

        appInstance.setHostId(toManagedObjectId(imAppInstance.hostId()));

        appInstance.setId(toManagedObjectId(imAppInstance.id()));

        List imInstrumentations = imAppInstance.instrumentations();
        int size = imInstrumentations.size();
        ManagedObjectId[] instrumentationIds = new ManagedObjectId[size];
        Iterator iter = imInstrumentations.iterator();
        com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
            imInstrumentation = null;
        for (int i = 0; i < size; i++)
        {
            imInstrumentation =
                (com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation)
                iter.next();
            instrumentationIds[i] = toManagedObjectId(imInstrumentation.id());
        }
        appInstance.setInstrumentationIds(instrumentationIds);

        appInstance.setLogfileDirectories(imAppInstance.logfileDirectorySet());

        appInstance.setLogfilePassword(imAppInstance.logfilePassword());

        appInstance.setLogfileTransport(toTransport(imAppInstance.logfileTransport()));

        appInstance.setLogfileUsername(imAppInstance.logfileUsername());

        appInstance.setManagementMode(toManagementMode(imAppInstance.managed()));

        appInstance.setName(imAppInstance.name());

        appInstance.setPropertyFile(imAppInstance.propertyFile());

        appInstance.setUnixPrompt(imAppInstance.unixPrompt());

        return appInstance;
    }

    public static AppInstance[] toAppInstances(
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance[] imAppInstances)
    {
        int appInstanceCount = imAppInstances.length;
        AppInstance[] appInstances = new AppInstance[appInstanceCount];
        for (int i = 0; i < appInstanceCount; i++)
        {
            appInstances[i] = toAppInstance(imAppInstances[i]);
        }
        return appInstances;
    }

    public static AppInstanceNode
        toAppInstanceNode(
        com.cisco.eManager.eManager.inventory.view.AppInstanceNode ain)
    {
        AppInstanceNode result = new AppInstanceNode();
        result.setId(toManagedObjectId(ain.id()));
        result.setName(ain.name());
        result.setParentId(toManagedObjectId(ain.parent().id()));
        return result;
    }

    public static AppType toAppType(com.cisco.eManager.eManager.inventory.appType.AppType imAppType)
    {
        AppType appType = new AppType();

        appType.setId(toManagedObjectId(imAppType.id()));

        List imHosts = imAppType.hosts();
        int hostCount = imHosts.size();
        LocalAppType[] localAppTypes = new LocalAppType[hostCount];
        LocalAppType localAppType = null;
        Iterator iter = imHosts.iterator();
        com.cisco.eManager.eManager.inventory.host.Host imHost = null;
        for (int i = 0; i < hostCount; i++)
        {
            imHost = (com.cisco.eManager.eManager.inventory.host.Host)iter.next();

            localAppType.setHostId(toManagedObjectId(imHost.id()));

            List imMgmtPolicies = imHost.mgmtPolicies(imAppType.id());
            Iterator imMgmtPolicyIter = imMgmtPolicies.iterator();
            com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy imMgmtPolicy = null;
            int mgmtPolicyCount = imMgmtPolicies.size();
            ManagedObjectId[] mgmtPolicyIds = new ManagedObjectId[mgmtPolicyCount];
            for (int j = 0; j < mgmtPolicyCount; j++)
            {
                imMgmtPolicy = (com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy)imMgmtPolicyIter.
                               next();
                mgmtPolicyIds[j] = toManagedObjectId(imMgmtPolicy.id());
            }
            localAppType.setMgmtPolicyId(mgmtPolicyIds);

            localAppType.setAppInstanceId(appInstanceIds(imHost, imAppType));
        }
        appType.setLocalAppType(localAppTypes);

        return appType;
    }

    public static AppTypeNode
        toAppTypeNode(
        com.cisco.eManager.eManager.inventory.view.AppTypeNode imAppTypeNode)
    {
        AppTypeNode result = new AppTypeNode();

        com.cisco.eManager.eManager.inventory.appType.AppType imAppType =
            imAppTypeNode.appType();
        List imAppInstances = imAppType.appInstances();
        int count = imAppInstances.size();
        ManagedObjectId[] appInstanceIds = new ManagedObjectId[count];
        Iterator iter = imAppInstances.iterator();
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance imAppInstance = null;
        for (int i = 0; i < count; i++)
        {
            imAppInstance =
                (com.cisco.eManager.eManager.inventory.appInstance.AppInstance)iter.next();
            appInstanceIds[i] = toManagedObjectId(imAppInstance.id());
        }
        result.setAppInstanceIds(appInstanceIds);

        result.setId(toManagedObjectId(imAppTypeNode.id()));

        result.setName(imAppTypeNode.name());

        result.setParentId(toManagedObjectId(imAppTypeNode.parent().id()));

        return result;
    }

    public static AppType[] toAppTypes(
        com.cisco.eManager.eManager.inventory.appType.AppType[] imAppTypes)
    {
        int appTypeCount = imAppTypes.length;
        AppType[] appTypes = new AppType[appTypeCount];
        for (int i = 0; i < appTypeCount; i++)
        {
            appTypes[i] = toAppType(imAppTypes[i]);
        }
        return appTypes;
    }

    public static ContainerNode
        toContainerNode(
        com.cisco.eManager.eManager.inventory.view.ContainerNode imNode)
    {
        ContainerNode node = new ContainerNode();
        node.setNodeName(imNode.name());
        node.setNodeId(toManagedObjectId(imNode.id()));
        com.cisco.eManager.eManager.inventory.view.Node imNodeParent =
            imNode.parent();
        if ( imNodeParent != null )
        {
            node.setParentNodeId(toManagedObjectId(imNodeParent.id()));
        }
        List children = imNode.children();
        int count = children.size();
        ManagedObjectId[] childNodeIds = new ManagedObjectId[count];
        Iterator iter = children.iterator();
        com.cisco.eManager.eManager.inventory.view.Node imChildNode = null;
        for (int i = 0; i < count; i++)
        {
            imChildNode =
                (com.cisco.eManager.eManager.inventory.view.Node)iter.next();
            childNodeIds[i] = toManagedObjectId(imChildNode.id());
        }
        node.setChildNodeId(childNodeIds);
        return node;
    }

    public static Host toHost(com.cisco.eManager.eManager.inventory.host.Host imHost)
    {
        Host host = new Host();

        host.setId(toManagedObjectId(imHost.id()));

        host.setName(imHost.name());

        List imAppTypes = imHost.appTypes();
        int appTypeCount = imAppTypes.size();
        AppBundle[] appBundles = new AppBundle[appTypeCount];
        AppBundle appBundle = null;
        Iterator iter = imAppTypes.iterator();
        com.cisco.eManager.eManager.inventory.appType.AppType imAppType = null;
        for (int i = 0; i < appTypeCount; i++)
        {
            appBundle = new AppBundle();

            imAppType = (com.cisco.eManager.eManager.inventory.appType.AppType)iter.next();

            appBundle.setAppTypeId(toManagedObjectId(imAppType.id()));

            appBundle.setAppInstanceId(appInstanceIds(imHost, imAppType));

            appBundles[i] = appBundle;
        }
        host.setAppBundle(appBundles);

        host.setDnsName(imHost.dnsName());

        host.setDomain(imHost.domain());

        host.setIpAddress(imHost.ipAddress());

        HostState hostState = new HostState();
        if ( imHost.isActive() )
        {
            hostState.setUp(1);
        }
        else
        {
            hostState.setDown(2);
        }
        host.setState(hostState);

        return host;
    }

    public static HostNode
        toHostNode(
        com.cisco.eManager.eManager.inventory.view.HostNode imHostNode)
    {
        HostNode result = new HostNode();

        com.cisco.eManager.eManager.inventory.host.Host imHost =
            imHostNode.host();
        List imAppInstances = imHost.appInstances();
        int count = imAppInstances.size();
        ManagedObjectId[] appInstanceIds = new ManagedObjectId[count];
        Iterator iter = imAppInstances.iterator();
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance imAppInstance = null;
        for (int i = 0; i < count; i++)
        {
            imAppInstance =
                (com.cisco.eManager.eManager.inventory.appInstance.AppInstance)iter.next();
            appInstanceIds[i] = toManagedObjectId(imAppInstance.id());
        }
        result.setAppInstanceIds(appInstanceIds);

        result.setId(toManagedObjectId(imHostNode.id()));

        result.setName(imHostNode.name());

        result.setParentId(toManagedObjectId(imHostNode.parent().id()));

        return result;
    }

    public static Host[] toHosts(
        com.cisco.eManager.eManager.inventory.host.Host[] imHosts)
    {
        int hostCount = imHosts.length;
        Host[] hosts = new Host[hostCount];
        for (int i = 0; i < hostCount; i++)
        {
            hosts[i] = toHost(imHosts[i]);
        }
        return hosts;
    }

    public static Instrumentation toInstrumentation(
        com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation
        imInstrumentation)
    {
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.setId(toManagedObjectId(imInstrumentation.id()));
        instrumentation.setName(imInstrumentation.registration());
        instrumentation.setAppInstanceId(
            toManagedObjectId(imInstrumentation.appInstanceId()));
        instrumentation.setMethods(methods(imInstrumentation));
        return instrumentation;
    }

    public static Instrumentation[] toInstrumentations(List imInstrumentations)
    {
        com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation imInstrumentation;
        int instrumentationCount = imInstrumentations.size();
        Instrumentation[] instrumentations = new Instrumentation[instrumentationCount];
        Iterator iter = imInstrumentations.iterator();
        for (int i = 0; i < instrumentationCount; i++)
        {
            imInstrumentation =
                (com.cisco.eManager.eManager.inventory.instrumentation.Instrumentation)iter.next();
            instrumentations[i] = toInstrumentation(imInstrumentation);
        }
        return instrumentations;
    }

    public static com.cisco.eManager.common.inventory.ManagedObjectId
        toImManagedObjectId(ManagedObjectId id)
    {
        com.cisco.eManager.common.inventory.ManagedObjectIdType imIdType = null;
        ObjectType objType = (ObjectType)id.getObjectType();
        if (objType.hasAppInstanceType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.ApplicationInstance;
        }
        else if (objType.hasApplicationHierarchyContainerType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.ApplicationHierarchyContainer;
        }
        else if (objType.hasAppTypeType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.ApplicationType;
        }
        else if (objType.hasAuditLogType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.AuditLog;
        }
        else if (objType.hasEventType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.Event;
        }
        else if (objType.hasHostAgentType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.HostAgent;
        }
        else if (objType.hasInstrumentationType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.Instrumentation;
        }
        else if (objType.hasMgmtPolicyType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.MgmtPolicy;
        }
        else if (objType.hasPhysicalHierarchyContainerType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.PhysicalHierarchyContainer;
        }
        else if (objType.hasUserAccountType())
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.UserAccount;
        }
        else
        {
            imIdType =
                com.cisco.eManager.common.inventory.ManagedObjectIdType.ApplicationInstance;
        }
        com.cisco.eManager.common.inventory.ManagedObjectId imId =
            new com.cisco.eManager.common.inventory.ManagedObjectId(
            imIdType,
            id.getObjectKey());
        return imId;
    }

    public static com.cisco.eManager.eManager.inventory.view.NodePath
        toImNodePath(FullyDistinguishedName fdn)
    {
        String[] components = fdn.getElementName();
        return new com.cisco.eManager.eManager.inventory.view.NodePath(components);
    }

    public static ManagedObjectId toManagedObjectId(
        com.cisco.eManager.common.inventory.ManagedObjectId imId)
    {
        ManagedObjectId id = new ManagedObjectId();
        id.setObjectKey(imId.getManagedObjectKey());
        ObjectType objType = new ObjectType();
        com.cisco.eManager.common.inventory.ManagedObjectIdType imObjType =
            imId.getManagedObjectType();
        if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.
            ApplicationHierarchyContainer))
        {
            objType.setApplicationHierarchyContainerType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.
            ApplicationInstance))
        {
            objType.setAppInstanceType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.
            ApplicationType))
        {
            objType.setAppTypeType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.AuditLog))
        {
            objType.setAuditLogType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.Event))
        {
            objType.setEventType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.HostAgent))
        {
            objType.setHostAgentType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.Instrumentation))
        {
            objType.setInstrumentationType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.MgmtPolicy))
        {
            objType.setMgmtPolicyType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.PhysicalHierarchyContainer))
        {
            objType.setPhysicalHierarchyContainerType(1);
        }
        else if (imObjType.equals(
            com.cisco.eManager.common.inventory.ManagedObjectIdType.UserAccount))
        {
            objType.setUserAccountType(1);
        }
        else
        {
            objType.setAppInstanceType(1);
        }

        id.setObjectType(objType);
        return id;
    }

    public static ManagementMode toManagementMode(boolean managed)
    {
        ManagementMode mMode = new ManagementMode();
        if (managed)
        {
            mMode.setManaged(true);
        }
        else
        {
            mMode.setUnmanaged(true);
        }
        return mMode;
    }

    public static Method toMethod(
        com.cisco.eManager.eManager.inventory.instrumentation.Method imMethod)
    {
        Method method = new Method();
        method.setName(imMethod.name());
        return method;
    }

    public static Method[] toMethods(List imMethods)
    {
        com.cisco.eManager.eManager.inventory.instrumentation.Method imMethod;
        int methodCount = imMethods.size();
        Method[] methods = new Method[methodCount];
        Iterator iter = imMethods.iterator();
        for (int i = 0; i < methodCount; i++)
        {
            imMethod =
                (com.cisco.eManager.eManager.inventory.instrumentation.Method)iter.next();
            methods[i] = toMethod(imMethod);
        }
        return methods;
    }

    public static MgmtPolicy
        toMgmtPolicy(com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy imMgmtPolicy)
    {
        MgmtPolicy mgmtPolicy = new MgmtPolicy();
        mgmtPolicy.setId(toManagedObjectId(imMgmtPolicy.id()));
        mgmtPolicy.setName(imMgmtPolicy.name());
        mgmtPolicy.setPathname(imMgmtPolicy.pathname());
        MgmtPolicyState mpState = new MgmtPolicyState();
        if ( imMgmtPolicy.isLoaded() )
        {
            mpState.setLoaded(1);
        }
        else
        {
            mpState.setUnloaded(2);
        }
        mgmtPolicy.setState(mpState);
        return mgmtPolicy;
    }

    public static MgmtPolicy[] toMgmtPolicies(
       com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy[] imMgmtPolicies)
    {
        int mgmtPolicyCount = imMgmtPolicies.length;
        MgmtPolicy[] mgmtPolicies = new MgmtPolicy[mgmtPolicyCount];
        for (int i = 0; i < mgmtPolicyCount; i++)
        {
            mgmtPolicies[i] = toMgmtPolicy(imMgmtPolicies[i]);
        }
        return mgmtPolicies;
    }

    public static Solution toSolution(
        com.cisco.eManager.eManager.inventory.solution.Solution imSolution)
    {
        Solution solution = new Solution();

        solution.setId(toManagedObjectId(imSolution.id()));

        solution.setName(imSolution.name());

        com.cisco.eManager.eManager.inventory.appInstance.AppInstance[]
            imAppInstances = imSolution.appInstances();
        ManagedObjectId appInstanceIds[] =
            new ManagedObjectId[imAppInstances.length];
        for (int i = 0; i < imAppInstances.length; i++)
        {
            appInstanceIds[i] = toManagedObjectId(imAppInstances[i].id());
        }

        return solution;
    }

    public static SolutionNode
        toSolutionNode(
         com.cisco.eManager.eManager.inventory.view.SolutionNode imSolutionNode)
    {
        SolutionNode result = new SolutionNode();

        com.cisco.eManager.eManager.inventory.solution.Solution imSolution =
            imSolutionNode.solution();
        com.cisco.eManager.eManager.inventory.appInstance.AppInstance[]
            imAppInstances = imSolution.appInstances();
        ManagedObjectId[] appInstanceIds =
            new ManagedObjectId[imAppInstances.length];
        for (int i = 0; i < imAppInstances.length; i++)
        {
            appInstanceIds[i] = toManagedObjectId(imAppInstances[i].id());
        }
        result.setAppInstanceIds(appInstanceIds);

        result.setId(toManagedObjectId(imSolutionNode.id()));

        result.setName(imSolutionNode.name());

        result.setParentId(toManagedObjectId(imSolutionNode.parent().id()));

        return result;
    }

    public static Solution[] toSolutions(
        com.cisco.eManager.eManager.inventory.solution.Solution[] imSolutions)
    {
        int solutionCount = imSolutions.length;
        Solution[] solutions = new Solution[solutionCount];
        for (int i = 0; i < solutionCount; i++)
        {
            solutions[i] = toSolution(imSolutions[i]);
        }
        return solutions;
    }

    public static Transport toTransport(com.cisco.eManager.common.inventory.Transport imTransport)
    {
        Transport transport = new Transport();
        if (imTransport.isSsh())
        {
            transport.setSsh(1);
        }
        else
        {
            transport.setTelnet(2);
        }
        return transport;
    }
}
