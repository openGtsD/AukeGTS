package no.auke.drone.services.impl;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Person;
import no.auke.drone.domain.User;
import no.auke.drone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by huyduong on 7/7/2015.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CRUDDao<User> userCRUDDao;

    @Autowired
    private CRUDDao<Person> personCRUDDao;

    @PostConstruct
    public void init() {
        userCRUDDao.setPersistentClass(User.class);
        personCRUDDao.setPersistentClass(Person.class);
    }

    @Override
    public User createUser(User user) {
        return userCRUDDao.create(user);
    }

    @Override
    public User updateUser(User user) {
        return userCRUDDao.update(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userCRUDDao.getById(userId);
        if(user != null) {
            userCRUDDao.delete(user);
        }

    }

    @Override
    public User getUserById(String userId) {
        return userCRUDDao.getById(userId);
    }

    @Override
    public Person createPerson(Person person) {
        return personCRUDDao.create(person);
    }

    @Override
    public Person updatePerson(Person person) {
        return personCRUDDao.update(person);
    }

    @Override
    public void deletePerson(String personId) {
        Person person = personCRUDDao.getById(personId);
        if(person != null) {
            personCRUDDao.delete(person);
        }
    }

    @Override
    public Person getPersonById(String personId) {
        return personCRUDDao.getById(personId);
    }
}
