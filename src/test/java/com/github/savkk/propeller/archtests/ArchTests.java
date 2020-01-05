package com.github.savkk.propeller.archtests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.testng.annotations.Test;

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
}
