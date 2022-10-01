package com.wak.igo.service;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


    @Service
    @RequiredArgsConstructor
    public class UserDetailsServiceImpl implements UserDetailsService {
        private final MemberRepository memberRepository;
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<Member> member = memberRepository.findBymemberId(username);
            return member
                    .map(UserDetailsImpl::new)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        }
    }