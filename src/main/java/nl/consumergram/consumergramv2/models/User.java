package nl.consumergram.consumergramv2.models;

//Deze klasse definieert de structuur van de gebruikersidentiteit met zijn eigenschappen en de relatie met autoriteiten.
//        Het wordt gebruikt om gebruikersgegevens in de database op te slaan en te manipuleren met behulp van JPA.


import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//De @Entity-annotatie geeft aan dat dit een JPA-entity is, wat betekent dat het overeenkomt met een tabel in de database.
@Entity
@Data
@Setter

//De @Table(name = "users")-annotatie specificeert de naam van de database-tabel waarmee deze entiteit overeenkomt.
@Table(name = "users")
public class User {

    // Deze eerste 3 variabelen zijn verplicht om te kunnen inloggen met een username, password en rol.

//    @Column(nullable = false, unique = true) specificeert dat username niet leeg mag zijn
//    (nullable = false) en uniek moet zijn in de database.
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

//    Dit definieert een een-op-veel-relatie tussen User en Authority
//    (waarschijnlijk een andere entiteit die de rollen of rechten van een gebruiker vertegenwoordigt).
    @OneToMany(
            targetEntity = Authority.class,
//            mappedBy = "username" geeft aan dat de User-entiteit de kant is die de relatie
//            beheert via het veld username in de Authority-entiteit.
            mappedBy = "username",
//            cascade = CascadeType.ALL geeft aan dat wijzigingen in User
//            (zoals toevoegen of verwijderen van Authority) moeten worden doorgegeven aan de gerelateerde Authority-entiteiten.
            cascade = CascadeType.ALL,
            orphanRemoval = true,
//            fetch = FetchType.EAGER specificeert dat de authorities bij
//            het laden van een User-object onmiddellijk moeten worden opgehaald.
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    // Deze 3 variabelen zijn niet verplicht.
    // Je mag ook een "String banaan;" toevoegen, als je dat graag wilt.

    //@Column word gebruikt om de tabel aan te passen
    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String apikey;

    @Column
    private String email;

    @OneToOne(mappedBy = "user")
    private ImageData imageData;

    @OneToOne(mappedBy = "user")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user")
    private List<BlogPost> blogPosts;


    //    De getters en setters worden gebruikt om toegang te krijgen tot en wijzigingen
//    aan te brengen in de eigenschappen van het gebruikersobject.



//    Set<Authority> authorities-verzameling:
//    Dit is een set van Authority-objecten die de rollen of rechten van de gebruiker vertegenwoordigen.


//    De addAuthority en removeAuthority-methoden worden gebruikt om autoriteiten toe te voegen of te verwijderen.
    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }
    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }


}