/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;


/**
 *
 * @author user
 */
@Entity
public class Guest extends Occupant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 32, unique = true)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;

    public Guest() {
    }

    public Guest(String username, String password, String name, String email, String phoneNumber, String passportNumber) {
        super(name, email, phoneNumber, passportNumber);
        this.username = username;
        this.password = password;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.occupantId != null ? this.occupantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestId fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.occupantId == null && other.occupantId != null) || (this.occupantId != null && !this.occupantId.equals(other.occupantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + this.occupantId + " ]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
