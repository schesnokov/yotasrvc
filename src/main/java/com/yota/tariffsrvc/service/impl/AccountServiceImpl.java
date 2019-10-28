package com.yota.tariffsrvc.service.impl;

import com.yota.tariffsrvc.exception.YotaCustomException;
import com.yota.tariffsrvc.model.dto.PackagesDTO;
import com.yota.tariffsrvc.model.entity.Account;
import com.yota.tariffsrvc.model.entity.BasePackage;
import com.yota.tariffsrvc.model.entity.GigabytesPackage;
import com.yota.tariffsrvc.model.entity.MinutesPackage;
import com.yota.tariffsrvc.repository.api.AccountRepository;
import com.yota.tariffsrvc.service.api.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Chesnokov Sergei
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private AccountRepository accountRepository;

    /**
     * Instantiates a new Account service.
     *
     * @param accountRepository the account repository
     */
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account findById(long accId) {
        try {
            return accountRepository.findById(accId).orElseThrow(() -> new YotaCustomException("No Account with id " + accId));
        } catch (YotaCustomException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeStatus(String phoneNumber) throws YotaCustomException {
        LOGGER.debug("Trying to change Account's status with phone {}", phoneNumber);
        Account acc = getAccByPhoneNumber(phoneNumber);

        //Changing account status to opposite
        acc.setActive(!acc.isActive());

        //Updating entity
        accountRepository.save(acc);
        LOGGER.debug("Account with phone {} status successfully changed to {}", phoneNumber, acc.isActive());

        //Returning true as success operation
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagesDTO addPackages(String phoneNumber, MinutesPackage minutesPackage, GigabytesPackage gigabytesPackage) throws YotaCustomException {
        PackagesDTO result = new PackagesDTO();
        LOGGER.debug("Trying to add packages to Account with phone {}", phoneNumber);
        Account acc = getAccByPhoneNumber(phoneNumber);

        //Check is fetched account active
        checkIsAccActive(acc);

        //Checking if input of minutes and period of validity in packages are valid
        checkValidInput("Minutes amount", minutesPackage.getAmount());
        checkValidInput("Minutes period of validity", minutesPackage.getDaysCount());
        checkValidInput("Gigabytes amount", gigabytesPackage.getAmount());
        checkValidInput("Gigabytes period of validity", gigabytesPackage.getDaysCount());

        //Setting expiration date based on todays date + period of validity of package
        minutesPackage.setExpirationDate(LocalDate.now().plusDays(minutesPackage.getDaysCount()));
        LOGGER.debug("Set new expiration date to minutes package equals to {}", minutesPackage.getExpirationDate());
        gigabytesPackage.setExpirationDate(LocalDate.now().plusDays(gigabytesPackage.getDaysCount()));
        LOGGER.debug("Set new expiration date to gigabytes package equals to {}", gigabytesPackage.getExpirationDate());

        //Setting new packages
        LOGGER.debug("Set minutes package to Account with phone {}. Amount of minutes = {}, Expiration date is {}", phoneNumber, minutesPackage.getAmount(), minutesPackage.getExpirationDate());
        setNewPackageToAccount(acc, minutesPackage);
        LOGGER.debug("Set gigabytes package to Account with phone {}. Amount of gigabytes = {}, Expiration date is {}", phoneNumber, gigabytesPackage.getAmount(), gigabytesPackage.getExpirationDate());
        setNewPackageToAccount(acc, gigabytesPackage);

        //Updating entity
        accountRepository.save(acc);
        LOGGER.debug("Account with phone {} updated in DB", phoneNumber);

        //Forming DTO for return
        result.setMinutesPackage(acc.getActiveMinutesPackage());
        result.setGigabytesPackage(acc.getActiveGigabytesPackage());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int spendMinutes(String phoneNumber, int amount) throws YotaCustomException {
        LOGGER.debug("Trying to spend {} minutes from Account with phone {}", amount, phoneNumber);
        //Check is amount of minutes valid
        checkValidInput("Minutes amount", amount);

        Account acc = getAccByPhoneNumber(phoneNumber);
        //Check is fetched account active
        checkIsAccActive(acc);

        //Get active minutes package if exists
        MinutesPackage minutesPackage = Optional.ofNullable(acc.getActiveMinutesPackage()).orElseThrow(() -> new YotaCustomException("No active package"));

        //Check is package valid
        checkPackageExpiration(minutesPackage);

        //Check could specified amount be spend or throws Exception
        int newAmount;
        if (minutesPackage.getAmount() >= amount) {
            newAmount = minutesPackage.getAmount() - amount;
            //Setting new amount of gigabytes
            minutesPackage.setAmount(newAmount);
            LOGGER.debug("Set new amount of minutes to Account with phone {}. Amount of gigabytes = {}", phoneNumber, newAmount);
        } else {
            throw new YotaCustomException("Not enough minutes to spend");
        }

        //Updating entity
        accountRepository.save(acc);
        LOGGER.debug("Account with phone {} updated in DB", phoneNumber);

        //Return new amount of minutes
        return newAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int spendGigabytes(String phoneNumber, int amount) throws YotaCustomException {
        LOGGER.debug("Trying to spend {} gigabytes from Account with phone {}", amount, phoneNumber);
        //Check is amount of gigabytes valid
        checkValidInput("Gigabytes amount", amount);

        Account acc = getAccByPhoneNumber(phoneNumber);
        //Check is fetched account active
        checkIsAccActive(acc);

        //Get active gigabytes package if exists
        GigabytesPackage gigabytesPackage = Optional.ofNullable(acc.getActiveGigabytesPackage()).orElseThrow(() -> new YotaCustomException("No active package"));

        //Check is package valid
        checkPackageExpiration(gigabytesPackage);

        //Check could specified amount be spend or throws Exception
        int newAmount;
        if (gigabytesPackage.getAmount() >= amount) {
             newAmount = gigabytesPackage.getAmount() - amount;
            //Setting new amount of gigabytes
            gigabytesPackage.setAmount(newAmount);
            LOGGER.debug("Set new amount of gigabytes to Account with phone {}. Amount of gigabytes = {}", phoneNumber, newAmount);
        } else {
            throw new YotaCustomException("Not enough gigabytes to spend");
        }

        //Updating entity
        accountRepository.save(acc);
        LOGGER.debug("Account with phone {} updated in DB", phoneNumber);

        //Return new amount of gigabytes
        return newAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagesDTO getAvailableMinsAndGigs(String phoneNumber) throws YotaCustomException {
        PackagesDTO result = new PackagesDTO();
        LOGGER.debug("Trying to get available minutes and gigabytes from Account with phone {}", phoneNumber);
        Account acc = getAccByPhoneNumber(phoneNumber);
        //Check is fetched account active
        checkIsAccActive(acc);

        //Get active minutes package if exists
        MinutesPackage minutesPackage = Optional.ofNullable(acc.getActiveMinutesPackage()).orElse(new MinutesPackage());

        //Forming DTO for return
        result.setMinutesPackage(minutesPackage);

        //Get active minutes package if exists
        GigabytesPackage gigabytesPackage = Optional.ofNullable(acc.getActiveGigabytesPackage()).orElse(new GigabytesPackage());

        //Forming DTO for return
        result.setGigabytesPackage(gigabytesPackage);
        LOGGER.debug("Amount of minutes = {}, Amount of gigabytes = {}", result.getMinutesPackage().getAmount(),
                result.getGigabytesPackage().getAmount());
        return result;
    }

    /**
     * Gets account by phone number or throws exception
     *
     * @param phoneNumber the phone number
     * @return the account
     * @throws YotaCustomException custom exception
     */
    private Account getAccByPhoneNumber(String phoneNumber) throws YotaCustomException {
        LOGGER.debug("Fetching account with phone {} from DB", phoneNumber);
        //Returns Account if exists or else throws Exception
        return accountRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new YotaCustomException("No account with this phone " + phoneNumber));
    }

    /**
     * Checks is account active or throws exception
     *
     * @param account the account
     * @throws YotaCustomException custom exception
     */
    private void checkIsAccActive(Account account) throws YotaCustomException {
        LOGGER.debug("Checking account with phone {} is active", account.getPhoneNumber());
        //Checks is account active or else throws Exception
        if (!account.isActive()) {
            throw new YotaCustomException("Account with phone " + account.getPhoneNumber() + " is inactive");
        }
    }

    /**
     * Checks is input valid or throws exception
     *
     * @param propertyTested the name of checked property
     * @param amount the property value
     */
    private void checkValidInput(String propertyTested, int amount) {
        LOGGER.debug("Checking is input for {} = {} is valid", propertyTested, amount);
        //Checks is input positive or else throws Exception
        if (amount < 0) {
            throw new IllegalArgumentException("Input for " + propertyTested + " is less than 0");
        }
    }

    /**
     * Checks is package expired
     *
     * @param basePackage the package
     * @throws YotaCustomException custom exception
     */
    private void checkPackageExpiration(BasePackage basePackage) throws YotaCustomException {
        LOGGER.debug("Checking if package is not expired");
        //Checks is package valid or else throws Exception
        if (Optional.ofNullable(basePackage.getExpirationDate()).orElse(LocalDate.MIN).isBefore(LocalDate.now())) {
            throw new YotaCustomException("Package is expired");
        }
    }

    /**
     * Sets package to account if inputs not equals to 0
     *
     * @param account the account
     * @param basePackage the package with new values
     */
    private void setNewPackageToAccount(Account account, BasePackage basePackage) {
        if (basePackage instanceof MinutesPackage) {
            if (basePackage.getAmount() != 0 && basePackage.getDaysCount() != 0) {
                account.setActiveMinutesPackage((MinutesPackage) basePackage);
            }
        } else if (basePackage instanceof GigabytesPackage) {
            if (basePackage.getAmount() != 0 && basePackage.getDaysCount() != 0) {
                account.setActiveGigabytesPackage((GigabytesPackage) basePackage);
            }
        }
    }
}
