package com.atlas.howardAI.service;

import com.atlas.howardAI.dto.Oauth2UserInfoDto;
import com.atlas.howardAI.entity.User;
import com.atlas.howardAI.repository.UserRepository;
import com.atlas.howardAI.security.UserPrincipal;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String fullName = oAuth2User.getAttributes().get("name").toString();
        String firstName = fullName;
        String lastName = "";

        if (fullName.contains(" ")) {
            int lastSpaceIndex = fullName.lastIndexOf(" ");
            firstName = fullName.substring(0, lastSpaceIndex);
            lastName = fullName.substring(lastSpaceIndex + 1);
        }

        Oauth2UserInfoDto userInfoDto = Oauth2UserInfoDto
                .builder()
                .name(firstName)
                .lastName(lastName)
                .id(oAuth2User.getAttributes().get("sub").toString())
                .email(oAuth2User.getAttributes().get("email").toString())
                .picture(oAuth2User.getAttributes().get("picture").toString())
                .build();

        Optional<User> userOptional = userRepository.findByEmail(userInfoDto.getEmail());

        User user = userOptional
                .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
                .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, Oauth2UserInfoDto userInfoDto) {
        User user = new User();
        user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        user.setProviderId(userInfoDto.getId());
        user.setFirstName(userInfoDto.getName());
        user.setLastName(userInfoDto.getLastName());
        user.setUsername(userInfoDto.getEmail());
        user.setEmail(userInfoDto.getEmail());
        user.setPicture(userInfoDto.getPicture());
        user.setRole("ROLE_USER"); // Default Role
        user.setMessageLimit(10); // Default Message limit
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, Oauth2UserInfoDto userInfoDto) {
        existingUser.setFirstName(userInfoDto.getName());
        existingUser.setLastName(userInfoDto.getLastName());
        existingUser.setPicture(userInfoDto.getPicture());
        return userRepository.save(existingUser);
    }
}