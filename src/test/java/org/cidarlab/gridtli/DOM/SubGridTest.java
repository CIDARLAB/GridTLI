/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import org.cidarlab.gridtli.dom.Cell;
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
        
        Cell g1 = new Cell(0,2);
        Cell g2 = new Cell(0,2);
        
        Set<Cell> set = new HashSet<Cell>();
        
        set.add(g1);
        set.add(g1);
        assertEquals(set.size(),1);
        
        set.add(g2);
        assertEquals(set.size(),1);
        
        set.add(new Cell(0,2));
        assertEquals(set.size(),1);
        
    }
    
}
