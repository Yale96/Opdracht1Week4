/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.service;

import auction.dao.ItemDAOJPAImpl;
import auction.domain.Bid;
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
    private SellerMgr seller;
    private RegistrationMgr registration;
    private AuctionMgr auction;
    private ItemDAOJPAImpl idao;

    @Before
    public void setUp() throws Exception {
        clean = new DatabaseCleaner();
        seller = new SellerMgr();
        registration = new RegistrationMgr();
        auction = new AuctionMgr();
        idao = new ItemDAOJPAImpl(em);
    }

    @Test
    public void bidirectionalBidToItem() {

        Category category = new Category("cat");
        User user = registration.registerUser("test@test.nl");
        Item item = seller.offerItem(user, category, "test");

        assertEquals(1, user.numberOfOfferdItems());
        assertNull(item.getHighestBid());
        
        Bid b = auction.newBid(item, user, new Money(50, "Euro"));
        assertNotNull(item.getHighestBid());
        assertSame(item, b.getItem());
        assertEquals(user, b.getBuyer());
        assertEquals(b.getAmount(), new Money(50, "Euro"));
        
        Bid secondBid = auction.newBid(item, user, new Money(100, "Euro"));
        assertNotNull(secondBid);
        assertEquals(item.getHighestBid().getAmount(), new Money(100, "Euro"));
    }
}
