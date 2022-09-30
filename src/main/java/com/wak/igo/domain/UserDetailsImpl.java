package com.wak.igo.domain;

import java.util.ArrayList;
import java.util.Collection;

import com.wak.igo.shared.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public class UserDetailsImpl implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_MEMBER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }
    public String getMemberId() {
        return member.getMemberid();
    }
    public Long getId_member() {
        return member.getId();
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }
        private Member member;

<<<<<<< HEAD
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_MEMBER.toString());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);
            return authorities;
        }
        public Long getId() { return member.getId(); }
        public String getMemberId() { return member.getMemberId(); }
        @Override
        public String getPassword() {
            return member.getPassword();
=======

        public Long getId() {
            return member.getId();
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
        }

        @Override
        public String getUsername() {
            return member.getNickname();
        }
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }


}