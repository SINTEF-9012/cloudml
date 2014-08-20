/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package test.cloudml.deployment;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import net.cloudml.core.VM;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.connectors.FlexiantConnector;
import org.cloudml.connectors.PowerShellConnector;
import org.cloudml.core.Deployment;
import org.cloudml.core.VMInstance;
import org.cloudml.core.builders.VMBuilder;
import org.cloudml.deployer.CloudMLModelComparator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.cloudml.core.builders.Commons.aProvidedExecutionPlatform;
import static org.cloudml.core.builders.Commons.aVM;
import static org.cloudml.core.builders.Commons.aVMInstance;

public class ComparatorTest extends TestCase {
	
	public ComparatorTest( String testName )
	{
		super( testName );
	}


	public static Test suite()
	{
		return new TestSuite( ComparatorTest.class );
	}

    private static VMBuilder vmML() {
        return aVM()
                .named("ML")
                .providedBy("flexiant")
                .with(aProvidedExecutionPlatform()
                        .named("m1Provided")
                        .offering("OS", "Windows")) // FIXME offering of the execution platform
                .withMinRam(1000)
                .withMinCores(2)
                .withMinStorage(50)
                .withGroupName("test-SINTEF")
                .with64OS()
                .withImageId("SINTEF-Windows 2008-WinRM");
    }

	public void testApp(){
        /*try {
            VMInstance vm=new VMInstance("SINTEFTestWindows", vmML().build());

            fc.createInstance(vm);
            fc.execCommand(vm.getId(),"C:\\Users\\nicolasf\\Desktop\\cloudml2.0\\connect.ps1","","2ug2E89r72SriVW7");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*


        Codec codec = new JsonCodec();
		InputStream stream = null;
		/*try {
			
			
			stream = new FileInputStream(new File(this.getClass().getResource("/sensappAdmin.json").toURI()));
			DeploymentModel model = (DeploymentModel)codec.load(stream);
			
			stream = new FileInputStream(new File(this.getClass().getResource("/sensappAdmin2.json").toURI()));
			DeploymentModel model2 = (DeploymentModel)codec.load(stream);
			
			//current,target
			DeploymentModelComparator dmc=new DeploymentModelComparator(model2, model);
			dmc.compareDeploymentModel();
			
			assertTrue(dmc.getAddedNodes().size() > 0);
			assertTrue(dmc.getAddedArtefacts().size() > 0);
			assertTrue(dmc.getRemovedBindings().size() > 0);
			
			dmc=new DeploymentModelComparator(model, model2);
			dmc.compareDeploymentModel();
			
			assertTrue(dmc.getRemovedNodes().size() > 0);
			assertTrue(dmc.getAddedNodes().size() == 0);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
