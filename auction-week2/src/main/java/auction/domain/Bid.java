package auction.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDENTIFIER", nullable = false, unique = true)
    private Long identifier;
    
    @Embedded
    private FontysTime time;
    @ManyToOne(cascade=CascadeType.PERSIST)
    private User buyer;
    @Embedded
    private Money amount;
    
    @OneToOne (cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private Item betttedItem;


    public Bid(User buyer, Money amount, Item bettedItem) {
        this.buyer = buyer;
        this.amount = amount;
        this.betttedItem = betttedItem;
        time = FontysTime.now();
    }
    
    public Bid(){
        
    }

    public Item getItem() {
        return betttedItem;
    }

    public void setItem(Item item) {
        this.betttedItem = item;
    }
    
    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }
    
    public long getId(){
        return identifier;
    }
    
    public void setId(Long identifier){
        this.identifier = identifier;
    }
}
