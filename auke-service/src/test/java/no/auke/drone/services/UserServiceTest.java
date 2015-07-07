package no.auke.drone.services;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Person;
import no.auke.drone.domain.User;
import no.auke.drone.domain.test.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by huyduong on 7/7/2015.
 */
public class UserServiceTest extends AbstractIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private CRUDDao<User> userCRUDDao;

    @Autowired
    private CRUDDao<Person> personCRUDDao;

    @Before
    public void setUp() throws Exception {
        userCRUDDao.setPersistentClass(User.class);
        userCRUDDao.deleteAll();

        personCRUDDao.setPersistentClass(Person.class);
        personCRUDDao.deleteAll();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("username");
        user.setPassword("password");

        User persistedUser = userService.createUser(user);
        Assert.assertEquals(user.getId(),persistedUser.getId());
        Assert.assertEquals(user.getUsername(),persistedUser.getUsername());
        Assert.assertEquals(user.getPassword(),persistedUser.getPassword());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("username");
        user.setPassword("password");
        userService.createUser(user);

        user.setPassword("password2");
        User persistedUser = userService.updateUser(user);

        Assert.assertEquals(user.getId(),persistedUser.getId());
        Assert.assertEquals(user.getUsername(),persistedUser.getUsername());
        Assert.assertEquals(user.getPassword(),persistedUser.getPassword());
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("username");
        user.setPassword("password");

        userService.createUser(user);

        User persistedUser = userService.getUserById(user.getId());

        Assert.assertEquals(user.getId(),persistedUser.getId());
        Assert.assertEquals(user.getUsername(),persistedUser.getUsername());
        Assert.assertEquals(user.getPassword(),persistedUser.getPassword());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("username");
        user.setPassword("password");

        userService.createUser(user);

        User persistedUser = userService.getUserById(user.getId());

        Assert.assertEquals(user.getId(),persistedUser.getId());
        Assert.assertEquals(user.getUsername(),persistedUser.getUsername());
        Assert.assertEquals(user.getPassword(),persistedUser.getPassword());

        userService.deleteUser(user.getId());

        persistedUser = userService.getUserById(user.getId());
        Assert.assertNull(persistedUser);
    }
}
