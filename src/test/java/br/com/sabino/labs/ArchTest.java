package br.com.sabino.labs;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.com.sabino.labs");

        noClasses()
            .that()
            .resideInAnyPackage("br.com.sabino.labs.domain.service..")
            .or()
            .resideInAnyPackage("br.com.sabino.labs.domain.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..br.com.sabino.labs.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
