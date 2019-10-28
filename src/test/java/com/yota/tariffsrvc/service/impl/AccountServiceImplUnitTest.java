package com.yota.tariffsrvc.service.impl;

import com.yota.tariffsrvc.exception.YotaCustomException;
import com.yota.tariffsrvc.model.dto.PackagesDTO;
import com.yota.tariffsrvc.model.entity.Account;
import com.yota.tariffsrvc.model.entity.GigabytesPackage;
import com.yota.tariffsrvc.model.entity.MinutesPackage;
import com.yota.tariffsrvc.repository.api.AccountRepository;
import com.yota.tariffsrvc.service.api.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplUnitTest {

    private static final String CORRECT_PHONE_NUMBER = "+79213335671";
    private static final String INCORRECT_PHONE_NUMBER = "+79213335675";

    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @Before
    public void setup() {
        accountService = new AccountServiceImpl(accountRepository);
        account = new Account(CORRECT_PHONE_NUMBER, true, new MinutesPackage(100, 10, LocalDate.MAX), new GigabytesPackage(10, 15, LocalDate.MAX));
    }

    @Test
    public void whenRequestAccountFindByIdThenReturnAccountPositive() {
        //given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //when
        Account acc = accountService.findById(1);

        //then
        Assert.assertEquals(CORRECT_PHONE_NUMBER, acc.getPhoneNumber());
    }

    @Test
    public void whenRequestAccountFindByIdThenReturnAccountNegative() {
        //given
        when(accountRepository.findById(13L)).thenReturn(Optional.ofNullable(null));

        //when
        Account acc = accountService.findById(13);

        //then
        Assert.assertNull(acc);
    }

    @Test
    public void whenChangeStatusRequestThenSetNewStatusPositive() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER)).thenReturn(Optional.of(account));

        //when
        boolean result = accountService.changeStatus(CORRECT_PHONE_NUMBER);
        boolean accStatus = account.isActive();

        //then
        Assert.assertTrue(result);
        Assert.assertFalse(accStatus);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }

    @Test(expected = YotaCustomException.class)
    public void whenChangeStatusRequestThenSetNewStatusNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(INCORRECT_PHONE_NUMBER)).thenReturn(Optional.ofNullable(null));

        //when
        accountService.changeStatus(INCORRECT_PHONE_NUMBER);
    }

    @Test
    public void whenRequestAddTwoPackagesThenAddNewPackagesPositive() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER)).thenReturn(Optional.of(account));

        //when
        PackagesDTO packages = accountService.addPackages(CORRECT_PHONE_NUMBER,
                                    new MinutesPackage(100, 10, null),
                                    new GigabytesPackage(10, 15, null));

        //then
        Assert.assertEquals(100, account.getActiveMinutesPackage().getAmount());
        Assert.assertEquals(10, account.getActiveGigabytesPackage().getAmount());
        Assert.assertNotNull(packages);
        Assert.assertEquals(LocalDate.now().plusDays(10), packages.getMinutesPackage().getExpirationDate());
        Assert.assertEquals(LocalDate.now().plusDays(15), packages.getGigabytesPackage().getExpirationDate());
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRequestAddTwoPackagesWithNegativeAmountsThenAddNewPackagesNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER)).thenReturn(Optional.of(account));

        //when
        accountService.addPackages(CORRECT_PHONE_NUMBER,
                new MinutesPackage(-100, 10, null),
                new GigabytesPackage(10, -15, null));
    }

    @Test(expected = YotaCustomException.class)
    public void whenRequestAddTwoPackagesToUnexistingAccountNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(INCORRECT_PHONE_NUMBER)).thenReturn(Optional.ofNullable(null));

        //when
        accountService.addPackages(INCORRECT_PHONE_NUMBER,
                new MinutesPackage(-100, 10, null),
                new GigabytesPackage(10, -15, null));


    }

    @Test
    public void whenRequestSpendMinutesThenUpdateAccPositive() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER)).thenReturn(Optional.of(account));

        //when
        accountService.spendMinutes(CORRECT_PHONE_NUMBER, 20);

        //then
        Assert.assertEquals(80, account.getActiveMinutesPackage().getAmount());
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }

    @Test(expected = YotaCustomException.class)
    public void whenRequestSpendMinutesInactiveAccountThenUpdateAccNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER))
                .thenReturn(Optional.of(new Account(CORRECT_PHONE_NUMBER, false, null, null)));

        //when
        accountService.spendMinutes(CORRECT_PHONE_NUMBER, 30);

        //then
        Mockito.verify(accountRepository, Mockito.times(0)).save(account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRequestSpendMinutesNegativeValueThenUpdateAccNegative() throws YotaCustomException {
        //when
        accountService.spendMinutes(CORRECT_PHONE_NUMBER, -30);

        //then
        Mockito.verify(accountRepository, Mockito.times(0)).save(account);
    }

    @Test
    public void whenRequestSpendGigabytesThenUpdateAccPositive() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER)).thenReturn(Optional.of(account));

        //when
        accountService.spendGigabytes(CORRECT_PHONE_NUMBER, 1);

        //then
        Assert.assertEquals(9, account.getActiveGigabytesPackage().getAmount());
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }

    @Test(expected = YotaCustomException.class)
    public void whenRequestSpendGigabytesInactiveAccountThenUpdateAccNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER))
                .thenReturn(Optional.of(new Account(CORRECT_PHONE_NUMBER, false, null, null)));

        //when
        accountService.spendGigabytes(CORRECT_PHONE_NUMBER, 30);

        //then
        Mockito.verify(accountRepository, Mockito.times(0)).save(account);
    }

    @Test(expected = YotaCustomException.class)
    public void whenRequestSpendGigabytesExpiredPackageThenUpdateAccNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER))
                .thenReturn(Optional.of(new Account(CORRECT_PHONE_NUMBER, true, null, new GigabytesPackage(7, 1, LocalDate.MIN))));

        //when
        accountService.spendGigabytes(CORRECT_PHONE_NUMBER, 30);

        //then
        Mockito.verify(accountRepository, Mockito.times(0)).save(account);
    }

    @Test
    public void whenRequestAvailableMinutesAndGigabytesThenReturnPostitive() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER))
                .thenReturn(Optional.of(account));

        //when
        PackagesDTO packages = accountService.getAvailableMinsAndGigs(CORRECT_PHONE_NUMBER);

        //then
        Assert.assertEquals(100, packages.getMinutesPackage().getAmount());
        Assert.assertEquals(10, packages.getGigabytesPackage().getAmount());
    }

    @Test(expected = YotaCustomException.class)
    public void whenRequestAvailableMinutesAndGigabytesAccInactiveThenReturnNegative() throws YotaCustomException {
        //given
        when(accountRepository.findByPhoneNumber(CORRECT_PHONE_NUMBER))
                .thenReturn(Optional.of(new Account(CORRECT_PHONE_NUMBER, false, null, null)));

        //when
        PackagesDTO packages = accountService.getAvailableMinsAndGigs(CORRECT_PHONE_NUMBER);

        //then
        Assert.assertNull(packages);

    }

}
