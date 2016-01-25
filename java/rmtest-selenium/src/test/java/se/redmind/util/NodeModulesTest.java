package se.redmind.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.UUID;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.redmind.utils.NodeModules;

public class NodeModulesTest {

	private static File file = new File("/tmp/"+UUID.randomUUID()+"/node_modules/");
	private static File noNodeModules = new File("/tmp/"+UUID.randomUUID()+"/no_node/");
	
	@BeforeClass
	public static void before(){
		file.mkdirs();
		file.deleteOnExit();
		noNodeModules.mkdirs();
		noNodeModules.deleteOnExit();
	}
	
	@Test
	public void test_modulePath() {
		Assume.assumeTrue(file.exists());
		String path = path(file);
		assertEquals(file.getAbsolutePath(), path);
	}
	
	@Test
	public void no_modulesPath(){
		Assume.assumeTrue(noNodeModules.exists());
		String noModule = path(noNodeModules);
		assertNull(noModule);
	}
	
	private String path(File file){
		return NodeModules.path(File.separator, file);
	}

}
