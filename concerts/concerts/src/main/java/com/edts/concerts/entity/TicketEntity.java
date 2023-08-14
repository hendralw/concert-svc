package com.edts.concerts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tbl_ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "concert_id")
    private String concert_id;

    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private float price;

    @Column(name = "available_qty")
    private Integer available_qty;
}
