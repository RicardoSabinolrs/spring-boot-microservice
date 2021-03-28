package br.com.sabino.lab;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.com.sabino.lab");

        noClasses()
            .that()
                .resideInAnyPackage("br.com.sabino.lab.domain.service..")
            .or()
                .resideInAnyPackage("br.com.sabino.lab.domain.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..br.com.sabino.lab.api..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
