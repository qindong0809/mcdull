package com.dqcer.mcdull.uac;

import com.dqcer.mcdull.framework.base.dto.DTO;
import com.dqcer.mcdull.framework.base.vo.VO;
import com.dqcer.mcdull.framework.enforcer.ArchitectureEnforcer;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


public class ArchitectureTest {

    private JavaClasses classes;

    @BeforeEach
    public void setUp() {

        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_PACKAGE_INFOS)
                .importPackages("com.dqcer");
    }

    @Test
    public void requiredRules() {
        for (ArchRule rule : ArchitectureEnforcer.REQUIRED_RULES) {
            rule.check(classes);
        }
    }

    /**
     * dto实现类名称规则
     */
    @Test
    public void dto_implement_class_name_rule() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.dqcer");
        ArchRule myRule = classes().that().implement(DTO.class)
                .should().haveSimpleNameEndingWith("DTO");
        myRule.check(importedClasses);

        myRule = classes().that().implement(VO.class)
                .should().haveSimpleNameEndingWith("VO");
        myRule.check(importedClasses);
    }
}
