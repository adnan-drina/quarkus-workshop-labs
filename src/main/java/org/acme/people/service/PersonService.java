package org.acme.people.service;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.acme.people.model.EyeColor;
import org.acme.people.model.Person;

import io.quarkus.vertx.ConsumeEvent;

@ApplicationScoped
public class PersonService {

     // Consuming events from add-persone address
    @ConsumeEvent(value = "add-person", blocking = true) 
    @Transactional
    public Person addPerson(String name) {
        LocalDate birth = LocalDate.now().plusWeeks(Math.round(Math.floor(Math.random() * 20 * 52 * -1)));
        EyeColor color = EyeColor.values()[(int)(Math.floor(Math.random() * EyeColor.values().length))];
        Person p = new Person();
        p.birth = birth;
        p.eyes = color;
        p.name = name;
        // A new Person entity is created and persisted
        Person.persist(p); 
        // The return value is used as response to the incoming message
        return p; 
    }

}