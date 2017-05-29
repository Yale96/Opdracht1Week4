/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.service;

import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nl.fontys.util.Money;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.DatabaseCleaner;

/**
 *
 * @author Yannick
 */
public class BidirectionalBidItemRelationTest {
    
    public BidirectionalBidItemRelationTest() {
    }
    
    final EntityManagerFactory emf = Persistence.createEntityManagerFactory("nl.fhict.se42_auction_jar_1.0-SNAPSHOTPU");
    final EntityManager em = emf.createEntityManager();
    DatabaseCleaner clean;

    @Before
    public void setUp() throws Exception {
        clean = new DatabaseCleaner();
    }
    
    @After
    public void tearDown() {
        try {
            clean.clean();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
    
    @Test
    public void bidirectionalBidToItem(){
        User seller = new User("seller@live.nl");
        User buyer = new User("buyer@live.nl");
        
        Category catOne = new Category("catOne");
        Item i = new Item(seller, catOne, "testItem");
        
        i.newBid(buyer, new Money(11, "eur"));
        
        assertEquals(11, i.getHighestBid().getAmount().getCents());
    }
}
