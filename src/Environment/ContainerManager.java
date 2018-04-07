package Environment;

import Agents.AgentDescription;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 * Created by ressay on 03/04/18.
 */
public class ContainerManager
{
    ContainerController mainContainer;
    Runtime runtime;
    Properties propreties;
    public ContainerManager() {


        runtime =Runtime.instance();
        propreties = new ExtendedProperties();
//        propreties.setProperty(Profile.GUI,"true");
        ProfileImpl profileImpl = new ProfileImpl(propreties);
        mainContainer =runtime.createMainContainer(profileImpl);
//        System.out.println("main container démmare");
    }

    public ContainerController addContainer()
    {
        return addContainer(propreties);
    }

    public ContainerController addContainer(Properties propreties)
    {
        ProfileImpl profile = new ProfileImpl(propreties);
        return runtime.createAgentContainer(profile);
    }

    public AgentController addAgent(ContainerController container, AgentDescription desc) throws StaleProxyException {
        return container.createNewAgent(desc.getName(),desc.getClassName(),desc.getArguments());
    }

    public AgentController addAgent(AgentDescription desc) throws StaleProxyException {
        return addAgent(mainContainer,desc);
    }

}
