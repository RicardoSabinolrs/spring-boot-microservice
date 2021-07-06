package br.com.sabino.labs.cucumber;

import br.com.sabino.labs.SabinoLabsApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = SabinoLabsApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
