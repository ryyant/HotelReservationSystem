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
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import util.enumeration.RoomStatusEnum;

/**
 *
 * @author user
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @Column(nullable = false, length = 4)
    private Integer roomNumber;
    @Column(nullable = false)
    private RoomStatusEnum roomStatus;
    @Column(nullable = false)
    private Boolean enabled;

    @OneToOne(fetch = FetchType.LAZY, mappedBy="Room")
    private Reservation reservation;
    
    @ManyToMany
    private List<RoomType> roomTypes;
    
    @OneToOne(fetch = FetchType.LAZY)
    private RoomRate roomRate;
    
    public Room() {
        this.roomTypes = new ArrayList<>();
    }

    public Room(Integer roomNumber, RoomStatusEnum roomStatus, Boolean enabled) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
        this.enabled = enabled;
    }

    
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatusEnum roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
}