/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.service;

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

    @Before
    public void setUp() throws Exception {
        clean = new DatabaseCleaner();
        seller = new SellerMgr();
        registration = new RegistrationMgr();
        auction = new AuctionMgr();
    }

    @Test
    public void bidirectionalBidToItem() {
        User seller = new User("seller@live.nl");
        User buyer = new User("buyer@live.nl");

        registration.registerUser(seller.getEmail());
        registration.registerUser(buyer.getEmail());

        Category catOne = new Category("catOne");

        em.getTransaction().begin();
        Item i = new Item(seller, catOne, "testItem");

        Bid b = new Bid(buyer, new Money(11, "euro"), i);
        i.newBid(buyer, b.getAmount());
        em.persist(i);
        em.persist(b);
        em.getTransaction().commit();

        assertNotNull(i.getId());
        assertNotNull(b.getId());
        assertEquals(b.getItem().getDescription(), i.getDescription());
        assertEquals(i.getHighestBid().getAmount().getCents(), b.getAmount().getCents());
    }

    @Test
    public void biderctionalBidToItemNullPointer() {
        try {
            User seller = new User("seller@live.nl");
            User buyer = new User("buyer@live.nl");

            registration.registerUser(seller.getEmail());
            registration.registerUser(buyer.getEmail());

            Category catOne = new Category("catOne");

            
            Item i = new Item(seller, catOne, "testItem");

            Bid b = new Bid(buyer, new Money(11, "euro"), i);
            i.newBid(buyer, b.getAmount());
            b.getId();
            
        } catch (NullPointerException ex) {
            System.out.println(ex.toString());
        }
    }
}
