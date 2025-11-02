package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Tải thông tin người dùng theo email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Không tìm thấy email: {}", email);
                    return new UsernameNotFoundException("Không tìm thấy email: " + email);
                });
    }

    public Long getUserIdByEmail(String email) throws UsernameNotFoundException {
        log.debug("Lấy ID người dùng theo email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Không tìm thấy email: {}", email);
                    return new UsernameNotFoundException("Không tìm thấy email: " + email);
                })
                .getId();
    }

}
