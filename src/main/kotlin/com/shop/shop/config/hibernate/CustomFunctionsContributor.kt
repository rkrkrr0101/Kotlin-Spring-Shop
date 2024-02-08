package com.shop.shop.config.hibernate

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.boot.model.FunctionContributor
import org.hibernate.type.StandardBasicTypes

class CustomFunctionsContributor:FunctionContributor {
    override fun contributeFunctions(functionContributions: FunctionContributions?) {
        if(functionContributions ==null){
            throw IllegalAccessException()
        }
        functionContributions.functionRegistry.registerPattern(
            "match_against",
            "match (?1) against (?2 in boolean mode)",
            functionContributions.typeConfiguration.basicTypeRegistry.resolve(StandardBasicTypes.BOOLEAN)
        )
    }
}
