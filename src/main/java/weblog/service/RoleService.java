package weblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weblog.RoleRepository;
import weblog.model.Role;

@Service
public class RoleService {

	@Autowired private RoleRepository roleRepository;
	
	public Role getByName(String name) {
		return roleRepository.findByName(name);
	}
}
