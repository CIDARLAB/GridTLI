/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prash
 */
public class SubGridTest {
    
    public SubGridTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSubGridSetTest(){
        
        SubGrid g1 = new SubGrid(0,2);
        SubGrid g2 = new SubGrid(0,2);
        
        Set<SubGrid> set = new HashSet<SubGrid>();
        
        set.add(g1);
        set.add(g1);
        assertEquals(set.size(),1);
        
        set.add(g2);
        assertEquals(set.size(),1);
        
        set.add(new SubGrid(0,2));
        assertEquals(set.size(),1);
        
    }
    
}
