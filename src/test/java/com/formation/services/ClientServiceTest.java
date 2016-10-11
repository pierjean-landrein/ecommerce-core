package com.formation.services;

import com.formation.exceptions.MetierException;
import com.formation.models.Client;
import org.junit.*;


import java.util.List;
import static org.junit.Assert.*;


public class ClientServiceTest {

  @BeforeClass
  public static void avantLaClasse() {
    System.out.println("avant la classe"+System.currentTimeMillis());
    System.out.flush();
  }

  @AfterClass
  public static void apresLaClasse(){
    System.out.println("apres la classe"+System.currentTimeMillis());
    System.out.flush();
  }
  @Before
  public void avantToutTest(){
    System.out.println("avant tout test"+System.currentTimeMillis());
    System.out.flush();
  }

  @After
  public void apresToutTest(){
    System.out.println("apres tout test"+System.currentTimeMillis());
    System.out.flush();
  }
  @Test
  public void testCreerClientScenarioNominal(){
    try {
      Client client = ClientService.creerClient("pierjeanl@gmail.com","landrein","pierjean");
      assertNotNull(client);
      assertEquals(client.nom,"landrein");
      assertEquals(client.prenom,"pierjean");
      assertEquals(client.email,"pierjeanl@gmail.com");
    } catch (MetierException e) {
      assertTrue(false);
    }
  }

  @Test
  public void testCreerClientNomNull(){
    try {
      Client client = ClientService.creerClient("pierjeanl@gmail.com",null,"pierjean");
      assertTrue(false);
    } catch (MetierException e) {
      assertEquals("Le nom ne peut être vide",e.getMessage());
    }
  }
  @Test
  public void testCreerClientNomVide(){
    try {
      Client client = ClientService.creerClient("pierjeanl@gmail.com","","pierjean");
      assertTrue(false);
     } catch (MetierException e) {
      assertEquals("Le nom ne peut être vide",e.getMessage());
    }
  }

  @Test
  public void testCreerClientPrenomNull(){
    try {
      Client client = ClientService.creerClient("pierjeanl@gmail.com","landrein",null);
      assertTrue(false);
    } catch (MetierException e) {
      assertEquals("Le prenom ne peut être vide",e.getMessage());
    }
  }

  @Test
  public void testCreerClientPrenomVide(){
    try {
      Client client = ClientService.creerClient("pierjeanl@gmail.com","landrein","");
      assertTrue(false);
    } catch (MetierException e) {
      assertEquals("Le prenom ne peut être vide",e.getMessage());
    }
  }

  @Test
  public void testCreerClientMailNull(){
    try {
      Client client = ClientService.creerClient(null,"landrein","pierjean");
      assertTrue(false);
    } catch (MetierException e) {
      assertEquals("L'email ne peut être vide",e.getMessage());
    }
  }

  @Test
  public void testCreerClientMailVide(){
    try {
      Client client = ClientService.creerClient("","landrein","pierjean");
      assertTrue(false);
    } catch (MetierException e) {
      assertEquals("L'email ne peut être vide",e.getMessage());
    }
  }

  @Test
  public void testCreerClientMailInvalid(){
    try {
      Client client = ClientService.creerClient("hgdljgfod","landrein","pierjean");
      assertTrue(false);
    } catch (MetierException e) {
      assertEquals("L'email n'est pas valide",e.getMessage());
    }
  }

}
