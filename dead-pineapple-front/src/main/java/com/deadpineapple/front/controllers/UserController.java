package com.deadpineapple.front.controllers;

import com.deadpineapple.dal.dao.IUserDao;
import com.deadpineapple.dal.entity.UserAccount;
import com.deadpineapple.front.Forms.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saziri on 10/03/2016.
 */
@Controller
@RequestMapping("/user")
public class UserController{

    @Autowired
    IUserDao userBdd;
    UserAccount user;
    public void setUserDAO(IUserDao userDAO) {
        this.userBdd = userDAO;
    }
    @RequestMapping(value="/add", method= RequestMethod.GET)
    public ModelAndView addUser(Model model, LoginForm loginForm, ModelMap param){
        System.out.println("Invoking User");
        model.addAttribute("loginAttribute", loginForm);
        param.remove("logout");
        param.remove("error");
        return new ModelAndView("userForm", "userAccount", new UserAccount());
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String saveUser(@ModelAttribute("user")UserAccount user,
                           BindingResult result, ModelMap model, HttpServletRequest request) throws Exception {
        UserAccount newUser = (UserAccount) request.getSession().getAttribute("USER_INFORMATIONS");
        if(newUser != null){
            Long newUserId = newUser.getId();
            newUser = user;
            newUser.setId(newUserId);
        }
        newUser.setPassword(LoginForm.getEncryptedPassword(user.getPassword()));
        Date creationDate = new Date();
        newUser.setCreationDate(creationDate);
        userBdd.saveUser(newUser);
        // Create a loginform and check in bdd if users exists
        user = userBdd.checkCredentials(user.getEmail(), user.getPassword());
        LoginForm loginForm = new LoginForm();
        loginForm.setPassword(user.getPassword());
        loginForm.setUsername(user.getEmail());
        request.getSession().setAttribute("LOGGEDIN_USER", loginForm);
        request.getSession().setAttribute("USER_INFORMATIONS", user);
        return "redirect:/upload";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request)
    {
        //facebook & google login behavior
        String id = request.getParameter("userOAuthID");
        String firstName = request.getParameter("userOAuthFirstName");
        String lastName = request.getParameter("userOAuthLastName");

        if (lastName == null)
        {
            // handle facebook name :
            // API return first ans last name in the same string
            // we need to split it
            String[] split = firstName.split("\\s+");
            firstName = split[0];
            lastName = "";

            for (int j=1; j<split.length; j++)
                lastName += split[j] + " ";

            // trim last space
            lastName.trim();
        }

        if (id != null) {
            UserAccount userOAuth = userBdd.find(id);

            // if user not exist, we create it
            if (userOAuth == null) {
                userOAuth = new UserAccount();
                userOAuth.setEmail(id);
                userOAuth.setPassword(LoginForm.getEncryptedPassword(String.valueOf(id)));
                userOAuth.setFirstName(firstName);
                userOAuth.setLastName(lastName);
                Date creationDate = new Date();
                userOAuth.setCreationDate(creationDate);
                userBdd.saveUser(userOAuth);
            }
            String username = String.valueOf(id);
            String password = String.valueOf(id);

            // check user in DB and add it to the Session
            user = userBdd.checkCredentials(username, LoginForm.getEncryptedPassword(password));
            if (user != null) {
                LoginForm loginForm = new LoginForm();
                loginForm.setUsername(username); loginForm.setPassword(password);
                request.getSession().setAttribute("LOGGEDIN_USER", loginForm);
                request.getSession().setAttribute("USER_INFORMATIONS", user);
                return "redirect:/upload";
            }
        }
        return "redirect:/index.failed";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(LoginForm loginForm, HttpServletRequest request, Model model, ModelMap param) throws Exception {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        if(username != null && password != null){
            user = userBdd.checkCredentials(username, LoginForm.getEncryptedPassword(password));
            if( user != null ){
                request.getSession().setAttribute("LOGGEDIN_USER", loginForm);
                request.getSession().setAttribute("USER_INFORMATIONS", user);
                param.remove("error");
                return "redirect:/upload";
            }else{
                model.addAttribute("loginAttribute", loginForm);
                param.put("error", "error");
                return "redirect:/index";
            }
        }else{
            model.addAttribute("loginAttribute", loginForm);
            return "redirect:/index";
        }
    }
    @RequestMapping(value="/logOff", method = RequestMethod.GET)
    public String logOff(Model model, LoginForm loginform, HttpServletRequest request, ModelMap param){
        HttpSession session = request.getSession();
        session.removeAttribute("LOGGEDIN_USER");
        session.removeAttribute("USER_INFORMATIONS");
        model.addAttribute("loginAttribute", loginform);
        param.put("logout", "success");
        return "redirect:/index";
    }

    @RequestMapping(value="/edit", method=RequestMethod.POST)
    public String editUser(@ModelAttribute("user")UserAccount editUser,
                           BindingResult result, ModelMap model, HttpServletRequest request)  {

        user.setPassword(LoginForm.getEncryptedPassword(editUser.getPassword()));
        user.setAdresse(editUser.getAdresse());
        user.setCodePostal(editUser.getCodePostal());
        user.setLastName(editUser.getLastName());
        user.setFirstName(editUser.getFirstName());
        user.setPhone(editUser.getPhone());
        userBdd.saveUser(user);
        // Create a loginform and check in bdd if users exists
        LoginForm loginForm = new LoginForm();
        loginForm.setPassword(user.getPassword());
        loginForm.setUsername(user.getEmail());
        request.getSession().setAttribute("LOGGEDIN_USER", loginForm);
        request.getSession().setAttribute("USER_INFORMATIONS", user);
        return "redirect:/dashboard";

    }


}
