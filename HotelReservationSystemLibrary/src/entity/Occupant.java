/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author user
 */
@Inheritance(strategy=InheritanceType.JOINED)
@Entity
public class Occupant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long occupantId;
    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, length = 32)
    private String email;
    @Column(nullable = false, length = 10)
    private String phoneNumber;
    @Column(nullable = false, length = 32)
    private String passportNumber;

    @OneToMany(mappedBy = "occupant")
    private List<Reservation> reservations;
    
    
    public Occupant() {
        this.reservations = new ArrayList<>();
    }

    public Occupant(String name, String email, String phoneNumber, String passportNumber) {
        this();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
    }
    

    public Long getOccupantId() {
        return occupantId;
    }

    public void setOccupantId(Long occupantId) {
        this.occupantId = occupantId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (occupantId != null ? occupantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the occupantId fields are not set
        if (!(object instanceof Occupant)) {
            return false;
        }
        Occupant other = (Occupant) object;
        if ((this.occupantId == null && other.occupantId != null) || (this.occupantId != null && !this.occupantId.equals(other.occupantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Occupant[ id=" + occupantId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
}
