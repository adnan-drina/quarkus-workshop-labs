package org.acme.people.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.acme.people.model.DataTable;
import org.acme.people.model.EyeColor;
import org.acme.people.model.Person;
import org.acme.people.utils.CuteNameGenerator;

import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/person")
@ApplicationScoped
public class PersonResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAll() {
        return Person.listAll();
    }

    // Basic queries
    @GET
    @Path("/eyes/{color}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByColor(@PathParam(value = "color") EyeColor color) {
        return Person.findByColor(color);
    }

    @GET
    @Path("/birth/before/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getBeforeYear(@PathParam(value = "year") int year) {
        return Person.getBeforeYear(year);
    }

    // Datatable query
        @GET
        @Path("/datatable")
        @Produces(MediaType.APPLICATION_JSON)
        public DataTable datatable(
            @QueryParam(value = "draw") int draw,
            @QueryParam(value = "start") int start,
            @QueryParam(value = "length") int length,
            @QueryParam(value = "search[value]") String searchVal

            ) {
                // Begin result
                DataTable result = new DataTable();
                result.setDraw(draw); 

                // Filter based on search
                PanacheQuery<Person> filteredPeople;

                if (searchVal != null && !searchVal.isEmpty()) { 
                    filteredPeople = Person.<Person>find("name like :search",
                        Parameters.with("search", "%" + searchVal + "%"));
                } else {
                    filteredPeople = Person.findAll();
                }

                // Page and return
                int page_number = start / length;
                filteredPeople.page(page_number, length);

                result.setRecordsFiltered(filteredPeople.count());
                result.setData(filteredPeople.list());
                result.setRecordsTotal(Person.count());

                return result;

        }

    // Add lifecycle hook
    @Transactional
    void onStart(@Observes StartupEvent ev) {
        for (int i = 0; i < 1000; i++) {
            String name = CuteNameGenerator.generate();
            LocalDate birth = LocalDate.now().plusWeeks(Math.round(Math.floor(Math.random() * 20 * 52 * -1)));
            EyeColor color = EyeColor.values()[(int)(Math.floor(Math.random() * EyeColor.values().length))];
            Person p = new Person();
            p.birth = birth;
            p.eyes = color;
            p.name = name;
            Person.persist(p);
        }
    }

}