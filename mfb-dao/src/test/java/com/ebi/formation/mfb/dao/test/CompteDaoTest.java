package com.ebi.formation.mfb.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.ebi.formation.mfb.dao.ICompteDao;
import com.ebi.formation.mfb.entities.Compte;
import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.RollbackTransactionalDataSetTestExecutionListener;

/**
 * Test unitaire de PersonDAO
 * 
 * @author excilys
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:persistence-config.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		RollbackTransactionalDataSetTestExecutionListener.class, TransactionalTestExecutionListener.class, })
@TransactionConfiguration
@Transactional
public class CompteDaoTest {

	@Autowired
	private ICompteDao compteDao;

	/**
	 * Test lorsqu'un utilisateur n'a aucun compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testNoAccount() {
		List<Compte> accounts = compteDao.findComptesByUsername("toto");
		assertTrue(accounts.isEmpty());
	}

	/**
	 * Test lorsqu'un utilisateur a un seul compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testSingleAccount() {
		List<Compte> accounts = compteDao.findComptesByUsername("foo");
		assertEquals(1, accounts.size());
	}

	/**
	 * Test lorsqu'un utilisateur a plusieurs comptes
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testMultiplesAccounts() {
		List<Compte> accounts = compteDao.findComptesByUsername("bastou");
		assertEquals(2, accounts.size());
	}

	/**
	 * Test l'appartenance d'un compte d'un utilisateur via le username et l'id du compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testCompteOwnership() {
		assertFalse(compteDao.checkCompteOwnershipByUsernameAndCompteId("toto", 1L));
		assertTrue(compteDao.checkCompteOwnershipByUsernameAndCompteId("foo", 1L));
		assertTrue(compteDao.checkCompteOwnershipByUsernameAndCompteId("bastou", 2L));
		assertTrue(compteDao.checkCompteOwnershipByUsernameAndCompteId("bastou", 3L));
	}

	/**
	 * Test la récupération d'un compte via son id
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testFindCompteById() {
		Compte result1 = compteDao.findCompteById(1);
		Compte result2 = compteDao.findCompteById(0);
		assertNotNull(result1);
		assertEquals(new Long(1L), result1.getId());
		assertNull(result2);
	}

	/**
	 * Test la récupération d'un compte via son numéro de compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testFindCompteByNumeroCompte() {
		Compte result1 = compteDao.findCompteByNumeroCompte("0001");
		Compte result2 = compteDao.findCompteByNumeroCompte("ABC");
		assertNotNull(result1);
		assertEquals(new Long(1L), result1.getId());
		assertNull(result2);
	}

	/**
	 * Test la persistance d'un compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testSaveCompte() {
		Compte compte = new Compte();
		compte.setLabel("label");
		compte.setNumeroCompte("123456789");
		compte.setSolde(new BigDecimal(0));
		compte.setEncoursCarte(new BigDecimal(0));
		compte.setSoldePrevisionnel(new BigDecimal(0));
		compteDao.save(compte);
		Compte tmp = compteDao.findCompteById(4L);
		assertNotNull(tmp);
		assertEquals(compte, tmp);
	}

	/**
	 * Test la récupération de tous les comptes
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testFindAllComptes() {
		List<Compte> listComptes = compteDao.findAllComptes();
		assertNotNull(listComptes);
		assertEquals(3, listComptes.size());
	}

	/**
	 * Test la mise à jour du solde d'un compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testUpdateCompteSolde() {
		compteDao.updateCompteSolde(1L, BigDecimal.TEN);
		Compte c = compteDao.findCompteById(1L);
		assertEquals(0, BigDecimal.TEN.compareTo(c.getSolde()));
		compteDao.updateCompteSolde(2L, new BigDecimal(-10));
		c = compteDao.findCompteById(2L);
		assertEquals(0, new BigDecimal(-10).compareTo(c.getSolde()));
	}

	/**
	 * Test la mise à jour du solde et de l'encours carte d'un compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testUpdateCompteSoldeAndEncoursCarte() {
		compteDao.updateCompteSoldeAndEncoursCarte(1L, BigDecimal.TEN);
		Compte c = compteDao.findCompteById(1L);
		assertEquals(0, BigDecimal.TEN.compareTo(c.getSolde()));
		assertEquals(0, new BigDecimal(-10).compareTo(c.getEncoursCarte()));
		compteDao.updateCompteSoldeAndEncoursCarte(2L, new BigDecimal(-10));
		c = compteDao.findCompteById(2L);
		assertEquals(0, new BigDecimal(-10).compareTo(c.getSolde()));
		assertEquals(0, BigDecimal.TEN.compareTo(c.getEncoursCarte()));
	}

	/**
	 * Test la mise à jour de l'encours carte et du solde prévisionnel d'un compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testUpdateCompteEncoursCarteAndSoldePrevisionnel() {
		compteDao.updateCompteEncoursCarteAndSoldePrevisionnel(1L, new BigDecimal(10));
		Compte c = compteDao.findCompteById(1L);
		assertEquals(0, BigDecimal.TEN.compareTo(c.getSoldePrevisionnel()));
		assertEquals(0, BigDecimal.TEN.compareTo(c.getEncoursCarte()));
	}

	/**
	 * Test la mise à jour du solde prévisionnel d'un compte
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testUpdateCompteSoldePrevisionnel() {
		compteDao.updateCompteSoldePrevisionnel(1L, BigDecimal.TEN);
		Compte c = compteDao.findCompteById(1L);
		assertEquals(0, BigDecimal.TEN.compareTo(c.getSoldePrevisionnel()));
	}

	/**
	 * Test la mise à jour du solde et du solde prévisionnel
	 */
	@DataSet("dataSet-CompteDaoTest.xml")
	@Test
	public void testUpdateCompteSoldeAndSoldePrevisionnel() {
		compteDao.updateCompteSoldeAndSoldePrevisionnel(1L, BigDecimal.TEN);
		Compte c = compteDao.findCompteById(1L);
		assertEquals(0, BigDecimal.TEN.compareTo(c.getSolde()));
		assertEquals(0, BigDecimal.TEN.compareTo(c.getSoldePrevisionnel()));
	}
}
