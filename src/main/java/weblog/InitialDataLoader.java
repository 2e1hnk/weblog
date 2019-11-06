package weblog;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import weblog.model.Entitlement;
import weblog.model.Logbook;
import weblog.model.Privilege;
import weblog.model.Role;
import weblog.model.User;
import weblog.service.LocationService;
import weblog.service.LogbookService;
import weblog.service.UserService;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
 
    boolean alreadySetup = false;
 
    @Autowired
    private UserRepository userRepository;
  
    @Autowired
    private RoleRepository roleRepository;
  
    @Autowired
    private PrivilegeRepository privilegeRepository;
  
    @Autowired
    private UserService userService;
  
    @Autowired
    private LogbookService logbookService;
    
    @Autowired
    private LocationService locationService;
    
    @Autowired
    private EntitlementRepository entitlementRepository;
  
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
  
        if (alreadySetup)
            return;
        Privilege readPrivilege
          = createPrivilegeIfNotFound("USER");
        Privilege writePrivilege
          = createPrivilegeIfNotFound("ADMIN");
  
        List<Privilege> adminPrivileges = Arrays.asList(
          readPrivilege, writePrivilege);        
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));
 
        /*
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername("Test");
        builder.password(userService.encodePassword("test"));
        builder.roles("ROLE_ADMIN");
        builder.disabled(false)
        UserDetails userDetails = builder.build();
        userRepository.save(userDetails.);
 */
        /*
         * This grants all privileges to each user on the logbooks they own
         */
        /*
        User user = userService.getUser("2e1hnk");
        Logbook logbook = logbookService.getLogbookById(5338).get();
        
        Entitlement entitlement = new Entitlement();
        entitlement.setEntitlement(Entitlement.ADD);
        entitlement.setLogbook(logbook);
        entitlement.setUser(user);
        
        logbook.addEntitlement(entitlement);
        
        userService.save(user);
        logbookService.save(logbook);
        entitlementRepository.save(entitlement);
    	*/
        
        alreadySetup = true;
    }
 
    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
  
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
 
    @Transactional
    private Role createRoleIfNotFound(
      String name, Collection<Privilege> privileges) {
  
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}