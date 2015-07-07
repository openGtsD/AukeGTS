package no.auke.drone.services;

import no.auke.drone.domain.Person;
import no.auke.drone.domain.User;

/**
 * Created by huyduong on 7/6/2015.
 */
public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(String userId);
    User getUserById(String userId);

    Person createPerson(Person person);
    Person updatePerson(Person person);
    void deletePerson(String personId);
    Person getPersonById(String personId);
}
