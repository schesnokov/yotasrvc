package com.yota.tariffsrvc.service.api;

import com.yota.tariffsrvc.exception.YotaCustomException;
import com.yota.tariffsrvc.model.dto.PackagesDTO;
import com.yota.tariffsrvc.model.entity.Account;
import com.yota.tariffsrvc.model.entity.GigabytesPackage;
import com.yota.tariffsrvc.model.entity.MinutesPackage;

/**
 * @author Chesnokov Sergei
 */
public interface AccountService {

    /**
     * Find by id account.
     *
     * @param accId the acc id
     * @return the account
     */
    Account findById(long accId);

    /**
     * Change status of account to opposite.
     *
     * @param phoneNumber the phone number
     * @return true if success operation
     * @throws YotaCustomException custom exception
     */
    boolean changeStatus(String phoneNumber) throws YotaCustomException;

    /**
     * Add packages packages dto.
     *
     * @param phoneNumber      the phone number
     * @param minutesPackage   the minutes package
     * @param gigabytesPackage the gigabytes package
     * @return the packages dto with amount of minutes, gigabytes and its expiration dates
     * @throws YotaCustomException custom exception
     */
    PackagesDTO addPackages(String phoneNumber, MinutesPackage minutesPackage, GigabytesPackage gigabytesPackage) throws YotaCustomException;

    /**
     * Spend minutes int.
     *
     * @param phoneNumber the phone number
     * @param amount      minutes to subtract
     * @return the amount of remaining minutes
     * @throws YotaCustomException custom exception
     */
    int spendMinutes(String phoneNumber, int amount) throws YotaCustomException;

    /**
     * Spend gigabytes int.
     *
     * @param phoneNumber the phone number
     * @param amount      gigabytes to subtract
     * @return the amount of remaining gigabytes
     * @throws YotaCustomException custom exception
     */
    int spendGigabytes(String phoneNumber, int amount) throws YotaCustomException;

    /**
     * Gets available mins and gigs.
     *
     * @param phoneNumber the phone number
     * @return the available minutes and gigabytes
     * @throws YotaCustomException custom exception
     */
    PackagesDTO getAvailableMinsAndGigs(String phoneNumber) throws YotaCustomException;
}
