package weblog;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import weblog.model.Logbook;
import weblog.model.Privilege;
import weblog.model.Role;
import weblog.model.User;
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
        for ( User user : userRepository.findAll() ) {
        	for ( Logbook logbook : user.getLogbooks() ) {
        		logbookService.grantEntitlement(logbook, user, Arrays.asList(EntitlementEnum.VIEW, EntitlementEnum.ADD, EntitlementEnum.UPDATE, EntitlementEnum.DELETE));
        	}
    	}
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