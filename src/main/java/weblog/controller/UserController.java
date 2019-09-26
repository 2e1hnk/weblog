package weblog.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import weblog.UserRepository;
import weblog.model.User;
import weblog.service.UserService;

@Controller
@RequestMapping(path="/admin/users")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final UserRepository userRepository;
	
	@Autowired private UserService userService;
	
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("")
    public String home(Model model, User user, @RequestParam("page") Optional<Integer> page, 
    	      @RequestParam("size") Optional<Integer> size, @RequestParam("edit") Optional<Long> editId) {
    	
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        
        PageRequest pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<User> userPage = userService.getPaginatedArticles(pageable);
        
        int totalPages = userPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        
        if ( editId.isPresent() ) {
        	user = userRepository.findById(editId.get())
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + editId.get()));
        }
        
        model.addAttribute("user", user);
              
        return "user";
    }
    
    @GetMapping("/adduser")
    public String showAddContactForm(User user) {
        return "add-user";
    }
    
    @PostMapping("/adduser")
    public String addContact(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }
        
        userRepository.save(user);
        
        
        return this.home(model, new User(), Optional.empty(), Optional.empty(), Optional.empty());
        
        //return "redirect:/log";
        // model.addAttribute("contacts", contactRepository.findAll());
        // return "index";
    }
    
    @GetMapping("/edit/{id}")
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
         
        model.addAttribute("user", user);
        model.addAttribute("submitUrl", "/admin/user/update/" + id);
        return this.home(model, user, Optional.empty(), Optional.empty(), Optional.empty());
    }
    
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "index";
        }
             
        userRepository.save(user);
        //model.addAttribute("contacts", contactRepository.findAll());
        return "redirect:/log";
    }
         
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
        userRepository.delete(user);
        //model.addAttribute("contacts", contactRepository.findAll());
        return "redirect:/log";
    }
}
