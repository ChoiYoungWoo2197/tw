package kr.co.cmtinfo.seal.app.web;

import kr.co.cmtinfo.seal.domain.member.domain.SimpleMember;
import kr.co.cmtinfo.seal.domain.member.service.SimpleMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Yongsu Son
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SimpleMemberService simpleMemberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SimpleMember member = simpleMemberService.getMemberByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return new MyUserDetails(member);
    }
}
