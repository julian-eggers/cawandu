package com.itelg.docker.cawandu.service.impl;

import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.container.ContainerFilter;
import com.itelg.docker.cawandu.domain.image.UpdateState;
import com.itelg.docker.cawandu.repository.ContainerRepository;
import com.itelg.docker.cawandu.service.ContainerService;
import com.itelg.docker.cawandu.service.ImageService;

@RunWith(PowerMockRunner.class)
public class DefaultContainerServiceTest
{
    private ContainerService containerService;
    
    @Mock
    private ContainerRepository containerRepository;
    
    @Mock
    private ImageService imageService;
    
    @Before
    public void before()
    {
        containerService = new DefaultContainerService();
        Whitebox.setInternalState(containerService, containerRepository);
        Whitebox.setInternalState(containerService, imageService);
    }
    
    @Test
    public void testRenameContainer()
    {
        containerRepository.renameContainer(EasyMock.anyObject(Container.class), EasyMock.anyString());
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        containerService.renameContainer(new Container(), "newName");
        PowerMock.verifyAll();
    }
    
    @Test
    public void testRecreateContainer()
    {
        imageService.pullImage("jeggers/cawandu:latest");
        PowerMock.expectLastCall().andReturn(UpdateState.PULLED);
        
        containerRepository.recreateContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        Container container = new Container();
        container.setImageName("jeggers/cawandu:latest");
        containerService.recreateContainer(container);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testSwitchTag()
    {
        imageService.pullImage("jeggers/cawandu:1.0.0-RELEASE");
        PowerMock.expectLastCall().andReturn(UpdateState.PULLED);
        
        containerRepository.recreateContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            Container container = (Container) EasyMock.getCurrentArguments()[0];
            Assert.assertEquals("jeggers/cawandu:1.0.0-RELEASE", container.getImageName());
            return null;
        });
        
        PowerMock.replayAll();
        Container container = new Container();
        container.setImageName("jeggers/cawandu:latest");
        containerService.switchTag(container, "1.0.0-RELEASE");
        PowerMock.verifyAll();
    }
    
    @Test
    public void testSwitchTagWithLabel()
    {
        imageService.pullImage("jeggers/cawandu:1.0.0-RELEASE");
        PowerMock.expectLastCall().andReturn(UpdateState.PULLED);
        
        containerRepository.recreateContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall().andAnswer(() ->
        {
            Container container = (Container) EasyMock.getCurrentArguments()[0];
            Assert.assertEquals("jeggers/cawandu:1.0.0-RELEASE", container.getImageName());
            Assert.assertEquals("jeggers/cawandu:1.0.0-RELEASE", container.getLabel(Container.IMAGE_NAME_LABEL));
            return null;
        });
        
        PowerMock.replayAll();
        Container container = new Container();
        container.setImageName("jeggers/cawandu:latest");
        container.addLabel(Container.IMAGE_NAME_LABEL, "jeggers/cawandu:latest");
        containerService.switchTag(container, "1.0.0-RELEASE");
        PowerMock.verifyAll();
    }
    
    @Test
    public void testStartContainer()
    {
        containerRepository.startContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        containerService.startContainer(new Container());
        PowerMock.verifyAll();
    }
    
    @Test
    public void testStopContainer()
    {
        containerRepository.stopContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        containerService.stopContainer(new Container());
        PowerMock.verifyAll();     
    }
    
    @Test
    public void restartContainer()
    {
        containerRepository.restartContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        containerService.restartContainer(new Container());
        PowerMock.verifyAll();
    }
    
    @Test
    public void testRemoveContainer()
    {
        containerRepository.removeContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        containerService.removeContainer(new Container());
        PowerMock.verifyAll();
    }
    
    @Test
    public void testKillContainer()
    {
        containerRepository.killContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        containerRepository.removeContainer(EasyMock.anyObject(Container.class));
        PowerMock.expectLastCall();
        
        PowerMock.replayAll();
        containerService.killContainer(new Container());
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetContainerById()
    {
        containerRepository.getContainerById("123");
        PowerMock.expectLastCall().andReturn(new Container());
        
        PowerMock.replayAll();
        Assert.assertNotNull(containerService.getContainerById("123"));
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetContainerByName()
    {
        containerRepository.getContainerByName("test");
        PowerMock.expectLastCall().andReturn(new Container());
        
        PowerMock.replayAll();
        Assert.assertNotNull(containerService.getContainerByName("test"));
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetContainersByFilter()
    {
        containerRepository.getContainersByFilter(EasyMock.anyObject(ContainerFilter.class));
        PowerMock.expectLastCall().andReturn(Collections.singletonList(new Container()));
        
        PowerMock.replayAll();
        Assert.assertEquals(1, containerService.getContainersByFilter(new ContainerFilter()).size());
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetAllContainers()
    {
        containerRepository.getAllContainers();
        PowerMock.expectLastCall().andReturn(Collections.singletonList(new Container()));
        
        PowerMock.replayAll();
        Assert.assertEquals(1, containerService.getAllContainers().size());
        PowerMock.verifyAll();        
    }
}