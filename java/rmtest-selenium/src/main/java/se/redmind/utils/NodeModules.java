package se.redmind.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeModules {
	
	private static Logger log = LoggerFactory.getLogger(NodeModules.class);

	public static String path(){
		String pwd = System.getProperty("user.dir");
		File file = new File(pwd);
		return path(File.separator, file);
	}
	
	public static String pathFromTestHome(){
		return path(File.separator, new File(TestHome.get()));
	}
	
	public static String path(String pathSeparator, File file){
		int size = file.getAbsolutePath().split(pathSeparator).length;
		log.info("Searching for node_modules for: "+file.getAbsolutePath());
		for (int i = 1; i < size; i++) {
			try {
				String absolutePath = file.toPath().getRoot()+file.toPath().subpath(0, i).toString();
				String testPath = absolutePath+pathSeparator+"node_modules";
				if(new File(testPath).exists()){
					return testPath;
				}
			} catch (Exception e) {
				log.error("error getting node_module root: ");
				e.printStackTrace();
				break;
			}
		}
		return null;
	}
	
}
