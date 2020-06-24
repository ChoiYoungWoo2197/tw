package kr.co.cmtinfo.seal.app.web.controller;

import kr.co.cmtinfo.seal.core.security.authorization.RegistrationDto;
import kr.co.cmtinfo.seal.domain.member.domain.SimpleMember;
import kr.co.cmtinfo.seal.domain.member.exception.UserAlreadyExistException;
import kr.co.cmtinfo.seal.domain.member.service.SimpleMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Yongsu Son
 */
@Controller
@RequestMapping("authorization")
public class AuthorizationController {

    @Autowired
    private SimpleMemberService simpleMemberService;

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {

        RegistrationDto registrationDto = new RegistrationDto();
        model.addAttribute("user", registrationDto);
        return "authorization/registration";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid RegistrationDto registrationDto,
                                            BindingResult bindingResult, HttpServletRequest request, Errors errors) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("authorization/registration");
        }

        SimpleMember simpleMember = new SimpleMember();
        simpleMember.setName(registrationDto.getName());
        simpleMember.setEmail(registrationDto.getEmail());
        simpleMember.setPassword(registrationDto.getPassword());

        try {
            simpleMemberService.createMember(simpleMember);
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
        }

        return new ModelAndView("authorization/success_register", "user", simpleMember);
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        authentication.getPrincipal();
        authentication.getCredentials();
        authentication.getAuthorities();

        AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
        boolean isAnonymous = authenticationTrustResolver.isAnonymous(authentication);
        boolean isRememberMe = authenticationTrustResolver.isRememberMe(authentication);
        boolean isAuthenticated = authentication.isAuthenticated();


//        MethodSecurityExpressionRoot
//
//        WebSecurityExpressionRoot webSecurityExpressionRoot = new MethodSecurityExpression(authentication, );
//        boolean isAnonymous2 = webSecurityExpressionRoot.isAnonymous();
//        boolean isAuthenticated2 = webSecurityExpressionRoot.isAuthenticated();
//        boolean isFullyAuthenticated = webSecurityExpressionRoot.isFullyAuthenticated();
//        boolean isRememberMe2 = webSecurityExpressionRoot.isRememberMe();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/welcome";
        }

        if (error != null && error.equals("true")) {
            model.addAttribute("loginError", true);
        }
        return "authorization/login";
    }
}
