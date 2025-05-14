package net.dunice.mstool.security;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        return new CustomUserDetails(user);
    }
}
