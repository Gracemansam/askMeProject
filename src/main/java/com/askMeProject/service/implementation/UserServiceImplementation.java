package com.askMeProject.service.implementation;


import com.askMeProject.common_constant.CommonConstant;
import com.askMeProject.dto.TwoFAVerificationRequest;
import com.askMeProject.security.JwtTokenProvider;
import com.askMeProject.converter.UserConverter;
import com.askMeProject.dto.LoginRequest;
import com.askMeProject.dto.UserDTO;
import com.askMeProject.dto.response.AuthResponse;
import com.askMeProject.exception.BusinessException;
import com.askMeProject.exception.ErrorModel;
import com.askMeProject.model.User;
import com.askMeProject.model.VerificationToken;
import com.askMeProject.repository.UserRepository;
import com.askMeProject.repository.VerificationTokenRepository;
import com.askMeProject.service.UserService;
import com.askMeProject.utils.ApplicationUrl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@Lazy
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetails customUserDetails;

    private final UserConverter userConverter;
    private final VerificationTokenRepository tokenRepository;
    private final ApplicationEventPublisher publisher;
    private final ApplicationUrl applicationUrl;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;


    @Override
    public ResponseEntity<AuthResponse> registerPatient(UserDTO userDto, HttpServletRequest request) {

        Optional<User> isEmailExist = userRepository.findByEmail(userDto.getEmail());

        // Check if user with the given email already exists
        if (isEmailExist.isPresent()) {
            ErrorModel errorModel = ErrorModel.builder()
                    .code(CommonConstant.USER_ALREADY_EXIST_CODE)
                    .message(CommonConstant.USER_ALREADY_EXIST)
//					.timestamp(LocalDateTime.now())
                    .build();
            throw new BusinessException(errorModel);


        }

        // Create new user
        User newUser = userConverter.convertDTOtoPatientEntity(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setAuthorities("PATIENT");
        newUser.setMfaEnabled(userDto.isMfaEnabled());
        if (newUser.isMfaEnabled()) {
            newUser.setSecret(twoFactorAuthenticationService.generateNewSecret());
        }
        userRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtToken = jwtTokenProvider.generateToken(authentication);
        var authResponse = AuthResponse.builder()
                .secretImageUri(twoFactorAuthenticationService.generateQrCodeImageUri(newUser.getSecret()))
                .jwt(jwtToken)
                .mfaEnabled(newUser.isMfaEnabled())
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);


//	try {
//
//		publisher.publishEvent(new RegistrationCompleteEvent(newUser, applicationUrl.applicationUrl(request)));
//	} catch (Exception e) {
//		e.printStackTrace();
//		throw new UserException(CommonConstant.USER_REGISTRATION_FAILED);
//	}


//		UserDto savedUserResponse = userConverter.convertEntityToDTO(savedUser);


    }

    @Override
    public ResponseEntity<AuthResponse> registerDoctor(UserDTO userDto, HttpServletRequest request) {

        Optional<User> isEmailExist = userRepository.findByEmail(userDto.getEmail());

        // Check if user with the given email already exists
        if (isEmailExist.isPresent()) {
            ErrorModel errorModel = ErrorModel.builder()
                    .code(CommonConstant.USER_ALREADY_EXIST_CODE)
                    .message(CommonConstant.USER_ALREADY_EXIST)
//					.timestamp(LocalDateTime.now())
                    .build();
            throw new BusinessException(errorModel);


        }

        // Create new user
        User newUser = userConverter.convertDTOtoDoctorEntity(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setAuthorities("DOCTOR");
        newUser.setMfaEnabled(userDto.isMfaEnabled());
        if (newUser.isMfaEnabled()) {
            newUser.setSecret(twoFactorAuthenticationService.generateNewSecret());
        }
        userRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtToken = jwtTokenProvider.generateToken(authentication);
        var authResponse = AuthResponse.builder()
                .secretImageUri(twoFactorAuthenticationService.generateQrCodeImageUri(newUser.getSecret()))
                .jwt(jwtToken)
                .mfaEnabled(newUser.isMfaEnabled())
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);


//	try {
//
//		publisher.publishEvent(new RegistrationCompleteEvent(newUser, applicationUrl.applicationUrl(request)));
//	} catch (Exception e) {
//		e.printStackTrace();
//		throw new UserException(CommonConstant.USER_REGISTRATION_FAILED);
//	}


    }

    @Override
    public ResponseEntity<AuthResponse> signIn(LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByEmail(username).orElseThrow();
        if (user.isMfaEnabled()) {
            var authResponse = AuthResponse.builder()
                    .jwt("")
                    .status(true)
                    .mfaEnabled(true)
                    .secretImageUri(user.getSecret())
                    .build();
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }

        String token = jwtTokenProvider.generateToken(authentication);

        var authResponse = AuthResponse.builder()
                .jwt(token)
                .status(true)
                .mfaEnabled(false)
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


        @Override
        public Authentication authenticate (String username, String password){
            UserDetails userDetails = customUserDetails.loadUserByUsername(username);

            System.out.println("sign in userDetails - " + userDetails);

            if (userDetails == null) {
                System.out.println("sign in userDetails - null " + userDetails);
                throw new BadCredentialsException("Invalid username or password");
            }
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                System.out.println("sign in userDetails - password not match " + userDetails);
                throw new BadCredentialsException("Invalid username or password");
            }
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }


        @Override
        public User findUserById (Long userId){
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                return user.get();
            }
            ErrorModel errorModel = ErrorModel.builder()
                    .code(CommonConstant.USER_NOT_FOUND_CODE)
                    .message(CommonConstant.USER_NOT_FOUND)
//				.timestamp(LocalDateTime.now())
                    .build();
            throw new BusinessException(errorModel);
        }

        @Override
        public User findUserProfileByJwt (String jwt){
            System.out.println("user service");
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);

            System.out.println("email" + email);

            Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                ErrorModel errorModel = ErrorModel.builder()
                        .code(CommonConstant.USER_NOT_FOUND_CODE)
                        .message(CommonConstant.USER_NOT_FOUND)
//					.timestamp(LocalDateTime.now())
                        .build();
                throw new BusinessException(errorModel);
            }
//		System.out.println("email user"+user.getEmail());
            return user.get();
        }

//	@Override
//	public List<User> findAllUsers() {
//		// TODO Auto-generated method stub
//		return userRepository.findAllByOrderByCreatedAtDesc();
//	}
        @Override
        public void saveUserVerificationToken (User theUser, String token){
            var verificationToken = new VerificationToken(token, theUser);
            tokenRepository.save(verificationToken);
        }
        @Override
        public String validateToken (String theToken){
            VerificationToken token = tokenRepository.findByToken(theToken);
            if (token == null) {
                return CommonConstant.TOKEN_NOT_VALID;
            }
            User user = token.getUser();
            Calendar calendar = Calendar.getInstance();
            if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
                tokenRepository.delete(token);
                return CommonConstant.TOKEN_ALREADY_EXPIRED;
            }
            user.setEnabled(true);
            userRepository.save(user);
            return CommonConstant.TOKEN_VALID;
        }
        @Override
        public VerificationToken findByToken (String token){
            return tokenRepository.findByToken(token);
        }

        public AuthResponse verifyCode (
                TwoFAVerificationRequest verificationRequest
	){
            User user = userRepository
                    .findByEmail(verificationRequest.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("No user found with %S", verificationRequest.getEmail()))
                    );
            if (twoFactorAuthenticationService.isOtpNotValid(user.getSecret(), verificationRequest.getCode())) {

                throw new BadCredentialsException("Code is not correct");
            }
            var authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var jwtToken = jwtTokenProvider.generateToken(authentication);
            return AuthResponse.builder()
                    .jwt(jwtToken)
                    .mfaEnabled(user.isMfaEnabled())
                    .build();
        }

    }
