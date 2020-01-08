package com.github.savkk.propeller.archtests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


public class ArchTests {
    private final JavaClasses tests = new ClassFileImporter().importPackages("com.github.savkk.propeller");

    @Test
    public void layerDependencies() {
        layeredArchitecture()
                .layer("Tests").definedBy("..tests..")
                .layer("Steps").definedBy("..steps..")
                .layer("Pages").definedBy("..pages..")
                .whereLayer("Tests").mayNotBeAccessedByAnyLayer()
                .whereLayer("Steps").mayOnlyBeAccessedByLayers("Tests")
                .whereLayer("Pages").mayOnlyBeAccessedByLayers("Steps").check(tests);
    }

    @Test
    public void noDirectAccessToSeleniumClasses() {
        noClasses()
                .that()
                .resideInAPackage("..tests..")
                .should()
                .accessClassesThat()
                .resideInAPackage("org.openqa.selenium..").check(tests);
    }

    @Test
    public void allPublicStepsMethodsShouldBeAnnotated() {
        methods().that().arePublic()
                .and().areDeclaredInClassesThat().resideInAPackage("..steps..")
                .should().beAnnotatedWith(Step.class).check(tests);
    }
}
