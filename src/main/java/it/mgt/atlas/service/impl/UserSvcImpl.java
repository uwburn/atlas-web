package it.mgt.atlas.service.impl;

import it.mgt.atlas.entity.User;
import it.mgt.atlas.exception.UserCreationException;
import it.mgt.atlas.repository.UserRepo;
import it.mgt.atlas.service.AuthSvc;
import it.mgt.atlas.service.UserSvc;
import it.mgt.util.spring.validation.EmailValidator;
import it.mgt.util.spring.validation.PasswordValidator;
import it.mgt.util.spring.validation.UsernameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class UserSvcImpl implements UserSvc {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserSvcImpl.class);

    @Autowired
    private AuthSvc authService;

    @Autowired
    private UserRepo userRepository;

    @Value("${it.mgt.atlas.usernameMinLength}")
    private int usernameMinLength;
    @Value("${it.mgt.atlas.usernameMaxLength}")
    private int usernameMaxLength;
    @Value("${it.mgt.atlas.usernameSpecialCharsWhitelist}")
    private String usernameSpecialCharsWhitelist;

    @Value("${it.mgt.atlas.passwordMinLength}")
    private int passwordMinLength;
    @Value("${it.mgt.atlas.passwordMaxLength}")
    private int passwordMaxLength;
    @Value("${it.mgt.atlas.passwordMinLetters}")
    private int passwordMinLetters;
    @Value("${it.mgt.atlas.passwordMinUppercase}")
    private int passwordMinUppercase;
    @Value("${it.mgt.atlas.passwordMinLowercase}")
    private int passwordMinLowercase;
    @Value("${it.mgt.atlas.passwordMinDigit}")
    private int passwordMinDigit;
    @Value("${it.mgt.atlas.passwordMinSpecialChars}")
    private int passwordMinSpecialChars;
    @Value("${it.mgt.atlas.passwordAllowUnknownChars}")
    private boolean passwordAllowUnknownChars;
    @Value("${it.mgt.atlas.passwordSpecialChars}")
    private String passwordSpecialChars;

    private UsernameValidator usernameValidator;
    private PasswordValidator passwordValidator;
    private EmailValidator emailValidator = new EmailValidator();

    @PostConstruct
    public void postConstruct() {
        usernameValidator = new UsernameValidator(usernameMinLength, usernameMaxLength, usernameSpecialCharsWhitelist, null);
        passwordValidator = new PasswordValidator(passwordMinLength, passwordMaxLength, passwordMinLetters,
                passwordMinUppercase, passwordMinLowercase, passwordMinDigit, passwordMinSpecialChars,
                passwordAllowUnknownChars, passwordSpecialChars);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        LOGGER.trace("Creating " + user);

        Date now = new Date();

        if (userRepository.countByName(user.getUsername()).longValue() > 0)
            throw new UserCreationException("U1", "Username already exists");

        if (!usernameValidator.validate(user.getUsername()))
            throw new UserCreationException("U2", "Invalid username");

        if (!passwordValidator.validate(user.getPassword()))
            throw new UserCreationException("U3", "Invalid password");

        if (userRepository.countByEmail(user.getEmail()).longValue() > 0)
            throw new UserCreationException("U4", "Email already exists");

        if (!emailValidator.validate(user.getEmail()))
            throw new UserCreationException("U5", "Invalid email");

        user.setRegistrationDate(now);
        user.setPassword(authService.hashPassword(user.getPassword()));

        userRepository.persist(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        LOGGER.trace("Updating " + user);

        if (!usernameValidator.validate(user.getUsername()))
            throw new UserCreationException("U2", "Invalid username");

        User existingUser = userRepository.findByName(user.getUsername());
        if (!existingUser.getId().equals(user.getId()))
            throw new UserCreationException("U1", "Username already exists");

        if (!passwordValidator.validate(user.getPassword()))
            throw new UserCreationException("U3", "Invalid password");

        if (!emailValidator.validate(user.getEmail()))
            throw new UserCreationException("U5", "Invalid email");

        existingUser = userRepository.findByEmail(user.getEmail());
        if (!existingUser.getId().equals(user.getId()))
            throw new UserCreationException("U4", "Email already exists");

        user.setPassword(authService.hashPassword(user.getPassword()));
    }

}
