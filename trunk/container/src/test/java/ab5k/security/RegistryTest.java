/*
 * RegistryTest.java
 * JUnit based test
 *
 * Created on August 3, 2006, 7:38 PM
 */
package ab5k.security;

import java.io.File;
import java.net.URL;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;

import junit.framework.TestCase;

/**
 *
 * @author cooper
 */
public class RegistryTest extends TestCase {
    public RegistryTest(String testName) {
        super(testName);
    }

    public void testReadDeskletConfig() throws Exception {
        System.out.println("readDeskletConfig");

        File file = new File("src/test/resources/registry-test");
        Policy p = new Policy(){
            public boolean implies(ProtectionDomain d, Permission p){ return true; }
        };
        Policy.setPolicy( p );
        
        SecurityManager sm = System.getSecurityManager();
        if (sm == null)
        {
            sm = new SecurityManager();
            System.setSecurityManager(sm);
        }
        
        Registry instance = Registry.getInstance();

        DeskletConfig expResult = new DeskletConfig();
        expResult.setName("Test Desklet");
        expResult.setClassName("foo.bar.TestDesklet");
        expResult.setHomeDir(new File("src/test/resources/registry-test"));
        expResult.setUUID("registry-test");
        expResult.setAuthorName("Robert Cooper");
        expResult.setHomePage(new URL("http://svn.joshy.org"));
        expResult.setDescription("This is a description!");
        expResult.setVersion("0.1");
        expResult.setSpecificationVersion("0.1");
        URL[] repos = new URL[2];
        repos[0] = new URL("http://ibiblio.org/maven/");
        repos[1] = new URL(
                "https://maven-repository.dev.java.net/nonav/repository/");
        expResult.setRepositories(repos);

        Dependency[] deps = new Dependency[2];
        deps[0] = new Dependency("commons-lang", "commons-lang", "2.1", null);
        deps[1] = new Dependency("commons-net", "commons-net", "1.4.0", "jar");
        expResult.setDependencies(deps);
        
        System.out.println(expResult);

        System.out.println(" ===== ");

        DeskletConfig result = instance.readDeskletConfig(file);
        result.setClassLoader( null ); // remove the classloader for now.
        System.out.println(result);
        
        assertEquals((Object)expResult, (Object) result);
    }

    public void testDownload() throws Exception {
        System.out.println("readDeskletConfig");

        File file = new File("src/test/resources/registry-test");

        Registry instance = Registry.getInstance();
        DeskletConfig config = instance.readDeskletConfig(file);
        URL[] result = instance.getDependencies(config);

        for (URL u : result) {
            System.out.println(u);
        }
    }
}
