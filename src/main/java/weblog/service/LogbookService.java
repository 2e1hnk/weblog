package weblog.service;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weblog.model.Logbook;
import weblog.model.User;
import weblog.repository.LogbookRepository;

@Service
public class LogbookService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LogbookRepository logbookRepository;
	
	@Autowired
	private UserService userService;
	
	public Logbook getLogbookById(long id) {
		return logbookRepository.getOne(id);
	}
	
	public Logbook createLogbook(String logbookName, User user) {
		Logbook logbook = this.createLogbook(logbookName);
		this.associateLogbookWithUser(logbook, user);
		userService.associateUserWithLogbook(user, logbook);
		return logbook;
	}
	
	public Logbook createLogbook(String logbookName) {
		Logbook logbook = new Logbook();
		logbook.setName(logbookName);
		logbookRepository.save(logbook);
		return logbook;
	}
	
	public void associateLogbookWithUser(Logbook logbook, User user) {
		logbook.associateUserWithLogbook(user);
		save(logbook);
		logger.info("Associated user " + user.getUsername() + " with logbook " + logbook.getName());
	}
	
	public void dissociateLogbookFromUser(Logbook logbook, User user) {
		logbook.dissociateUserFromLogbook(user);
		save(logbook);
	}
	
	public List<Logbook> getAllLogbooks() {
		return logbookRepository.findAll();
	}
	
	public void save(Logbook logbook) {
		logbookRepository.save(logbook);
	}
}
