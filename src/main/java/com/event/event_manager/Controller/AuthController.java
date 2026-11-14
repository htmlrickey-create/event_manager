package com.event.event_manager.Controller;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Form.LoginForm;
import com.event.event_manager.Form.RegisterForm;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class AuthController {

    private final UsersService service;

    private final PasswordEncoder passwordEncoder;

    private final EventMemberRepository repository;

    @GetMapping ("/login")
    public String view(Model model, LoginForm form){
        model.addAttribute("loginForm", new LoginForm());

        model.addAttribute("registerForm", new RegisterForm());

        return "login";
    }
    
    @PostMapping ("/login")
    public String login(Model model,LoginForm form, HttpSession session, HttpServletRequest request, HttpServletResponse response){

        var userInfo = service.serchUserByEmail(form.getEmail());

        var isCorrectUserAuth = userInfo.isPresent()
                && passwordEncoder.matches(form.getPassword(), userInfo.get().getPasswordHash());

        

        if(isCorrectUserAuth){

            request.changeSessionId();

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(form.getEmail(),null, AuthorityUtils.NO_AUTHORITIES);

            SecurityContext context = SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

            new HttpSessionSecurityContextRepository().saveContext(context,request,response);

            List<EventMembers> memberships = repository.findByUser_Email(form.getEmail());

            boolean isStaff = memberships.stream().anyMatch(member -> "STAFF".equals(member.getRole()));
            if(isStaff){
                return "redirect:/staff/eventMG";
            }

            return "redirect:/create_event";
        }else{
            model.addAttribute("errorMsg", "メールアドレスまたはパスワードが間違えています");

            model.addAttribute("loginForm", form);

            model.addAttribute("registerForm", new RegisterForm());

            return "login";
        }
    }

    @GetMapping("/register")
public String viewRegister(Model model) {

    model.addAttribute("registerForm", new RegisterForm());

    return "register";
}

@PostMapping("/register")
public String register(
        @ModelAttribute("registerForm") RegisterForm form,
        Model model,
        HttpSession session,
        HttpServletRequest request,
        HttpServletResponse response) {

    service.RegisterUser(form);

    request.changeSessionId();

    session.setAttribute("email", form.getEmail());

    Authentication authentication =
            new UsernamePasswordAuthenticationToken(
                    form.getEmail(),
                    null,
                    AuthorityUtils.NO_AUTHORITIES
            );

    SecurityContext context =
            SecurityContextHolder.createEmptyContext();

    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);

    new HttpSessionSecurityContextRepository()
            .saveContext(context, request, response);

    return "redirect:/create_event";
}
}
