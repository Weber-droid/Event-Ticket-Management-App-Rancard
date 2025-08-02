package com.rancard.eventmanagement.app.model;


import jakarta.persistence.*;

//package com.example.ticketing.model;

import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Event> events;

//    @OneToMany(mappedBy = "purchaser", cascade = CascadeType.ALL)
//    private List<Ticket> purchasedTickets;
}
