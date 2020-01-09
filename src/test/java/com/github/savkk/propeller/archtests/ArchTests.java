package com.github.savkk.propeller.archtests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


public class ArchTests {
    private final JavaClasses classes = new ClassFileImporter().importPackages("com.github.savkk.propeller");

    @Test
    public void layerDependencies() {
        layeredArchitecture()
                .layer("Tests").definedBy("..tests..")
                .layer("Steps").definedBy("..steps..")
                .layer("Pages").definedBy("..pages..")
                .whereLayer("Tests").mayNotBeAccessedByAnyLayer()
                .whereLayer("Steps").mayOnlyBeAccessedByLayers("Tests")
                .whereLayer("Pages").mayOnlyBeAccessedByLayers("Steps").check(classes);
    }

    @Test
    public void noDirectAccessToSeleniumClasses() {
        noClasses()
                .that()
                .resideInAPackage("..tests..")
                .should()
                .accessClassesThat()
                .resideInAPackage("org.openqa.selenium..")
                .check(classes);
    }

    @Test
    public void allPublicStepsMethodsShouldBeAnnotated() {
        methods()
                .that().arePublic()
                .and().areDeclaredInClassesThat().resideInAPackage("..steps..")
                .should().beAnnotatedWith(Step.class)
                .check(classes);
    }

    @Test
    public void allTestClassesShouldBeAnnotatedFeature() {
        classes()
                .that()
                .resideInAnyPackage("..tests..")
                .should()
                .beAnnotatedWith(Feature.class)
                .check(classes);
    }

    @Test
    public void allTestMethodsShouldBeAnnotatedStory() {
        methods()
                .that().areDeclaredInClassesThat().resideInAnyPackage("..tests..")
                .and().areAnnotatedWith(Test.class)
                .should()
                .beAnnotatedWith(Story.class)
                .check(classes);
    }
}
