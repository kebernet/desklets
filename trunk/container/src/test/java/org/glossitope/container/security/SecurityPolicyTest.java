/*
 * SecurityPolicyTest.java
 * JUnit based test
 *
 * Created on August 9, 2006, 1:42 PM
 */

package org.glossitope.container.security;

import junit.framework.TestCase;

/**
 *
 * @author cooper
 */
public class SecurityPolicyTest extends TestCase {
    
    public SecurityPolicyTest(String testName) {
        super(testName);
    }

    public void testSerializeAlways() throws Exception {
        System.out.println("serializeAlways");
        
        /*
        HashMap<URL, ArrayList<String>> always = new HashMap<URL, ArrayList<String>>();
        ArrayList<String> list = new ArrayList<String>();
        list.add("foo.com");
        list.add("/home/cooper/.ab5k/foo");
        always.put( new URL("file://something.jar"), list );
        
        
        
        SecurityPolicy instance = new SecurityPolicy();
        
        String expResult = "file://something.jar\tfoo.com\t/home/cooper/.ab5k/foo";
        String result = instance.serializeRemembered(always);
        System.out.println("Excpeted:{"+expResult+"}");
        System.out.println("Got:     {"+result+"}");
        assertEquals(expResult, result);
        
        HashMap<URL, ArrayList<String>> deserializeResult = instance.deserializeRemembered( result );
        
        assertEquals( always, deserializeResult );
        */
        
    }

    
}
