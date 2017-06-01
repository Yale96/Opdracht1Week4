package auction.domain;

import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import nl.fontys.util.Money;

@Entity
@NamedQueries({
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i"),
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description"),
    @NamedQuery(name = "Item.findById", query = "select i from Item as i where i.identifier = :identifier") 
})
public class Item implements Comparable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDENTIFIER", nullable = false, unique = true)
    private Long identifier;

    
    @ManyToOne (cascade = CascadeType.PERSIST)
    private User seller;

    public void setSeller(User seller) {
        this.seller = seller;
    }
    
    
   @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "description", column = @Column(name = "category_description"))
    })

    private Category category;
    private String description;
    
    @OneToOne(cascade=CascadeType.PERSIST, mappedBy="betttedItem")
    private Bid highest;
    
    
    
    
    public Item(User seller, Category category, String description) {
        this.seller = seller;
        this.category = category;
        this.description = description;
    }
    
    public Item(){
        
    }
    
    public Long getId() {
        return identifier;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Bid getHighestBid() {
        return highest;
    }
    
    public void setId(Long identifier){
        this.identifier = identifier;
    }

    public Bid newBid(User buyer, Money amount) {
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
            return null;
        }
        highest = new Bid(buyer, amount, this);
        return highest;
    }

    @Override
    public int compareTo(Object arg0) {
       return identifier.compareTo(((Item)arg0).identifier);
    }

    @Override
    public boolean equals(Object o) {
         if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final Item other = (Item) o;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (!Objects.equals(this.seller, other.seller)) {
            return false;
        }
        if (this.category != other.category) {
            return false;
        }
        if (this.description.equals(other.description)) {
            return false;
        }
        if (this.highest != other.highest) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        //TODO
        //Haal de parentconstructor op met super, dus waar het object daadwerkelijk wordt aangemaakt. 
        return super.hashCode();
    }
}
